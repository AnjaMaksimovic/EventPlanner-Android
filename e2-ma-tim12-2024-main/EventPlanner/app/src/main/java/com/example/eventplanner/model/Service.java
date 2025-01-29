package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.EventType;
import com.example.eventplanner.model.enums.ProductState;
import com.example.eventplanner.model.enums.ReservationMethod;
import com.example.eventplanner.model.pricelist.Priceable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service implements Parcelable, Priceable {

    private String id;
    private Category category;
    private Subcategory subcategory;
    private String name;
    private String description;
    private String specifics;
    private double price;
    private double discount;
    private List<String> images;
    private List<EventType> eventTypes;

    private boolean visibility;
    private boolean availability;
    private List<Person> employees;
    private double serviceDurability;
    private int reservationDeadline;
    private int cancellationDeadline;
    private ReservationMethod reservationMethod;
    private boolean deleted;
    private ProductState serviceState;
    private String lastChange;


    public Service(String id, Category category, Subcategory subcategory, String name, String description, String specifics, double price, double discount, List<String> images, List<EventType> eventTypes, boolean visibility, boolean availability, List<Person> employees, double serviceDurability, int reservationDeadline, int cancellationDeadline, ReservationMethod reservationMethod, boolean deleted, ProductState serviceState, String lastChange) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.specifics = specifics;
        this.price = price;
        this.discount = discount;
        this.images = images;
        this.eventTypes = eventTypes;
        this.visibility = visibility;
        this.availability = availability;
        this.employees = employees;
        this.serviceDurability = serviceDurability;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.reservationMethod = reservationMethod;
        this.deleted = deleted;
        this.serviceState = serviceState;
        this.lastChange = lastChange;
    }

    public Service() {
        this.deleted = false;
        this.images = new ArrayList<>();
        this.eventTypes = new ArrayList<>();
    }


    protected Service(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readString();
        }
        category = in.readParcelable(Category.class.getClassLoader());
        subcategory = in.readParcelable(Subcategory.class.getClassLoader());
        name = in.readString();
        description = in.readString();
        specifics = in.readString();
        price = in.readDouble();
        discount = in.readDouble();
        in.readList(images, Integer.class.getClassLoader());
        in.readList(eventTypes, EventType.class.getClassLoader());
        visibility = in.readByte() != 0;
        availability = in.readByte() != 0;
        in.readList(employees, Person.class.getClassLoader());
        serviceDurability = in.readDouble();
        reservationDeadline = in.readInt();
        cancellationDeadline = in.readInt();
        reservationMethod = ReservationMethod.valueOf(in.readString());
        deleted = in.readByte() != 0;
        serviceState = ProductState.valueOf(in.readString());
        lastChange = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecifics() {
        return specifics;
    }

    public void setSpecifics(String specifics) {
        this.specifics = specifics;
    }

    @Override
    public double getPrice() {

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Double.parseDouble(decimalFormat.format(price));
    }

    public void setPrice(double price) {
        this.price = price;
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
        return Double.parseDouble(decimalFormat.format(price - (price * discount / 100)));
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }


    public double getServiceDurability() {
        return serviceDurability;
    }

    public void setServiceDurability(double serviceDurability) {
        this.serviceDurability = serviceDurability;
    }

    public int getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(int reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public ReservationMethod getReservationMethod() {
        return reservationMethod;
    }

    public void setReservationMethod(ReservationMethod reservationMethod) {
        this.reservationMethod = reservationMethod;
    }

    public String getId() {
        return id;
    }

    public List<String> getImages() {
        return images;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public ProductState getServiceState() {
        return serviceState;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setServiceState(ProductState serviceState) {
        this.serviceState = serviceState;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }


    @Override
    public String toString() {
        return "Service{" +
                "id='" + id + '\'' +
                ", category=" + category +
                ", subcategory=" + subcategory +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", specifics='" + specifics + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", images=" + images +
                ", eventTypes=" + eventTypes +
                ", visibility=" + visibility +
                ", availability=" + availability +
                ", employees=" + employees +
                ", serviceDurability=" + serviceDurability +
                ", reservationDeadline=" + reservationDeadline +
                ", cancellationDeadline=" + cancellationDeadline +
                ", reservationMethod=" + reservationMethod +
                ", deleted=" + deleted +
                ", serviceState=" + serviceState +
                ", lastChange='" + lastChange + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(id);
        }
        dest.writeParcelable(category, flags);
        dest.writeParcelable(subcategory, flags);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(specifics);
        dest.writeDouble(price);
        dest.writeDouble(discount);
        dest.writeList(images);
        dest.writeList(eventTypes);
        dest.writeByte((byte) (visibility ? 1 : 0));
        dest.writeByte((byte) (availability ? 1 : 0));
        dest.writeList(employees);
        dest.writeDouble(serviceDurability);
        dest.writeInt(reservationDeadline);
        dest.writeInt(cancellationDeadline);
        dest.writeString(reservationMethod.name());
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(serviceState.name());
        dest.writeString(lastChange);
    }
}
