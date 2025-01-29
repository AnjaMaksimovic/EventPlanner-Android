package com.example.eventplanner.fragments.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventActivityListAdapter;
import com.example.eventplanner.adapters.EventGuestListAdapter;
import com.example.eventplanner.databinding.FragmentEventActivityListBinding;
import com.example.eventplanner.databinding.FragmentEventGuestListBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.model.EventGuest;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventGuestListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventGuestListFragment extends ListFragment {

    private static final String ARG_PARAM = "param";
    private static final String EVENT_PARAM = "event";
    private EventGuestListAdapter adapter;
    private Event event;
    private ArrayList<EventGuest> mEventGuests;
    private FragmentEventGuestListBinding binding;

    public EventGuestListFragment() {
        // Required empty public constructor
    }

    public static EventGuestListFragment newInstance(Event event, ArrayList<EventGuest> eventGuests) {
        EventGuestListFragment fragment = new EventGuestListFragment();
        Bundle args = new Bundle();
        args.putParcelable(EVENT_PARAM, event);
        args.putParcelableArrayList(ARG_PARAM, eventGuests);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            event = getArguments().getParcelable(EVENT_PARAM);

            if (event != null) {
                mEventGuests = event.getEventGuests();
                if(mEventGuests != null) {
                    Log.d("EventGuestFragment", "EventGuests size: " + mEventGuests.size());

//                    for (EventActivity eventActivity : mEventActivities) {
//                        Log.d("EventActivityFragment", "EventActivity: " + eventActivity.toString());
//                    }

                    adapter = new EventGuestListAdapter(getActivity(), event, mEventGuests);
                    setListAdapter(adapter);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventGuestListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}