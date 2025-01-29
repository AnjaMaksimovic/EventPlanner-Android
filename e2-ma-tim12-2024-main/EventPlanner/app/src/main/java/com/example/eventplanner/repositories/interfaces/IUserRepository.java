package com.example.eventplanner.repositories.interfaces;

import com.example.eventplanner.model.User;

import java.util.ArrayList;

public interface IUserRepository {
    public void Create(User user);
    public void GetByEmail(String email, GetUsersCallback callback);
    public void Delete(String userId);
    public void Deactivate(String userId, GetUsersCallback callback);
    public void Activate(String userId, GetUsersCallback callback);
    public void BlockUser(String userId, BlockUserCallback callback);
    public void getAllAdmins(GetUsersCallback callback);

    public interface GetUsersCallback{
        default void OnGetUsers(ArrayList<User> users) {}
        default void onResult(boolean contains) {}
    }

    interface BlockUserCallback {
        void onBlockSuccess();
        void onBlockFailure(Exception e);
    }
}
