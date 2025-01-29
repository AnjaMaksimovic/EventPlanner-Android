package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubcategorySuggestion implements Parcelable{
    private String id;
    private String name;
    private String description;
    private String categoryId;
    private String subcategoryType;

    public SubcategorySuggestion(String id, String name, String description, String categoryId, String subcategoryType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.subcategoryType = subcategoryType;
    }

    public SubcategorySuggestion() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
    }

    protected SubcategorySuggestion(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        categoryId = in.readString();
        subcategoryType = in.readString();
    }

    public static final Parcelable.Creator<SubcategorySuggestion> CREATOR = new Parcelable.Creator<SubcategorySuggestion>() {
        @Override
        public SubcategorySuggestion createFromParcel(Parcel in) {
            return new SubcategorySuggestion(in);
        }

        @Override
        public SubcategorySuggestion[] newArray(int size) {
            return new SubcategorySuggestion[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(categoryId);
        dest.writeString(subcategoryType);
    }

}
