package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.database.repository.UserRepository;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.utility.AuthUserUtils;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class UserModule {

    public UserModule() {
    }

    /**
     * Provide instance of UserDBService.
     *
     * @param context  base application context
     * @param dbHelper application SQLiteOpenHelper
     * @param base64Utils instance of Base64 utils
     * @return new instance of UserDBService
     */
    @Provides
    @Singleton
    UserDBService provideUserDbService(Context context, MainSQLiteOpenHelper dbHelper,
                                       Base64Utils base64Utils) {
        UserRepository repository = new UserRepository(context, dbHelper, base64Utils);
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

    /**
     * Provides instance of the AuthUserUtils.
     *
     * @param context     application context
     * @param preferences application shared preferences
     * @param dbService   instance of UserDBService
     * @return instance of the AuthUserUtils
     */
    @Provides
    @Singleton
    AuthUserUtils provideAuthUserUtils(Context context, SharedPreferences preferences,
                                       UserDBService dbService) {
        return new AuthUserUtils(context, preferences, dbService);
    }
}
