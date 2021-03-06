package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashViewComponent;
import com.android.finki.mpip.footballdreamteam.ui.presenter.SplashViewPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 06.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class SplashActivityTest {

    @Mock
    private SplashViewComponent component;

    @Mock
    private SplashViewPresenter presenter;

    private ActivityController<SplashActivity> controller;
    private SplashActivity activity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setSplashViewComponent(component);
        this.mockDependencies();
        controller = Robolectric.buildActivity(SplashActivity.class);
        activity = controller.create().start().resume().visible().get();
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                SplashActivity activity = (SplashActivity) invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(SplashActivity.class));
    }

    /**
     * Test thet the activity is created correctly.
     */
    @Test
    public void testActivityIsCreated() {
        assertNull(shadowOf(activity).getContentView());
        verify(presenter).onViewLayoutCreated();
    }

    /**
     * Test that showInfoDialog method will start a new AlertDialog.
     */
    @Test
    public void testShownInfoDialog() {
        activity.showInfoDialog();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }

    /**
     * Test the behavior when onDialogDone is called.
     */
    @Test
    public void testOnDialogDone() {
        activity.onDialogDone();
        verify(presenter).checkIfUserIsAuthenticated();
    }

    /**
     * Test that showLoginActivity method will start a new activity and finish the current.
     */
    @Test
    public void testShowLoginActivity() {
        activity.showLoginView();
        Intent expectedIntent = new Intent(activity, LoginActivity.class);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertTrue(activity.isFinishing());
    }

    /**
     * Test that showHomeActivity will start a new activity and finish the current.
     */
    @Test
    public void testShowHomeActivity() {
        activity.showHomeView();
        ShadowActivity shadow = shadowOf(activity);
        Intent expectedIntent = new Intent(activity, HomeActivity.class);
        Intent actualIntent = shadow.getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertTrue(activity.isFinishing());
        assertNotNull(((MockApplication) RuntimeEnvironment.application).getAuthComponent());
    }
}
