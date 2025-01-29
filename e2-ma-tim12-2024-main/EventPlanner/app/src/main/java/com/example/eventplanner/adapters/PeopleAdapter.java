package com.example.eventplanner.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Person;

import java.util.ArrayList;

public class PeopleAdapter extends ArrayAdapter<Person> {
    private ArrayList<Person> aPersons;
    private OnPersonClickListener mListener;

    public interface OnPersonClickListener {
        void OnPersonClick(Person person);
    }

    public PeopleAdapter(Context context, ArrayList<Person> persons, OnPersonClickListener listener) {
        super(context, R.layout.details_card, persons);
        aPersons = persons;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return aPersons.size();
    }

    @Nullable
    @Override
    public Person getItem(int position) {
        return aPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Person person  = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.details_card,
                    parent, false);
        }
        LinearLayout detailsCard = convertView.findViewById(R.id.details_card_item);
        ImageView image  = convertView.findViewById(R.id.employee_image);
        TextView name = convertView.findViewById(R.id.employee_name);
        TextView lastName = convertView.findViewById(R.id.employee_lastname);
        TextView email = convertView.findViewById(R.id.employee_email);
        Button infoBtn = convertView.findViewById(R.id.show_info_btn);

        if(person != null){
//            image.setImageResource(person.getPhotoPath());
            name.setText(person.getName());
            lastName.setText(person.getLastname());
            email.setText(person.getEmail());
            infoBtn.setOnClickListener(v -> {
                Log.i("PersonListAdapter", "Person selected");
                if(mListener != null){
                    mListener.OnPersonClick(person);
                }
            });
        }
        return convertView;
    }
}
