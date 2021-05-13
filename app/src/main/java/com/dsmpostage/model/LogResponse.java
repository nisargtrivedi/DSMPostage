package com.dsmpostage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LogResponse extends BaseResponse implements Serializable {

    @SerializedName("data")
    public List<tblLog> logList;
}
