package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;
import com.android.finki.mpip.footballdreamteam.ui.presenter.SplashActivityPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class SplashActivityModule {

    private SplashActivity activity;

    public SplashActivityModule(SplashActivity activity) {
        this.activity = activity;
    }

    /**
     * Provide instance of the SplashActivity presenter.
     *
     * @param context application base context
     * @param preferences application SharedPreferences
     * @param service instance of UserDBService
     * @return instance of SplashActivity presenter
     */
    @Provides
    @ActivityScope
    SplashActivityPresenter provideSplashActivityPresenter(Context context,
                                        SharedPreferences preferences, UserDBService service) {
        return new SplashActivityPresenter(activity, context, preferences, service);
    }
}
