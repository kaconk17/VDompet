package com.kaconk.vdompet;

public class Dompet {
    String id_dompet;
    int id_user;
    String nama_dompet;
    double saldo;
    String created_at;

    public Dompet(){

    }

    public Dompet(String id_dompet, int id_user, String nama, double saldo){
        this.id_dompet = id_dompet;
        this.id_user = id_user;
        this.nama_dompet = nama;
        this.saldo = saldo;
    }
    public void setId_dompet(String id){
        this.id_dompet = id;
    }
    public void setId_user(int id_user){
        this.id_user = id_user;
    }
    public void setNama_dompet(String nama){
        this.nama_dompet = nama;
    }
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    public void setCreated_at(String create_at){
        this.created_at = create_at;
    }

    public String getId_dompet(){
        return this.id_dompet;
    }
    public int getId_user(){
        return this.id_user;
    }
    public String getNama_dompet(){
        return this.nama_dompet;
    }
    public double getSaldo(){
        return this.saldo;
    }
    public String getCreated_at(){
        return this.created_at;
    }
}
