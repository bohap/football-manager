package com.android.finki.mpip.footballdreamteam.exception;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Borce on 08.09.2016.
 */
public class NotAuthenticatedException extends IOException {

    private Response response;

    public NotAuthenticatedException() {
    }

    public NotAuthenticatedException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
