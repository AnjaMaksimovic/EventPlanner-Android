package com.example.eventplanner.fragments.system;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.eventplanner.adapters.NotificationListAdapter;
import com.example.eventplanner.databinding.FragmentNotificationsListBinding;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.system.INotificationRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotificationsPageFragment extends ListFragment implements SensorEventListener {

    private static final String[] notificationsStatuses = new String[]{"all", "read", "unread"};
    private static int index = 0;
    private static final int INDEX_MAX = 2;
    private static final float SHAKE_THRESHOLD = 3.25f;
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private ArrayList<Notification> backupNotifications = new ArrayList<>();
    private NotificationRepository notificationRepository;
    private String userId;
    private NotificationListAdapter adapter;
    private FragmentNotificationsListBinding binding;

    public NotificationsPageFragment() {
    }

    public static NotificationsPageFragment newInstance() {
        NotificationsPageFragment fragment = new NotificationsPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());
        notificationRepository = new NotificationRepository();
        prepareNotifications();
        adapter = new NotificationListAdapter(getActivity(), notifications, notification -> {
            if(notification.getStatus().equals(NotificationStatus.read)) return;
            notificationRepository.Update(notification.getId(), NotificationStatus.read, new INotificationRepository.GetNotificationsCallback() {
                @Override
                public void OnGetNotifications(ArrayList<Notification> notificationsList) {
                    INotificationRepository.GetNotificationsCallback.super.OnGetNotifications(notificationsList);
                    if(notificationsList.isEmpty()) return;
                    Optional<Notification> result = notificationsList.stream()
                            .filter(n -> n.getId().equals(notification.getId())).findAny();
                    if(result.isPresent()){
                        if(backupNotifications.removeIf(n -> n.getId().equals(notification.getId()))){
                            backupNotifications.add(result.get());
                            notifications.removeIf(n -> n.getId().equals(notification.getId()));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        });
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsListBinding.inflate(inflater, container, false);
        binding.filterNotifications.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedButton = group.findViewById(checkedId);
            if(checkedButton == null) return;
            String value = checkedButton.getText().toString().trim();
            filterNotifications(value);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSensorMgr = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null){
            mSensorMgr.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2) +
                        Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;
                Log.d("NOTIFICATIONS", "Acceleration is " + acceleration + "m/s^2");
                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime;
                    Log.d("NOTIFICATIONS", "Shake, Rattle, and Roll");
                    changeNotificationView();
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mSensorMgr != null){
            mSensorMgr.unregisterListener(this);
        }
    }
    private void changeNotificationView(){
        if(index > INDEX_MAX)
            index = 0;
        filterNotifications(notificationsStatuses[index++]);
    }
    private void prepareNotifications() {
        notificationRepository.GetAllByReceiver(userId, new INotificationRepository.GetNotificationsCallback() {
            @Override
            public void OnGetNotifications(ArrayList<Notification> notificationsList) {
                INotificationRepository.GetNotificationsCallback.super.OnGetNotifications(notificationsList);
                if(notificationsList.isEmpty()) return;
                notifications.addAll(notificationsList);
                backupNotifications.addAll(notificationsList);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void filterNotifications(String value) {
        adapter.clear();
        notifications.clear();
        if(value.equals("all")){
            notifications.addAll(backupNotifications);
            adapter.notifyDataSetChanged(); return;
        }
        if(value.equals("read")){
            notifications.addAll(backupNotifications.stream()
                    .filter(n -> n.getStatus().equals(NotificationStatus.read))
                    .collect(Collectors.toList()));
            adapter.notifyDataSetChanged(); return;
        }
        notifications.addAll(backupNotifications.stream()
                .filter(n -> n.getStatus().equals(NotificationStatus.unread))
                .collect(Collectors.toList()));
        adapter.notifyDataSetChanged();
    }
}