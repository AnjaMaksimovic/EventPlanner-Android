package com.example.eventplanner.fragments.products;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductCreatingActivity;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.repositories.ProductRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsPageFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    private FragmentProductsPageBinding binding;
    private ProductRepository productRepo;

    private SearchView searchView;
    public ProductsPageFragment() {
        // Required empty public constructor
    }

    public static ProductsPageFragment newInstance() {
        ProductsPageFragment fragment = new ProductsPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = binding.searchText;

        String searchText = "";
        FragmentTransition.to(ProductsListFragment.newInstance(products, searchText), getActivity(),
                false, R.id.scroll_products_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submit
                updateProductsList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    updateProductsList("");
                }
                return false;
            }
        });


        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenProductsSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.products_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        Button addNewProduct = binding.btnAddNew;
        addNewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ProductCreatingActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void updateProductsList(String query) {
        FragmentTransition.to(ProductsListFragment.newInstance(products, query), getActivity(),
                false, R.id.scroll_products_list);
    }
}