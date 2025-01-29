package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Person;

import java.util.List;

public class EmployeesNamesAdapter extends ArrayAdapter<Person> {
    private Context context;
    private List<Person> people;

    public EmployeesNamesAdapter(Context context, int resource, List<Person> people) {
        super(context, resource, people);
        this.context = context;
        this.people = people;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(people.get(position).getName());  // Set only the category name
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(people.get(position).getName());  // Set only the category name
        return textView;
    }
}
