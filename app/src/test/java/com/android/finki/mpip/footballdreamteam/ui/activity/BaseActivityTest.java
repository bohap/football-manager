package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Build;
import android.view.View;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.*;

/**
 * Created by Borce on 08.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class BaseActivityTest {

    private MockApplication application;
    private ActivityController<BaseActivity> controller;
    private BaseActivity activity;

    @Before
    public void setup() {
        application = (MockApplication)RuntimeEnvironment.application;
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
}
