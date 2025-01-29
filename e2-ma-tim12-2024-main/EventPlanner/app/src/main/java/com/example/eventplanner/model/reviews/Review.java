package com.example.eventplanner.model.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class Review implements Parcelable {
    private String id;
    private String companyId;
    private String comment;
    private String reviewerId;
    private String reviewerEmail;
    private int grade;
    private Date date;

    public Review() {
    }

    public Review(String companyId, String comment, String reviewerId, Date date, String reviewerEmail, int grade) {
        this.id = "Review_" + System.currentTimeMillis();
        this.companyId = companyId;
        this.comment = comment;
        this.reviewerId = reviewerId;
        this.date = date;
        this.reviewerEmail = reviewerEmail;
        this.grade = grade;
    }

    protected Review(Parcel in) {
        id = in.readString();
        companyId = in.readString();
        comment = in.readString();
        reviewerId = in.readString();
        reviewerEmail = in.readString();
        grade = in.readInt();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = "Review_" + System.currentTimeMillis();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(companyId);
        dest.writeString(comment);
        dest.writeString(reviewerId);
        dest.writeString(reviewerEmail);
        dest.writeInt(grade);
    }
}
