package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.Role;

public class User implements Parcelable{

    private String id;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private boolean blocked;

    public User() {
        this.id = GenerateId();
    }

    public User(String email, String password, Role role, boolean active) {
        this.id = GenerateId();
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active;
        this.blocked = false;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        password = in.readString();
        active = in.readByte() != 0;
        blocked = in.readByte() != 0;
    }


    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeByte((byte) (blocked ? 1 : 0));
    }

    private String GenerateId() {
        return "User_" + System.currentTimeMillis();
    }
}