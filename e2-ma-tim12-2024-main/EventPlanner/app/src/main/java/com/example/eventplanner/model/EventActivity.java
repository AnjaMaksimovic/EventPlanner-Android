package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalTime;

public class EventActivity implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private Event event;
    private int stability;

    public EventActivity(String id, String name, String description, String startTime, String endTime, String location, Event event) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.event = event;
    }

    protected EventActivity(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        location = in.readString();
        event = in.readParcelable(Event.class.getClassLoader());
    }

    public EventActivity() {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }
    @Override
    public String toString() {
        return "EventActivity{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime='" + endTime + '\'' +
                ", location='" + location + '\'' +
                ", event='" + event + '\'' +
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
        dest.writeString(description);
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);
        dest.writeString(location);
        if (event != null) {
            dest.writeParcelable(event, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
    }

    public static final Creator<EventActivity> CREATOR = new Creator<EventActivity>() {
        @Override
        public EventActivity createFromParcel(Parcel in) {
            return new EventActivity(in);
        }

        @Override
        public EventActivity[] newArray(int size) {
            return new EventActivity[size];
        }
    };
}
