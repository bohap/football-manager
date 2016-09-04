package com.android.finki.mpip.footballdreamteam.exception;

import java.util.List;

/**
 * Created by Borce on 06.08.2016.
 */
public class AuthenticateException extends RuntimeException {

    private List<String> errors;

    public AuthenticateException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
