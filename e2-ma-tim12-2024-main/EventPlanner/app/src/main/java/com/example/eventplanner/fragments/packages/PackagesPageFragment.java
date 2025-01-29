package com.example.eventplanner.fragments.packages;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.packages.PackageCreatingActivity;
import com.example.eventplanner.databinding.FragmentPackagesPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Package;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PackagesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackagesPageFragment extends Fragment {

    public static ArrayList<Package> packages = new ArrayList<Package>();
    private FragmentPackagesPageBinding binding;

    public PackagesPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PackagesPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PackagesPageFragment newInstance(String param1, String param2) {
        PackagesPageFragment fragment = new PackagesPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackagesPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        preparePackageList(packages);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenProductsSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.packages_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        Button addNewPackage = binding.btnAddNewPackage;
        addNewPackage.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PackageCreatingActivity.class);
            startActivity(intent);
        });

        FragmentTransition.to(PackagesListFragment.newInstance(packages), getActivity(),
                false, R.id.scroll_packages_list);

        return root;
    }

    private void preparePackageList(ArrayList<Package> packages){
        packages.add(new Package("1L", "Event recording", "All the necessary items to decorate your wedding.", R.drawable.photobook, 640, 10.0));
        packages.add(new Package("2L", "Event service", "Everything you need for the best service at your event.", R.drawable.photobook_set, 1150, 10.0));
        packages.add(new Package("3L", "Event recording", "All the necessary items to decorate your wedding.", R.drawable.photobook, 640, 10.0));
        packages.add(new Package("4L", "Event service", "Everything you need for the best service at your event.", R.drawable.photobook_set, 1150, 10.0));
    }
}