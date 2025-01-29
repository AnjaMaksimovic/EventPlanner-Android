package com.example.eventplanner.activities.packages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.ActivityPackageCreatingBinding;
import com.example.eventplanner.databinding.ActivityProductCreatingBinding;
import com.example.eventplanner.model.Product;

import java.util.ArrayList;

public class PackageCreatingActivity extends AppCompatActivity {

    private ActivityPackageCreatingBinding binding;
    private ProductListAdapter adapter;
    private ArrayList<Product> mProducts = new ArrayList<>();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPackageCreatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareProductList();

        ImageButton backBtn = binding.packageFormInclude.btnBack;
        backBtn.setOnClickListener(v -> {
            finish();
        });

        Button productsPopupBtn = binding.packageFormInclude.openProductsListPopup;

        productsPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductsPopup(v);
            }
        });

    }

    private void showProductsPopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.products_list_popup, null);

        int desiredWidth = 1050;
        int desiredHeight = 1700;
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                desiredWidth,
                desiredHeight,
                true
        );

        adapter = new ProductListAdapter(this, mProducts, "", true, false, false, false);
        ListView listView = popupView.findViewById(R.id.list_view_products);
        listView.setAdapter(adapter);

        Button addBtn = popupView.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(anchorView);
    }

    private void prepareProductList(){
        //mProducts.add(new Product(1L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //mProducts.add(new Product(2L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
        //mProducts.add(new Product(3L, "Photo book", "Capture your moments with our beautifully designed photo book.", R.drawable.photobook, 640));
        //mProducts.add(new Product(4L, "Photo book set", "Designed to capture every detail and emotion.", R.drawable.photobook_set, 1150));
    }
}