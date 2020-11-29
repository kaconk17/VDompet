package com.kaconk.vdompet;

public class OutTrans {
    String id_out;
    String id_dompet;
    String tgl_out;
    String ket_out;
    double jumlah;

    public OutTrans(){

    }

    public OutTrans(String id, String id_domp, String tgl_in, String ket, double jumlah){
        this.id_out = id;
        this.id_dompet = id_domp;
        this.tgl_out = tgl_in;
        this.ket_out = ket;
        this.jumlah = jumlah;
    }
    public void setId_out(String id){
        this.id_out = id;
    }
    public void setId_dompet(String id_domp){
        this.id_dompet = id_domp;
    }
    public void setTgl_out(String tgl){
        this.tgl_out=tgl;
    }
    public void setKet_out(String ket){
        this.ket_out = ket;
    }
    public void setJumlah(double juml){
        this.jumlah = juml;
    }
    public String getId_out(){
        return this.id_out;
    }
    public String getId_dompet(){
        return this.id_dompet;
    }
    public String getTgl_out(){
        return this.tgl_out;
    }
    public String getKet_out(){
        return  this.ket_out;
    }
    public double getJumlah(){
        return this.jumlah;
    }
}
