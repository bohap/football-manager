package com.android.finki.mpip.footballdreamteam;

import android.app.Application;

import com.android.finki.mpip.footballdreamteam.dependency.component.DaggerTestAppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.TestAppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestAppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestAuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestNetModule;

/**
 * Created by Borce on 25.07.2016.
 */
public class TestApplication extends Application {

    private TestAppComponent testAppComponent;

    /**
     * Called when the application is ready to be created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Get a new instance of TestAppModule.
     *
     * @return instance of TestAppModule
     */
    private TestAppModule getTestAppModule() {
        return new TestAppModule(this);
    }

    /**
     * Get a new instance of TestNetModule.
     *
     * @return instance of TestNetModule
     */
    private TestNetModule getTestNetModule(String baseUrl) {
        return new TestNetModule(baseUrl);
    }

    /**
     * Get a instance of TestAuthModule.
     *
     * @return instance of TestAuthModule
     */
    private TestAuthModule getTestAuthModule() {
        return new TestAuthModule();
    }

    /**
     * Create a new instance of TestAppComponent.
     *
     * @param baseUrl base url to the api
     */
    private void setTestAppComponent(String baseUrl) {
//        testAppComponent = DaggerTestAppComponent.builder()
//                .testAppModule(getTestAppModule())
//                .testNetModule(getTestNetModule(baseUrl))
//                .testAuthModule(getTestAuthModule())
//                .build();
    }

    /**
     * Get the TestAppComponent instance.
     *
     * @param baseUrl base url to the api
     * @return instance of TestAppComponent
     */
    public TestAppComponent getTestAppComponent(String baseUrl) {
        if (testAppComponent == null) {
            this.setTestAppComponent(baseUrl);
        }
        return testAppComponent;
    }
}
