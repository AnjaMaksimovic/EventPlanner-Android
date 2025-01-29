package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.model.enums.ProductState;
import com.example.eventplanner.model.pricelist.Priceable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Product implements Parcelable, Priceable {
    private String id;
    private Category category;
    private Subcategory subcategory;
    private String name;
    private String description;
    private List<String> images;
    private double price;
    private double discount;
    private List<EventType> types;
    private boolean availability;
    private boolean visibility;
    private boolean deleted;
    private ProductState productState;
    private String lastChange;

    public Product(String id, Category category, Subcategory subcategory, String name, String description, List<String> images, double price, double discount, List<EventType> types, boolean availability, boolean visibility, boolean deleted, ProductState state, String lastChange) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.images = images;
        this.price = price;
        this.discount = discount;
        this.types = types;
        this.availability = availability;
        this.visibility = visibility;
        this.deleted = deleted;
        this.productState = state;
        this.lastChange = lastChange;
    }

    public Product() {
        this.deleted = false;
        this.images = new ArrayList<>();
    }
    protected Product(Parcel in) {
        id = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        subcategory = in.readParcelable(Subcategory.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        images = new ArrayList<>();
        in.readList(images, Integer.class.getClassLoader());
        price = in.readDouble();
        discount = in.readDouble();
        types = new ArrayList<>();
        in.readList(types, EventType.class.getClassLoader());
        availability = in.readByte() != 0;
        visibility = in.readByte() != 0;
        deleted = in.readByte() != 0;
        productState = in.readParcelable(ProductState.class.getClassLoader());
    }

    public void setProductState(ProductState productState) {
        this.productState = productState;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public ProductState getProductState() {
        return productState;
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

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public List<String> getImages() {
        return images;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public double getPrice() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Double.parseDouble(decimalFormat.format(price));   }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getDiscount() {
        return discount;
    }
    @Override
    public double getPriceWithDiscount() {

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Double.parseDouble(decimalFormat.format(price - (price * discount / 100)));
    }

    public List<EventType> getTypes() {
        return types;
    }

    public boolean isAvailability() {
        return availability;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setTypes(List<EventType> types) {
        this.types = types;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Category getCategory() {
        return category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", category=" + category +
                ", subcategory=" + subcategory +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", price=" + price +
                ", discount=" + discount +
                ", types=" + types +
                ", availability=" + availability +
                ", visibility=" + visibility +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeParcelable(category, flags);
        if (subcategory != null) {
            dest.writeParcelable(subcategory, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(images);
        dest.writeDouble(price);
        dest.writeDouble(discount);
        dest.writeList(types);
        dest.writeByte((byte) (availability ? 1 : 0));
        dest.writeByte((byte) (visibility ? 1 : 0));
        dest.writeByte((byte) (deleted ? 1 : 0));
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
