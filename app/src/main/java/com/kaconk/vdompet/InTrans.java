package com.kaconk.vdompet;

import com.google.gson.annotations.SerializedName;

public class InTrans {
    @SerializedName("id_in")
    String id_in;
    @SerializedName("id_dompet")
    String id_dompet;
    @SerializedName("tgl_in")
    String tgl_in;
    @SerializedName("ket_in")
    String ket_in;
    @SerializedName("jumlah")
    double jumlah;

    public InTrans(){

    }

    public InTrans(String id, String id_domp, String tgl_in, String ket, double jumlah){
        this.id_in = id;
        this.id_dompet = id_domp;
        this.tgl_in = tgl_in;
        this.ket_in = ket;
        this.jumlah = jumlah;
    }
    public void setId_in(String id){
        this.id_in = id;
    }
    public void setId_dompet(String id_domp){
        this.id_dompet = id_domp;
    }
    public void setTgl_in(String tgl){
        this.tgl_in=tgl;
    }
    public void setKet_in(String ket){
        this.ket_in = ket;
    }
    public void setJumlah(double juml){
        this.jumlah = juml;
    }
    public String getId_in(){
        return this.id_in;
    }
    public String getId_dompet(){
        return this.id_dompet;
    }
    public String getTgl_in(){
        return this.tgl_in;
    }
    public String getKet_in(){
        return  this.ket_in;
    }
    public double getJumlah(){
        return this.jumlah;
    }
}
