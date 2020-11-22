package com.kaconk.vdompet;

public class Users {
    int id;
    String nama;
    String email;
    String password;

    public Users(){

    }

    public Users(String nama, String email, int id, String password){
        this.nama = nama;
        this.email = email;
        this.id = id;
        this.password = password;
    }
    public void setId(int id){
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

    public long getId(){
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

}
