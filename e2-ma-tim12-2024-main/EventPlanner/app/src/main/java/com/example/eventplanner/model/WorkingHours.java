package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.Day;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WorkingHours  implements Parcelable {

    private String start;
    private String end;
    private Day day;

    public WorkingHours() {
    }

    public WorkingHours(String start, String end, Day day) {
        this.start = start;
        this.end = end;
        this.day = day;
    }

    protected WorkingHours(Parcel in) {
        start = in.readString();
        end = in.readString();
    }

    public static final Creator<WorkingHours> CREATOR = new Creator<WorkingHours>() {
        @Override
        public WorkingHours createFromParcel(Parcel in) {
            return new WorkingHours(in);
        }

        @Override
        public WorkingHours[] newArray(int size) {
            return new WorkingHours[size];
        }
    };

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(start);
        dest.writeString(end);
    }
}
