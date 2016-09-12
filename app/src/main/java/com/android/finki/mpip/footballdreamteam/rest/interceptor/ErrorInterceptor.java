package com.android.finki.mpip.footballdreamteam.rest.interceptor;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
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
        Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        if (code >= 400 && code <= 500) {
            switch (code) {
                case 401:
                    throw new NotAuthenticatedException(response);
                case 500:
                    throw new InternalServerErrorException(response);
                default:
                    throw new IOException(String
                            .format("server responded with error code %d", code));
            }
        }
        return response;
    }
}
