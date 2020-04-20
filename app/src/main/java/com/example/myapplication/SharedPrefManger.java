package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManger {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_FirstNAME = "keyfirstname";
    private static final String KEY_LastNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_USRNAME = "keyusername";
    private static final String KEY_IMAGE = "keyimage";
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
    public boolean userLogin(int id,String fname,String lname,String email,String username) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_FirstNAME, fname);
        editor.putString(KEY_LastNAME,lname);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USRNAME,username);
       // editor.putString(KEY_Image, user.getEmail());
        editor.apply();
        return true;
    }
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USRNAME,null)!=null){
            return true;
        }
        else
            return false;
    }
    public String getusername(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USRNAME,null);
    }
    public String getfirstname(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FirstNAME,null);
    }
    public String getlastname(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LastNAME,null);
    }
    public String getId(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return String.valueOf(sharedPreferences.getInt(KEY_ID,-1));
    }
    public String getemail(){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null);
    }
    public Boolean logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
