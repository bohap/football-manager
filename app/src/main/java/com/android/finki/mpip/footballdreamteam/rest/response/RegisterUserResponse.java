package com.android.finki.mpip.footballdreamteam.rest.response;

import com.android.finki.mpip.footballdreamteam.rest.model.AuthUser;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Borce on 27.07.2016.
 */
public class RegisterUserResponse extends ServerResponse implements Serializable {

    @SerializedName("user")
    private AuthUser user;

    @SerializedName("jwt_token")
    private String token;

    public RegisterUserResponse() {
    }

    public RegisterUserResponse(int code, String status, String message,
                                AuthUser user, String token) {
        super(code, status, message);
        this.user = user;
        this.token = token;
    }

    public AuthUser getUser() {
        return user;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
