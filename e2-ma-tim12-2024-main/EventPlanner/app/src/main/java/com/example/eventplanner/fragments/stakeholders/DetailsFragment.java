package com.example.eventplanner.fragments.stakeholders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentDetailsBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.Person;
import com.example.eventplanner.repositories.PersonRepository;

public class DetailsFragment extends Fragment {

    private PersonRepository personRepository;
    private FragmentDetailsBinding binding;

    private static final String ARG_PERSON = "person";

    private Person mPerson;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Person person) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PERSON,person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personRepository = new PersonRepository();
        if (getArguments() != null) {
            mPerson = (Person) getArguments().getParcelable(ARG_PERSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        FragmentTransition.to(InfoFragment.newInstance(mPerson), getActivity(), false, R.id.details_container);

        binding.infoRbtn.setOnClickListener(v -> {
            FragmentTransition.to(InfoFragment.newInstance(mPerson), getActivity(), false, R.id.details_container);
        });
        binding.calendarRbtn.setOnClickListener(v -> {
            FragmentTransition.to(WorkCalendarFragment.newInstance("", ""), getActivity(), false, R.id.details_container);
        });
        return binding.getRoot();
    }
}