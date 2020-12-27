package com.kaconk.vdompet.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kaconk.vdompet.Users;

import java.util.List;

public class GetUser {
    @SerializedName("status")
    @Expose
    String status;
    @SerializedName("data")
    @Expose
    Users mUser;
    @SerializedName("error")
    @Expose
    String err;

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public Users getUser(){
        return mUser;
    }
    public void setUser(Users listuser){
        this.mUser = listuser;
    }
    public void setErr(String ree){
        this.err = err;
    }
    public String getErr(){
        return err;
    }
}
