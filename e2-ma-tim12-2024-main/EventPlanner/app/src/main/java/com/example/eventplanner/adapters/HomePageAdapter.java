package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.fragments.HomePageFragment;
import com.example.eventplanner.fragments.SettingsFragment;
import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.packages.AllPackagesFragment;
import com.example.eventplanner.fragments.packages.PackagesPageFragment;
import com.example.eventplanner.fragments.products.AllProductsFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.example.eventplanner.fragments.services.AllServicesFragment;

public class HomePageAdapter extends FragmentStateAdapter {

    public HomePageAdapter(@NonNull HomePageFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllProductsFragment();
            case 1:
                return new AllServicesFragment();
            case 2:
                return new AllPackagesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
