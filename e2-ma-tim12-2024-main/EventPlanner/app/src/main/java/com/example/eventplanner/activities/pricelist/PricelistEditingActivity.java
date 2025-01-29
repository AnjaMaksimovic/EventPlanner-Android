package com.example.eventplanner.activities.pricelist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventplanner.databinding.ActivityPricelistEditingBinding;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Package;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.model.enums.ProductState;
import com.example.eventplanner.model.enums.ReservationMethod;
import com.example.eventplanner.repositories.PackageRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.ServiceRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PricelistEditingActivity extends AppCompatActivity {

    private ActivityPricelistEditingBinding binding;
    private EditText price;
    private EditText discount;
    private Product product;
    private Service service;
    private Package aPackage;
    private PackageRepository packageRepo;
    private ProductRepository productRepo;
    private ServiceRepository serviceRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPricelistEditingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        serviceRepo = new ServiceRepository();
        productRepo = new ProductRepository();
        packageRepo = new PackageRepository();


        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("product")){
            product = intent.getParcelableExtra("product");

            price = binding.pricelistEditFormInclude.priceField;
            price.setText(String.valueOf(product.getPrice())); // Assuming getPrice() returns double or int

            discount = binding.pricelistEditFormInclude.discountField;
            discount.setText(String.valueOf(product.getDiscount()));

            Button submitBtn = binding.pricelistEditFormInclude.submitBtn;
            submitBtn.setOnClickListener(v -> {
                editProductPriceList();
                productRepo.updateProduct(product);
                finish();
            });
        }

        if(intent != null && intent.hasExtra("service")){
            service = intent.getParcelableExtra("service");

            price = binding.pricelistEditFormInclude.priceField;
            price.setText(String.valueOf(service.getPrice())); // Assuming getPrice() returns double or int

            discount = binding.pricelistEditFormInclude.discountField;
            discount.setText(String.valueOf(service.getDiscount()));

            Button submitBtn = binding.pricelistEditFormInclude.submitBtn;
            submitBtn.setOnClickListener(v -> {
                editServicePriceList();
                serviceRepo.updateService(service);
                finish();
            });
        }

        if(intent != null && intent.hasExtra("package")){
            aPackage = intent.getParcelableExtra("package");
            Log.d("PackageContents", "Discount: " + aPackage.getDiscount());
            Log.d("PackageContents", "Service: " + aPackage.toString());

            price = binding.pricelistEditFormInclude.priceField;
            TextView priceLabel = binding.pricelistEditFormInclude.priceLabel;
            priceLabel.setVisibility(View.GONE);
            price.setVisibility(View.GONE);

            List<Service> services = aPackage.getmServices();
            Log.d("PackageContents", "Services lengtgh: " + services.size());

            discount = binding.pricelistEditFormInclude.discountField;
            discount.setText(String.valueOf(aPackage.getDiscount()));

            Button submitBtn = binding.pricelistEditFormInclude.submitBtn;
            submitBtn.setOnClickListener(v -> {
                editPackagePriceList();
                packageRepo.updatePackage(aPackage);
                finish();
            });
        }

        ImageButton backBtn = binding.pricelistEditFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void editProductPriceList() {
        Double productPrice = Double.parseDouble(price.getText().toString());
        Double productDiscount = Double.parseDouble(discount.getText().toString());

        product.setPrice(productPrice);
        product.setDiscount(productDiscount);
        LocalDateTime currentTime = LocalDateTime.now();
        product.setLastChange(currentTime.toString());
    }

    private void editServicePriceList() {
        Double productPrice = Double.parseDouble(price.getText().toString());
        Double productDiscount = Double.parseDouble(discount.getText().toString());

        service.setPrice(productPrice);
        service.setDiscount(productDiscount);
        LocalDateTime currentTime = LocalDateTime.now();
        service.setLastChange(currentTime.toString());
    }

    private void editPackagePriceList() {
        Double packageDiscount = Double.parseDouble(discount.getText().toString());
        List<Product> products = new ArrayList<>();
        List<Service> services =new ArrayList<>();
        List<Person> employees = Arrays.asList(new Person(), new Person());
        List<String> imagesS = Arrays.asList("img1","img2");
        List<EventType> types = Arrays.asList(EventType.charity, EventType.corporate);
        List<String> images = Arrays.asList("image1", "image2");
        Service service1 = new Service("676765", new Category(), new Subcategory(), "Usluga slikanja", "Opis usluge slikanja", "Slikanje i na kucnoj adresi", 199.99, 15.0, imagesS, types, true, true, employees, 4.5, 2, 1, ReservationMethod.automatically, false, ProductState.approved, "2022-06-05");
        Service service2 = new Service("4453", new Category(), new Subcategory(), "Usluga sminkanja", "Opis usluge sminkanja", "Sminkanje i na kucnoj adresi", 199.99, 15.0, imagesS, types, true, true, employees, 4.5, 2, 1, ReservationMethod.automatically, false, ProductState.approved, "2022-06-05");
        services.add(service1);
        services.add(service2);

        Product product1 = new Product("9L9L", new Category(), new Subcategory(), "Proizvod 1", "Opis prozivoda 1", images, 1209.99, 10.0,  types, true, true, false, ProductState.approved, "2024-06-10");
        Product product2 = new Product("8L8L", new Category(), new Subcategory(), "Proizvod 2", "Opis prozivosa 2 ", images, 100.0, 10.0,  types, true, true, false, ProductState.approved, "2024-06-10");
        products.add(product1);
        products.add(product2);

        aPackage.setmProducts(products);
        aPackage.setmServices(services);
        aPackage.setDiscount(packageDiscount);
        LocalDateTime currentTime = LocalDateTime.now();
        aPackage.setLastChange(currentTime.toString());
    }

}