package com.example.eventplanner.repositories.reviews.interfaces;

import com.example.eventplanner.model.enums.ReportStatus;
import com.example.eventplanner.model.reviews.ReviewReport;

import java.util.ArrayList;

public interface IReviewReportRepository {
    public void Create(ReviewReport report);
    public void Update(String reportId, ReportStatus newStatus, GetReportsCallback callback);
    public void GetAll(GetReportsCallback callback);

    public interface GetReportsCallback{
        default void OnGetReports(ArrayList<ReviewReport> reports){}
        default void OnResult(boolean contains){}
    }
}