package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.utils.JWTTokenInterceptor;
import com.android.finki.mpip.footballdreamteam.rest.utils.AuthenticateInterceptor;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class AuthNetModule {

    /**
     * Provides instance of the JWT token interceptor.
     *
     * @param preferences instance of application preferences
     * @param context     instance of application context
     * @return instance of JWT token interceptor
     */
    @Provides
    @UserScope
    JWTTokenInterceptor provideJWTTokenInterceptor(SharedPreferences preferences,
                                                   Context context) {
        String key = context.getString(R.string.preference_jwt_token_key);
        return new JWTTokenInterceptor(preferences, key);
    }

    /**
     * Provides instance of the Authenticate interceptor.
     *
     * @param user        authenticated user
     * @param api         instance of AuthApi
     * @return instance of the Authenticate interceptor
     */
    @Provides
    @UserScope
    AuthenticateInterceptor provideAuthenticateInterceptor(User user, AuthApi api) {
        return new AuthenticateInterceptor(user, api);
    }

    /**
     * Provides instance of the OkHttpClient for requests that need authentication token.
     *
     * @param builder     instance of default OkHttpClient builder
     * @param jwtTokenInterceptor instance of the JWT interceptor
     * @return instance of OkHttpClient
     */
    @Provides
    @Named("authenticated")
    @UserScope
    OkHttpClient provideAuthenticatedOkHttpClient(OkHttpClient.Builder builder,
                                                  JWTTokenInterceptor jwtTokenInterceptor,
                                                  AuthenticateInterceptor authenticateInterceptor,
                                                  HttpLoggingInterceptor loggingInterceptor) {
        builder.addInterceptor(jwtTokenInterceptor);
        builder.addInterceptor(authenticateInterceptor);
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }

    /**
     * Provides instance of Retrofit for the requests that need authetication token.
     *
     * @param baseUrl              base api url
     * @param client               instance of authenticated OkHttpClient
     * @param gsonConverterFactory instance of GSON converter factory
     * @return instance of Retrofit
     */
    @Provides
    @Named("authenticated")
    @UserScope
    Retrofit provideAuthenticatedRetrofit(@Named("api_base_url") String baseUrl,
                                          @Named("authenticated") OkHttpClient client,
                                          GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }
}
