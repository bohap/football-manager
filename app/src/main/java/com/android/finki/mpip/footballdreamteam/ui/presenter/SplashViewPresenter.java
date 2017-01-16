package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.ui.component.SplashView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 06.08.2016.
 */
public class SplashViewPresenter {

    private Logger logger = LoggerFactory.getLogger(SplashViewPresenter.class);
    private SplashView view;
    private SharedPreferences preferences;
    private String firstTimeKey;
    private String authUserIdKey;

    public SplashViewPresenter(SplashView view, Context context, SharedPreferences preferences) {
        this.view = view;
        this.preferences = preferences;
        this.firstTimeKey = context.getString(R.string.preference_first_time_key);
        this.authUserIdKey = context.getString(R.string.preference_auth_user_id_key);
    }

    /**
     * Called when the view is visible to the user.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        if (isFirstTime()) {
            logger.info("app start for first time");
            view.showInfoDialog();
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
            logger.info("user not authenticated");
            view.showLoginView();
        } else {
            logger.info("user authenticated");
            view.showHomeView();
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
