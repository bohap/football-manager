package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.ui.component.SplashView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 06.08.2016.
 */
public class SplashViewPresenterTest {

    @Mock
    private SplashView view;

    @Mock
    private Context context;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private SharedPreferences.Editor editor;

    private SplashViewPresenter presenter;

    private String FIRST_TIME_KEY = "first_time";
    private String AUTH_USER_ID_KEY = "auth_user_id";

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(context.getString(R.string.preference_first_time_key)).thenReturn(FIRST_TIME_KEY);
        when(context.getString(R.string.preference_auth_user_id_key)).thenReturn(AUTH_USER_ID_KEY);
        when(preferences.edit()).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);
        presenter = new SplashViewPresenter(view, context, preferences);
    }

    /**
     * Test the behavior when onViewLayout created is called and the application is
     * started for the first time.
     */
    @Test
    public void testOnViewLayoutCreatedWithAppStartedForTheFirstTime() {
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(true);
        when(editor.putBoolean(FIRST_TIME_KEY, false)).thenReturn(editor);
        presenter.onViewLayoutCreated();
        verify(editor).putBoolean(FIRST_TIME_KEY, false);
        verify(editor).apply();
        verify(view).showInfoDialog();
        verify(view, never()).showLoginView();
        verify(view, never()).showHomeView();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and the user is not authenticated.
     */
    @Test
    public void testOnViewLayoutCreatedWhenUserIsNotAuthenticated() {
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(false);
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(-1);
        presenter.onViewLayoutCreated();
        verify(preferences).getBoolean(FIRST_TIME_KEY, true);
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
        verify(view).showLoginView();
        verify(view, never()).showInfoDialog();
        verify(view, never()).showHomeView();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and the user is authenticated.
     */
    @Test
    public void testOnViewLayoutCreatedWhenTheUserIsAuthenticated() {
        int userId = 1;
        when(preferences.getBoolean(FIRST_TIME_KEY, true)).thenReturn(false);
        when(preferences.getInt(AUTH_USER_ID_KEY, -1)).thenReturn(userId);
        presenter.onViewLayoutCreated();
        verify(preferences).getBoolean(FIRST_TIME_KEY, true);
        verify(preferences).getInt(AUTH_USER_ID_KEY, -1);
        verify(view).showHomeView();
        verify(view, never()).showInfoDialog();
        verify(view, never()).showLoginView();
    }
}
