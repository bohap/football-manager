package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 06.08.2016.
 */
public class SplashActivityPresenter {

    private Logger logger = LoggerFactory.getLogger(SplashActivityPresenter.class);
    private SplashActivity activity;
    private SharedPreferences preferences;
    private UserDBService userDBService;

    private String firstTimeKey;
    private String authUserIdKey;

    public SplashActivityPresenter(SplashActivity activity, Context context,
                                   SharedPreferences preferences, UserDBService service) {
        this.activity = activity;
        this.preferences = preferences;
        this.userDBService = service;
        this.firstTimeKey = context.getString(R.string.preference_first_time_key);
        this.authUserIdKey = context.getString(R.string.preference_auth_user_id_key);
    }

    /**
     * Do the logic when the activity is fully created.
     */
    public void onActivityCreated() {
        if (isFirstTime()) {
            activity.showInfoDialog();
        } else {
            this.checkIfUserIsAuthenticated();
        }
    }

    /**
     * Checks if the user is authenticated and depending of that do some action.
     */
    public void checkIfUserIsAuthenticated() {
        int userId = getAuthUserId();
        if (userId == -1) {
            activity.showLoginActivity();
        } else {
            logger.info(String.format("Creating UserComponent for user with id %d.", userId));
            userDBService.open();
            User user = userDBService.get(userId);
            userDBService.close();
            ((MainApplication)activity.getApplication()).createUserComponent(user);
            activity.showHomeActivity();
        }
    }

    /**
     * Check if the application is started for the fist time.
     *
     * @return whatever the application is started for the first time
     */
    private boolean isFirstTime() {
        boolean result = preferences.getBoolean(firstTimeKey, true);
        if (result) {
            preferences.edit().putBoolean(firstTimeKey, false).apply();
        }
        return result;
    }

    /**
     * Get the authenticated user id.
     *
     * @return authenticated user id or -1 if the user is not authenticated
     */
    private int getAuthUserId() {
        return preferences.getInt(authUserIdKey, -1);
    }
}
