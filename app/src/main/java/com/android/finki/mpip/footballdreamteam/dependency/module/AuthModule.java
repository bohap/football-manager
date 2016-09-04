package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.database.repository.UserRepository;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class AuthModule {

    public AuthModule() {
    }

    /**
     * Provide instance of UserDBService.
     *
     * @param context base application context
     * @param dbHelper application SQLiteOpenHelper
     * @return new instance of UserDBService
     */
    @Provides
    @Singleton
    UserDBService provideUserDbService(Context context, MainSQLiteOpenHelper dbHelper) {
        UserRepository repository = new UserRepository(context, dbHelper);
        return new UserDBService(repository);
    }

    /**
     * Provide instance of retrofit AuthApi used for authenticating the user.
     *
     * @param retrofit Retrofit instance
     * @return instance of AuthApi
     */
    @Provides
    @Singleton
    AuthApi provideAuthApi(@Named("un_authenticated") Retrofit retrofit) {
        return retrofit.create(AuthApi.class);
    }
}
