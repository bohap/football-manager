package com.android.finki.mpip.footballdreamteam.rest.web;

import com.android.finki.mpip.footballdreamteam.rest.model.AuthUser;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.request.RegisterUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.RegisterUserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Borce on 27.07.2016.
 */
public interface AuthApi {

    @POST("auth/login")
    Call<AuthenticateUserResponse> login(@Body AuthenticateUserRequest user);

    @POST("auth/register")
    Call<RegisterUserResponse> register(@Body RegisterUserRequest user);

    @GET("auth/me")
    Call<AuthUser> user();
}