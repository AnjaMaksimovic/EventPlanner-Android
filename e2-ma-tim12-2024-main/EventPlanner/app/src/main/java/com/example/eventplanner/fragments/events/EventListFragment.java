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
import com.example.eventplanner.adapters.EventListAdapter;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentEventListBinding;
import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends ListFragment {

    private String userId;
    private EventRepository eventRepository;
    private EventListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<Event> mEvents;
    private FragmentEventListBinding binding;


    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(ArrayList<Event> events) {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, events);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());

        eventRepository = new EventRepository();
        eventRepository.getEventsByOrganiserId(userId, new EventRepository.EventFetchCallback() {
            @Override
            public void onEventFetch(ArrayList<Event> events) {
                if (events != null) {
                    adapter = new EventListAdapter(getActivity(), events);
                    setListAdapter(adapter);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Event List Fragment");
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}