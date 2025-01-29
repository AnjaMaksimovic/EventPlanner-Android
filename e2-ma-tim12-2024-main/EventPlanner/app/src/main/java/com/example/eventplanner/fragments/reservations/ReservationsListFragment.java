package com.example.eventplanner.fragments.reservations;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ReservationsListAdapter;
import com.example.eventplanner.databinding.FragmentReservationsListBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.reviews.ReviewCreationFragment;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reservations.ReservationStatus;
import com.example.eventplanner.model.reviews.ReviewReport;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.ReservationRepository;
import com.example.eventplanner.repositories.interfaces.IReservationRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;
import java.util.Optional;

public class ReservationsListFragment extends ListFragment {

    //Logged user
    private String userId;
    private Role userRole;
    public static ArrayList<Reservation> reservations = new ArrayList<>();
    public static ArrayList<Reservation> backupReservations = new ArrayList<>();
    private ReservationsListAdapter adapter;
    private ReservationRepository reservationRepository;
    private NotificationRepository notificationRepository;
    private FragmentReservationsListBinding binding;
    private CheckBox cbNew, cbPupCancel, cbOrgCancel, cbAdminCancel, cbAccepted, cbRealized;
    private SearchView searchView;

    public ReservationsListFragment() {
    }

    public static ReservationsListFragment newInstance() {
        ReservationsListFragment fragment = new ReservationsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = PreferencesManager.getLoggedUserId(getContext());
        userRole = PreferencesManager.getLoggedUserRole(getContext());

        reservationRepository = new ReservationRepository();
        notificationRepository = new NotificationRepository();
        prepareReservationsList();
        adapter = new ReservationsListAdapter(getActivity(), reservations, userRole, new ReservationsListAdapter.OnReservationClickListener() {
            @Override
            public void OnCancelClick(Reservation reservation) {
                if(reservation == null) return;
                ReservationStatus newStatus;
                if(userRole != null && (userRole.equals(Role.employee) || userRole.equals(Role.owner))){
                    newStatus = ReservationStatus.canceledByPup;
                }
                else {
                    newStatus = ReservationStatus.canceledByOrganizator;
                }
                reservationRepository.Update(reservation.getId(), newStatus, new IReservationRepository.ReservationsCallback() {
                        @Override
                        public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                            IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                            Optional<Reservation> result = reservationList.stream().filter(r -> r.getId().equals(reservation.getId())).findAny();
                            if(result.isPresent()){
                                reservations.removeIf(r -> r.getId().equals(reservation.getId()));
                                reservations.add(result.get());
                                backupReservations.clear();
                                backupReservations.addAll(reservations);
                                adapter.notifyDataSetChanged();

                                String title = "Reservation cancellation";
                                if(userRole != null && (userRole.equals(Role.employee) || userRole.equals(Role.owner))){
                                    String senderEmail = PreferencesManager.getLoggedUserEmail(getContext());
                                    String text = "Your reservation has been canceled by " + senderEmail;
                                    Notification notification = new Notification(userId, senderEmail, reservation.getOrganizatorId(), title, text, NotificationStatus.unread);
                                    notificationRepository.Create(notification);
                                    return;
                                }
                                String senderEmail = PreferencesManager.getLoggedUserEmail(getContext());
                                String text = "I'm informing you that I want to cancel reservation " + reservation.getId() + "of " + reservation.getItem().getServiceName();
                                Notification notification = new Notification(userId, senderEmail, reservation.getItem().getEmployeeId(), title, text, NotificationStatus.unread);
                                notificationRepository.Create(notification);
                            }
                        }
                    });
            }

            @Override
            public void OnAcceptClick(Reservation reservation) {
                    if(reservation == null) return;
                    reservationRepository.Update(reservation.getId(), ReservationStatus.accepted, new IReservationRepository.ReservationsCallback() {
                        @Override
                        public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                            IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                            Optional<Reservation> result = reservationList.stream().filter(r -> r.getId().equals(reservation.getId())).findAny();
                            if(result.isPresent()){
                                reservations.removeIf(r -> r.getId().equals(reservation.getId()));
                                reservations.add(result.get());
                                backupReservations.clear();
                                backupReservations.addAll(reservations);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
            }

            @Override
            public void OnRateClick(Reservation reservation) {
                FragmentTransition.to(ReviewCreationFragment.newInstance(reservation), getActivity(), false, R.id.fragment_nav_content_main);
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationsListBinding.inflate(inflater, container, false);
        searchView = binding.searchReservations;
        if(userRole.equals(Role.organizator)){
            searchView.setVisibility(View.GONE);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchReservations(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    reservations.clear();
                    adapter.clear();
                    reservations.addAll(backupReservations);
                    adapter.notifyDataSetChanged();
                    return false;
                }
                searchReservations(newText);
                return true;
            }
        });
        initializeFiltering();
        return binding.getRoot();
    }

    private void initializeFiltering() {
        cbNew = binding.cbNewRes;
        cbPupCancel = binding.cbPupCancelRes;
        cbOrgCancel = binding.cbOrgCancelRes;
        cbAdminCancel = binding.cbAdminCancelRes;
        cbAccepted = binding.cbAcceptedRes;
        cbRealized = binding.cbRealizedRes;
        CompoundButton.OnCheckedChangeListener checkBoxListener = (buttonView, isChecked) -> filterReservations();
        cbNew.setOnCheckedChangeListener(checkBoxListener);
        cbPupCancel.setOnCheckedChangeListener(checkBoxListener);
        cbOrgCancel.setOnCheckedChangeListener(checkBoxListener);
        cbAdminCancel.setOnCheckedChangeListener(checkBoxListener);
        cbAccepted.setOnCheckedChangeListener(checkBoxListener);
        cbRealized.setOnCheckedChangeListener(checkBoxListener);
    }

    private void filterReservations() {
        adapter.clear();
        reservations.clear();
        if(!cbNew.isChecked() && !cbPupCancel.isChecked() && !cbOrgCancel.isChecked() && !cbAdminCancel.isChecked() && !cbRealized.isChecked() && !cbAccepted.isChecked()){
            reservations.addAll(backupReservations);
        }
        else {
            for (Reservation reservation : backupReservations) {
                ReservationStatus status = reservation.getStatus();
                if ((cbNew.isChecked() && status.equals(ReservationStatus.newReservation)) ||
                        (cbPupCancel.isChecked() && status.equals(ReservationStatus.canceledByPup)) ||
                        (cbOrgCancel.isChecked() && status.equals(ReservationStatus.canceledByOrganizator)) ||
                        (cbAdminCancel.isChecked() && status.equals(ReservationStatus.canceledByAdmin)) ||
                        (cbAccepted.isChecked() && status.equals(ReservationStatus.accepted)) ||
                        (cbRealized.isChecked() && status.equals(ReservationStatus.realized)))
                    reservations.add(reservation);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void prepareReservationsList(){
        if(userId == null || userRole == null) return;
        switch (userRole){
            case employee: reservationRepository.GetAllByEmployee(userId, new IReservationRepository.ReservationsCallback() {
                    @Override
                    public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                        IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                        setReservations(reservationList);
                    }
                }); break;
            case organizator: reservationRepository.GetAllByOrganizator(userId, new IReservationRepository.ReservationsCallback() {
                @Override
                public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                    IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                    setReservations(reservationList);
                }
            }); break;
            default: reservationRepository.GetAll(new IReservationRepository.ReservationsCallback() {
                @Override
                public void OnReservationsGet(ArrayList<Reservation> reservationList) {
                    IReservationRepository.ReservationsCallback.super.OnReservationsGet(reservationList);
                    setReservations(reservationList);
                }
            });
        }
    }

    private void setReservations(ArrayList<Reservation> reservationList) {
        if(reservationList.isEmpty()) return;
        reservations.addAll(reservationList);
        backupReservations.addAll(reservationList);
        adapter.notifyDataSetChanged();
    }

    private void searchReservations(String query){
        adapter.clear();
        if(query.isEmpty()) return;
        String queryParts[] = query.toLowerCase().split(" ");
        if(queryParts.length < 1) return;
        reservations.clear();
        for(Reservation r : backupReservations){
            boolean matches = true;
            for(String q : queryParts){
                if(!(checkIfContains(r.getOrganizatorName(), q) ||
                        checkIfContains(r.getOrganizatorLastname(), q) ||
                        checkIfContains(r.getItem().getEmployeeName(), q) ||
                        checkIfContains(r.getItem().getEmployeeLastname(), q) ||
                        checkIfContains(r.getItem().getServiceName(), q))){
                    matches = false;
                    break;
                }
            }
            if(!matches) continue;
            reservations.add(r);
        }
        adapter.notifyDataSetChanged();
    }

    private boolean checkIfContains(String word, String query){
        if(word.isEmpty() || query.isEmpty()) return false;
        return word.toLowerCase().contains(query);
    }
}