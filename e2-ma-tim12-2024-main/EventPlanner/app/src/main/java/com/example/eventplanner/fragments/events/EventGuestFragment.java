package com.example.eventplanner.fragments.events;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.events.EventActivityCreationActivity;
import com.example.eventplanner.activities.events.EventGuestCreationActivity;
import com.example.eventplanner.databinding.FragmentEventActivityBinding;
import com.example.eventplanner.databinding.FragmentEventGuestBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.model.EventGuest;
import com.example.eventplanner.repositories.EventActivityRepository;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.utils.EventAgendaPdfGenerator;
import com.example.eventplanner.utils.EventGuestListPdfGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventGuestFragment extends Fragment {

    private String eventId;
    private EventRepository eventRepository;
    private Event event = new Event();
    private ArrayList<EventGuest> eventGuests = new ArrayList<>();
    private FragmentEventGuestBinding binding;
    private EventGuestListPdfGenerator pdfGenerator;

    public EventGuestFragment() {
        // Required empty public constructor
    }

    public static EventGuestFragment newInstance(String eventId) {
        EventGuestFragment fragment = new EventGuestFragment();
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
        binding = FragmentEventGuestBinding.inflate(inflater, container, false);

        eventRepository = new EventRepository();
        eventRepository.getById(new EventRepository.EventByIdFetchCallback() {
            @Override
            public void onEventByIdFetch(Event fetchedEvent) {
                if (fetchedEvent != null) {
                    event = fetchedEvent;
                    eventGuests = event.getEventGuests();

                    Log.d("EVENT NEKI", "Event ID: " + event.getId());

                    FragmentTransition.to(
                            EventGuestListFragment.newInstance(event, null),
                            getActivity(),
                            false,
                            R.id.scroll_event_guests_list
                    );

                }
            }
        }, eventId);


        Button addNewEventGuest = binding.addNewGuest;
        addNewEventGuest.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EventGuestCreationActivity.class);
            intent.putExtra("SelectedEventId", eventId);
            getContext().startActivity(intent);
        });

        pdfGenerator = new EventGuestListPdfGenerator();

        binding.exportButton.setOnClickListener( v ->{
            if(event != null) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "event_guests.pdf";
                ArrayList<EventGuest> guests = new ArrayList<>();
                for (EventGuest guest : event.getEventGuests()) {
                    guests.add(guest);
                }
                pdfGenerator.createEventGuestListPdf(guests, path);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}