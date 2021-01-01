package com.kaconk.vdompet.Rest;

import android.text.GetChars;

import com.kaconk.vdompet.Dompet;
import com.kaconk.vdompet.InTrans;
import com.kaconk.vdompet.Model.GetDompet;
import com.kaconk.vdompet.Model.GetHystory;
import com.kaconk.vdompet.Model.GetIn;
import com.kaconk.vdompet.Model.GetOut;
import com.kaconk.vdompet.Model.GetUser;
import com.kaconk.vdompet.Model.NewDompet;
import com.kaconk.vdompet.Model.NewIn;
import com.kaconk.vdompet.Model.NewOut;
import com.kaconk.vdompet.OutTrans;
import com.kaconk.vdompet.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers({
            "Content-Type: application/json"
    })
    //Auth api
    @POST("api/auth/signup")
    Call<GetUser> createUser(@Body Users user);
    @POST("api/auth/signin")
    Call<GetUser> signIn(@Body Users user);

    //Dompet api
    @GET("api/dompet/getall")
    Call<GetDompet> getalldompet(@Header("token") String token);
    @POST("api/dompet/create")
    Call<NewDompet> createDompet(@Header("token") String token, @Body Dompet dompet);
    @DELETE("api/dompet/del/{dompetId}")
    Call<NewDompet> delDompet(@Header("token") String token, @Path("dompetId") String iddompet);
    @PUT("api/dompet/update/{dompetId}")
    Call<NewDompet> updatedompet(@Header("token") String token, @Path("dompetId") String id, @Body Dompet dompet);
    @GET("api/dompet/getdompet/{dompetId}")
    Call<NewDompet> getdompet(@Header("token") String token, @Path("dompetId") String id);

    //IN api
    @DELETE("api/in/del/{inId}")
    Call<NewIn> delIn(@Header("token") String token, @Path("inId") String id);
    @GET("api/in/getall")
    Call<GetIn> getallIn(@Header("token") String token, @Query("tgl1") String tgl1,@Query("tgl2") String tgl2, @Query("dompetid") String id);
    @POST("api/in/create")
    Call<NewIn> createIn(@Header("token") String token, @Body InTrans in);
    @PUT("api/in/update/{inId}")
    Call<NewIn> updIn(@Header("token") String token, @Body InTrans in);

    //Out api
    @DELETE("api/out/del/{outId}")
    Call<NewOut> delOut(@Header("token") String token, @Path("outId") String id);
    @GET("api/out/getall")
    Call<GetOut> getallOut(@Header("token") String token, @Query("tgl1") String tgl1, @Query("tgl2") String tgl2, @Query("dompetid") String id);
    @POST("api/out/create")
    Call<NewOut> createOut(@Header("token") String token, @Body OutTrans out);
    @PUT("api/out/update/{outId}")
    Call<NewOut> updOut(@Header("token") String token, @Body OutTrans out);

    //History
    @GET("api/dompet/history/{dompetId}")
    Call<GetHystory> getHist(@Header("token") String token, @Path("dompetId") String id, @Query("tgl1") String tgl1, @Query("tgl2") String tgl2);
}
