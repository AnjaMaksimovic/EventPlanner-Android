package com.example.eventplanner.fragments.events;

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
import com.example.eventplanner.databinding.FragmentEventInfoBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.packages.AllPackagesListFragment;
import com.example.eventplanner.fragments.products.AllProductsListFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInfoFragment extends Fragment {

    private String eventId;
    public static ArrayList<Package> packages = new ArrayList<Package>();
    public static ArrayList<Product> products = new ArrayList<Product>();
    private FragmentEventInfoBinding binding;
    public EventInfoFragment() {
    }

    public static EventInfoFragment newInstance(String eventId) {
        EventInfoFragment fragment = new EventInfoFragment();
        Bundle args = new Bundle();
        args.putString("eventId", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareProductList(products);
        preparePackageList(packages);

      //  FragmentTransition.to(AllProductsListFragment.newInstance(products), getActivity(),
      //          false, R.id.scroll_products_list);
      //  FragmentTransition.to(AllPackagesListFragment.newInstance(packages), getActivity(),
      //          false, R.id.scroll_packages_list);


        return root;
    }

    private void preparePackageList(ArrayList<Package> packages){
        packages.add(new Package("1L", "Event recording", "All the necessary items to decorate your wedding.", R.drawable.photobook, 640, 10.0));
        packages.add(new Package("2L", "Event service", "Everything you need for the best service at your event.", R.drawable.photobook_set, 1150, 10.0));
        packages.add(new Package("3L", "Event recording", "All the necessary items to decorate your wedding.", R.drawable.photobook, 640, 10.0));
        packages.add(new Package("4L", "Event service", "Everything you need for the best service at your event.", R.drawable.photobook_set, 1150, 10.0));
    }

    private void prepareProductList(ArrayList<Product> products){
        //products.add(new Product(1L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //products.add(new Product(2L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
        //products.add(new Product(3L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //products.add(new Product(4L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
    }
}