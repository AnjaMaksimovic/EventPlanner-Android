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
import com.example.eventplanner.activities.categories.CreateCategoryActivity;
import com.example.eventplanner.activities.events.EventTypeCreationActivity;
import com.example.eventplanner.databinding.FragmentCategoriesPageBinding;
import com.example.eventplanner.databinding.FragmentEventTypeBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.events.EventTypeFragment;
import com.example.eventplanner.fragments.events.EventTypeListFragment;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.Category;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.repositories.CategoryRepository;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesPageFragment extends Fragment {

    public static ArrayList<Category> categories = new ArrayList<>();
    private FragmentCategoriesPageBinding binding;
    private CategoryRepository categoryRepo;

    public CategoriesPageFragment() {
        // Required empty public constructor
    }


    public static CategoriesPageFragment newInstance() {
        CategoriesPageFragment fragment = new CategoriesPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "kreiran CategoriesPageFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //FragmentTransition.to(CategoriesCrudListFragment.newInstance(categories), getActivity(),
          //      false, R.id.scroll_products_list);
        //prepareCategoriesList(categories);
        /*categoryRepo = new CategoryRepository();

        categoryRepo.getAllCategories(new CategoryRepository.CategoryFetchCallback() {
            @Override
            public void onCategoryFetch(ArrayList<Category> fetchedCategories) {
                categories.clear();
                categories.addAll(fetchedCategories);
                Log.d("Evo ti kategorija", categories.toString());
            }

        });*/

        Button addNewCategory = binding.btnAddNew;
        addNewCategory.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), CreateCategoryActivity.class);
            startActivity(intent);
        });

        FragmentTransition.to(CategoriesCrudListFragment.newInstance(categories), getActivity(),
                false, R.id.scroll_products_list);

        return  root;
    }

}