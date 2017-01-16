package com.android.finki.mpip.footballdreamteam.rest.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 27.07.2016.
 */
public class ServerResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ServerResponse(int code, String status) {
        this(code, status, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
