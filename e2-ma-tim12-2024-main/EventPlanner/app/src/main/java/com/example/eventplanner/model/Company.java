package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class Company implements Parcelable {

    private String id;
    private String ownerId;
    private String name;
    private String descrtiption;
    private String email;
    private String phoneNumber;
    private String companyAdress;
    private Date createDate;
    private String ownerName;
    private String ownerLastname;
    private String ownerEmail;
    private Boolean isRequestHandled; //false if it isn't
    private ArrayList<Category> categories;
    private ArrayList<EventType> eventTypes;
    private ArrayList<User> employees ;

    public Company() {
        this.id = GenerateId();
    }

    public Company(String ownerId, String name, String descrtiption, String email, String phoneNumber, String companyAdress, Date createDate, String ownerName, String ownerLastname, String ownerEmail, Boolean isRequestHandled, ArrayList<Category> categories, ArrayList<EventType> eventTypes, ArrayList<User> employees) {
        this.id = GenerateId();
        this.ownerId = ownerId;
        this.name = name;
        this.descrtiption = descrtiption;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.companyAdress = companyAdress;
        this.createDate = createDate;
        this.ownerName = ownerName;
        this.ownerLastname = ownerLastname;
        this.ownerEmail = ownerEmail;
        this.isRequestHandled = isRequestHandled;
        this.categories = categories;
        this.eventTypes = eventTypes;
        this.employees = employees;
    }

    protected Company(Parcel in) {
        id = in.readString();
        ownerId = in.readString();
        name = in.readString();
        descrtiption = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        companyAdress = in.readString();
        long tmpDate = in.readLong();
        ownerName = in.readString();
        ownerLastname = in.readString();
        ownerEmail = in.readString();
        isRequestHandled = in.readBoolean();
        createDate = tmpDate == -1 ? null : new Date(tmpDate);
        categories = in.createTypedArrayList(Category.CREATOR);
        eventTypes = in.createTypedArrayList(EventType.CREATOR);
        employees = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(ownerId);
        dest.writeString(name);
        dest.writeString(descrtiption);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(companyAdress);
        dest.writeLong(createDate != null ? createDate.getTime() : -1);
        dest.writeString(ownerName);
        dest.writeString(ownerLastname);
        dest.writeString(ownerEmail);
        dest.writeBoolean(isRequestHandled);
        dest.writeTypedList(categories);
        dest.writeTypedList(eventTypes);
        dest.writeTypedList(employees);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescrtiption() {
        return descrtiption;
    }

    public void setDescrtiption(String descrtiption) {
        this.descrtiption = descrtiption;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyAdress() {
        return companyAdress;
    }

    public void setCompanyAdress(String companyAdress) {
        this.companyAdress = companyAdress;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<User> employees) {
        this.employees = employees;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public Boolean getRequestHandled() {
        return isRequestHandled;
    }

    public void setRequestHandled(Boolean requestHandled) {
        isRequestHandled = requestHandled;
    }

    private String GenerateId(){
        return "Company_" + System.currentTimeMillis();
    }

}
