package com.android.finki.mpip.footballdreamteam.exception;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Borce on 13.09.2016.
 */
public class UnProcessableEntityException extends IOException {

    private Response response;

    public UnProcessableEntityException() {
    }

    public UnProcessableEntityException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
