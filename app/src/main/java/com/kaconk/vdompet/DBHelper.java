package com.kaconk.vdompet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
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

    private static final String CREATE_TABLE_IN = "CREATE TABLE "+ TABLE_IN+" (id_in TEXT PRIMARY KEY, id_dompet TEXT, tgl_in DATETIME, jumlah_in NUMBER, ket_in TEXT)";
    private static final String CREATE_TABLE_OUT = "CREATE TABLE "+ TABLE_OUT+" (id_out TEXT PRIMARY KEY, id_dompet TEXT, tgl_out DATETIME, jumlah_out NUMBER, ket_out TEXT)";
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

    public List<Dompet> getAllDompet(int id_user){
        List<Dompet> dompet = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_DOMPET+" WHERE id_user = "+ id_user;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                Dompet dp = new Dompet();
                dp.setId_dompet(c.getString((c.getColumnIndex("id_dompet"))));
                dp.setId_user(c.getInt(c.getColumnIndex("id_user")));
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
        dp.setId_user(c.getInt(c.getColumnIndex("id_user")));
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
        db.delete(TABLE_IN,  "id_dompet = ?",
                new String[] { id_dompet });
        db.delete(TABLE_OUT,  "id_dompet = ?",
                new String[] { id_dompet });
        db.delete(TABLE_DOMPET,  "id_dompet = ?",
                new String[] { id_dompet });
        return true;
    }

    public String createIn(InTrans in, String id_dompet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String id = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
           date = sdf.parse(in.getTgl_in());
        }catch (ParseException e){
            e.printStackTrace();
        }
        values.put("id_in",id);
        values.put("id_dompet",id_dompet);
        values.put("tgl_in", sdf.format(date));
        values.put("jumlah_in",in.getJumlah());
        values.put("ket_in", in.getKet_in());

        db.insert(TABLE_IN, null , values);

        return id;
    }

    public List<InTrans> getInTrans(String tgl1, String tgl2, Dompet domp){
        List<InTrans> inAll = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_IN+" WHERE id_dompet = '"+ domp.getId_dompet()+"' AND tgl_in BETWEEN '"+tgl1+" 00:00:00'"+" AND '"+tgl2+" 23:59:59' ORDER BY tgl_in DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                InTrans in = new InTrans();
                in.setId_in(c.getString(c.getColumnIndex("id_in")));
                in.setId_dompet(c.getString(c.getColumnIndex("id_dompet")));
                in.setTgl_in(c.getString(c.getColumnIndex("tgl_in")));
                in.setJumlah(c.getDouble(c.getColumnIndex("jumlah_in")));
                in.setKet_in(c.getString(c.getColumnIndex("ket_in")));
                inAll.add(in);
            }while (c.moveToNext());
        }
        return inAll;
    }

    public InTrans getIn(String id_in){
        String selectQuery = "SELECT * FROM "+ TABLE_IN+" WHERE id_in = '"+ id_in+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c != null)
            c.moveToFirst();
        InTrans in = new InTrans();
        in.setId_in(c.getString(c.getColumnIndex("id_in")));
        in.setId_dompet(c.getString(c.getColumnIndex("id_dompet")));
        in.setTgl_in(c.getString(c.getColumnIndex("tgl_in")));
        in.setJumlah(c.getDouble(c.getColumnIndex("jumlah_in")));
        in.setKet_in(c.getString(c.getColumnIndex("ket_in")));
        return in;
    }

    public boolean updateIn(InTrans in){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("tgl_in", in.getTgl_in());
        values.put("jumlah_in",in.getJumlah());
        values.put("ket_in",in.getKet_in());

        db.update(TABLE_IN, values,"id_in = ?", new String[]{in.getId_in()});
        return true;
    }
    public boolean deleteIn(String id_in){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IN,  "id_in = ?",
                new String[] { id_in });
        return true;
    }

    public String createOut(OutTrans out, String id_dompet){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String id = UUID.randomUUID().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try{
            date = sdf.parse(out.getTgl_out());
        }catch (ParseException e){
            e.printStackTrace();
        }
        values.put("id_out",id);
        values.put("id_dompet",id_dompet);
        values.put("tgl_out", sdf.format(date));
        values.put("jumlah_out",out.getJumlah());
        values.put("ket_out", out.getKet_out());

        db.insert(TABLE_OUT, null , values);

        return id;
    }
    public List<OutTrans> getOutTrans(String tgl1, String tgl2, Dompet dompet){
        List<OutTrans> outAll = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_OUT+" WHERE id_dompet = '"+ dompet.getId_dompet()+"' AND tgl_out BETWEEN '"+tgl1+" 00:00:00'"+" AND '"+tgl2+" 23:59:59' ORDER BY tgl_out DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                OutTrans out = new OutTrans();
                out.setId_out(c.getString(c.getColumnIndex("id_out")));
                out.setId_dompet(c.getString(c.getColumnIndex("id_dompet")));
                out.setTgl_out(c.getString(c.getColumnIndex("tgl_out")));
                out.setJumlah(c.getDouble(c.getColumnIndex("jumlah_out")));
                out.setKet_out(c.getString(c.getColumnIndex("ket_out")));
                outAll.add(out);
            }while (c.moveToNext());
        }
        return outAll;
    }

    public OutTrans getOut(String id_out){
        String selectQuery = "SELECT * FROM "+ TABLE_OUT+" WHERE id_out = '"+ id_out+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c != null)
            c.moveToFirst();
       OutTrans out = new OutTrans();
        out.setId_out(c.getString(c.getColumnIndex("id_out")));
        out.setId_dompet(c.getString(c.getColumnIndex("id_dompet")));
        out.setTgl_out(c.getString(c.getColumnIndex("tgl_out")));
        out.setJumlah(c.getDouble(c.getColumnIndex("jumlah_out")));
        out.setKet_out(c.getString(c.getColumnIndex("ket_out")));
        return out;
    }

    public boolean updateOut(OutTrans out){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("tgl_out", out.getTgl_out());
        values.put("jumlah_out",out.getJumlah());
        values.put("ket_out",out.getKet_out());

        db.update(TABLE_OUT, values,"id_out = ?", new String[]{out.getId_out()});
        return true;
    }

    public boolean deleteOut(String id_out){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OUT,  "id_out = ?",
                new String[] { id_out });
        return true;
    }
    public List<History> getHistory(String tgl1, String tgl2, Dompet dompet){
        List<History> hystAll = new ArrayList<>();
        String selectQuery = "SELECT id_dompet, tgl_in as tgl, ket_in as ket, jumlah_in as jumlah, 'IN' as jenis FROM "+ TABLE_IN+" WHERE id_dompet = '"+dompet.getId_dompet()+"' AND tgl_in BETWEEN '"+tgl1+" 00:00:00'"+" AND '"+tgl2+" 23:59:59'  UNION ALL SELECT id_dompet, tgl_out as tgl, ket_out as ket, jumlah_out as jumlah, 'OUT' as jenis FROM "+TABLE_OUT+" WHERE id_dompet = '"+dompet.getId_dompet()+"' AND tgl_out BETWEEN '"+tgl1+" 00:00:00'"+" AND '"+tgl2+" 23:59:59' ORDER BY tgl ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        if (c.moveToFirst()){
            do {
                History history = new History();
                history.setId_dompet(c.getString(c.getColumnIndex("id_dompet")));
                history.setTgl(c.getString(c.getColumnIndex("tgl")));
                history.setKet(c.getString(c.getColumnIndex("ket")));
                history.setJumlah(c.getDouble(c.getColumnIndex("jumlah")));
                history.setJenis(c.getString(c.getColumnIndex("jenis")));
                hystAll.add(history);
            }while (c.moveToNext());
        }
        return hystAll;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

}
