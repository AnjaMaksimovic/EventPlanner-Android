package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.pricelist.Priceable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Package implements Parcelable, Priceable {
    private String id;
    private String name;
    private String description;
    private int image;
    private double price;
    private double discount;
    private boolean deleted;

    private List<Product> mProducts;

    private List<Service> mServices;
    private String lastChange;


    public Package(String id, String name, String description, int image, double price, double discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        deleted = false;
        this.discount = discount;
        this.mProducts = new ArrayList<>();
        this.mServices = new ArrayList<>();
    }

    public Package() {
        this.mProducts = new ArrayList<>();
        this.mServices = new ArrayList<>();
    }
    protected Package(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.readInt();
        price = in.readDouble();
        discount = in.readDouble();
        mProducts = new ArrayList<>();
        mServices = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public double getPriceWithDiscount() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return  Double.parseDouble(decimalFormat.format(getPrice() - (getPrice() * discount / 100)));
    }
    public List<Product> getmProducts() {
        return mProducts;
    }

    public void setmProducts(List<Product> mProducts) {
        this.mProducts = mProducts;
    }

    public List<Service> getmServices() {
        return mServices;
    }

    public void setmServices(List<Service> mServices) {
        this.mServices = mServices;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public double getPrice() {double totalPrice = price;

        if (mProducts != null) {
            for (Product product : mProducts) {
                totalPrice += product.getPrice();
            }
        }

        if (mServices != null) {
            for (Service service : mServices) {
                totalPrice += service.getPrice();
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Double.parseDouble(decimalFormat.format(totalPrice)); }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price='" + price + '\'' +
                ", mProducts=" + mProducts +
                ", mServices=" + mServices +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(image);
        dest.writeDouble(price);
        dest.writeDouble(discount);
        dest.writeList(mProducts);
        dest.writeList(mServices);
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };
}
