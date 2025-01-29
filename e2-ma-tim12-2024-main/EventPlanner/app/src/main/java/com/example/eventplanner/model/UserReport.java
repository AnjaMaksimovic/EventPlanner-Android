package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.ReportStatus;

public class UserReport implements Parcelable {
    private String id;
    private String userId;
    private String userEmail;
    private ReportStatus status;
    private String reportDate;
    private String reporteeId;
    private String reporteeEmail;
    private String reportingReason;

    public UserReport() {
    }

    public UserReport(String id, String userId, String reportingReason,  String reporteeEmail,
                      String userEmail, ReportStatus status, String reportDate, String reporteeId) {
        this.id = id;
        this.userId = userId;
        this.reportingReason = reportingReason;
        this.reporteeEmail = reporteeEmail;
        this.userEmail = userEmail;
        this.status = status;
        this.reportDate = reportDate;
        this.reporteeId = reporteeId;
    }

    protected UserReport(Parcel in) {
        id = in.readString();
        userId = in.readString();
        reportingReason = in.readString();
        reporteeEmail = in.readString();
        userEmail = in.readString();
        reporteeId = in.readString();
        reportDate = in.readString();
    }

    public static final Creator<UserReport> CREATOR = new Creator<UserReport>() {
        @Override
        public UserReport createFromParcel(Parcel in) {
            return new UserReport(in);
        }

        @Override
        public UserReport[] newArray(int size) {
            return new UserReport[size];
        }
    };


    public String getReporteeEmail() {
        return reporteeEmail;
    }

    public void setReporteeEmail(String reporteeEmail) {
        this.reporteeEmail = reporteeEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReportingReason() {
        return reportingReason;
    }

    public void setReportingReason(String reportingReason) {
        this.reportingReason = reportingReason;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getReporteeId() {
        return reporteeId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public void setReporteeId(String reporteeId) {
        this.reporteeId = reporteeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(reportingReason);
        dest.writeString(reporteeEmail);
        dest.writeString(reporteeId);
        dest.writeString(userEmail);
        dest.writeString(reportDate);
    }
}
