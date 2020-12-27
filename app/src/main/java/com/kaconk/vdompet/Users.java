package com.kaconk.vdompet;

import com.google.gson.annotations.SerializedName;

public class Users {
    @SerializedName("id")
    String id;
    @SerializedName("nama")
    String nama;
    @SerializedName("email")
    String email;
    @SerializedName("password")
    String password;
    @SerializedName("token")
    String token;

    public Users(){

    }

    public Users(String nama, String email, String id, String password, String token){
        this.nama = nama;
        this.email = email;
        this.id = id;
        this.password = password;
        this.token = token;
    }
    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }
    public  void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void  setToken(String token){
        this.token = token;
    }

    public String getId(){
        return this.id;
    }

    public String getNama(){
        return this.nama;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
    public String getToken(){
        return this.token;
    }

}
