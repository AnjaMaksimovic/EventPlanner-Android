package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Subcategory implements Parcelable {
    private String id;
    private String type;
    private String name;
    private String description;
    private String categoryId;
    private boolean deleted;


    public Subcategory() {
        this.deleted = false;
    }
    public Subcategory(String id, String type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Subcategory(String id, String type, String name, String description, String categoryId, boolean deleted) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.deleted = deleted;
    }

    protected Subcategory(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readString();
        }
        type = in.readString();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readString();
        }
        deleted = in.readByte() != 0;

    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getDeleted() {return  deleted;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(id);
        }
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(description);
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeString(categoryId);
        }
        dest.writeByte((byte) (deleted ? 1 : 0));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override
        public Subcategory createFromParcel(Parcel in) {
            return new Subcategory(in);
        }

        @Override
        public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

}

