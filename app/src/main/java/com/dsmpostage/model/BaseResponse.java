package com.dsmpostage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    @SerializedName("status")
    public int Status;

    @SerializedName("message")
    public String Message;
}
