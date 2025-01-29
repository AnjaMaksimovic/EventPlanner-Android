package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.fragments.SettingsFragment;
import com.example.eventplanner.fragments.categories.CategoriesPageFragment;
import com.example.eventplanner.fragments.categories.CategorySubcategoryFragment;
import com.example.eventplanner.fragments.categories.SubcategoriesPageFragment;
import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.events.EventTypeFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;

public class CategoriesSubcategoriesPagerAdapter extends FragmentStateAdapter {

    public CategoriesSubcategoriesPagerAdapter(@NonNull CategorySubcategoryFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CategoriesPageFragment();
            //case 1:
               // return new SubcategoriesPageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 1; // Number of fragments in the ViewPager2
    }
}
