package com.android.finki.mpip.footballdreamteam.rest.interceptor;

import android.content.SharedPreferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Borce on 09.08.2016.
 */
public class JWTTokenInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(JWTTokenInterceptor.class);
    private SharedPreferences preferences;
    private String key;

    public JWTTokenInterceptor(SharedPreferences preferences, String key) {
        this.preferences = preferences;
        this.key = key;
    }

    /**
     * Add the user jwt authorization token to every request.
     *
     * @param chain requests chain
     * @return server response
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        /**
         * TODO
         * when the connection times out, and the token is expired the server generates a
         * new token which will not be intercepted here so when the next request is send
         * we have the old token and the server returns 401 un authenticated.
         */
        String token = preferences.getString(key, null);
        logger.info(String.format("add JWT token to the request, token  %s", token));
        Request request = chain.request();
        if (token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", String.format("Bearer %s", token))
                    .build();
        }
        Response response = chain.proceed(request);
        token = response.header("Authorization", null);
        if (token != null) {
            logger.info(String.format("new user token %s", token));
            token = token.split(" ")[1];
            preferences.edit().putString(key, token).apply();
        }
        return response;
    }
}
