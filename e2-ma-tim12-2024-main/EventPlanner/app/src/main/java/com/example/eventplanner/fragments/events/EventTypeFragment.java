package com.example.eventplanner.fragments.events;

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
import com.example.eventplanner.activities.events.EventTypeCreationActivity;
import com.example.eventplanner.databinding.FragmentEventTypeBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.products.ProductsListFragment;
import com.example.eventplanner.model.EventType;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTypeFragment extends Fragment {

    public static ArrayList<EventType> eventTypes = new ArrayList<>();
    private FragmentEventTypeBinding binding;

    public EventTypeFragment() {
        // Required empty public constructor
    }


    public static EventTypeFragment newInstance() {
        EventTypeFragment fragment = new EventTypeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "kreiran eventType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventTypeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        prepareEventTypesList(eventTypes);
        Button addNewEventType = binding.btnAddNew;
        addNewEventType.setOnClickListener(v-> {
            Intent intent = new Intent(requireActivity(), EventTypeCreationActivity.class);
            startActivity(intent);
        });

        FragmentTransition.to(EventTypeListFragment.newInstance(eventTypes), getActivity(),
                false, R.id.scroll_products_list);

        return  root;
    }

    private void prepareEventTypesList(ArrayList<EventType> eventTypes){
      //  eventTypes.add(new EventType(1L, "Team building", "Best team buildings for your company", "Photography, Cakes, Drinks", "Unactive"));
      //  eventTypes.add(new EventType(2L, "Birthday party", "Best birthday party", "Drinks, Food", "Active"));
      //  eventTypes.add(new EventType(3L, "Proposal", "Romantic proposal for your better half", "Food, Drone filming", "Active"));
    }
}