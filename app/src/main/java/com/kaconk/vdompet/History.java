package com.kaconk.vdompet;

import com.google.gson.annotations.SerializedName;

public class History {
   @SerializedName("id_dompet")
    String id_dompet;
   @SerializedName("tgl")
    String tgl;
   @SerializedName("ket")
    String ket;
   @SerializedName("jumlah")
    double jumlah;
   @SerializedName("jenis")
    String jenis;

    public History(){

    }

    public History(String id_dompet, String tgl, String ket, double juml, String jenis){

        this.id_dompet = id_dompet;
        this.tgl = tgl;
        this.ket = ket;
        this.jumlah = juml;
        this.jenis = jenis;
    }

    public void setId_dompet(String id_domp){
        this.id_dompet = id_domp;
    }
    public void setTgl(String tgl){
        this.tgl = tgl;
    }
    public void setKet(String ket){
        this.ket = ket;
    }
    public void setJumlah(double juml){
        this.jumlah = juml;
    }
    public void setJenis(String jenis){
        this.jenis = jenis;
    }

    public String getId_dompet(){
        return this.id_dompet;
    }
    public String getTgl(){
        return this.tgl;
    }
    public String getKet(){
        return this.ket;
    }
    public double getJumlah(){
        return this.jumlah;
    }
    public String getJenis(){
        return this.jenis;
    }
}
