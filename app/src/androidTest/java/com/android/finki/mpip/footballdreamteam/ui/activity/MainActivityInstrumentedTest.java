package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.MockTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Borce on 26.07.2016.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Before
    public void setup() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        MainApplication application = (MainApplication) instrumentation
                .getTargetContext().getApplicationContext();
        assertTrue(true);
    }

    @Test
    public void simpleTest() {
        assertTrue(false);
    }
}
