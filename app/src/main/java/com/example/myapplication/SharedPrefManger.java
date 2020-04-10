package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.myapplication.Models.User;

public class SharedPrefManger {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_FirstNAME = "keyfirstname";
    private static final String KEY_LastNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_USRNAME = "keyusername";
    private static final String KEY_MOBILE = "keymobile";
    private static final String KEY_ID = "keyid";
    private static SharedPrefManger mInstance;
    private static Context ctx;

    public SharedPrefManger(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManger getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManger(context);
        }
        return mInstance;
    }
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_FirstNAME, user.getFname());
        editor.putString(KEY_LastNAME, user.getLname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USRNAME,user.getUsername());
        editor.putString(KEY_MOBILE,user.getUsername());
       // editor.putString(KEY_Image, user.getEmail());
        editor.apply();
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, null) != null;
    }
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_FirstNAME,null),
                sharedPreferences.getString(KEY_LastNAME,null),
                sharedPreferences.getString(KEY_MOBILE,null),
                sharedPreferences.getString(KEY_USRNAME,null)
        );
    }
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
