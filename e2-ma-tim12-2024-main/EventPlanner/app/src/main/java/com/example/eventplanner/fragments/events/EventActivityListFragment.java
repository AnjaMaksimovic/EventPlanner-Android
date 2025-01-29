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
import com.example.eventplanner.adapters.EventActivityListAdapter;
import com.example.eventplanner.adapters.EventListAdapter;
import com.example.eventplanner.databinding.FragmentEventActivityListBinding;
import com.example.eventplanner.databinding.FragmentEventListBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.repositories.EventActivityRepository;
import com.example.eventplanner.repositories.EventRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventActivityListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventActivityListFragment extends ListFragment {

    private EventActivityRepository eventActivityRepository;
    private EventActivityListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private static final String EVENT_PARAM = "event";
    private Event event;
    private ArrayList<EventActivity> mEventActivities;
    private FragmentEventActivityListBinding binding;


    public static EventActivityListFragment newInstance(Event event, ArrayList<EventActivity> eventActivities) {
        EventActivityListFragment fragment = new EventActivityListFragment();
        Bundle args = new Bundle();
        args.putParcelable(EVENT_PARAM, event);
        args.putParcelableArrayList(ARG_PARAM, eventActivities);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            event = getArguments().getParcelable(EVENT_PARAM);

            if (event != null) {
                mEventActivities = event.getEventActivities();
                if(mEventActivities != null) {
                    Log.d("EventActivityFragment", "EventActivities size: " + mEventActivities.size());

                    for (EventActivity eventActivity : mEventActivities) {
                        Log.d("EventActivityFragment", "EventActivity: " + eventActivity.toString());
                    }

                    adapter = new EventActivityListAdapter(getActivity(), mEventActivities);
                    setListAdapter(adapter);
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventActivityListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
