package com.example.eventplanner.fragments.services;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.packages.PackageCreatingActivity;
import com.example.eventplanner.activities.products.ProductCreatingActivity;
import com.example.eventplanner.activities.services.ServiceCreatingActivity;
import com.example.eventplanner.databinding.FragmentPackagesPageBinding;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.databinding.FragmentServicesPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.packages.PackagesListFragment;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.repositories.ServiceRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServicesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServicesPageFragment extends Fragment {

    public static ArrayList<Service> services = new ArrayList<Service>();
    private FragmentServicesPageBinding binding;
    private ServiceRepository serviceRepo;
    private SearchView searchView;
    public ServicesPageFragment() {
        // Required empty public constructor
    }

    public static ServicesPageFragment newInstance() {
        ServicesPageFragment fragment = new ServicesPageFragment();
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
        binding = FragmentServicesPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = binding.searchText;

        String searchText = "";
        FragmentTransition.to(ServicesListFragment.newInstance(services, searchText), getActivity(),
                false, R.id.scroll_services_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
              public boolean onQueryTextSubmit(String query) {
                  // Handle search submit
                  updateServicesList(query);
                  return true;
              }

              @Override
              public boolean onQueryTextChange(String newText) {
                  if (newText.isEmpty()) {
                      updateServicesList("");
                  }
                  return false;

              }
        });

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenProductsSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.products_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        Button addNewService = binding.btnAddNewService;
        addNewService.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ServiceCreatingActivity.class);
            startActivity(intent);
        });

        return root;
    }


    private void updateServicesList(String query) {
        FragmentTransition.to(ServicesListFragment.newInstance(services, query), getActivity(),
                false, R.id.scroll_services_list);
    }

}