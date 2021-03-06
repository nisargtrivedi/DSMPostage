package com.dsmpostage.netowork;


import com.dsmpostage.model.LogResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {


    //Live
//    @GET("prod")
//    Call<LogResponse> getData(@Header("Content-Type") String type,
//                              @Header("customer-key") String customerKey,
//                              @Header("customer-secret") String customerSecret,
//                              @Header("x-api-key") String xKey,
//                              @Query("email") String phone);
//
//
//    @Multipart
//    @POST("prod")
//    Call<String> save_updateProfile(
//            @Header("customer-key") String customerKey,
//            @Header("customer-secret") String customerSecret,
//            @Header("x-api-key") String xKey,
//            @Part("invoice_code") RequestBody invoice_code,
//            @Part("system_type") RequestBody system_type,
//            @Part("email") RequestBody email,
//            @Part("document_type") RequestBody document_type,
//            @Part MultipartBody.Part pic);

    //Development
    @GET("prod")
    Call<LogResponse> getData(@Header("Content-Type") String type,
                              @Header("customer-key") String customerKey,
                              @Header("customer-secret") String customerSecret,
                              @Header("x-api-key") String xKey,
                              @Query("email") String phone);


    @Multipart
    @POST("prod")
    Call<String> save_updateProfile(
            @Header("customer-key") String customerKey,
            @Header("customer-secret") String customerSecret,
            @Header("x-api-key") String xKey,
            @Part("invoice_code") RequestBody invoice_code,
            @Part("system_type") RequestBody system_type,
            @Part("email") RequestBody email,
            @Part("document_type") RequestBody document_type,
            @Part MultipartBody.Part pic);








}
