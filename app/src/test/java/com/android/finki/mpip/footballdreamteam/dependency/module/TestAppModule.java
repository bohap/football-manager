package com.android.finki.mpip.footballdreamteam.dependency.module;

import android.app.Application;

import org.codehaus.plexus.context.Context;
import org.robolectric.RuntimeEnvironment;

import dagger.Module;

/**
 * Created by Borce on 07.08.2016.
 */
@Module
public class TestAppModule extends AppModule {

    public TestAppModule(Application application) {
        super(application);
    }
}
