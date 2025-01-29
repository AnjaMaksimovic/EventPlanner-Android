package com.example.eventplanner.fragments.pricelist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CompanyPagerAdapter;
import com.example.eventplanner.adapters.PricelistPagerAdapter;
import com.example.eventplanner.databinding.FragmentCompanyPageBinding;
import com.example.eventplanner.databinding.FragmentPricelistPageBinding;
import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.products.ProductsPricelistFragment;
import com.example.eventplanner.fragments.services.ServicesPricelistFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PricelistPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PricelistPageFragment extends Fragment {

    private FragmentPricelistPageBinding binding;

    public PricelistPageFragment() {
        // Required empty public constructor
    }

    public PricelistPageFragment newInstance(String param1, String param2) {
        PricelistPageFragment fragment = new PricelistPageFragment();
        Bundle args = new Bundle();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPricelistPageBinding.inflate(inflater, container, false);

        ViewPager2 viewPager = binding.pricelistViewPager;
        TabLayout tabLayout = binding.tabLayout;

        PricelistPagerAdapter pagerAdapter = new PricelistPagerAdapter(this);
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
                return getString(R.string.products);
            case 1:
                return getString(R.string.services);
            case 2:
                return getString(R.string.packages);
            default:
                return "";
        }
    }
}