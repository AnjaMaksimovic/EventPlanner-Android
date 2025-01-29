package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.model.enums.EventType;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Event implements Parcelable {
    private String id;
    private String userId;
    private String eventTypeId;
    private String eventTypeName;
    private String name;
    private String description;
    private int participants;
    private String location;
    private int maxKm;
    private String date;
    private boolean isPrivate;
    private ArrayList<EventActivity> eventActivities;
    private ArrayList<EventGuest> eventGuests;
    private ArrayList<Product> boughtProducts;
    private int stability;

    public Event(String id, String userId, String eventTypeId, String eventTypeName, String name, String description, int participants, String location, int maxKm, String date, boolean isPrivate, ArrayList<Product> products) {
        this.id = id;
        this.userId = userId;
        this.eventTypeId = eventTypeId;
        this.eventTypeName = eventTypeName;
        this.name = name;
        this.description = description;
        this.participants = participants;
        this.location = location;
        this.maxKm = maxKm;
        this.date = date;
        this.isPrivate = isPrivate;
        this.eventActivities = new ArrayList<EventActivity>();
        this.eventGuests = new ArrayList<EventGuest>();
        this.boughtProducts = products;
    }

    public Event() {}

    protected Event(Parcel in) {
        id = in.readString();
        userId = in.readString();
        eventTypeId = in.readString();
        eventTypeName = in.readString();
        name = in.readString();
        description = in.readString();
        participants = in.readInt();
        location = in.readString();
        maxKm = in.readInt();
        date = in.readString();
        isPrivate = in.readByte() != 0;
        eventActivities = in.createTypedArrayList(EventActivity.CREATOR);
        eventGuests = in.createTypedArrayList(EventGuest.CREATOR);
        boughtProducts = in.createTypedArrayList(Product.CREATOR);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(String eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeId = eventTypeName;
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

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxKm() {
        return maxKm;
    }

    public void setMaxKm(int maxKm) {
        this.maxKm = maxKm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public ArrayList<EventActivity> getEventActivities() {
        return eventActivities;
    }

    public void setEventActivities(ArrayList<EventActivity> eventActivities) {
        this.eventActivities = eventActivities;
    }

    public void addEventActivity(EventActivity eventActivity) {
        if (eventActivities == null) {
            eventActivities = new ArrayList<>();
        }
        eventActivities.add(eventActivity);
    }
    public ArrayList<EventGuest> getEventGuests() {
        return eventGuests;
    }

    public void setEventGuests(ArrayList<EventGuest> eventGuests) {
        this.eventGuests = eventGuests;
    }

    public void addEventGuest(EventGuest eventGuest) {
        if (eventGuests == null) {
            eventGuests = new ArrayList<>();
        }
        eventGuests.add(eventGuest);
    }

    public void removeEventGuest(EventGuest eventGuest) {
        eventGuests.remove(eventGuest);
    }

    public ArrayList<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(ArrayList<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventTypeName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", participants=" + participants +
                ", location='" + location + '\'' +
                ", maxKm='" + maxKm + '\'' +
                ", date='" + date + '\'' +
                ", isPrivate=" + isPrivate +
                ", eventActivities=" + eventActivities +
                ", eventGuests=" + eventGuests +
                ", boughtProducts=" + boughtProducts +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(eventTypeName);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(participants);
        dest.writeString(location);
        dest.writeInt(maxKm);
        dest.writeString(date);
        dest.writeByte((byte) (isPrivate ? 1 : 0));
        dest.writeTypedList(eventActivities);
        dest.writeTypedList(eventGuests);
        dest.writeTypedList(boughtProducts);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
