package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductDetailsActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.activities.services.ServiceDetailsActivity;
import com.example.eventplanner.activities.services.ServiceEditingActivity;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ServiceRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;

import java.util.ArrayList;

public class ServiceListAdapter extends ArrayAdapter<Service> {

    private PersonRepository personRepository;
    private Person person;
    private String UserId;
    private ArrayList<Service> aServices;
    private boolean CheckboxVisibility;
    private boolean ButtonsVisibility;
    private boolean ServiceDetailsVisibility;
    private boolean RemoveFavouriteServiceVisibility;
    private ServiceRepository serviceRepo;

    public ServiceListAdapter(Context context, ArrayList<Service> services, String userId, boolean checkboxVisibility, boolean buttonsVisibility, boolean serviceDetailsVisibility, boolean removeFavouriteServiceVisibility){
        super(context, R.layout.service_card, services);
        aServices = services;
        CheckboxVisibility = checkboxVisibility;
        ButtonsVisibility = buttonsVisibility;
        ServiceDetailsVisibility = serviceDetailsVisibility;
        RemoveFavouriteServiceVisibility = removeFavouriteServiceVisibility;
        UserId = userId;
    }

    @Override
    public int getCount() {
        return aServices.size();
    }


    @Nullable
    @Override
    public Service getItem(int position) {
        return aServices.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card,
                    parent, false);
        }
        LinearLayout serviceCard = convertView.findViewById(R.id.service_card_item);
        ImageView imageView = convertView.findViewById(R.id.service_image);
        TextView serviceName = convertView.findViewById(R.id.service_title);
        TextView serviceDescription = convertView.findViewById(R.id.service_description);
        TextView servicePrice = convertView.findViewById(R.id.service_price);
        servicePrice.setTypeface(null, Typeface.BOLD);
        ImageButton editButton = convertView.findViewById(R.id.btnEdit);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);
        Button removeButton = convertView.findViewById(R.id.btnRemove);

        serviceRepo = new ServiceRepository();

        CheckBox checkBox = convertView.findViewById(R.id.item_checkbox);
        if (CheckboxVisibility) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        if (ButtonsVisibility) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

        if (RemoveFavouriteServiceVisibility) {
            removeButton.setVisibility(View.VISIBLE);
        } else {
            removeButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + service.getName() +
                    ", id: " + service.getId());
            Intent intent = new Intent(getContext(), ServiceEditingActivity.class);
            intent.putExtra("service", service);
            getContext().startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the service " + service.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            service.setDeleted(true);
                            serviceRepo.updateService(service);

                            aServices.remove(service);
                            notifyDataSetChanged();

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
        });

        if(service != null){
            //imageView.setImageResource(service.getImage());
            serviceName.setText(service.getName());
            serviceDescription.setText(service.getDescription());
            servicePrice.setText("Price: " + service.getPrice());
            serviceCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + service.getName() + ", id: " +
                        service.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + service.getName()  +
                        ", id: " + service.getId().toString(), Toast.LENGTH_SHORT).show();

            });
        }

        if (ServiceDetailsVisibility){
            serviceCard.setOnClickListener(v -> {
                Service selectedService  = getItem(position);
                if (selectedService != null) {
                    Intent intent = new Intent(getContext(), ServiceDetailsActivity.class);
                    intent.putExtra("SelectedServiceId", selectedService.getId());
                    getContext().startActivity(intent);
                } else {
                    Log.e("ShopApp", "NEMA GA");
                }
            });
        }

        personRepository = new PersonRepository();
        removeButton.setOnClickListener(v -> {
            if(UserId != null){
                personRepository.getPersonByUserId(UserId,  new IPersonRepository.GetPersonsCallback() {
                    @Override
                    public void OnGetPeople(ArrayList<Person> people) {
                        if (people != null && !people.isEmpty()) {
                            person = people.get(0);
                            if (person != null && service != null) {
                                Log.d("UserProfileActivity", "Service details: " + service.toString());

                                person.getFavouriteServices().removeIf(p -> p.getId().equals(service.getId()));
                                personRepository.updateForFavouriteList(person.getId(), person);
                                Log.d("UserProfileActivity", "Person found: " + person.toString());
                            } else {
                                Log.d("UserProfileActivity", "Person is null.");
                            }
                        } else {
                            Log.d("UserProfileActivity", "No person found with the given UserId.");
                        }
                    }
                });
            }
        });


        return convertView;
    }
}
