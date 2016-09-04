package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LoginActivity;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginActivityPresenter;

import javax.inject.Singleton;

import butterknife.OnEditorAction;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class LoginActivityModule {

    private LoginActivity activity;

    public LoginActivityModule(LoginActivity activity) {
        this.activity = activity;
    }

    /**
     * Provides instance of the LoginActivity presenter.
     *
     * @param preferences application shared preferences
     * @param userDBService instance of UserDBService
     * @param authApi instance of authentication retrofit api
     * @return instance of LoginActivity presenter
     */
    @Provides
    @ActivityScope
    LoginActivityPresenter provideLoginActivityPresenter(SharedPreferences preferences,
                                             UserDBService userDBService, AuthApi authApi) {
        return new LoginActivityPresenter(activity,  preferences, userDBService, authApi);
    }
}
