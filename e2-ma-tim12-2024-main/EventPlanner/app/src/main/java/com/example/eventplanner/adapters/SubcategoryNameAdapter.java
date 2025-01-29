package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;

import java.util.List;

public class SubcategoryNameAdapter extends ArrayAdapter<Subcategory> {
    private Context context;
    private List<Subcategory> subcategories;

    public SubcategoryNameAdapter(Context context, int resource, List<Subcategory> subcategories) {
        super(context, resource, subcategories);
        this.context = context;
        this.subcategories = subcategories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setText(subcategories.get(position).getName());  // Set only the category name
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setText(subcategories.get(position).getName());  // Set only the category name
        return textView;
    }
}
