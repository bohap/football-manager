package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.rest.interceptor.ErrorInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borce on 29.07.2016.
 */
@Module
public class NetModule {

    /**
     * Provides instance of the ErrorInterceptor.
     *
     * @return instance of the ErrorInterceptor
     */
    @Provides
    @Singleton
    ErrorInterceptor provideErrorInterceptor() {
        return new ErrorInterceptor();
    }

    /**
     * Provides the base url to the api.
     *
     * @param context base application context
     * @return base api url
     */
    @Provides
    @Named("api_base_url")
    @Singleton
    String provideApiBaseUrl(Context context) {
        return context.getString(R.string.api_baseUrl);
    }

    /**
     * Provides instance of the OkHttpClient for when the requests don't need authentication token.
     *
     * @param interceptor instance of the ErrorInterceptor
     * @return instance of OkHttpClient
     */
    @Provides
    @Named("un_authenticated")
    @Singleton
    OkHttpClient provideUnAuthenticatedOkHttpClient(ErrorInterceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    /**
     * Provides instance of the GSON converter factory.
     *
     * @return instance of the GSON converter factory
     */
    @Provides
    @Singleton
    GsonConverterFactory provideGSONConverterFactory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return GsonConverterFactory.create(gson);
    }

    /**
     * Provides instance of the Retrofit for requests that don't need authentication token.
     *
     * @param baseUrl base api url
     * @param client instance of un authenticated OkHttpClient
     * @param gsonConverterFactory instance of GSON converter factory
     * @return instance of Retrofit
     */
    @Provides
    @Named("un_authenticated")
    @Singleton
    Retrofit provideUnAuthenticatedRetrofit(@Named("api_base_url") String baseUrl,
                                    @Named("un_authenticated") OkHttpClient client,
                                    GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }
}