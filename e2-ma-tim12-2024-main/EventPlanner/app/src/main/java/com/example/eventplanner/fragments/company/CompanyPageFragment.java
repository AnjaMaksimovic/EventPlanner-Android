package com.example.eventplanner.fragments.company;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.CompanyPagerAdapter;
import com.example.eventplanner.databinding.FragmentCompanyPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyPageFragment extends Fragment {

    private FragmentCompanyPageBinding binding;

    public CompanyPageFragment() {
        // Required empty public constructor
    }

    public CompanyPageFragment newInstance(String param1, String param2) {
        CompanyPageFragment fragment = new CompanyPageFragment();
        Bundle args = new Bundle();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompanyPageBinding.inflate(inflater, container, false);

        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        FragmentStateAdapter pagerAdapter = new CompanyPagerAdapter(this);
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
                return "Packages";
            case 3:
                return "Comments";
            default:
                return "";
        }
    }
}