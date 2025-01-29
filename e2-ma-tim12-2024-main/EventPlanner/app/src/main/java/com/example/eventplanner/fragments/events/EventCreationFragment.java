package com.example.eventplanner.fragments.events;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.HomeActivity;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.settings.PreferencesManager;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventCreationFragment extends Fragment {
    private String userId;
    private Event event;
    private EventRepository eventRepository;
    private TextView eventTypeTextView;
    private TextView categoryTextView;
    private TextView privacyTextView;
    private RadioButton selectedRadioButton;
    private RadioButton selectedPrivacyRadioButton;
    private TextView eventDateField;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public EventCreationFragment() {
        // Required empty public constructor
    }

    public static EventCreationFragment newInstance(String param1, String param2) {
        EventCreationFragment fragment = new EventCreationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation, container, false);
        eventTypeTextView = view.findViewById(R.id.event_type_field);
        categoryTextView = view.findViewById(R.id.event_category_field);
        privacyTextView = view.findViewById(R.id.privacy_type_field);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        event = new Event();

        Button createButton = view.findViewById(R.id.create_event_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventType = eventTypeTextView.getText().toString();
                String eventDate = eventDateField.getText().toString();
                String eventName = ((EditText) view.findViewById(R.id.event_name_field)).getText().toString();
                String eventGuestNumber = ((EditText) view.findViewById(R.id.event_guest_number_field)).getText().toString();
                String eventDescription = ((EditText) view.findViewById(R.id.event_description_field)).getText().toString();
                String eventLocation = ((EditText) view.findViewById(R.id.event_address_field)).getText().toString();
                String eventMaxKm = ((EditText) view.findViewById(R.id.event_km_field)).getText().toString();

                event = new Event();
                event.setUserId(userId);
                event.setName(eventName);
                event.setEventTypeId("1");
                event.setEventTypeName("Birthday");
                event.setDescription(eventDescription);
                event.setParticipants(Integer.parseInt(eventGuestNumber));
                event.setLocation(eventLocation);
                event.setMaxKm(Integer.parseInt(eventMaxKm));
                event.setDate(eventDate);

                eventRepository = new EventRepository();
                eventRepository.create(event);
                Toast.makeText(getContext(), "Created event", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireActivity(), HomeActivity.class);
                        startActivity(intent);

            }
        });

        Button openPopupButton = view.findViewById(R.id.open_event_type_popup);
        openPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

        Button openPrivacyPopupButton = view.findViewById(R.id.open_event_privacy_popup);
        openPrivacyPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrivacyPopupWindow(v);
            }
        });


        eventDateField = view.findViewById(R.id.event_date_field);
        eventDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Initialize the dateSetListener
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update the EditText with the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                eventDateField.setText(selectedDate);
            }
        };
    }

    private void showPopupWindow(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.event_type_popup, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        RadioButton option1RadioButton = popupView.findViewById(R.id.option1);
        RadioButton option2RadioButton = popupView.findViewById(R.id.option2);
        RadioButton option3RadioButton = popupView.findViewById(R.id.option3);
        RadioButton option4RadioButton = popupView.findViewById(R.id.option4);


        option1RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRadioButton = option1RadioButton;
                eventTypeTextView.setText(selectedRadioButton.getText());
                categoryTextView.setText("Category: Photo and Video\nProduct and Service Subcategory: Photography, Photos and Albums, Videography");
                popupWindow.dismiss();
            }
        });

        option2RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRadioButton = option2RadioButton;
                eventTypeTextView.setText(selectedRadioButton.getText());
                categoryTextView.setText("Category: Photo and Video\nProduct and Service Subcategory: Photography, Photos and Albums, Videography");
                popupWindow.dismiss();
            }
        });

        option3RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRadioButton = option3RadioButton;
                eventTypeTextView.setText(selectedRadioButton.getText());
                categoryTextView.setText("Category: Audio equipment, Stage equipment, Food and beverages\nProduct and Service Subcategory: Mixers and Controllers, Cables and Accessories, Special Effects Equipment, Lightning equipment");
                popupWindow.dismiss();
            }
        });

        option4RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRadioButton = option4RadioButton;
                eventTypeTextView.setText(selectedRadioButton.getText());
                categoryTextView.setText("");
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(anchorView);
    }


    private void showPrivacyPopupWindow(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.event_privacy_popup, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        RadioButton option1RadioButton = popupView.findViewById(R.id.option_private);
        RadioButton option2RadioButton = popupView.findViewById(R.id.option_open);


        option1RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPrivacyRadioButton = option1RadioButton;
                privacyTextView.setText(selectedPrivacyRadioButton.getText());
                event.setPrivate(true);
                popupWindow.dismiss();
            }
        });

        option2RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPrivacyRadioButton = option2RadioButton;
                privacyTextView.setText(selectedPrivacyRadioButton.getText());
                event.setPrivate(false);
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(anchorView);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                dateSetListener,
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }
}
