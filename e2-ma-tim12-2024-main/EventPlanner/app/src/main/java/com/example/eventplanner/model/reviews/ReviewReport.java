package com.example.eventplanner.model.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.ReportStatus;

public class ReviewReport implements Parcelable {
    private String id;
    private String reviewId;
    private String reporteeEmail;
    private String reporteeId;
    private String reportingReason;
    private ReportStatus status;

    public ReviewReport() {
    }

    public ReviewReport(String reviewId, String reportingReason, ReportStatus status, String reporteeEmail, String reporteeId) {
        this.id = "ReviewReport_" + System.currentTimeMillis();
        this.reviewId = reviewId;
        this.reportingReason = reportingReason;
        this.status = status;
        this.reporteeEmail = reporteeEmail;
        this.reporteeId = reporteeId;
    }

    protected ReviewReport(Parcel in) {
        id = in.readString();
        reviewId = in.readString();
        reportingReason = in.readString();
        reporteeEmail = in.readString();
        reporteeId = in.readString();
    }

    public static final Creator<ReviewReport> CREATOR = new Creator<ReviewReport>() {
        @Override
        public ReviewReport createFromParcel(Parcel in) {
            return new ReviewReport(in);
        }

        @Override
        public ReviewReport[] newArray(int size) {
            return new ReviewReport[size];
        }
    };

    public String getReporteeId() {
        return reporteeId;
    }

    public void setReporteeId(String reporteeId) {
        this.reporteeId = reporteeId;
    }

    public String getReporteeEmail() {
        return reporteeEmail;
    }

    public void setReporteeEmail(String reporteeEmail) {
        this.reporteeEmail = reporteeEmail;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = "ReviewReport_" + System.currentTimeMillis();
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReportingReason() {
        return reportingReason;
    }

    public void setReportingReason(String reportingReason) {
        this.reportingReason = reportingReason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(reviewId);
        dest.writeString(reportingReason);
        dest.writeString(reporteeEmail);
        dest.writeString(reporteeId);
    }
}