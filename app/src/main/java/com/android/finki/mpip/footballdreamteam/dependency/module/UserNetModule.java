package com.android.finki.mpip.footballdreamteam.dependency.module;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.interceptor.JWTTokenInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class UserNetModule {

    /**
     * Provides instance of the JWT token interceptor.
     *
     * @return instance of JWT token interceptor
     */
    @Provides
    @UserScope
    JWTTokenInterceptor provideJWTTokenInterceptor(User user, UserDBService userDBService) {
        return new JWTTokenInterceptor(user, userDBService);
    }

    /**
     * Provides instance of the OkHttpClient for requests that need authentication token.
     *
     * @return instance of OkHttpClient
     */
    @Provides
    @Named("authenticated")
    @UserScope
    OkHttpClient provideAuthenticatedOkHttpClient(JWTTokenInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    /**
     * Provides instance of Retrofit for the requests that need authetication token.
     *
     * @param baseUrl base api url
     * @param client instance of authenticated OkHttpClient
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
