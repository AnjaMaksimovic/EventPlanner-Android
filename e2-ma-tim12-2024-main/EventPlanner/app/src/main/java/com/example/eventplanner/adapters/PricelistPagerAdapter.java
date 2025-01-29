package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.packages.PackagesPageFragment;
import com.example.eventplanner.fragments.packages.PackagesPricelistFragment;
import com.example.eventplanner.fragments.pricelist.PricelistPageFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.example.eventplanner.fragments.products.ProductsPricelistFragment;
import com.example.eventplanner.fragments.reviews.ReviewsPageFragment;
import com.example.eventplanner.fragments.services.ServicesPageFragment;
import com.example.eventplanner.fragments.services.ServicesPricelistFragment;

public class PricelistPagerAdapter extends FragmentStateAdapter {
    public PricelistPagerAdapter(@NonNull PricelistPageFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductsPricelistFragment();
            case 1:
                return new ServicesPricelistFragment();
            case 2:
                return new PackagesPricelistFragment();
            default:
                return new ProductsPricelistFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of fragments in the ViewPager2
    }

}
