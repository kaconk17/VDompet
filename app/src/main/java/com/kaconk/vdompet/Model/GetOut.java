package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.OutTrans;


import java.util.List;

public class GetOut {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    List<OutTrans> listOut;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setListOut(List<OutTrans> list){
        this.listOut = list;
    }
    public List<OutTrans> getListOut(){
        return listOut;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
