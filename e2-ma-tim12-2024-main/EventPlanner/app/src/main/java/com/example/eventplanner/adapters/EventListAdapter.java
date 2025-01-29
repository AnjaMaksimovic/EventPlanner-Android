package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.events.EventDetailsActivity;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {

    private ArrayList<Event> aEvents;

    public EventListAdapter(Context context, ArrayList<Event> events){
        super(context, R.layout.event_card, events);
        aEvents = events;

    }

    @Override
    public int getCount() {
        return aEvents.size();
    }


    @Nullable
    @Override
    public Event getItem(int position) {
        return aEvents.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_card,
                    parent, false);
        }
        LinearLayout eventCard = convertView.findViewById(R.id.event_card_item);
        TextView eventName = convertView.findViewById(R.id.event_name);
        TextView eventType = convertView.findViewById(R.id.event_type);
        TextView eventDescription = convertView.findViewById(R.id.event_description);
        TextView eventGuestNumber = convertView.findViewById(R.id.event_guest_number);
        TextView eventLocation = convertView.findViewById(R.id.event_location);
        TextView eventDate = convertView.findViewById(R.id.event_date);
        TextView eventPrivacy = convertView.findViewById(R.id.event_privacy);

        if(event != null){
            eventName.setText(event.getName());
            eventType.setText(event.getEventTypeName());
            eventDescription.setText(event.getDescription());
            eventGuestNumber.setText(String.valueOf(event.getParticipants()));
            eventLocation.setText(event.getLocation());
            eventDate.setText(String.valueOf(event.getDate()));
            eventPrivacy.setText(event.isPrivate() ? "Private" : "Open");
            eventCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + event.getName() + ", id: " +
                       event.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + event.getName()  +
                        ", id: " + event.getId().toString(), Toast.LENGTH_SHORT).show();
                  Toast.makeText(getContext(), "Clicked: ", Toast.LENGTH_SHORT).show();
            });
        }

        eventCard.setOnClickListener(v -> {
            Event selectedEvent  = getItem(position);
            if (selectedEvent != null) {
                Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                intent.putExtra("SelectedEventId", selectedEvent.getId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
