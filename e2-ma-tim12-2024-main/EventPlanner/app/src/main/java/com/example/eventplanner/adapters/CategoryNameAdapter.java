package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventplanner.model.Category;

import java.util.List;

public class CategoryNameAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categories;

    public CategoryNameAdapter(Context context, int resource, List<Category> categories) {
        super(context, resource, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(categories.get(position).getName());  // Set only the category name
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(categories.get(position).getName());  // Set only the category name
        return textView;
    }
}