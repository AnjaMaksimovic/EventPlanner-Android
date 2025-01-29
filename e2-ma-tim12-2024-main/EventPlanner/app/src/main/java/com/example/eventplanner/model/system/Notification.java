package com.example.eventplanner.model.system;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.eventplanner.model.enums.NotificationStatus;

public class Notification implements Parcelable {

    private String id;
    private String senderId;
    private String senderEmail;
    private String receiverId;
    private String title;
    private String text;
    private NotificationStatus status;

    public Notification() {
    }

    public Notification(String senderId, String senderEmail, String receiverId, String title, String text, NotificationStatus status) {
        this.senderEmail = senderEmail;
        this.id = "Notification_" + System.currentTimeMillis();;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.text = text;
        this.status = status;
    }

    protected Notification(Parcel in) {
        id = in.readString();
        senderId = in.readString();
        senderEmail = in.readString();
        receiverId = in.readString();
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = "Notification_" + System.currentTimeMillis();
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(senderId);
        dest.writeString(senderEmail);
        dest.writeString(receiverId);
        dest.writeString(title);
        dest.writeString(text);
    }
}