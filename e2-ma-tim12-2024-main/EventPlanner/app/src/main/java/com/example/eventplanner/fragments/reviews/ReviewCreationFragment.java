package com.example.eventplanner.fragments.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentReviewCreationBinding;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.reservations.Reservation;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.reviews.ReviewRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import org.checkerframework.checker.units.qual.N;

import java.util.Date;

public class ReviewCreationFragment extends Fragment {

    private FragmentReviewCreationBinding binding;
    private ReviewRepository reviewRepository;
    private NotificationRepository notificationRepository;
    private int Rating;
    private static final String ARG_RESERVATION = "reservation";
    private Reservation mReservation;

    public ReviewCreationFragment() {
    }

    public static ReviewCreationFragment newInstance(Reservation reservation) {
        ReviewCreationFragment fragment = new ReviewCreationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_RESERVATION, reservation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReservation = getArguments().getParcelable(ARG_RESERVATION);
        }
        reviewRepository = new ReviewRepository();
        notificationRepository = new NotificationRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewCreationBinding.inflate(inflater, container, false);
        binding.confirmReviewBtn.setOnClickListener(v -> {
            CreateReview();
        });
        binding.reviewCreationRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedButton = group.findViewById(checkedId);
                if(checkedButton == null) return;
                String value = checkedButton.getText().toString().trim();
                Rating = Integer.valueOf(value);
            }
        });
        return binding.getRoot();
    }
    private void CreateReview(){
        String companyId = "Company_1";
        String comment = String.valueOf(binding.reviewComment.getText());
        String reviewerId = PreferencesManager.getLoggedUserId(getContext());
        String reviewerEmail = PreferencesManager.getLoggedUserEmail(getContext());
        Date dateOfReviewing = new Date();
        Review review = new Review(companyId, comment,reviewerId, dateOfReviewing, reviewerEmail, Rating);
        reviewRepository.Create(review);

        String title = "New review of your company";
        String text = "User " + reviewerEmail + " has just left a review on your company";
        Notification notification = new Notification(reviewerId, reviewerEmail, mReservation.getItem().getEmployeeId(), title, text, NotificationStatus.unread);
        notificationRepository.Create(notification);
    }
}