package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventType;

import java.util.List;

public class EventNameAdapter extends ArrayAdapter<Event> {
    private Context context;
    private List<Event> events;

    public EventNameAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(events.get(position).getName());  // Set only the category name
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(events.get(position).getName());  // Set only the category name
        return textView;
    }
}
