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
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.adapters.ServiceListAdapter;
import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.databinding.FragmentServicesListBinding;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.repositories.ServiceRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesListFragment extends ListFragment {

    private ServiceListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Service> mServices;
    private FragmentServicesListBinding binding;
    private ServiceRepository serviceRepo;
    private static String searchText;


    public static ServicesListFragment newInstance(ArrayList<Service> services, String query) {
        ServicesListFragment fragment = new ServicesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, services);
        args.putString("searchText", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceRepo = new ServiceRepository();
        Bundle args = getArguments();
        if (args != null) {
            searchText = args.getString("searchText");
            if (searchText != null) {
                serviceRepo.getAllServicesBySearchName(searchText, new ServiceRepository.ServiceFetchCallback() {
                    @Override
                    public void onServiceFetch(ArrayList<Service> services) {
                        if (services != null) {
                            ArrayList<Service> filteredServices = new ArrayList<>();
                            for (Service service : services) {
                                if (!service.isDeleted()) {
                                    filteredServices.add(service);
                                }
                            }
                            adapter = new ServiceListAdapter(getActivity(), filteredServices, "",false, true, false, false);
                            setListAdapter(adapter);
                        }

                    }
                });
            } else if (searchText == "") {
                serviceRepo.getAllServices(new ServiceRepository.ServiceFetchCallback() {
                    @Override
                    public void onServiceFetch(ArrayList<Service> services) {
                        if (services != null) {
                            ArrayList<Service> filteredServices = new ArrayList<>();
                            for (Service service : services) {
                                if (!service.isDeleted()) {
                                    filteredServices.add(service);
                                }
                            }
                            adapter = new ServiceListAdapter(getActivity(), filteredServices, "",false, true, false, false);
                            setListAdapter(adapter);
                        }
                    }
                });
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServicesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}