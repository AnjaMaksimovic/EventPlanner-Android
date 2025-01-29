package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventGuest implements Parcelable {
    private String id;
    private String name;
    private String surname;
    private String age;
    private boolean isInvited;
    private boolean hasAcceptedInvitation;
    private String specialRequests;
    private int stability;

    public EventGuest(String id, String name, String surname, String age, boolean isInvited, boolean hasAcceptedInvitation, String specialRequests) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.isInvited = isInvited;
        this.hasAcceptedInvitation = hasAcceptedInvitation;
        this.specialRequests = specialRequests;
    }

    protected EventGuest(Parcel in) {
        id = in.readString();
        name = in.readString();
        surname = in.readString();
        age = in.readString();
        isInvited = in.readByte() != 0;
        hasAcceptedInvitation = in.readByte() != 0;
        specialRequests = in.readString();
    }

    public EventGuest() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public boolean hasAcceptedInvitation() {
        return hasAcceptedInvitation;
    }

    public void setAcceptedInvitation(boolean hasAcceptedInvitation) {
        this.hasAcceptedInvitation = hasAcceptedInvitation;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }

    @Override
    public String toString() {
        return "EventGuest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age='" + age + '\'' +
                ", isInvited=" + isInvited +
                ", hasAcceptedInvitation=" + hasAcceptedInvitation +
                ", specialRequests='" + specialRequests + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(age);
        dest.writeByte((byte) (isInvited ? 1 : 0));
        dest.writeByte((byte) (hasAcceptedInvitation ? 1 : 0));
        dest.writeString(specialRequests);
    }

    public static final Creator<EventGuest> CREATOR = new Creator<EventGuest>() {
        @Override
        public EventGuest createFromParcel(Parcel in) {
            return new EventGuest(in);
        }

        @Override
        public EventGuest[] newArray(int size) {
            return new EventGuest[size];
        }
    };
}
