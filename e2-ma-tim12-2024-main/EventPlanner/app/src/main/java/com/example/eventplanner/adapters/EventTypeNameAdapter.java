package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;

import java.util.List;

public class EventTypeNameAdapter extends ArrayAdapter<EventType> {

    private Context context;
    private List<EventType> eventTypes;

    public EventTypeNameAdapter(Context context, int resource, List<EventType> eventTypes) {
        super(context, resource, eventTypes);
        this.context = context;
        this.eventTypes = eventTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(eventTypes.get(position).getName());  // Set only the category name
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(eventTypes.get(position).getName());  // Set only the category name
        return textView;
    }
}
