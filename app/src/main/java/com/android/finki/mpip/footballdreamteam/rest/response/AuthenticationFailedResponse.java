package com.android.finki.mpip.footballdreamteam.rest.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 07.08.2016.
 */
public class AuthenticationFailedResponse implements Serializable {

    @SerializedName("errors")
    private List<String> errors;

    public AuthenticationFailedResponse() {
    }

    public AuthenticationFailedResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
