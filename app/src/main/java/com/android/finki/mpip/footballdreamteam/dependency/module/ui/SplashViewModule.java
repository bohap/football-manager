package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.component.SplashView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.SplashViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class SplashViewModule {

    private SplashView view;

    public SplashViewModule(SplashView view) {
        this.view = view;
    }

    /**
     * Provide instance of the SplashActivity presenter.
     *
     * @param context application base context
     * @param preferences application SharedPreferences
     * @return instance of SplashActivity presenter
     */
    @Provides
    @ViewScope
    SplashViewPresenter provideSplashActivityPresenter(Context context,
                                                       SharedPreferences preferences) {
        return new SplashViewPresenter(view, context, preferences);
    }
}
