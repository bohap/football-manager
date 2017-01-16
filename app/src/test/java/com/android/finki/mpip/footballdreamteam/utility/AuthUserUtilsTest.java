package com.android.finki.mpip.footballdreamteam.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 19.09.2016.
 */
public class AuthUserUtilsTest {

    @Mock
    private Context context;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private UserDBService userDBService;

    @Mock
    private SharedPreferences.Editor editor;

    private AuthUserUtils utils;

    private String AUTH_USER_ID_KEY = "auth_user_id";
    private String JWT_TOKEN_KEY = "jwt_token";
    private String USER_STATISTIC_LAST_CHECKED_KEY = "user_statistic_last_checked";
    private User user = new User(1, "User");

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
        when(context.getString(R.string.preference_jwt_token_key)).thenReturn(JWT_TOKEN_KEY);
        when(context.getString(R.string.preference_user_statistic_service_last_check_mills))
                .thenReturn(USER_STATISTIC_LAST_CHECKED_KEY);
        when(preferences.edit()).thenReturn(editor);
        utils = new AuthUserUtils(context, preferences, userDBService);
    }

    /**
     * Test the behavior when authenticate is called and the preference
     * auth user id key is not set.
     */
    @Test
    public void testAuthenticateWhenAuthUserIdNotSet() {
        assertNull(utils.authenticate());
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
    }

    /**
     * Test the behavior when authenticate is called and the auth user data is not
     * saved in the database.
     */
    @Test
    public void testAuthenticateWhenUserNoInTheDatabase() {
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(user.getId());
        assertNull(utils.authenticate());
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
        verify(userDBService).open();
        verify(userDBService).close();
        verify(userDBService).get(user.getId());
    }

    /**
     * Test that authenticate returns the auth user data when the preferences key is set and the
     * user is saved in the database.
     */
    @Test
    public void testSuccessAuthenticate() {
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(user.getId());
        when(userDBService.get(user.getId())).thenReturn(user);
        User auth = utils.authenticate();
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
        verify(userDBService).open();
        verify(userDBService).close();
        verify(userDBService).get(user.getId());
        assertSame(user, auth);
    }

    /**
     * Test the behavior when releaseAuthUser is called.
     */
    @Test
    public void testReleaseAuthUser() {
        utils.removeAuthUser();
        verify(editor).putInt(AUTH_USER_ID_KEY, -1);
        verify(editor).putString(JWT_TOKEN_KEY, null);
        verify(editor).remove(USER_STATISTIC_LAST_CHECKED_KEY);
        verify(editor).apply();
    }
}
