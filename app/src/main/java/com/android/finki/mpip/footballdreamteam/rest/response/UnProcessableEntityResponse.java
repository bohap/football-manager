package com.android.finki.mpip.footballdreamteam.rest.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 07.08.2016.
 */
public class UnProcessableEntityResponse implements Serializable {

    @SerializedName("errors")
    private Map<String, List<String>> errors;

    public UnProcessableEntityResponse() {
    }

    public UnProcessableEntityResponse(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>>  getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }
}

