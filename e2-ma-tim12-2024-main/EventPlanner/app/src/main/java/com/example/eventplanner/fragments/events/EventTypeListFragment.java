package com.example.eventplanner.fragments.events;

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
import com.example.eventplanner.adapters.EventTypeListAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentEventTypeListBinding;
import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventTypeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTypeListFragment extends ListFragment {

    private EventTypeListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<EventType> mEventTypes;
    private FragmentEventTypeListBinding binding;
    public EventTypeListFragment() {
        // Required empty public constructor
    }

    public static EventTypeListFragment newInstance(ArrayList<EventType> eventTypes){
        EventTypeListFragment fragment = new EventTypeListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, eventTypes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("ShopApp", "onCreate Event Types List Fragment");
        if (getArguments() != null) {
            mEventTypes = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new EventTypeListAdapter(getActivity(), mEventTypes);
            setListAdapter(adapter);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Event Types List Fragment");
        binding = FragmentEventTypeListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}