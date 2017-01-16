package com.android.finki.mpip.footballdreamteam.rest.response;

import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.model.AuthUser;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 27.07.2016.
 */
public class AuthenticateUserResponse extends AuthUser implements Serializable {

    @SerializedName("jwt_token")
    private String jwtToken;

    public AuthenticateUserResponse() {
    }

    public AuthenticateUserResponse(int id, String name, String email, String jwtToken) {
        super(id, name, email);
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}