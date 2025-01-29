package com.example.eventplanner.fragments.services;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.databinding.FragmentAllServicesListBinding;
import com.example.eventplanner.databinding.FragmentEventListBinding;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;

public class AllServicesListFragment extends ListFragment {

    private String userId;
    private Role userRole;
    private ServiceRepository serviceRepository;
    private ServiceListAdapter adapter;
    private ArrayList<Service> mServices = new ArrayList<>();
    private FragmentAllServicesListBinding binding;
    private static final String ARG_PARAM = "param";

    public AllServicesListFragment() {
        // Required empty public constructor
    }

    public static AllServicesListFragment newInstance(ArrayList<Service> services) {
        AllServicesListFragment fragment = new AllServicesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, services);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());
        userRole = PreferencesManager.getLoggedUserRole(getContext());

        serviceRepository = new ServiceRepository();

        if (userRole == Role.employee || userRole == Role.admin || userRole == Role.owner) {
            serviceRepository.getAllServices(new ServiceRepository.ServiceFetchCallback() {
                @Override
                public void onServiceFetch(ArrayList<Service> services) {
                    if(services != null) {

                        Log.d("AllServicesListFragment", "Fetched all services for admin/employee/owner");
                        for (Service service : services) {
                            Log.d("AllServicesListFragment", "Service: " + service.toString());
                        }
                            adapter = new ServiceListAdapter(getActivity(), services, "", false, false, true, false);
                            setListAdapter(adapter);

                    }
                }
            });
        } else {
            serviceRepository.getAllVisibleServices(new ServiceRepository.ServiceFetchCallback() {
                @Override
                public void onServiceFetch(ArrayList<Service> visibleServices) {
                    if (visibleServices != null) {
                        Log.d("AllServicesListFragment", "Fetched all visible services");
                        for (Service service : visibleServices) {
                            Log.d("AllServicesListFragment", "Visible Service: " + service.toString());
                        }
                        adapter = new ServiceListAdapter(getActivity(), visibleServices,"", false, false, true, false);
                        setListAdapter(adapter);
                   }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView SERVICES LIST FRAGMENT");
        binding = FragmentAllServicesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
}
