package com.example.eventplanner.model.reservations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ReservationItem implements Parcelable {
    private String serviceId;
    private String employeeId;
    private String serviceName;
    private String employeeName;
    private String employeeLastname;

    public ReservationItem(String serviceId, String employeeId, String serviceName, String employeeName, String employeeLastname) {
        this.serviceId = serviceId;
        this.employeeId = employeeId;
        this.serviceName = serviceName;
        this.employeeName = employeeName;
        this.employeeLastname = employeeLastname;
    }

    public ReservationItem() {
    }

    protected ReservationItem(Parcel in) {
        serviceId = in.readString();
        employeeId = in.readString();
        serviceName = in.readString();
        employeeName = in.readString();
        employeeLastname = in.readString();
    }

    public static final Creator<ReservationItem> CREATOR = new Creator<ReservationItem>() {
        @Override
        public ReservationItem createFromParcel(Parcel in) {
            return new ReservationItem(in);
        }

        @Override
        public ReservationItem[] newArray(int size) {
            return new ReservationItem[size];
        }
    };

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeLastname() {
        return employeeLastname;
    }

    public void setEmployeeLastname(String employeeLastname) {
        this.employeeLastname = employeeLastname;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(serviceId);
        dest.writeString(employeeId);
        dest.writeString(serviceName);
        dest.writeString(employeeName);
        dest.writeString(employeeLastname);
    }
}