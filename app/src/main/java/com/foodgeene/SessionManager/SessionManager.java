package com.foodgeene.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.foodgeene.MainActivity;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.register.RegisterModel;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";
    public static  String SHARED_PREF_NAME = "my_shared_preff";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user_id){

        editor.putBoolean(LOGIN, true);
        editor.putString(USER_ID, user_id);
        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }


    public void recordRunTime(){
    editor.putLong("lastRun", System.currentTimeMillis());
    editor.commit();

    }

    public void enableNotification(View v) {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.commit();
    }

    public void disableNotification(View v) {
        editor.putBoolean("enabled", false);
        editor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, null));
        return user;
    }


    public void saveUserId(RegisterModel registerModel){

        editor.putString("userid", registerModel.getUsersid());
        editor.putString("getText", registerModel.getText());
        editor.putString("getStatus", registerModel.getStatus());

    }


    public RegisterModel getUserId(){

        return new RegisterModel(sharedPreferences.getString("userid",  null),
                sharedPreferences.getString("getText", null),
                sharedPreferences.getString("getStatus", null));
    }
    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }



}