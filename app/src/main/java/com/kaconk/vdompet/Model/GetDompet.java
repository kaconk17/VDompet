package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.Dompet;

import java.util.List;

public class GetDompet {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    List<Dompet> listDompet;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
   public void setListDompet(List<Dompet> listdompet){
        this.listDompet = listdompet;
   }
   public List<Dompet> getListDompet(){
        return listDompet;
   }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
