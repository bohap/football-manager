package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.utility.AlarmManagerUtils;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;

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
     * Provides instance of the application notification manager.
     *
     * @return instance of application notification manager
     */
    @Provides
    @Singleton
    NotificationManager provideNotificationManager() {
        return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * Provides instance of the application alarm manager.
     *
     * @return instance of application alarm manager
     */
    @Provides
    @Singleton
    AlarmManager provideAlarmManager() {
        return (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
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

    /**
     * Provides instance of the AlarmManagerUtils.
     *
     * @param context     application context
     * @param manager     application alarm manager
     * @param preferences application shared preferences
     * @return instance of the AlarmManagerUtils
     */
    @Provides
    @Singleton
    AlarmManagerUtils provideAlarmMangerUtils(Context context, AlarmManager manager,
                                              SharedPreferences preferences) {
        return new AlarmManagerUtils(context, manager, preferences);
    }

    /**
     * Provides instance of the Base64 utils.
     *
     * @return instance of Base64 utils
     */
    @Provides
    @Singleton
    Base64Utils provideBase64Utils() {
        return new Base64Utils();
    }
}
