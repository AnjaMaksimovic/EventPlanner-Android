package com.example.eventplanner.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.ActivityEventGuestUpdateBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.events.EventActivityListFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventGuest;
import com.example.eventplanner.repositories.EventGuestRepository;
import com.example.eventplanner.repositories.EventRepository;

public class EventGuestUpdateActivity extends AppCompatActivity {

    private ActivityEventGuestUpdateBinding binding;
    private Event event;
    private String eventId;
    private EventGuest eventGuest;
    private Spinner ageSpinner;
    private EditText name;
    private EditText surname;
    private CheckBox invitedCheckbox;
    private CheckBox notInvitedCheckbox;
    private CheckBox invitationAcceptedCheckbox;
    private CheckBox invitationDeclinedCheckbox;
    private EditText specialRequirements;
    private EventGuestRepository eventGuestRepository;
    private EventRepository eventRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventGuestUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventRepository = new EventRepository();
        eventGuestRepository = new EventGuestRepository();
        ageSpinner = binding.spinAge;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.age_ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("eventGuest") && intent.hasExtra("eventId")) {
            eventGuest = intent.getParcelableExtra("eventGuest");
            eventId = intent.getStringExtra("eventId");

            eventRepository.getById(new EventRepository.EventByIdFetchCallback() {
                @Override
                public void onEventByIdFetch(Event fetchedEvent) {
                    if (fetchedEvent != null) {
                        event = fetchedEvent;

                        Log.d("EVENT NEKI", "Event ID: " + event.getId());
                    }
                }
            }, eventId);
            Log.i("EventApp", "Received EventGuest: " + eventGuest.toString());
         //   Log.i("EventApp", "Received Event: " + event.toString());

            name = binding.eventGuestNameField;
            name.setText(eventGuest.getName());

            surname = binding.eventGuestSurnameField;
            surname.setText(eventGuest.getSurname());

            String ageRange = eventGuest.getAge();
            if (ageRange != null) {
                int spinnerPosition = adapter.getPosition(ageRange);
                ageSpinner.setSelection(spinnerPosition);
            }

            invitedCheckbox = binding.invitedCheckbox;
            notInvitedCheckbox = binding.notInvitedCheckbox;
            if (eventGuest.isInvited()) {
                invitedCheckbox.setChecked(true);
                notInvitedCheckbox.setChecked(false);
            } else {
                invitedCheckbox.setChecked(false);
                notInvitedCheckbox.setChecked(true);
            }

            invitationAcceptedCheckbox = binding.invitationAcceptedCheckbox;
            invitationDeclinedCheckbox = binding.invitationDeclinedCheckbox;
            if (eventGuest.hasAcceptedInvitation()) {
                invitationAcceptedCheckbox.setChecked(true);
                invitationDeclinedCheckbox.setChecked(false);
            } else {
                invitationAcceptedCheckbox.setChecked(false);
                invitationDeclinedCheckbox.setChecked(true);
            }

            specialRequirements = binding.eventGuestSpecialRequirementsField;
            specialRequirements.setText(eventGuest.getSpecialRequests());
        }

        Button submitBtn = binding.updateEventGuestButton;
        submitBtn.setOnClickListener(v -> {
            String eventGuestName = binding.eventGuestNameField.getText().toString();
            String eventGuestSurname = binding.eventGuestSurnameField.getText().toString();
            String selectedAgeRange = ageSpinner.getSelectedItem().toString();

            eventGuest.setName(eventGuestName);
            eventGuest.setSurname(eventGuestSurname);
            eventGuest.setAge(selectedAgeRange);

            if (invitedCheckbox.isChecked()) {
                eventGuest.setInvited(true);
            } else {
                eventGuest.setInvited(false);
            }

            if (invitationAcceptedCheckbox.isChecked()) {
                eventGuest.setAcceptedInvitation(true);
            } else {
                eventGuest.setAcceptedInvitation(false);
            }

            eventGuest.setSpecialRequests(binding.eventGuestSpecialRequirementsField.getText().toString());

            eventGuestRepository.updateEventGuest(eventGuest);

            EventGuest existingGuest = null;
            for (EventGuest guest : event.getEventGuests()) {
                if (guest.getId().equals(eventGuest.getId())) {
                    existingGuest = guest;
                    break;
                }
            }
            if (existingGuest != null) {
                event.removeEventGuest(existingGuest);
            }

            event.addEventGuest(eventGuest);
            eventRepository.update(event);

            finish();
        });
    }
}
