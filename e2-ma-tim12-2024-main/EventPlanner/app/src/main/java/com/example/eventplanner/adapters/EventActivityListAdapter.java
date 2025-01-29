package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.EventActivity;

import java.util.ArrayList;

public class EventActivityListAdapter extends ArrayAdapter<EventActivity> {
    private ArrayList<EventActivity> aEventActivities;

     public EventActivityListAdapter(Context context, ArrayList<EventActivity> eventActivities){
         super(context, R.layout.event_agenda_card, eventActivities);
         aEventActivities = eventActivities;
     }

    @Override
    public int getCount() {
        return aEventActivities.size();
    }


    @Nullable
    @Override
    public EventActivity getItem(int position) {
        return aEventActivities.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventActivity eventActivity = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_agenda_card,
                    parent, false);
        }
        LinearLayout eventActivityCard = convertView.findViewById(R.id.event_agenda_card_item);
        TextView eventActivityName = convertView.findViewById(R.id.event_activity_name);
        TextView eventActivityDestription = convertView.findViewById(R.id.event_activity_description);
        TextView eventActivityStartTime = convertView.findViewById(R.id.event_activity_start_time);
        TextView eventActivityEndTime = convertView.findViewById(R.id.event_activity_end_time);
        TextView eventActivityLocation = convertView.findViewById(R.id.event_activity_location);

        if(eventActivity != null){
            eventActivityName.setText(eventActivity.getName());
            eventActivityDestription.setText(eventActivity.getDescription());
            eventActivityStartTime.setText(String.valueOf(eventActivity.getStartTime()));
            eventActivityEndTime.setText(String.valueOf(eventActivity.getEndTime()));
            eventActivityLocation.setText(eventActivity.getLocation());
            eventActivityCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + eventActivity.getName() + ", id: " +
                        eventActivity.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + eventActivity.getName()  +
                        ", id: " + eventActivity.getId().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Clicked: ", Toast.LENGTH_SHORT).show();
            });
        }
        return convertView;
    }
}
