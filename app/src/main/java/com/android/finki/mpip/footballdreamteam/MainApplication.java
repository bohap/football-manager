package com.android.finki.mpip.footballdreamteam;

import android.app.Application;
import android.content.Context;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.DaggerAppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.UserComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.model.User;

/**
 * Created by Borce on 25.07.2016.
 */
public class MainApplication extends Application {

    private AppComponent appComponent;
    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initAppComponent();
    }

    /**
     * Get a new instance of the AppModule.
     *
     * @return instance of AppModule
     */
    private AppModule getAppModule() {
        return new AppModule(this);
    }

    /**
     * Get a new instance of the NetModule.
     *
     * @return instance of NetModule
     */
    private NetModule getNetModule() {
        return new NetModule();
    }

    /**
     * Get a new instance of the AuthModule.
     *
     * @return  instance of the AuthModule
     */
    private AuthModule getAuthModule() {
        return new AuthModule();
    }

    /**
     * Create a new instance of the AppComponent
     */
    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(getAppModule())
                .netModule(getNetModule())
                .authModule(getAuthModule())
                .build();
    }

    /**
     * Get the instance of AppComponent.
     *
     * @return instance of AppComponent
     */
    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * Get a new instance of UserModule.
     *
     * @param user authenticated user
     * @return instance of UserModule
     */
    private UserModule getUserModule(User user) {
        return new UserModule(user);
    }

    /**
     * Create a new instance of UserComponent.
     *
     * @param user authenticated user
     */
    public void createUserComponent(User user) {
        userComponent = appComponent.plus(getUserModule(user));
    }

    /**
     * get the instance of the UserComponent.
     *
     * @return instance of the UserComponent
     */
    public UserComponent getUserComponent() {
        return userComponent;
    }

    /**
     * Release the instance of UserComponent.
     */
    public void releaseUserComponent() {
        userComponent = null;
    }
}
