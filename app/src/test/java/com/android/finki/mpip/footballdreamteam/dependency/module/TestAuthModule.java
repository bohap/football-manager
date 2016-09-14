package com.android.finki.mpip.footballdreamteam.dependency.module;

import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Borce on 07.08.2016.
 */
@Module
public class TestAuthModule extends UserModule {

    /**
     * Provide instance of retrofit AuthApi used for authenticating the user.
     *
     * @param retrofit Retrofit instance
     * @return instance of AuthApi
     */
    @Provides
    @Named("test_auth_api")
    @Singleton
    AuthApi provideTestAuthApi(@Named("test_un_authenticated") Retrofit retrofit) {
        return super.provideAuthApi(retrofit);
    }
}
