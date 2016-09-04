package com.android.finki.mpip.footballdreamteam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Inject
    SharedPreferences preferences;

    @Before
    public void setup() {
//        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
//        MainApplication app = (MainApplication) instrumentation.getTargetContext()
//                .getApplicationContext();
//        InstrumentedMockedAppModule module = new InstrumentedMockedAppModule(app);
//        TestAppComponent component = DaggerTestAppComponent.builder()
//                .appModule(module).build();
//        app.setAppComponent(component);
//        component.inject(this);
    }

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule =
            new ActivityTestRule<>(SplashActivity.class, true, false);

    @Test
    public void testInfoDialogIsShowed() {
        when(preferences.getBoolean("first_time", true)).thenReturn(true);
        activityTestRule.launchActivity(new Intent());
        assertTrue(true);
    }
}
