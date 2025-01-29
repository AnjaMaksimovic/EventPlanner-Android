package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class WorkSchedule implements Parcelable {

    private Date start;
    private Date end;
    private ArrayList<WorkingHours> workingHours;

    public WorkSchedule(Date start, Date end, ArrayList<WorkingHours> workingHours) {
        this.start = start;
        this.end = end;
        this.workingHours = workingHours;
    }

    public WorkSchedule(){
    }

    protected WorkSchedule(Parcel in) {
        workingHours = in.createTypedArrayList(WorkingHours.CREATOR);
    }

    public static final Creator<WorkSchedule> CREATOR = new Creator<WorkSchedule>() {
        @Override
        public WorkSchedule createFromParcel(Parcel in) {
            return new WorkSchedule(in);
        }

        @Override
        public WorkSchedule[] newArray(int size) {
            return new WorkSchedule[size];
        }
    };

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public ArrayList<WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setWorkingHours(ArrayList<WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(workingHours);
    }
}