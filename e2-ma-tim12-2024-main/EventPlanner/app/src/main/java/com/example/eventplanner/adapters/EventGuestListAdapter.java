package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.events.EventGuestUpdateActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventActivity;
import com.example.eventplanner.model.EventGuest;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.repositories.EventGuestRepository;
import com.example.eventplanner.repositories.EventRepository;

import java.util.ArrayList;

public class EventGuestListAdapter extends ArrayAdapter<EventGuest> {
    private ArrayList<EventGuest> aEventGuests;
    private Event event;
    private EventGuestRepository eventGuestRepository;
    private EventRepository eventRepository;


    public EventGuestListAdapter(Context context, Event event, ArrayList<EventGuest> eventGuests){
        super(context, R.layout.event_guest_card, eventGuests);
        aEventGuests = eventGuests;
        this.event = event;
        this.eventGuestRepository = new EventGuestRepository();
        this.eventRepository = new EventRepository();

        Log.d("EVENT NEKI", "DA VIDIM JEL TU ON ??????????????????? " + event.getId());

    }

    @Override
    public int getCount() {
        return aEventGuests.size();
    }


    @Nullable
    @Override
    public EventGuest getItem(int position) {
        return aEventGuests.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventGuest eventGuest = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_guest_card,
                    parent, false);
        }
        LinearLayout eventGuestCard = convertView.findViewById(R.id.event_guest_card_item);
        TextView eventGuestFullName = convertView.findViewById(R.id.event_guest_fullname);
        TextView eventGuestAge = convertView.findViewById(R.id.event_guest_age);
        TextView eventGuestInvited = convertView.findViewById(R.id.event_guest_invited);
        TextView eventGuestInviteAccepted = convertView.findViewById(R.id.event_guest_invite_accepted);
        TextView eventGuestSpecialRequirements = convertView.findViewById(R.id.event_guest_special_requirements);

        ImageButton editButton = convertView.findViewById(R.id.btnEdit);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);

        if(eventGuest != null){
            eventGuestFullName.setText(eventGuest.getFullName());
            eventGuestAge.setText("Age: " + eventGuest.getAge());
            eventGuestInvited.setText("Invited: " + (eventGuest.isInvited() ? "Yes" : "No"));
            eventGuestInviteAccepted.setText("Invitation accepted: " + (eventGuest.hasAcceptedInvitation() ? "Yes" : "No"));
            eventGuestSpecialRequirements.setText("Special requests: " + eventGuest.getSpecialRequests());
            eventGuestCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + eventGuest.getName() + ", id: " +
                        eventGuest.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + eventGuest.getName()  +
                        ", id: " + eventGuest.getId().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Clicked: ", Toast.LENGTH_SHORT).show();
            });
        }

        deleteButton.setOnClickListener(v -> {
            if (eventGuest != null) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setMessage("Are you sure you want to remove guest " + eventGuest.getFullName() + "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                event.removeEventGuest(eventGuest);
                                aEventGuests.remove(eventGuest);
                                eventGuestRepository.Delete(eventGuest.getId());

                                eventRepository.update(event);

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        editButton.setOnClickListener(v -> {
            if(eventGuest !=null) {
                Log.i("ShopApp", "Clicked Edit Button for: " + eventGuest.getName() +
                        ", id: " + eventGuest.getId());
                Intent intent = new Intent(getContext(), EventGuestUpdateActivity.class);
                intent.putExtra("eventGuest", eventGuest);
                intent.putExtra("eventId", event.getId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
