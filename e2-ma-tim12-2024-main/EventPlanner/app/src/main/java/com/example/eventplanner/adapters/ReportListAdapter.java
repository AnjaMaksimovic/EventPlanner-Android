package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.reviews.ReviewReport;

import java.util.ArrayList;
import java.util.List;

public class ReportListAdapter extends ArrayAdapter<ReviewReport> {

    private ArrayList<ReviewReport> aReports;
    private OnReportClickListener mListener;

    public interface  OnReportClickListener{
        void OnRejectClick(ReviewReport report);
        void OnApproveClick(ReviewReport report);
    }
    public ReportListAdapter(@NonNull Context context, @NonNull ArrayList<ReviewReport> reports, OnReportClickListener listener) {
        super(context, R.layout.report_card, reports);
        aReports = reports;
        mListener = listener;
    }
    @Override
    public int getCount() {
        return aReports.size();
    }
    @Nullable
    @Override
    public ReviewReport getItem(int position) {
        return aReports.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewReport report = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_card,
                    parent, false);
        }
        TextView reportee = convertView.findViewById(R.id.report_reportee);
        TextView reportReason = convertView.findViewById(R.id.report_reason);
        TextView reportStatus = convertView.findViewById(R.id.report_status);
        Button approveBtn = convertView.findViewById(R.id.approve_report_btn);
        Button rejectBtn = convertView.findViewById(R.id.reject_report_btn);

        if(report != null){
            reportee.setText(report.getReporteeEmail());
            reportReason.setText(report.getReportingReason());
            reportStatus.setText(report.getStatus().toString());

            approveBtn.setEnabled(report.getStatus().equals(ReportStatus.reported));
            rejectBtn.setEnabled(report.getStatus().equals(ReportStatus.reported));
            approveBtn.setOnClickListener(v -> {
                Log.i("ReviewReportsListAdapter", "Report approved");
                if(mListener != null){
                    mListener.OnApproveClick(report);
                }
            });
            rejectBtn.setOnClickListener(v -> {
                Log.i("ReviewReportsListAdapter", "Report rejected");
                if(mListener != null){
                    mListener.OnRejectClick(report);
                }
            });
        }
        return convertView;
    }
}