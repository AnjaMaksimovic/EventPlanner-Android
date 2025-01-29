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
import com.example.eventplanner.activities.categories.EditSubcategoryActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.activities.regRequests.RequestDetailsActivity;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CompanyRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class RegistrationRequestAdapter extends ArrayAdapter<Company> {
    //private ArrayList<Person> aPeople;
    private Context context;
    private ArrayList<Company> aCompanies;
    private PersonRepository personRepository;
    private CompanyRepository companyRepository;
    public RegistrationRequestAdapter(Context context, ArrayList<Company> companies){
        super(context, R.layout.request_card, companies);
        this.context = context;
        this.aCompanies = companies;
        this.personRepository = new PersonRepository();
        this.companyRepository = new CompanyRepository();
    }
    @Override
    public int getCount() {
        return aCompanies.size();
    }

    @Nullable
    @Override
    public Company getItem(int position) {
        return aCompanies.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Person person = getItem(position);
        Company company = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_card,
                    parent, false);
        }
        LinearLayout personCard = convertView.findViewById(R.id.request_card_item);
        TextView personName = convertView.findViewById(R.id.person_name);
        TextView personEmail = convertView.findViewById(R.id.person_email);
        TextView companyName = convertView.findViewById(R.id.company_name);
        TextView companyEmail = convertView.findViewById(R.id.company_email);
        TextView dateCreated = convertView.findViewById(R.id.date_requested);
        TextView categories = convertView.findViewById(R.id.categories);
        TextView eventTypes = convertView.findViewById(R.id.eventTypes);
        personRepository = new PersonRepository();
        companyRepository = new CompanyRepository();

        Button detailsButton = convertView.findViewById(R.id.details_btn);

        detailsButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + company.getName() +
                    ", id: " + company.getId());
            Intent intent = new Intent(getContext(), RequestDetailsActivity.class);
            intent.putExtra("company", company);
            getContext().startActivity(intent);
        });

        if(company != null){
            personName.setText(company.getOwnerName() + " " + company.getOwnerLastname());
            personEmail.setText("Owner email: " + company.getOwnerEmail());
            companyName.setText("Company: " + company.getName());
            companyEmail.setText("Company email: " + company.getEmail());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date createDate = company.getCreateDate();
            String formattedDate = createDate != null ? dateFormat.format(createDate) : "Unknown date";

            dateCreated.setText("Date requested: " + formattedDate);


            // Prikazivanje naziva podkategorija
            if (company.getCategories() !=null && !company.getCategories().isEmpty()){
                StringBuilder categoryNames = new StringBuilder();
                for (Category category : company.getCategories()){
                    categoryNames.append(category.getName()).append(", ");
                }
                categoryNames.deleteCharAt(categoryNames.length() - 2);
                categories.setText("Categories: " + categoryNames.toString());
            }else{
                categories.setText(" ");
            }

            // Prikazivanje naziva tipova dogadjaja
            if (company.getEventTypes() !=null && !company.getEventTypes().isEmpty()){
                StringBuilder eventNames = new StringBuilder();
                for (EventType e : company.getEventTypes()){
                    eventNames.append(e.getName()).append(", ");
                }
                eventNames.deleteCharAt(eventNames.length() - 2);
                eventTypes.setText("Event types: " + eventNames.toString());
            }else{
                eventTypes.setText(" ");
            }

            personCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + company.getName() + ", id: " +
                        company.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + company.getName()  +
                        ", id: " + company.getId().toString(), Toast.LENGTH_SHORT).show();

            });
        }

        return convertView;
    }

}

