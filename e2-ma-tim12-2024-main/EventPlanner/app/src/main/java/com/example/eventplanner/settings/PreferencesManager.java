package com.example.eventplanner.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.eventplanner.model.enums.Role;

public class PreferencesManager {
    private static final String PREF_NAME = "pref_file";
    private static final String KEY_USER_EMAIL = "logged_user";
    private static final String KEY_USER_ROLE = "logged_user_role";
    private static final String KEY_USER_ID = "logged_user_id";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setLoggedUser(Context context, String email, String role, String id) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role);
        editor.putString(KEY_USER_ID, id);
        editor.apply(); // Commit changes asynchronously
    }

    public static Role getLoggedUserRole(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String role = sharedPreferences.getString(KEY_USER_ROLE, null);
        if(role != null) return Role.valueOf(role);
        return null; // Return null if no user is found
    }
    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        return email != null; // Return true if email is found, meaning user is logged in
    }

    public static String getLoggedUserEmail(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String email = sharedPreferences.getString(KEY_USER_EMAIL, null);
        return email;
    }

    public static String getLoggedUserId(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public static void clearLoggedUser(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_USER_ID);
        editor.apply(); // Commit changes asynchronously
    }
}