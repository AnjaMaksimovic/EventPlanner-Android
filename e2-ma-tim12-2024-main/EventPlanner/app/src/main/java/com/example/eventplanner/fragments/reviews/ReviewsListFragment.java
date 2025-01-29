package com.example.eventplanner.fragments.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ReviewsListAdapter;
import com.example.eventplanner.databinding.FragmentReviewsListBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.repositories.reviews.ReviewRepository;
import com.example.eventplanner.repositories.reviews.interfaces.GetReviewsCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ReviewsListFragment extends ListFragment {

    public static ArrayList<Review> reviews = new ArrayList<>();
    private static ArrayList<Review> backupReviews = new ArrayList<>();
    private ReviewRepository reviewRepository;
    private ReviewsListAdapter adapter;
    private FragmentReviewsListBinding binding;
    private static final String ARG_COMPANY = "company";

    private String mCompanyId;

    public ReviewsListFragment() {
    }

    public static ReviewsListFragment newInstance(String companyId) {
        ReviewsListFragment fragment = new ReviewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COMPANY,  companyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCompanyId = getArguments().getString(ARG_COMPANY);
        }
        reviewRepository = new ReviewRepository();
        prepareReviewsList();
        adapter = new ReviewsListAdapter(getActivity(), reviews, new ReviewsListAdapter.OnReviewClickListener() {
            @Override
            public void OnReportClick(Review review) {
                FragmentTransition.to(ReportCreationFragment.newInstance(review),getActivity(), false, R.id.fragment_nav_content_main);
            }
        });
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewsListBinding.inflate(inflater, container, false);
        binding.filterReviewsRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = group.findViewById(checkedId);
                if(checkedButton == null) return;
                String value = checkedButton.getText().toString().trim();
                filterReviews(value);
            }
        });

        return binding.getRoot();
    }
    private void prepareReviewsList(){
        reviews.clear();
        reviewRepository.GetAllByCompany("Company_1", new GetReviewsCallback() {
            @Override
            public void OnReviewsGet(ArrayList<Review> reviewsList) {
                GetReviewsCallback.super.OnReviewsGet(reviewsList);
                if(reviewsList.isEmpty()) return;
                reviews.addAll(reviewsList);
                backupReviews.addAll(reviewsList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void filterReviews(String value) {
        if(value.equals("all")){
           adapter.clear();
           reviews.clear();
           reviews.addAll(backupReviews);
           adapter.notifyDataSetChanged();
           return;
        }
        int numericValue = Integer.valueOf(value);
        if( numericValue < 1 || numericValue > 5) return;
        adapter.clear();
        reviews.clear();
        reviews.addAll(backupReviews.stream()
                .filter(r -> r.getGrade() == numericValue)
                .collect(Collectors.toList()));
        adapter.notifyDataSetChanged();
    }
}