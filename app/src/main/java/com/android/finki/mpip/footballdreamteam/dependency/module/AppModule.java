package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 25.07.2016.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    /**
     * Provides the main application object.
     *
     * @return main application
     */
    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    /**
     * Provides the base application context.
     *
     * @return base application context
     */
    @Provides
    @Singleton
    Context provideBaseContext() {
        return application.getBaseContext();
    }

    /**
     * Provides instance to the shared preferences.
     *
     * @return application shared preferences
     */
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application.getBaseContext());
    }

    /**
     * Provides instance of SQLite helper.
     *
     * @return application SQLIte open helper
     */
    @Provides
    @Singleton
    MainSQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        return new MainSQLiteOpenHelper(context);
    }
}
