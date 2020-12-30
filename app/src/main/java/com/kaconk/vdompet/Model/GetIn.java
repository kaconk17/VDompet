package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.InTrans;

import java.util.List;

public class GetIn {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    List<InTrans> listIn;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setListIn(List<InTrans> listin){
        this.listIn = listin;
    }
    public List<InTrans> getListIn(){
        return listIn;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
