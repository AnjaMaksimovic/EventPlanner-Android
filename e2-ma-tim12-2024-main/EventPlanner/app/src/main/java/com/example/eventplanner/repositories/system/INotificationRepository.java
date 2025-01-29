package com.example.eventplanner.repositories.system;

import com.example.eventplanner.model.enums.NotificationStatus;
import com.example.eventplanner.model.system.Notification;

import java.util.ArrayList;

public interface INotificationRepository {
    public void Create(Notification notification);
    public void GetAllByReceiver(String receiverId, GetNotificationsCallback callback);
    public void Update(String id, NotificationStatus newStatus, GetNotificationsCallback callback);

    public interface GetNotificationsCallback{
        default void OnGetNotifications(ArrayList<Notification> notifications){}
        default void OnResult(boolean contains){}
    }
}