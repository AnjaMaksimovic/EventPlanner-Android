package com.example.eventplanner.fragments.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentProductsPageBinding;
import com.example.eventplanner.databinding.FragmentReviewsPageBinding;
import com.example.eventplanner.fragments.FragmentTransition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsPageFragment extends Fragment {

    private FragmentReviewsPageBinding binding;

    public ReviewsPageFragment() {
        // Required empty public constructor
    }

    public static ReviewsPageFragment newInstance() {
        ReviewsPageFragment fragment = new ReviewsPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewsPageBinding.inflate(inflater, container, false);
        FragmentTransition.to(ReviewsListFragment.newInstance("Company_1"), getActivity(), false, R.id.reviews_list);
        return binding.getRoot();
    }
}