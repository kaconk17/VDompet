package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.History;


import java.util.List;

public class GetHystory {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    List<History> listHyst;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setListHyst(List<History> listhyst){
        this.listHyst = listhyst;
    }
    public List<History> getListHyst(){
        return listHyst;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
