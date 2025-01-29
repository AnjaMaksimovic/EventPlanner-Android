package com.example.eventplanner.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEventCreationBinding;
import com.example.eventplanner.databinding.ActivityEventGuestCreationBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.model.EventGuest;
import com.example.eventplanner.repositories.EventActivityRepository;
import com.example.eventplanner.repositories.EventGuestRepository;
import com.example.eventplanner.repositories.EventRepository;

import java.util.ArrayList;

public class EventGuestCreationActivity extends AppCompatActivity {

    private Event event;
    private EventGuest eventGuest;
    private EventGuestRepository eventGuestRepository;
    private EventRepository eventRepository;
    private ActivityEventGuestCreationBinding binding;
    private Spinner ageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventGuestCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventRepository = new EventRepository();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SelectedEventId")) {
            String eventId = intent.getStringExtra("SelectedEventId");
            eventRepository.getById(new EventRepository.EventByIdFetchCallback() {
                @Override
                public void onEventByIdFetch(Event fetchedEvent) {
                    if (fetchedEvent != null) {
                        // Add all fetched events to the existing events list
                        event = fetchedEvent;

                        Log.d("EVENT NEKI", "Event ID: " + event.getId());
                    }
                }
            }, eventId);
        }

        ageSpinner = binding.spinAge;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        eventGuest = new EventGuest();

        Button createButton = binding.createEventGuestButton;
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventGuestName = binding.eventGuestNameField.getText().toString();
                String eventGuestSurname = binding.eventGuestSurnameField.getText().toString();
                String selectedAgeRange = ageSpinner.getSelectedItem().toString();
                CheckBox eventGuestInvitedCheckbox = binding.invitedCheckbox;
                CheckBox eventGuestNotInvitedCheckbox = binding.notInvitedCheckbox;
                CheckBox eventGuestInvitationAccepted = binding.invitationAcceptedCheckbox;
                CheckBox eventGuestInvitationDeclined = binding.invitationDeclinedCheckbox;
                String eventGuestSpecialRequirements = binding.eventGuestSpecialRequirementsField.getText().toString();

                eventGuest = new EventGuest();
                eventGuest.setName(eventGuestName);
                eventGuest.setSurname(eventGuestSurname);
                eventGuest.setAge(selectedAgeRange);

                if(eventGuestInvitedCheckbox.isChecked()){
                    eventGuest.setInvited(true);
                }
                if(eventGuestNotInvitedCheckbox.isChecked()){
                    eventGuest.setInvited(false);
                }

                if(eventGuestInvitationAccepted.isChecked()){
                    eventGuest.setAcceptedInvitation(true);
                }
                if(eventGuestInvitationDeclined.isChecked()){
                    eventGuest.setAcceptedInvitation(false);
                }

                eventGuest.setSpecialRequests(eventGuestSpecialRequirements);

                eventGuestRepository = new EventGuestRepository();
                eventGuestRepository.Create(eventGuest);

                event.addEventGuest(eventGuest);
                eventRepository = new EventRepository();
                eventRepository.update(event);

                finish();
            }
        });

    }
}