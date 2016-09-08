package com.android.finki.mpip.footballdreamteam.rest.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Borce on 08.09.2016.
 */
public class ErrorInterceptor implements Interceptor {

    /**
     * CHeck if the server has responded with a error.
     *
     * @param chain requests chain
     * @return request response
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
