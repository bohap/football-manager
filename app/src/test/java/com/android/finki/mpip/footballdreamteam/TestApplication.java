package com.android.finki.mpip.footballdreamteam;

import com.android.finki.mpip.footballdreamteam.dependency.component.DaggerTestAppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.TestAppComponent;

/**
 * Created by Borce on 25.07.2016.
 */
public class TestApplication extends MainApplication {

    private TestAppComponent testAppComponent;

    /**
     * Create a new instance of TestAppComponent.
     */
    private void createTestAppComponent() {
        testAppComponent = DaggerTestAppComponent.builder()
                .appModule(super.getAppModule())
                .build();
    }

    /**
     * Get the TestAppComponent instance.
     *
     * @return instance of TestAppComponent
     */
    public TestAppComponent getTestAppComponent() {
        if (testAppComponent == null) {
            this.createTestAppComponent();
        }
        return testAppComponent;
    }
}
