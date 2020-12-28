package com.kaconk.vdompet.Rest;

import android.text.GetChars;

import com.kaconk.vdompet.Dompet;
import com.kaconk.vdompet.Model.GetDompet;
import com.kaconk.vdompet.Model.GetUser;
import com.kaconk.vdompet.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
    Call<Dompet> createDompet(@Header("token") String token, @Body Dompet dompet);
}
