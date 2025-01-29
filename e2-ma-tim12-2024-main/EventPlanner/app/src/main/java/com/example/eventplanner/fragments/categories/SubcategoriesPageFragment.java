package com.example.eventplanner.fragments.categories;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.categories.CreateSubcategoryActivity;
import com.example.eventplanner.activities.events.EventTypeCreationActivity;
import com.example.eventplanner.databinding.FragmentCategoriesPageBinding;
import com.example.eventplanner.databinding.FragmentSubcategoriesPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.Subcategory;
import com.example.eventplanner.repositories.CategoryRepository;
import com.example.eventplanner.repositories.SubcategoryRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubcategoriesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubcategoriesPageFragment extends Fragment {

    public static ArrayList<Subcategory> subcategories = new ArrayList<>();
    private FragmentSubcategoriesPageBinding binding;
    private SubcategoryRepository subcategoryRepository;

    public SubcategoriesPageFragment() {
        // Required empty public constructor
    }


    public static SubcategoriesPageFragment newInstance() {
        SubcategoriesPageFragment fragment = new SubcategoriesPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "kreiran SubcategoriesPageFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubcategoriesPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        subcategoryRepository = new SubcategoryRepository();

        subcategoryRepository.getAllSubcategories(new SubcategoryRepository.SubcategoryFetchCallback() {
            @Override
            public void onSubcategoryFetch(ArrayList<Subcategory> fetchedSubcategories) {
                subcategories.clear();
                subcategories.addAll(fetchedSubcategories);
                Log.d("Evo ti potkategorija", subcategories.toString());
            }

        });

        //prepareSubcategoriesList(subcategories);
        Button addNewCategory = binding.btnAddNew;
        addNewCategory.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), CreateSubcategoryActivity.class);
            startActivity(intent);
        });

        FragmentTransition.to(SubcategoriesCrudListFragment.newInstance(subcategories), getActivity(),
              false, R.id.scroll_products_list);

        return  root;
    }
/*
    private void prepareSubcategoriesList(ArrayList<Subcategory> subcategories){
        subcategories.add(new Subcategory("1L", "Product", "Food for events", "Appetizers, main dishes, desserts. Thematic and seasonal menu. Specialties of the house."));
        subcategories.add(new Subcategory("2L", "Product", "Photos and Albums", "Print photos in different formats. Design and manufacture of personalized photo books and albums"));
        subcategories.add(new Subcategory("3L", "Product", "Beverage", "Alcoholic and non-alcoholic beverages. Special cocktails. Wine lists and craft beers."));
    }*/
}