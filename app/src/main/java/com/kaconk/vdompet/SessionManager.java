package com.kaconk.vdompet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "UserPref";
    private static final String IS_LOGIN = "IsLoggedin";
    public static final String KEY_NAME = "nama";
    public static final String KEY_EMAIL = "email";

    public static final String KEY_ID = "id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void CreateLoginSession(String nama, String email, int id){
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_NAME, nama);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);


        editor.putInt(KEY_ID, id);
        editor.commit();
    }

    public Users getUserDetails(){
       Users usr = new Users();
        // user name
        usr.setNama(pref.getString(KEY_NAME, null));

        // user email id
        usr.setEmail(pref.getString(KEY_EMAIL, null));

        usr.setId(pref.getInt(KEY_ID,0));
        return usr;
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public  void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }
}
