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

    public RegisterUserResponse() {
    }

    public RegisterUserResponse(int code, String status, String message, AuthUser user) {
        super(code, status, message);
        this.user = user;
    }

    public AuthUser getUser() {
        return user;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }
}
