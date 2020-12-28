package com.kaconk.vdompet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBdompet.db";

    //nama tabel
    private static final String TABLE_USERS = "users";
    private static final String TABLE_DOMPET = "dompet";
    private static final String TABLE_IN = "tb_in";
    private static final String TABLE_OUT = "tb_out";

    private static final String KEY_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_ID_DOMPET = "id_dompet";
    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_NAMA_DOMPET = "nama_dompet";
    private static final String KEY_SALDO = "saldo";
    private static final String KEY_CREATE = "create_at";


    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMA
            + " TEXT," + KEY_EMAIL + " TEXT," + KEY_PASSWORD
            + " TEXT" + ")";

    private static final String CREATE_TABLE_DOMPET = "CREATE TABLE "+TABLE_DOMPET+" ("+KEY_ID_DOMPET+" TEXT PRIMARY KEY,"+KEY_ID_USER+" INTEGER,"+KEY_NAMA_DOMPET+" TEXT,"+KEY_SALDO+" NUMERIC,"+ KEY_CREATE+" DATETIME)";

    private static final String CREATE_TABLE_IN = "CREATE TABLE "+ TABLE_IN+" (id_in TEXT PRIMARY KEY, id_dompet TEXT, tgl_in DATETIME, jumlah_in NUMBER)";
    private static final String CREATE_TABLE_OUT = "CREATE TABLE "+ TABLE_OUT+" (id_out TEXT PRIMARY KEY, id_dompet TEXT, tgl_out DATETIME, jumlah_out NUMBER)";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DOMPET);
        db.execSQL(CREATE_TABLE_IN);
        db.execSQL(CREATE_TABLE_OUT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_DOMPET);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_IN);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_OUT);
        onCreate(db);
    }

    public List<String> getalltable(){
        List<String> tb = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
               tb.add(c.getString(0));
                c.moveToNext();
            }
        }
        return tb;
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
            user1.setId(c.getString(c.getColumnIndex(KEY_ID)));
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
                us.setId(c.getString((c.getColumnIndex(KEY_ID))));
                us.setNama(c.getString(c.getColumnIndex(KEY_NAMA)));
                us.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                us.setPassword(c.getString(c.getColumnIndex(KEY_PASSWORD)));

                usr.add(us);
            }while (c.moveToNext());
        }
        return usr;
    }

    public int updateUser(Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, user.getNama());
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_PASSWORD,user.getPassword());

        return db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    public String createDompet(Dompet dompet, Users user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String id = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        values.put("id_dompet",id);
        values.put("id_user",user.getId());
        values.put("nama_dompet", dompet.getNama_dompet());
        values.put("saldo",0);
        values.put("create_at", currentDateandTime);

        db.insert(TABLE_DOMPET, null , values);

        return id;
    }

    public List<Dompet> getAllDompet(String id_user){
        List<Dompet> dompet = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_DOMPET+" WHERE id_user = "+ id_user;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                Dompet dp = new Dompet();
                dp.setId_dompet(c.getString((c.getColumnIndex("id_dompet"))));
                dp.setId_user(c.getString(c.getColumnIndex("id_user")));
                dp.setNama_dompet(c.getString(c.getColumnIndex("nama_dompet")));
                dp.setSaldo(c.getDouble(c.getColumnIndex("saldo")));
                dp.setCreated_at(c.getString(c.getColumnIndex("create_at")));
                dompet.add(dp);
            }while (c.moveToNext());
        }
        return dompet;
    }
    public Dompet getDompet(String id_dompet){
        String selectQuery = "SELECT * FROM "+ TABLE_DOMPET+" WHERE id_dompet = '"+ id_dompet+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c != null)
            c.moveToFirst();

        Dompet dp = new Dompet();
        dp.setId_dompet(c.getString((c.getColumnIndex("id_dompet"))));
        dp.setId_user(c.getString(c.getColumnIndex("id_user")));
        dp.setNama_dompet(c.getString(c.getColumnIndex("nama_dompet")));
        dp.setSaldo(c.getDouble(c.getColumnIndex("saldo")));
        dp.setCreated_at(c.getString(c.getColumnIndex("create_at")));
        return dp;
    }

    public boolean updateDompet(Dompet dompet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("nama_dompet", dompet.getNama_dompet());
        values.put("saldo",dompet.getSaldo());

        db.update(TABLE_DOMPET, values,"id_dompet = ?", new String[]{dompet.getId_dompet()});
        return true;
    }

    public boolean deleteDompet(String id_dompet){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOMPET,  "id_dompet = ?",
                new String[] { id_dompet });
        return true;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
