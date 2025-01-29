package com.example.eventplanner.model.reservations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Reservation implements Parcelable{
    private String id;
    private String organizatorId;
    private String ownerId;
    private String packageId;
    private String organizatorName;
    private String organizatorLastname;
    private Date reservationDate;
    private ReservationStatus status;
    private boolean isPackage;
    private ReservationItem item;

    public Reservation(String organizatorId, String packageId, String organizatorName, String organizatorLastname, Date reservationDate, ReservationStatus status, boolean isPackage, ReservationItem item) {
        this.id = "Reservation_" + System.currentTimeMillis();
        this.organizatorId = organizatorId;
        this.packageId = packageId;
        this.organizatorName = organizatorName;
        this.organizatorLastname = organizatorLastname;
        this.reservationDate = reservationDate;
        this.status = status;
        this.isPackage = isPackage;
        this.item = item;
    }

    public Reservation() {
        this.id = "Reservation_" + System.currentTimeMillis();
    }

    protected Reservation(Parcel in) {
        id = in.readString();
        organizatorId = in.readString();
        packageId = in.readString();
        organizatorName = in.readString();
        organizatorLastname = in.readString();
        isPackage = in.readByte() != 0;
        item = in.readParcelable(ReservationItem.class.getClassLoader());
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizatorId() {
        return organizatorId;
    }

    public void setOrganizatorId(String organizatorId) {
        this.organizatorId = organizatorId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getOrganizatorName() {
        return organizatorName;
    }

    public void setOrganizatorName(String organizatorName) {
        this.organizatorName = organizatorName;
    }

    public String getOrganizatorLastname() {
        return organizatorLastname;
    }

    public void setOrganizatorLastname(String organizatorLastname) {
        this.organizatorLastname = organizatorLastname;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public boolean isPackage() {
        return isPackage;
    }

    public void setPackage(boolean aPackage) {
        isPackage = aPackage;
    }

    public ReservationItem getItem() {
        return item;
    }

    public void setItem(ReservationItem item) {
        this.item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(organizatorId);
        dest.writeString(packageId);
        dest.writeString(organizatorName);
        dest.writeString(organizatorLastname);
        dest.writeByte((byte) (isPackage ? 1 : 0));
        dest.writeParcelable(item, flags);
    }
}