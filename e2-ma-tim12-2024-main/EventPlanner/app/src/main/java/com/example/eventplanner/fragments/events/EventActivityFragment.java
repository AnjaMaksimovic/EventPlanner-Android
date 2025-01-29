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
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.events.EventActivityCreationActivity;
import com.example.eventplanner.databinding.FragmentEventActivityBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.pricelist.PriceListItem;
import com.example.eventplanner.repositories.EventActivityRepository;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.utils.EventAgendaPdfGenerator;
import com.example.eventplanner.utils.PdfGenerator;

import java.io.File;
import java.util.ArrayList;

public class EventActivityFragment extends Fragment {

    private String eventId;
    private EventRepository eventRepository;
    private Event event = new Event();
    private EventActivityRepository eventActivityRepository;
    public static ArrayList<EventActivity> eventActivities = new ArrayList<>();
    private FragmentEventActivityBinding binding;
    private EventAgendaPdfGenerator pdfGenerator;

    public EventActivityFragment() {
        // Required empty public constructor
    }

    public static EventActivityFragment newInstance(String eventId) {
        EventActivityFragment fragment = new EventActivityFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventActivityBinding.inflate(inflater, container, false);

        eventRepository = new EventRepository();
        eventRepository.getById(new EventRepository.EventByIdFetchCallback() {
            @Override
            public void onEventByIdFetch(Event fetchedEvent) {
                if (fetchedEvent != null) {
                    event = fetchedEvent;

                    Log.d("EVENT NEKI", "Event ID: " + event.getId());

                    FragmentTransition.to(
                            EventActivityListFragment.newInstance(event, null),
                            getActivity(),
                            false,
                            R.id.scroll_event_activities_list
                    );
                }
            }
        }, eventId);


        Button addNewEventActivity = binding.addNewActivity;
        addNewEventActivity.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EventActivityCreationActivity.class);
            intent.putExtra("SelectedEventId", eventId);
            getContext().startActivity(intent);
        });

        pdfGenerator = new EventAgendaPdfGenerator();

        binding.exportButton.setOnClickListener( v ->{
            if(event != null) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "event_agenda.pdf";
                ArrayList<EventActivity> activities = new ArrayList<>();
                for (EventActivity activity : event.getEventActivities()) {
                    activities.add(activity);
                }
                pdfGenerator.createEventAgendaPdf(activities, path);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
