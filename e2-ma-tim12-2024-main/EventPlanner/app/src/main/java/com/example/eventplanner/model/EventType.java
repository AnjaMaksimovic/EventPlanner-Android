package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class EventType implements Parcelable {
    private String id;
    private String name;
    private String description;
    private List<Subcategory> subcategories;
    private String isActive;

    public EventType() {
    }

    public EventType(String id, String name, String description, List<Subcategory> subcategories, String isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subcategories = subcategories;
        this.isActive = isActive;
    }

    protected EventType(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        in.readList(subcategories, com.example.eventplanner.model.Subcategory.class.getClassLoader());
        this.isActive = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
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

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "EventType{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subcategories='" + subcategories + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeList(subcategories);
        dest.writeString(isActive);
    }

    public static final Creator<EventType> CREATOR = new Creator<EventType>() {
        @Override
        public EventType createFromParcel(Parcel in) {
            return new EventType(in);
        }

        @Override
        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };
}
