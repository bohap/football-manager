package com.android.finki.mpip.footballdreamteam.rest.utils;

import com.android.finki.mpip.footballdreamteam.exception.UnProcessableEntityException;
import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.exception.NotAuthenticatedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Borce on 08.09.2016.
 */
public class ErrorInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(ErrorInterceptor.class);

    /**
     * Check if the server has responded with a error.
     *
     * @param chain requests chain
     * @return request response
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        logger.info("intercept");
        Response response = chain.proceed(chain.request());
        logger.info("checking server response");
        int code = response.code();
        if (code >= 400) {
            switch (code) {
                case 401:
                    throw new NotAuthenticatedException(response);
                case 422:
                    throw new UnProcessableEntityException(response);
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
