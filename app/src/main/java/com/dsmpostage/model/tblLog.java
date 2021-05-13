package com.dsmpostage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tblLog implements Serializable {

    @SerializedName("invoice_code")
    public String No;

    @SerializedName("logged_at")
    public String Time;

    @SerializedName("company_name")
    public String Name;

    @SerializedName("signature_document")
    public String SignView;
}

//{"status":404,"data":[],"message":"Not Found","error":"Not Found"}
