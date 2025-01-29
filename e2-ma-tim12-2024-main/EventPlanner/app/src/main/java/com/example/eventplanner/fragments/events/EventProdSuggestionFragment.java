package com.example.eventplanner.fragments.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentEventProdSuggestionBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.AllProductsListFragment;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventProdSuggestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventProdSuggestionFragment extends Fragment {

    private String eventId;
    public static ArrayList<Product> products = new ArrayList<Product>();
    private FragmentEventProdSuggestionBinding binding;
    public EventProdSuggestionFragment() {
        // Required empty public constructor
    }

    public static EventProdSuggestionFragment newInstance(String eventId) {
        EventProdSuggestionFragment fragment = new EventProdSuggestionFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentEventProdSuggestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareProductList(products);

        //FragmentTransition.to(AllProductsListFragment.newInstance(products), getActivity(),
          //      false, R.id.scroll_products_list);

        return root;
    }

    private void prepareProductList(ArrayList<Product> products){
        //products.add(new Product(1L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //products.add(new Product(2L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
        //products.add(new Product(3L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //products.add(new Product(4L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
    }
}