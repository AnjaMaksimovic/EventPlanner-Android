package com.example.eventplanner.activities.products;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.activities.regRequests.RequestDetailsActivity;
import com.example.eventplanner.adapters.EventNameAdapter;
import com.example.eventplanner.databinding.ActivityProductDetailsBinding;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.enums.Role;
import com.example.eventplanner.repositories.EventRepository;
import com.example.eventplanner.repositories.PersonRepository;
import com.example.eventplanner.repositories.interfaces.IPersonRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private EventRepository eventRepository;
    private PersonRepository personRepository;
    private Person person;
    private ArrayList<Event> events = new ArrayList<>();
    private String userId;
    private ActivityProductDetailsBinding binding;
    private Product selectedProduct;
    private ImageView productImage;
    private List<String> imageUrls;
    private int currentImageIndex;
    private Spinner eventsSpinner;
    private Event selectedEvent;
    private View buyProductButton;
    private View addToFavoriteProductButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = PreferencesManager.getLoggedUserId(this);
        Log.i("ShopApp", "USER ID KOJI JE: " + userId);

        EdgeToEdge.enable(this);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productImage = findViewById(R.id.product_image);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("SelectedProduct")){
            selectedProduct = intent.getParcelableExtra("SelectedProduct");
            Log.i("ShopApp", "Received selectedProduct: " + selectedProduct.toString());

            imageUrls = selectedProduct.getImages();
            currentImageIndex = 0;
            showImage(currentImageIndex);

            binding.productName.setText(selectedProduct.getName());
            binding.productDescription.setText("Description: " + selectedProduct.getDescription());
            binding.productCategory.setText("Category: " + selectedProduct.getCategory().getName());
            binding.productSubcategory.setText("Subcategory: " + selectedProduct.getSubcategory().getName());
            binding.productTypes.setText("Event types: " +  TextUtils.join(", ", selectedProduct.getTypes()));
            binding.productPrice.setText("Price: " + selectedProduct.getPrice() +" $");
            binding.productDiscount.setText("Discount: " + selectedProduct.getDiscount() + "%");
            binding.productAvailability.setText(selectedProduct.isAvailability() ? "Available" : "Not Available");
        }

        eventRepository = new EventRepository();
        eventsSpinner = binding.spinEvent;
        ArrayList<Event> eventsSpin = new ArrayList<>();
        eventRepository.getEventsByOrganiserId(userId, new EventRepository.EventFetchCallback(){
            @Override
            public void onEventFetch(ArrayList<Event> fetchedEvents) {
                if (fetchedEvents != null){
                    eventsSpin.addAll(fetchedEvents);
                    EventNameAdapter eventAdapter = new EventNameAdapter(ProductDetailsActivity.this, android.R.layout.simple_spinner_item, eventsSpin);
                    eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eventsSpinner.setAdapter(eventAdapter);
                    eventsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedEvent = eventsSpin.get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }
        });

        personRepository = new PersonRepository();

        // Check user role and product availability to show or hide views
        if (PreferencesManager.getLoggedUserRole(this) == Role.organizator && selectedProduct.isAvailability()) {
            binding.productEvent.setVisibility(View.VISIBLE);
            binding.spinEvent.setVisibility(View.VISIBLE);
            binding.buyProductButton.setVisibility(View.VISIBLE);
            binding.addToFavoritesButton.setVisibility(View.VISIBLE);
            buyProductButton = findViewById(R.id.buyProductButton);
            addToFavoriteProductButton = binding.addToFavoritesButton;
            buyProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyProduct(selectedEvent, selectedProduct);
                }
            });

            addToFavoriteProductButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFavouritesProduct(selectedProduct);
                }
            });

        } else {
            binding.productEvent.setVisibility(View.GONE);
            binding.spinEvent.setVisibility(View.GONE);
            binding.buyProductButton.setVisibility(View.GONE);
        }
    }

    private void buyProduct(Event event, Product product) {
        if (event != null && product != null) {
            // Add the selected product to the list of bought products for the event
            // Provera da li kategorija već ima listu potkategorija
            if (event.getBoughtProducts() == null) {
                event.setBoughtProducts(new ArrayList<>());
            }
            // Dodavanje nove potkategorije u listu potkategorija izabrane kategorije
            event.getBoughtProducts().add(product);
            Log.i("Proizvodi", event.getBoughtProducts().toString());
            // Ažuriranje kategorije u bazi podataka
            eventRepository.update(event);
            Toast.makeText(ProductDetailsActivity.this, "Bravo", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.e("ProductDetailsActivity", "Event or Product is null.");
        }
    }

    private void addToFavouritesProduct(Product product) {
        personRepository.getPersonByUserId(userId,  new IPersonRepository.GetPersonsCallback() {
            @Override
            public void OnGetPeople(ArrayList<Person> people) {
                if (people != null && !people.isEmpty()) {
                    person = people.get(0);
                    if (person != null && product != null) {

                      //  if (person.getFavouriteProducts() == null) {
                      //      person.setFavouriteProducts(new ArrayList<>());
                      //   }
                       // person.getFavouriteProducts().add(product);
                       // Log.i("Omiljeni proizvodi", person.getFavouriteProducts().toString());

                        person.addFavouriteProduct(product);
                        personRepository.updateForFavouriteList(person.getId(), person);
                        Toast.makeText(ProductDetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        finish();
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

    private void showImage(int index) {
        if (imageUrls != null && index >= 0 && index < imageUrls.size()) {
            Glide.with(this)
                    .load(imageUrls.get(index))
                    .into(productImage);
        }
    }

    public void showPreviousImage(View view) {
        if (currentImageIndex > 0) {
            currentImageIndex--;
            showImage(currentImageIndex);
        }
    }

    public void showNextImage(View view) {
        if (imageUrls != null && currentImageIndex < imageUrls.size() - 1) {
            currentImageIndex++;
            showImage(currentImageIndex);
        }
    }
}
