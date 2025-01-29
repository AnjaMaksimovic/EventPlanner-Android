package com.example.eventplanner.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventDetailsAdapter;
import com.example.eventplanner.databinding.ActivityEventDetailsBinding;
import com.example.eventplanner.model.Event;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    private ActivityEventDetailsBinding binding;
    private String selectedEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Use view binding to inflate the layout
        binding = ActivityEventDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if(intent!= null && intent.hasExtra("SelectedEventId")){
            selectedEventId = intent.getStringExtra("SelectedEventId");
        }
        setupViewPager();
    }

    private void setupViewPager() {
        ViewPager2 viewPager = binding.viewPagerEvent;
        EventDetailsAdapter eventPageAdapter = new EventDetailsAdapter(this, selectedEventId);
        viewPager.setAdapter(eventPageAdapter);

        TabLayout tabLayout = binding.tabLayoutEvent;
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();
    }

    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return getString(R.string.purchased_items);
            case 1:
                return getString(R.string.budget);
            case 2:
                return getString(R.string.tab_event_info);
            case 3:
                return getString(R.string.tab_event_agenda);
            case 4:
                return getString(R.string.tab_event_guests);
            default:
                return "";
        }
    }
}