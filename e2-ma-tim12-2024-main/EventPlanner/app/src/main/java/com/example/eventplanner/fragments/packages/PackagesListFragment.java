package com.example.eventplanner.fragments.packages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.eventplanner.adapters.PackageListAdapter;
import com.example.eventplanner.databinding.FragmentPackagesListBinding;
import com.example.eventplanner.model.Package;

import java.util.ArrayList;

import androidx.fragment.app.ListFragment;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PackagesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackagesListFragment extends ListFragment {

    private PackageListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Package> mPackages;
    private FragmentPackagesListBinding binding;

    public PackagesListFragment() {
        // Required empty public constructor
    }


    public static PackagesListFragment newInstance(ArrayList<Package> packages) {
        PackagesListFragment fragment = new PackagesListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, packages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ShopApp", "onCreate Products List Fragment???????");
        if (getArguments() != null) {
            mPackages = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new PackageListAdapter(getActivity(), mPackages, false, true);
            setListAdapter(adapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPackagesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}