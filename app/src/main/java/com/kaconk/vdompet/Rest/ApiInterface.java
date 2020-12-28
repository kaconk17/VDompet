package com.kaconk.vdompet.Rest;

import android.text.GetChars;

import com.kaconk.vdompet.Dompet;
import com.kaconk.vdompet.Model.GetDompet;
import com.kaconk.vdompet.Model.GetUser;
import com.kaconk.vdompet.Model.NewDompet;
import com.kaconk.vdompet.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("api/auth/signup")
    Call<GetUser> createUser(@Body Users user);
    @POST("api/auth/signin")
    Call<GetUser> signIn(@Body Users user);
    @GET("api/dompet/getall")
    Call<GetDompet> getalldompet(@Header("token") String token);
    @POST("api/dompet/create")
    Call<NewDompet> createDompet(@Header("token") String token, @Body Dompet dompet);
    @DELETE("api/dompet/del/{dompetId}")
    Call<NewDompet> delDompet(@Header("token") String token, @Path("dompetId") String iddompet);
}
