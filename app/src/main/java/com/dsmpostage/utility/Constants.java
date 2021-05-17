package com.dsmpostage.utility;

import android.os.Environment;

import com.amazonaws.regions.Regions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    //v3.0 Live Server
    public static String BASE_URL = "https://bi8mwrv779.execute-api.ap-southeast-2.amazonaws.com/";

    //public static String BASE_URL = "http://192.168.1.204/api/";
//    https://bi8mwrv779.execute-api.ap-southeast-2.amazonaws.com/Beta


    public static String CUSTOMER_KEY="98f13708210194c475687be6106a3b84";
    public static String CUSTOMER_SECRET="fb191b5a87e994fc286b26efeef99c68ea71d461a828212df76b5515c43e8d5d";
    public static String XKEY="8yZpsgTmPr8RS0xIO9Ebb66bzpjdn3Ev4kwh9ZKD";
    public static String TYPE="multipart/form-data; boundary="+new Date().getTime()+"; charset=UTF-8";



    public static String poolID = "ap-southeast-2_Li6mtJEjd";
    public static String clientID = "7ssf34oku1ug890thv8dgpurvc";
    public static String clientSecret = "10kd46djq7j5ge57f48u0ahjk7gibc0q1qglu77ctn0bu52hhck3";
    public static Regions awsRegion = Regions.AP_SOUTHEAST_2;




    public static DateFormat from_df = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat from_df_one = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static DateFormat payment_df_one = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DateFormat to_df = new SimpleDateFormat("dd-MM-yyyy");
    public static DateFormat to_df_ddMMYYY = new SimpleDateFormat("dd-MM-yyyy");







//    http://docstamp.in/spykar/public/index.php/api/Auth/getOTP
}
