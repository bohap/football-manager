package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.rest.web.AuthApi;
import com.android.finki.mpip.footballdreamteam.ui.component.RegisterView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.RegisterViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 13.09.2016.
 */
@Module
public class RegisterViewModule {

    private RegisterView view;

    public RegisterViewModule(RegisterView view) {
        this.view = view;
    }

    /**
     * Provides instance of the RegisterView presenter.
     *
     * @param preferences application shared preferences
     * @param context application context
     * @param dbService instance of UserDbService
     * @param api instance of AuthApi
     * @return instance of RegisterView presenter
     */
    @Provides
    @ViewScope
    RegisterViewPresenter provideRegisterViewPresenter(SharedPreferences preferences,
                                                       Context context, UserDBService dbService,
                                                       AuthApi api) {
        return new RegisterViewPresenter(view, preferences, context, dbService, api);
    }
}
