package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.packages.PackagesPageFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.example.eventplanner.fragments.reviews.ReviewsListFragment;
import com.example.eventplanner.fragments.reviews.ReviewsPageFragment;
import com.example.eventplanner.fragments.services.ServicesPageFragment;

public class CompanyPagerAdapter extends FragmentStateAdapter {

    public CompanyPagerAdapter(@NonNull CompanyPageFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductsPageFragment();
            case 1:
                return new ServicesPageFragment();
            case 2:
                return new PackagesPageFragment();
            case 3:
                return new ReviewsPageFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Number of fragments in the ViewPager2
    }
}
