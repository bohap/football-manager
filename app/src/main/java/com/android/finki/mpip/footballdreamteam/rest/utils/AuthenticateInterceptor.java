package com.android.finki.mpip.footballdreamteam.rest.utils;

import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.AuthenticateUserRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.AuthenticateUserResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by Borce on 12.09.2016.
 */
public class AuthenticateInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateInterceptor.class);
    private User user;
    private AuthApi api;

    public AuthenticateInterceptor(User user, AuthApi api) {
        this.user = user;
        this.api = api;
    }

    /**
     * Checks if the server respond with a 401 status code, and then try to
     * authenticate the user again.
     *
     * @param chain requests chain
     * @return server response
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        logger.info("intercept");
        Request request = chain.request();
        Response response = chain.proceed(request);
        logger.info("checking server response");
        int code = response.code();
        if (code == 401) {
            logger.info("401 response, sending login user request");
            AuthenticateUserRequest authRequest =
                    new AuthenticateUserRequest(user.getEmail(), user.getPassword());
            Call<AuthenticateUserResponse> call = api.login(authRequest);
            AuthenticateUserResponse callResponse = call.execute().body();
            logger.info("login user request success");
            String token = callResponse.getJwtToken();
            request = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", String.format("Bearer %s", token))
                    .build();
            return chain.proceed(request);
        }
        return response;
    }
}
