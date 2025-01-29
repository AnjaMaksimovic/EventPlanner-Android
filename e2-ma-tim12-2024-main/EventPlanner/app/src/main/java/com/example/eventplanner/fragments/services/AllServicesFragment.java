package com.example.eventplanner.fragments.services;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentAllProductsBinding;
import com.example.eventplanner.databinding.FragmentAllServicesBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.AllProductsListFragment;
import com.example.eventplanner.model.Service;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllServicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllServicesFragment extends Fragment {

    public static ArrayList<Service> services = new ArrayList<Service>();
    private FragmentAllServicesBinding binding;

    public AllServicesFragment() {
        // Required empty public constructor
    }


    public static AllServicesFragment newInstance() {
        AllServicesFragment fragment = new AllServicesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllServicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(AllServicesListFragment.newInstance(services), getActivity(),
                false, R.id.scroll_all_services_list);

        return root;
    }
}