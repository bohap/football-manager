package com.android.finki.mpip.footballdreamteam.rest.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 29.07.2016.
 */
public class CommentRequest implements Serializable {

    @SerializedName("body")
    private String body;

    public CommentRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
