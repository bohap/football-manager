package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.UserDBService;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 06.08.2016.
 */
public class SplashActivityPresenterTest {

    @Mock
    private SplashActivity activity;

    @Mock
    private Context context;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private UserDBService service;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private MainApplication application;

    private SplashViewPresenter presenter;

    private String FIRST_TIME_KEY = "first_time";
    private String AUTH_USER_ID_KEY = "auth_user_id";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        when(activity.getApplication()).thenReturn(application);
        presenter = new SplashViewPresenter(activity, context, preferences, service);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.preference_first_time_key)).thenReturn(FIRST_TIME_KEY);
        when(context.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
    }

    /**
     * Test that on activityCreated method will call showInfoDialog method on view when the
     * app is started for the first time.
     */
    @SuppressLint("CommitPrefEdits")
    @Test
    public void testAppStartedForFirstTime() {
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(true);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putBoolean(FIRST_TIME_KEY, false)).thenReturn(editor);

        presenter.onActivityCreated();
        verify(editor).putBoolean(FIRST_TIME_KEY, false);
        verify(activity).showInfoDialog();
        verify(activity, never()).showLoginActivity();
        verify(activity, never()).showHomeActivity();
    }

    /**
     * Test that the onViewCreated will call showLoginActivity method on view
     * when the user is not authenticated.
     */
    @Test
    public void testUserNotAuthenticated() {
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(false);
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(-1);

        presenter.onActivityCreated();
        verify(preferences).getBoolean(FIRST_TIME_KEY, true);
        verify(activity).showLoginActivity();
        verify(activity, never()).showInfoDialog();
        verify(activity, never()).showHomeActivity();
    }

    /**
     * Test that onViewCreated will call showHomeActivity method on view when the user
     * is authenticated.
     */
    @Test
    public void testUserAuthenticated() {
        int userId = 1;
        User user = new User(userId, "User");
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(false);
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(userId);
        when(service.get(userId)).thenReturn(user);

        presenter.onActivityCreated();
        verify(preferences).getBoolean(FIRST_TIME_KEY, true);
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
        verify(service).open();
        verify(service).get(userId);
        verify(service).close();
        verify(activity).showHomeActivity();
        verify(activity, never()).showInfoDialog();
        verify(activity, never()).showLoginActivity();
    }
}
