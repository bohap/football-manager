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
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
     * Provides instance of HttpLoggingInterceptor.
     *
     * @return instance of HttpLoggingInterceptor
     */
    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
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
     * Provides instance of the default OkHttpClient builder.
     *
     * @param loggingInterceptor instance of HttpLoggingInterceptor
     * @param errorInterceptor Error interceptor for intercepting server error response codes.
     * @return OkHttpClient builder
     */
    @Provides
    OkHttpClient.Builder providesOkHttpClientBuild(HttpLoggingInterceptor loggingInterceptor,
                                                   ErrorInterceptor errorInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(errorInterceptor);
        builder.addInterceptor(loggingInterceptor);
        return builder;
    }

    /**
     * Provides instance of the OkHttpClient for when the requests don't need authentication token.
     *
     * @param builder instance of default OkHttpClient builder
     * @return instance of OkHttpClient
     */
    @Provides
    @Named("un_authenticated")
    @Singleton
    OkHttpClient provideUnAuthenticatedOkHttpClient(OkHttpClient.Builder builder) {
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