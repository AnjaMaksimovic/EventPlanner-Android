package com.example.eventplanner.fragments.packages;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentAllPackagesBinding;
import com.example.eventplanner.databinding.FragmentAllProductsBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.AllProductsFragment;
import com.example.eventplanner.fragments.products.AllProductsListFragment;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPackagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPackagesFragment extends Fragment {

    public static ArrayList<Package> packages = new ArrayList<Package>();
    private FragmentAllPackagesBinding binding;

    public AllPackagesFragment() {}

    public static AllPackagesFragment newInstance() {
        AllPackagesFragment fragment = new AllPackagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "kreiran packagePage");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllPackagesBinding.inflate(inflater, container, false);
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

        FragmentTransition.to(AllPackagesListFragment.newInstance(packages), getActivity(),
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