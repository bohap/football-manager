package com.android.finki.mpip.footballdreamteam.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 14.09.2016.
 */
public class AuthUserUtils {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserUtils.class);
    private String AUTH_USER_ID_KEY;
    private String JWT_TOKEN_KEY;
    private String USER_STATISTIC_LAST_CHECKED_KEY;
    private SharedPreferences preferences;
    private UserDBService dbService;

    public AuthUserUtils(Context context, SharedPreferences preferences,
                         UserDBService dbService) {
        this.AUTH_USER_ID_KEY = context.getString(R.string.preference_auth_user_id_key);
        this.JWT_TOKEN_KEY = context.getString(R.string.preference_jwt_token_key);
        this.USER_STATISTIC_LAST_CHECKED_KEY = context
                .getString(R.string.preference_user_statistic_service_last_check_mills);
        this.preferences = preferences;
        this.dbService = dbService;
    }

    /**
     * Authenticate the user.
     *
     * @return authenticated user
     */
    public User authenticate() {
        logger.info("authenticate");
        int id = preferences.getInt(AUTH_USER_ID_KEY, -1);
        if (id < 1) {
            logger.info("shared preferences id not set");
            return null;
        }
        dbService.open();
        User user = dbService.get(id);
        dbService.close();
        if (user == null) {
            logger.info("user not in the database");
            return null;
        }
        return user;
    }

    /**
     * Remove the auth user data from the shared preferences.
     */
    public void removeAuthUser() {
        logger.info("removeAuthUser");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(AUTH_USER_ID_KEY, -1);
        editor.putString(JWT_TOKEN_KEY, null);
        editor.remove(USER_STATISTIC_LAST_CHECKED_KEY);
        editor.apply();
    }
}
