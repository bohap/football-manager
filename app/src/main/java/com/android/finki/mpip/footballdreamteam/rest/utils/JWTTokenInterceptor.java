package com.android.finki.mpip.footballdreamteam.rest.utils;

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
        logger.info("intercept");
        String token = preferences.getString(key, null);
        Request request = chain.request();
        if (token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", String.format("Bearer %s", token))
                    .build();
        }
        Response response = chain.proceed(request);
        logger.info("checking server response");
        token = response.header("Authorization", null);
        if (token != null) {
            logger.info("updating jwt token in preferences");
            token = token.split(" ")[1];
            preferences.edit().putString(key, token).apply();
        }
        return response;
    }
}
