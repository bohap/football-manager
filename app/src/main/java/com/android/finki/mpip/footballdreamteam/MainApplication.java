
 package com.android.finki.mpip.footballdreamteam;

import android.app.Application;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.AuthComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.DaggerAppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.AlarmManagerUtils;
import com.android.finki.mpip.footballdreamteam.utility.AuthUserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by Borce on 25.07.2016.
 */
public class MainApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    protected AppComponent appComponent;
    protected AuthComponent authComponent;
    private AuthUserUtils authUserUtils;
    private AlarmManagerUtils alarmManagerUtils;

    /**
     * Set the instance of the AuthUserUtils.
     *
     * @param authUserUtils instance of the AuthUserUtils
     */
    @Inject
    public void setAuthUserUtils(AuthUserUtils authUserUtils) {
        this.authUserUtils = authUserUtils;
    }

    /**
     * Set the instance of the AlarmManagerUtils.
     *
     * @param alarmManagerUtils instance of AlarmManagerUtils
     */
    @Inject
    public void setAlarmManagerUtils(AlarmManagerUtils alarmManagerUtils) {
        this.alarmManagerUtils = alarmManagerUtils;
    }

    /**
     * Called when the application is ready to brew created.
     */
    @Override
    public void onCreate() {
        logger.info("onCreate");
        super.onCreate();
        this.initAppComponent();
    }

    /**
     * Called when the application is using to many memory.
     */
    @Override
    public void onLowMemory() {
        logger.info("onLowMemory");
        super.onLowMemory();
    }

    /**
     * Get a new instance of the AppModule.
     *
     * @return instance of AppModule
     */
    protected AppModule getAppModule() {
        return new AppModule(this);
    }

    /**
     * Get a new instance of the NetModule.
     *
     * @return instance of NetModule
     */
    protected NetModule getNetModule() {
        return new NetModule();
    }

    /**
     * Get a new instance of the UserModule.
     *
     * @return instance of the UserModule
     */
    protected UserModule getUserModule() {
        return new UserModule();
    }

    /**
     * Create a new instance of the AppComponent
     */
    protected void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(getAppModule())
                .netModule(getNetModule())
                .userModule(getUserModule())
                .build();
        appComponent.inject(this);
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
     * Get a new instance of AuthModule.
     *
     * @param user authenticated user
     * @return instance of AuthModule
     */
    protected AuthModule getUserModule(User user) {
        return new AuthModule(user);
    }

    /**
     * Create a new instance of AuthComponent.
     */
    public void createAuthComponent() {
        if (authComponent == null) {
            User user = authUserUtils.authenticate();
            if (user == null) {
                logger.info("auth user data not valid ");
                System.exit(0);
            }
            logger.info(String.format("Creating AuthComponent for user with id %d.", user.getId()));
            authComponent = appComponent.plus(getUserModule(user));
            alarmManagerUtils.setupUserStatisticRepeatingService();
        }
    }

    /**
     * get the instance of the AuthComponent.
     *
     * @return instance of the AuthComponent
     */
    public AuthComponent getAuthComponent() {
        return authComponent;
    }

    /**
     * Release the instance of AuthComponent.
     */
    public void releaseAuthComponent() {
        logger.info("releasing user component");
        authUserUtils.removeAuthUser();
        authComponent = null;
        alarmManagerUtils.cancelUserStatisticRepeatingService();
    }
}
