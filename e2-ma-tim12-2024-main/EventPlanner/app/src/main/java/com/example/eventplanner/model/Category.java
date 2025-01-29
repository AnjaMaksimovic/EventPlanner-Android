package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {

    private String id;
    private String name;
    private String description;
    private List<Subcategory> subcategories;
    private boolean deleted;

    public Category(String id, String name, String description, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subcategories = new ArrayList<>();
        this.deleted = deleted;
    }

    public Category(String id, String name, String description, List<Subcategory> subcategories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subcategories = subcategories;
    }

    public Category(){
        this.deleted = false;
    }
    protected Category(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readString();
        }
        name = in.readString();
        description = in.readString();
        //subcategories = in.createTypedArrayList(Subcategory.CREATOR);
        deleted = in.readByte() != 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {return  deleted;}
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

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subcategories='" + subcategories + '\'' +
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
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (deleted ? 1 : 0));
        //dest.writeTypedList(subcategories);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
