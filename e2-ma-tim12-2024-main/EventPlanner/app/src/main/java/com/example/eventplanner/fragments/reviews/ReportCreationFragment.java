package com.example.eventplanner.fragments.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentReportCreationBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.HomePageFragment;
import com.example.eventplanner.fragments.company.CompanyPageFragment;
import com.example.eventplanner.model.User;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.reviews.Review;
import com.example.eventplanner.model.reviews.ReviewReport;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.UserRepository;
import com.example.eventplanner.repositories.interfaces.IUserRepository;
import com.example.eventplanner.repositories.reviews.ReviewReportRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReportCreationFragment extends Fragment {

    private ReviewReportRepository reportRepository;
    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private FragmentReportCreationBinding binding;
    private static final String ARG_REVIEW = "review";
    private ArrayList<User> admins = new ArrayList<>();
    private Review mReview;
    public ReportCreationFragment() {
    }

    public static ReportCreationFragment newInstance(Review review) {
        ReportCreationFragment fragment = new ReportCreationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REVIEW, review);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReview = (Review) getArguments().getParcelable(ARG_REVIEW);
        }
        reportRepository = new ReviewReportRepository();
        notificationRepository = new NotificationRepository();
        userRepository = new UserRepository();
        userRepository.getAllAdmins( new IUserRepository.GetUsersCallback() {
            @Override
            public void OnGetUsers(ArrayList<User> users) {
                IUserRepository.GetUsersCallback.super.OnGetUsers(users);
                if(users != null) {
                    admins = users;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentReportCreationBinding.inflate(inflater, container, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        TextView date = binding.textViewDate;
        TextView comment = binding.textViewComment;
        TextView grade = binding.textViewGrade;
        Button submitBtn = binding.confirmReportBtn;
        Button cancelBtn = binding.cancelReportBtn;

        date.setText(dateFormat.format(mReview.getDate()));
        comment.setText(mReview.getComment());
        grade.setText(String.valueOf(mReview.getGrade()));

        submitBtn.setOnClickListener(v -> {
            String reporteeEmail = PreferencesManager.getLoggedUserEmail(getContext());
            String reporteeId = PreferencesManager.getLoggedUserId(getContext());
            String reportingReason = binding.reportingReasonEt.getText().toString();
            ReviewReport report = new ReviewReport(mReview.getId(), reportingReason, ReportStatus.reported, reporteeEmail != null ? reporteeEmail : "loginInPlease", reporteeId != null ? reporteeId : "loginPlease");
            reportRepository.Create(report);
            String title = "Review report";
            String text = "I'm reporting this review " + mReview.getComment()  + ", created by " + mReview.getReviewerEmail() + " on " + mReview.getDate();
            for(User admin: admins){
                Notification notification = new Notification(reporteeId, reporteeEmail, admin.getId(), title, text, NotificationStatus.unread);
                notificationRepository.Create(notification);
            }
        });
        cancelBtn.setOnClickListener(v -> {
        });
        return binding.getRoot();
    }
}