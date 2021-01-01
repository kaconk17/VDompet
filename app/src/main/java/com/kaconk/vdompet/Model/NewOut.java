package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.OutTrans;


public class NewOut {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    OutTrans mOut;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setmOut(OutTrans out){
        this.mOut = out;
    }
    public OutTrans getmOut(){
        return mOut;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
