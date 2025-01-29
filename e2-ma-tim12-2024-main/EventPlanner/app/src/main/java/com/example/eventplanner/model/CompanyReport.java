package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.ReportStatus;

public class CompanyReport implements Parcelable {
    private String id;
    private String companyId;
    private String reporteeEmail;
    private String reportingReason;

    public CompanyReport() {
    }

    public CompanyReport(String id, String companyId, String reportingReason,  String reporteeEmail) {
        this.id = id;
        this.companyId = companyId;
        this.reportingReason = reportingReason;
        this.reporteeEmail = reporteeEmail;
    }

    protected CompanyReport(Parcel in) {
        id = in.readString();
        companyId = in.readString();
        reportingReason = in.readString();
        reporteeEmail = in.readString();
    }

    public static final Creator<CompanyReport> CREATOR = new Creator<CompanyReport>() {
        @Override
        public CompanyReport createFromParcel(Parcel in) {
            return new CompanyReport(in);
        }

        @Override
        public CompanyReport[] newArray(int size) {
            return new CompanyReport[size];
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getReportingReason() {
        return reportingReason;
    }

    public void setReportingReason(String reportingReason) {
        this.reportingReason = reportingReason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(companyId);
        dest.writeString(reportingReason);
        dest.writeString(reporteeEmail);
    }
}
