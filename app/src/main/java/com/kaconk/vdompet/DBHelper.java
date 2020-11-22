package com.kaconk.vdompet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBdompet.db";

    //nama tabel
    private static final String TABLE_USERS = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA
            + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD
            + " TEXT" + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
        onCreate(db);
    }

    public long createUser(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, user.getNama());
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_PASSWORD,user.getPassword());

        long user_id = db.insert(TABLE_USERS, null , values);

        return user_id;
    }

    public Users Authenticate(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean stat = false;
        String selectQuery = "SELECT * FROM "+ TABLE_USERS + " WHERE "+ KEY_EMAIL +" = '"+ email+"'";

        Log.e (LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
            Users user1 = new Users();
            user1.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            user1.setNama(c.getString(c.getColumnIndex(KEY_NAMA)));
            user1.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            user1.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));

            if (password.equalsIgnoreCase(user1.password)){
                return user1;
            }
        }

        return null;
    }

    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ TABLE_USERS + " WHERE "+ KEY_EMAIL + " = '" + email+"'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()&& c.getCount()>0){
            return true;
        }
        return false;
    }

    public List<Users> getAllUsers(){
        List<Users> usr = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                Users us = new Users();
                us.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                us.setNama(c.getString(c.getColumnIndex(KEY_NAMA)));
                us.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                us.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));

                usr.add(us);
            }while (c.moveToNext());
        }
        return usr;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
