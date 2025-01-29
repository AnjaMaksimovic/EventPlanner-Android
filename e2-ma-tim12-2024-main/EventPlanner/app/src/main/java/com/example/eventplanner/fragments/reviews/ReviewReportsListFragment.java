package com.example.eventplanner.fragments.reviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ReportListAdapter;
import com.example.eventplanner.databinding.FragmentReviewReportsListBinding;
import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.reviews.ReviewReport;
import com.example.eventplanner.model.system.Notification;
import com.example.eventplanner.repositories.reviews.ReviewReportRepository;
import com.example.eventplanner.repositories.reviews.ReviewRepository;
import com.example.eventplanner.repositories.reviews.interfaces.IReviewReportRepository;
import com.example.eventplanner.repositories.system.NotificationRepository;
import com.example.eventplanner.settings.PreferencesManager;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Optional;

public class ReviewReportsListFragment extends ListFragment {

    public static ArrayList<ReviewReport> reports = new ArrayList<>();
    private ReviewReportRepository reportRepository;
    private ReviewRepository reviewRepository;
    private NotificationRepository notificationRepository;
    private ReportListAdapter adapter;
    private FragmentReviewReportsListBinding binding;
    public ReviewReportsListFragment() {
    }
    public static ReviewReportsListFragment newInstance() {
        ReviewReportsListFragment fragment = new ReviewReportsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportRepository = new ReviewReportRepository();
        reviewRepository = new ReviewRepository();
        notificationRepository = new NotificationRepository();
        prepareReportsList();
        adapter = new ReportListAdapter(getActivity(), reports, new ReportListAdapter.OnReportClickListener() {
            @Override
            public void OnRejectClick(ReviewReport report) {
                if(report == null) return;
                reportRepository.Update(report.getId(), ReportStatus.rejected, new IReviewReportRepository.GetReportsCallback() {
                    @Override
                    public void OnGetReports(ArrayList<ReviewReport> reportsList) {
                        IReviewReportRepository.GetReportsCallback.super.OnGetReports(reportsList);
                        Optional<ReviewReport> result = reportsList.stream().filter(r -> r.getId().equals(report.getId())).findAny();
                        if(result.isPresent()){
                            reports.removeIf(r -> r.getId().equals(report.getId()));
                            reports.add(result.get());
                            adapter.notifyDataSetChanged();

                            String senderId = PreferencesManager.getLoggedUserId(getContext());
                            String senderEmail = PreferencesManager.getLoggedUserEmail(getContext());
                            String title = "Rejection of review report";
                            String text = "Your report has been rejected by " + senderEmail;
                            Notification notification = new Notification(senderId, senderEmail, report.getReporteeId(), title, text, NotificationStatus.unread);
                            notificationRepository.Create(notification);
                        }
                    }
                });
            }
            @Override
            public void OnApproveClick(ReviewReport report) {
                if(report == null) return;
                reportRepository.Update(report.getId(), ReportStatus.approved, new IReviewReportRepository.GetReportsCallback() {
                    @Override
                    public void OnGetReports(ArrayList<ReviewReport> reportsList) {
                        IReviewReportRepository.GetReportsCallback.super.OnGetReports(reportsList);
                        Optional<ReviewReport> result = reportsList.stream().filter(r -> r.getId().equals(report.getId())).findAny();
                        if(result.isPresent()){
                            reports.removeIf(r -> r.getId().equals(report.getId()));
                            reports.add(result.get());
                            adapter.notifyDataSetChanged();
                            reviewRepository.Delete(report.getReviewId());
                        }
                    }
                });
            }
        });
        setListAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewReportsListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void prepareReportsList() {
        reports.clear();
        reportRepository.GetAll(new IReviewReportRepository.GetReportsCallback() {
            @Override
            public void OnGetReports(ArrayList<ReviewReport> reportsList) {
                IReviewReportRepository.GetReportsCallback.super.OnGetReports(reports);
                if(reportsList.isEmpty()) return;
                reports.addAll(reportsList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}