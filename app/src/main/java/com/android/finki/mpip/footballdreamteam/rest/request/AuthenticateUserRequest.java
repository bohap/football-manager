package com.android.finki.mpip.footballdreamteam.rest.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 27.07.2016.
 */
public class AuthenticateUserRequest implements Serializable {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public AuthenticateUserRequest() {
    }

    public AuthenticateUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}