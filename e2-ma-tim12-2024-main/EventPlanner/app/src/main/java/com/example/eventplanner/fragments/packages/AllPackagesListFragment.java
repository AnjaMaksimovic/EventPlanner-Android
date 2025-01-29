package com.example.eventplanner.fragments.packages;

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
import com.example.eventplanner.adapters.PackageListAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentAllPackagesListBinding;
import com.example.eventplanner.databinding.FragmentAllProductsListBinding;
import com.example.eventplanner.fragments.products.AllProductsListFragment;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllPackagesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllPackagesListFragment extends ListFragment {

    private PackageListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Package> mPackage;
    private FragmentAllPackagesListBinding binding;

    public static AllPackagesListFragment newInstance(ArrayList<Package> packages) {
        AllPackagesListFragment fragment = new AllPackagesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, packages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            mPackage = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new PackageListAdapter(getActivity(), mPackage, false, false);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAllPackagesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}