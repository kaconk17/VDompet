package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.InTrans;

import java.util.List;

public class NewIn {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    InTrans mIn;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setmIn(InTrans in){
        this.mIn = in;
    }
    public InTrans getmIn(){
        return mIn;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
