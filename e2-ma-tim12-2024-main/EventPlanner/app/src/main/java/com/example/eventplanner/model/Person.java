package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Person implements Parcelable{
    private String id;
    private String userId;
    private String name;
    private String lastname;
    private String email;
    private String address;
    private String phoneNumber;
    private String photoPath;

    //These fields are null except for PUP-Z aka Employee
    private ArrayList<WorkSchedule> workSchedule;
    private String companyId;
    private ArrayList<Product> favouriteProducts;
    private ArrayList<Service> favouriteServices;

    public Person() {
        this.id = GenerateId();
    }

    public Person(String userId, String name, String lastname, String email, String address, String phoneNumber, String photoPath, ArrayList<WorkSchedule> workSchedule, String companyId) {
        this.id = GenerateId();
        this.userId = userId;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.photoPath = photoPath;
        this.workSchedule = workSchedule;
        this.companyId = companyId;
        this.favouriteProducts = new ArrayList<Product>();
        this.favouriteServices = new ArrayList<Service>();
    }

    protected Person(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        lastname = in.readString();
        email = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        photoPath = in.readString();
        workSchedule = in.createTypedArrayList(WorkSchedule.CREATOR);
        companyId = in.readString();
        favouriteProducts = in.createTypedArrayList(Product.CREATOR);
        favouriteServices = in.createTypedArrayList(Service.CREATOR);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public ArrayList<WorkSchedule> getWorkSchedule() {
        return workSchedule;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setWorkSchedule(ArrayList<WorkSchedule> workSchedule) {
        this.workSchedule = workSchedule;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public ArrayList<Product> getFavouriteProducts() {
        return favouriteProducts;
    }

    public void setFavouriteProducts(ArrayList<Product> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }

    public ArrayList<Service> getFavouriteServices() {
        return favouriteServices;
    }

    public void setFavouriteServices(ArrayList<Service> favouriteServices) {
        this.favouriteServices = favouriteServices;
    }

    public void addFavouriteProduct(Product product) {
        if (favouriteProducts == null) {
            favouriteProducts = new ArrayList<>();
        }
        favouriteProducts.add(product);
    }

    public void removeFavouriteProduct(Product product) {
        favouriteProducts.remove(product);
    }

    public void addFavouriteService(Service service) {
        if (favouriteServices == null) {
            favouriteServices = new ArrayList<>();
        }
        favouriteServices.add(service);
    }

    public void removeFavouriteService(Service service) {
        favouriteServices.remove(service);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(photoPath);
        dest.writeTypedList(workSchedule);
        dest.writeString(companyId);
        dest.writeTypedList(favouriteProducts);
        dest.writeTypedList(favouriteServices);
    }

    private String GenerateId(){
        return "Person_" + System.currentTimeMillis();
    }
}