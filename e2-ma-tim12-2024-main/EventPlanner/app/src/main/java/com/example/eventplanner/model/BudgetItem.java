package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BudgetItem implements Parcelable {
    private Long id;
    private String subcategory;
    private double price;

    public BudgetItem(Long id, String subcategory, double price) {
        this.id = id;
        this.subcategory = subcategory;
        this.price = price;
    }

    public BudgetItem() {
    }

    protected BudgetItem(Parcel in) {
        id = in.readLong();
        subcategory = in.readString();
        price = in.readDouble();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BudgetItem{" +
                "id=" + id +
                ", subcategory='" + subcategory + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(subcategory);
        dest.writeDouble(price);
    }

    public static final Creator<BudgetItem> CREATOR = new Creator<BudgetItem>() {
        @Override
        public BudgetItem createFromParcel(Parcel in) {
            return new BudgetItem(in);
        }

        @Override
        public BudgetItem[] newArray(int size) {
            return new BudgetItem[size];
        }
    };
}
