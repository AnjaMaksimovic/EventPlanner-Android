package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.metrics.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.events.EventTypeEditActivity;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;


public class EventTypeListAdapter extends ArrayAdapter<EventType> {
    private ArrayList<EventType> aEventTypes;

    public EventTypeListAdapter(Context context, ArrayList<EventType> eventTypes){
        super(context, R.layout.event_type_card, eventTypes);
        aEventTypes = eventTypes;
    }


    @Override
    public int getCount() {
        return aEventTypes.size();
    }


    @Nullable
    @Override
    public EventType getItem(int position) {
        return aEventTypes.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventType eventType = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_type_card,
                    parent, false);
        }
        LinearLayout eventTypeCard = convertView.findViewById(R.id.event_type_card_item);
        TextView eventTypeName = convertView.findViewById(R.id.event_type_name);
        TextView eventTypeDescription = convertView.findViewById(R.id.event_type_description);
        TextView eventTypeSubcategories = convertView.findViewById(R.id.event_type_subcategories);
        TextView eventTypeActivity = convertView.findViewById(R.id.event_type_activity);

        ImageButton editButton = convertView.findViewById(R.id.btnEdit);

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + eventType.getName() +
                    ", id: " + eventType.getId());
            Intent intent = new Intent(getContext(), EventTypeEditActivity.class);
            intent.putExtra("productId", eventType.getId());
            getContext().startActivity(intent);
        });


        if (eventType != null) {
            eventTypeName.setText(eventType.getName());
            eventTypeDescription.setText(eventType.getDescription());
            eventTypeSubcategories.setText("Suggested subcategories: " + eventType.getSubcategories());
            eventTypeActivity.setText(eventType.getIsActive());

            eventTypeCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + eventType.getName() + ", id: " +
                        eventType.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + eventType.getName() +
                        ", id: " + eventType.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }
}
