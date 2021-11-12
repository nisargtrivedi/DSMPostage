package com.dsmpostage.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;
import com.dsmpostage.utility.AppPreferences;
import com.dsmpostage.utility.Constants;
import com.dsmpostage.utility.Util;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DSMPostage extends Application {
    final  String TAG= getClass().getName();
    private static DSMPostage mInstance;
    private static Retrofit retrofit=null;

    AppPreferences appPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        appPreferences=new AppPreferences(this);

    }
    public static synchronized DSMPostage getInstance(){
        return mInstance;
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=null;
        if(connectivityManager!=null){
            networkInfo=connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo!=null && networkInfo.isConnected();
    }
    public static Retrofit getRetrofitClient(){
        Interceptor interceptor = new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder().addHeader("Cache-Control", "no-cache");
                request = builder.build();
                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(retrofit==null){
            //okhttp3.OkHttpClient client=new okhttp3.OkHttpClient.Builder().build();
            OkHttpClient.Builder client=new OkHttpClient.Builder();
            client.addInterceptor(logging);
            client.addInterceptor(interceptor);
            client.connectTimeout(60, TimeUnit.SECONDS);
            client.readTimeout(60, TimeUnit.SECONDS);

            retrofit=new Retrofit.Builder()
                    .client(client.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .build();
        }
        return retrofit;
    }
    public void showDialog(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


}
