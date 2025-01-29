package com.example.eventplanner.fragments.favourites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentAllServicesBinding;
import com.example.eventplanner.databinding.FragmentFavouritesBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.services.AllServicesListFragment;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Service> services = new ArrayList<Service>();
    private FragmentFavouritesBinding binding;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(FavouriteProductListFragment.newInstance(products), getActivity(),
                false, R.id.scroll_fav_products_list);

        FragmentTransition.to(FavouriteServiceListFragment.newInstance(services), getActivity(),
                false, R.id.scroll_fav_services_list);

        return root;
    }
}