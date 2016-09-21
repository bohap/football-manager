package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.TestApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.util.ActivityController;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 08.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = TestApplication.class)
public class BaseActivityTest {

    @Inject
    SharedPreferences preferences;

    private ActivityController<BaseActivity> controller;
    private BaseActivity activity;

    @Before
    public void setup() {
        TestApplication application = (TestApplication) RuntimeEnvironment.application;
        application.getTestAppComponent().inject(this);
        controller = Robolectric.buildActivity(BaseActivity.class);
        activity = controller.create().start().resume().visible().get();
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Test the toggleVisibility method will hide the view when called with false param.
     */
    @Test
    public void testToggleVisibilityWithHide() {
        View view = View.inflate(activity, android.R.layout.simple_expandable_list_item_1, null);
        activity.toggleVisibility(view, false);
        assertEquals(View.GONE, view.getVisibility());
    }

    /**
     * Test the toggleVisibility method will hide the view when called with true param.
     */
    @Test
    public void testToggleVisibilityWithShow() {
        View view = View.inflate(activity, android.R.layout.simple_expandable_list_item_1, null);
        view.setVisibility(View.GONE);
        activity.toggleVisibility(view, true);
        assertEquals(View.VISIBLE, view.getVisibility());
    }

    /**
     * Test the behavior when showNotAuthenticated is called.
     */
    @Test
    public void testShowNotAuthenticated() {
        String authUserKey = activity.getString(R.string.preference_auth_user_id_key);
        String jwtTokenKey = activity.getString(R.string.preference_jwt_token_key);
        String userStatisticLastChecked =
                activity.getString(R.string.preference_user_statistic_service_last_check_mills);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(authUserKey, 10);
        edit.putString(jwtTokenKey, "JWT Token");
        edit.putLong(userStatisticLastChecked, 1000000000);
        edit.apply();
        activity.showNotAuthenticated();
        assertEquals(-1, preferences.getInt(authUserKey, -1));
        assertNull(preferences.getString(jwtTokenKey, null));
        assertEquals(-1, preferences.getLong(userStatisticLastChecked, -1));
        Intent expectedIntent = new Intent(activity, LoginActivity.class);
        expectedIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertEquals(expectedIntent.getFlags(), actualIntent.getFlags());
    }

    /**
     * Test the behavior when showInternalServerError is called.
     */
    @Test
    public void testShowInternalServerError() {
        activity.showInternalServerError();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }

    /**
     * Test the behavior when showSocketTimeout is called.
     */
    @Test
    public void testSocketTimeout() {
        activity.showSocketTimeout();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }

    /**
     * Test the behavior when showNoInternetConnection is called.
     */
    @Test
    public void testShowNoInternetConnection() {
        activity.showNoInternetConnection();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }
}
