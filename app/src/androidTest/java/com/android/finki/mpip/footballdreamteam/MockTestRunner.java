package com.android.finki.mpip.footballdreamteam;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by Borce on 26.07.2016.
 */
public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
                    throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, InstrumentedTestApplication.class.getName(), context);
    }
}
