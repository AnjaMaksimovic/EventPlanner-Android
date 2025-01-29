package com.example.eventplanner.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.eventplanner.adapters.UserReportListAdapter;
import com.example.eventplanner.databinding.FragmentUserReportsBinding;
import com.example.eventplanner.model.UserReport;
import com.example.eventplanner.repositories.UserReportRepository;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserReportsFragment extends Fragment {

    private UserReportRepository reportRepository;
    private ArrayList<UserReport> allReports = new ArrayList<>();;
    private FragmentUserReportsBinding binding;

    public UserReportsFragment() {
        // Required empty public constructor
    }
    public static UserReportsFragment newInstance() {
        UserReportsFragment fragment = new UserReportsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reloadData();

        return root;
    }

    public void reloadData() {
        reportRepository = new UserReportRepository();

        reportRepository.getUserReports(new UserReportRepository.UserReportFetchCallback() {
            @Override
            public void onUserReportFetch(ArrayList<UserReport> reports){
                if (reports != null) {
                    for (UserReport report : reports) {
                        allReports.add(report);
                        Log.d("CompanyInfo", "Reportee email: " + report.getReporteeEmail() + ", User email: " + report.getUserEmail() );
                    }
                    ListView listView = binding.userReportListview;
                    UserReportListAdapter adapter = new UserReportListAdapter(requireContext(), allReports);
                    listView.setAdapter(adapter);
                }
            }
        });
    }
}