package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Borce on 07.08.2016.
 */
@Module
public class TestNetModule extends NetModule {

    private String url;

    public TestNetModule(String url) {
        this.url = url;
    }

    /**
     * Provides the base url to the api.
     *
     * @return base url to the api
     */
    @Provides
    @Named("mock_api_base_url")
    String provideMockApiBaseUrl() {
        return url;
    }

    /**
     * Provides instance of Retrofit for testing purposes.
     *
     * @param baseUrl mocked api base url
     * @param client instance of OkHttpClient
     * @param gsonConverterFactory instance of GSONConverterFactory
     * @return instance of Retrofit
     */
    @Provides
    @Named("test_un_authenticated")
    @Singleton
    Retrofit providesTestRetrofit(@Named("mock_api_base_url") String baseUrl,
                                  @Named("un_authenticated") OkHttpClient client,
                                  GsonConverterFactory gsonConverterFactory) {
        return super.provideUnAuthenticatedRetrofit(baseUrl, client, gsonConverterFactory);
    }
}