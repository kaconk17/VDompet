package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.Dompet;

import java.util.List;

public class NewDompet {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    Dompet mdompet;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setDompet(Dompet dompet){
        this.mdompet = dompet;
    }
    public Dompet getDompet(){
        return mdompet;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
