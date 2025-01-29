package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.system.Notification;

import java.util.ArrayList;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> aNotifications;
    private  OnNotificationClickListener mListner;
    public  interface OnNotificationClickListener{
        void OnNotificationClick(Notification notification);
    }
    public NotificationListAdapter(@NonNull Context context, @NonNull ArrayList<Notification> notifications, OnNotificationClickListener listener) {
        super(context, R.layout.notification_card, notifications);
        aNotifications = notifications;
        mListner = listener;
    }
    @Override
    public int getCount() {
        return aNotifications.size();
    }
    @Nullable
    @Override
    public Notification getItem(int position) {
        return aNotifications.get(position);
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Notification notification = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card,
                    parent, false);
        }
        TextView title = convertView.findViewById(R.id.notification_title);
        TextView senderEmail = convertView.findViewById(R.id.notification_sender_email);
        TextView body = convertView.findViewById(R.id.notification_body);
        LinearLayout notificationCard = convertView.findViewById(R.id.notification_card);

        if(notification != null){
            title.setText(notification.getTitle());
            senderEmail.setText(notification.getSenderEmail());
            body.setText(notification.getText());

            notificationCard.setOnClickListener(v -> {
                if(mListner != null){
                    mListner.OnNotificationClick(notification);
                }
            });
        }
        return convertView;
    }
}