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
import com.example.eventplanner.activities.events.EventDetailsActivity;
import com.example.eventplanner.activities.products.ProductDetailsActivity;
import com.example.eventplanner.activities.products.ProductEditingActivity;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private PersonRepository personRepository;
    private Person person;
    private String UserId;
    private ArrayList<Product> aProducts;
    private boolean CheckboxVisibility;
    private boolean ButtonsVisibility;
    private boolean RemoveFavouriteProdVisibility;
    private boolean ProductDetailsVisibility;
    private ProductRepository productRepo;

    public ProductListAdapter(Context context, ArrayList<Product> products, String userId, boolean checkboxVisibility, boolean buttonsVisibility, boolean productDetailsVisibility, boolean removeFavouriteProdVisibility){
        super(context, R.layout.product_card, products);
        aProducts = products;
        CheckboxVisibility = checkboxVisibility;
        ButtonsVisibility = buttonsVisibility;
        ProductDetailsVisibility = productDetailsVisibility;
        RemoveFavouriteProdVisibility = removeFavouriteProdVisibility;
        UserId = userId;
    }

    @Override
    public int getCount() {
        return aProducts.size();
    }


    @Nullable
    @Override
    public Product getItem(int position) {
        return aProducts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card,
                    parent, false);
        }
        LinearLayout productCard = convertView.findViewById(R.id.product_card_item);
        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_title);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView productPrice = convertView.findViewById(R.id.product_price);
        productPrice.setTypeface(null, Typeface.BOLD);
        ImageButton editButton = convertView.findViewById(R.id.btnEdit);
        ImageButton deleteButton = convertView.findViewById(R.id.btnDelete);
        Button removeButton = convertView.findViewById(R.id.btnRemove);

        productRepo = new ProductRepository();

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

        if (RemoveFavouriteProdVisibility) {
            removeButton.setVisibility(View.VISIBLE);
        } else {
            removeButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(v -> {
            Log.i("ShopApp", "Clicked Edit Button for: " + product.getName() +
                    ", id: " + product.getId());
            Intent intent = new Intent(getContext(), ProductEditingActivity.class);
            intent.putExtra("product", product);
            getContext().startActivity(intent);
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("Are you sure you want to delete the product " + product.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            product.setDeleted(true);
                            productRepo.updateProduct(product);

                            aProducts.remove(product);
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

        if(product != null){
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText("Price: " + product.getPrice());
            productCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("ShopApp", "Clicked: " + product.getName() + ", id: " +
                        product.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + product.getName()  +
                        ", id: " + product.getId().toString(), Toast.LENGTH_SHORT).show();

            });
        }

        if (ProductDetailsVisibility){
            productCard.setOnClickListener(v -> {
                Product selectedProduct  = getItem(position);
                if (selectedProduct != null) {
                    Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
                    intent.putExtra("SelectedProduct", selectedProduct);
                    getContext().startActivity(intent);
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
                        if (person != null && product != null) {
                            Log.d("UserProfileActivity", "Product details: " + product.toString());

                            person.getFavouriteProducts().removeIf(p -> p.getId().equals(product.getId()));
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
