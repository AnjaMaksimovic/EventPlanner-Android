package com.example.eventplanner.fragments.products;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.products.ProductCreatingActivity;
import com.example.eventplanner.adapters.ProductListAdapter;
import com.example.eventplanner.databinding.FragmentAllProductsBinding;
import com.example.eventplanner.databinding.FragmentAllProductsListBinding;
import com.example.eventplanner.databinding.FragmentProductsListBinding;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.EventTypeRepository;
import com.example.eventplanner.repositories.ProductRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllProductsFragment extends Fragment {

    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<EventType> eventTypes = new ArrayList<EventType>();
    public static ArrayList<Category> categories = new ArrayList<Category>();
    public static ArrayList<Subcategory> subcategories = new ArrayList<Subcategory>();
    private FragmentAllProductsBinding binding;
    private ProductRepository productRepo;
    private CategoryRepository categoryRepo;
    private SubcategoryRepository subcategoryRepo;
    private SearchView searchView;
    private EventTypeRepository eventTypeRepository;

    public AllProductsFragment() {}

    public static AllProductsFragment newInstance() {
        AllProductsFragment fragment = new AllProductsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAllProductsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = binding.searchText;
        String searchText = "";

        FragmentTransition.to(AllProductsListFragment.newInstance(products, searchText), getActivity(),
                false, R.id.scroll_products_list);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

        eventTypeRepository = new EventTypeRepository();
        eventTypeRepository.getAllEventTypes(new EventTypeRepository.EventTypeFetchCallback() {
            @Override
            public void onEventTypeFetch(ArrayList<EventType> fetchedEventTypes) {
                eventTypes.clear();
                eventTypes.addAll(fetchedEventTypes);
            }
        });

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("ShopApp", "Bottom Sheet Dialog");


            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenProductsSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.products_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);

            LinearLayout eventTypesLayout = dialogView.findViewById(R.id.event_types_layout);

            for (EventType eventType : eventTypes) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                radioButton.setText(eventType.getName());
                eventTypesLayout.addView(radioButton);
            }

            categoryRepo = new CategoryRepository();
            categoryRepo.getAllCategories(new CategoryRepository.CategoryFetchCallback() {
                @Override
                public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                    categories.clear();
                    categories.addAll(fetchedCategories);

                    String[] categoryNames = new String[categories.size()];
                    for (int i = 0; i < categories.size(); i++) {
                        categoryNames[i] = categories.get(i).getName();
                    }

                    Spinner btnCategory = dialogView.findViewById(R.id.btnCategory);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames);
                    btnCategory.setAdapter(adapter);
                }
            });

            subcategoryRepo = new SubcategoryRepository();
            subcategoryRepo.getAllSubcategories(new SubcategoryRepository.SubcategoryFetchCallback() {
                @Override
                public void onSubcategoryFetch(ArrayList<Subcategory> fetchedSubcategories) {
                    subcategories.clear();
                    subcategories.addAll(fetchedSubcategories);

                    String[] subcategoryNames = new String[subcategories.size()];
                    for (int i = 0; i < subcategories.size(); i++) {
                        subcategoryNames[i] = subcategories.get(i).getName();
                    }

                    Spinner btnSubcategory = dialogView.findViewById(R.id.btnSubcategory);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, subcategoryNames);
                    btnSubcategory.setAdapter(adapter);
                }
            });

            bottomSheetDialog.show();
        });

        return root;
    }

    private void updateProductsList(String query){
        FragmentTransition.to(AllProductsListFragment.newInstance(products, query), getActivity(),
                false, R.id.scroll_products_list);
    }
}