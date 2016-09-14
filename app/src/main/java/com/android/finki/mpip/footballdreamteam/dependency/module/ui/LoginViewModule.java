package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LoginView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class LoginViewModule {

    private LoginView view;

    public LoginViewModule(LoginView view) {
        this.view = view;
    }

    /**
     * Provides instance of the LoginActivity presenter.
     *
     * @param context instance of application context
     * @param preferences application shared preferences
     * @param userDBService instance of UserDBService
     * @param authApi instance of authentication retrofit api
     * @return instance of LoginActivity presenter
     */
    @Provides
    @ViewScope
    LoginViewPresenter provideLoginViewPresenter(SharedPreferences preferences,
                                                 Context context,
                                                 UserDBService userDBService,
                                                 AuthApi authApi) {
        return new LoginViewPresenter(view, preferences, context, userDBService, authApi);
    }
}
