package com.example.eventplanner.fragments.events;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductCreatingActivity;
import com.example.eventplanner.databinding.FragmentEventPageBinding;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.settings.PreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventPageFragment extends Fragment {

    private String userId;
    private EventRepository eventRepository;
    public static ArrayList<Event> events = new ArrayList<Event>();
    private FragmentEventPageBinding binding;

    public EventPageFragment() {
        // Required empty public constructor
    }

    public static EventPageFragment newInstance() {
        EventPageFragment fragment = new EventPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "kreiran eventsPage");

        userId = PreferencesManager.getLoggedUserId(getContext());

        Log.d(TAG, "DA VIM KOJI JE USER" + userId.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentTransition.to(EventListFragment.newInstance(events), getActivity(),
                false, R.id.scroll_events_list);

        return root;
    }
}