package com.example.eventplanner.fragments.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CategoriesSubcategoriesPagerAdapter;
import com.example.eventplanner.adapters.CompanyPagerAdapter;
import com.example.eventplanner.databinding.FragmentCategorySubcategoryBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategorySubcategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategorySubcategoryFragment extends Fragment {

    private FragmentCategorySubcategoryBinding binding;
    public CategorySubcategoryFragment() {
        // Required empty public constructor
    }

    public static CategorySubcategoryFragment newInstance(String param1, String param2) {
        CategorySubcategoryFragment fragment = new CategorySubcategoryFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_category_subcategory, container, false);
        binding = FragmentCategorySubcategoryBinding.inflate(inflater, container, false);

        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        FragmentStateAdapter pagerAdapter = new CategoriesSubcategoriesPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        Log.d("ViewPagerItems", "Number of items: " + pagerAdapter.getItemCount());


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();

        return binding.getRoot();

    }

    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "Categories";
            //case 1:
                //return "Subcategories";
            default:
                return "";
        }
    }
}