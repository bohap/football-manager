package com.android.finki.mpip.footballdreamteam.rest.interceptor;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;

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
    private User user;
    private UserDBService userDBService;

    public JWTTokenInterceptor(User user, UserDBService userDBService) {
        this.user = user;
        this.userDBService = userDBService;
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
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", user.getJwtToken()))
                .build();

        Response response = chain.proceed(request);
        String token = response.header("Authorization", null);
        if (token != null) {
            logger.info(String.format("new user token %s", token));
            token = token.split(" ")[1];
            user.setJwtToken(token);
            try {
                userDBService.open();
                userDBService.update(user);
            } catch (UserException exp) {
                exp.printStackTrace();
            } finally {
                userDBService.close();
            }
        }
        return response;
    }
}
