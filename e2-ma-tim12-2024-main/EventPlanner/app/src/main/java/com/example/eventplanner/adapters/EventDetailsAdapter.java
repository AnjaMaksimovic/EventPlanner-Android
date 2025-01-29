package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.eventplanner.activities.events.EventDetailsActivity;
import com.example.eventplanner.fragments.SettingsFragment;
import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.fragments.events.EventActivityFragment;
import com.example.eventplanner.fragments.events.EventBudgetFragment;
import com.example.eventplanner.fragments.events.EventGuestFragment;
import com.example.eventplanner.fragments.events.EventInfoFragment;
import com.example.eventplanner.fragments.events.EventProdSuggestionFragment;
import com.example.eventplanner.fragments.products.ProductsPageFragment;
import com.example.eventplanner.model.Event;


public class EventDetailsAdapter extends FragmentStateAdapter {

    private String selectedEventId;
    private Event event;

    public EventDetailsAdapter(@NonNull EventDetailsActivity fragmentActivity, String eventId) {
        super(fragmentActivity);
        this.selectedEventId = eventId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return EventInfoFragment.newInstance(selectedEventId);
            case 1:
                return EventBudgetFragment.newInstance(selectedEventId);
            case 2:
                return EventProdSuggestionFragment.newInstance(selectedEventId);
            case 3:
                return EventActivityFragment.newInstance(selectedEventId);
            case 4:
                return EventGuestFragment.newInstance(selectedEventId);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}