package com.example.eventplanner.activities.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.adapters.EventListAdapter;
import com.example.eventplanner.databinding.ActivityEventCreationBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.repositories.EventActivityRepository;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

public class EventActivityCreationActivity extends AppCompatActivity {
    private Event event;
    private ArrayList<Event> events = new ArrayList<Event>();
    private EventActivity eventActivity;
    private EventActivityRepository eventActivityRepository;
    private EventRepository eventRepository;
    private ActivityEventCreationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventCreationBinding.inflate(getLayoutInflater());
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

        EditText startTimeField = binding.eventActivityStartTimeField;
        EditText endTimeField = binding.eventActivityEndTimeField;

        startTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(startTimeField);
            }
        });

        endTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(endTimeField);
            }
        });

        eventActivity = new EventActivity();

        Button createButton = binding.createEventActivityButton;
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventActivityName = binding.eventActivityNameField.getText().toString();
                String eventActivityDescription = binding.eventActivityDescriptionField.getText().toString();
                String eventActivityStartTime = binding.eventActivityStartTimeField.getText().toString();
                String eventActivityEndTime = binding.eventActivityEndTimeField.getText().toString();
                String eventActivityLocation = binding.eventActivityLocationField.getText().toString();

                eventActivity = new EventActivity();
                eventActivity.setName(eventActivityName);
                eventActivity.setDescription(eventActivityDescription);
                eventActivity.setStartTime(eventActivityStartTime);
                eventActivity.setEndTime(eventActivityEndTime);
                eventActivity.setLocation(eventActivityLocation);

                eventActivityRepository = new EventActivityRepository();
                eventActivityRepository.Create(eventActivity);

                event.addEventActivity(eventActivity);
                eventRepository = new EventRepository();
                eventRepository.update(event);

                finish();
            }
        });
    }

    private void showTimePickerDialog(final EditText timeField) {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            timeField.setText(formattedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }
}