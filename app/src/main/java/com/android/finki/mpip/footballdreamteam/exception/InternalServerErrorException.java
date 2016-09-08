package com.android.finki.mpip.footballdreamteam.exception;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Borce on 08.09.2016.
 */
public class InternalServerErrorException extends IOException {

    private Response response;

    public InternalServerErrorException() {
    }

    public InternalServerErrorException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
