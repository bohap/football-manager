package com.android.finki.mpip.footballdreamteam.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.AlarmManagerUtils;
import com.android.finki.mpip.footballdreamteam.utility.AuthUserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by Borce on 14.09.2016.
 */
public class ApplicationBootReceiver extends BroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationBootReceiver.class);
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
     * @param alarmManagerUtils instance of the AlarmManagerUtils
     */
    @Inject
    public void setAlarmManagerUtils(AlarmManagerUtils alarmManagerUtils) {
        this.alarmManagerUtils = alarmManagerUtils;
    }

    /**
     * Called when the intent is receiver.
     *
     * @param context application context
     * @param intent intent that was sent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.info("onReceive");
        ((MainApplication) context.getApplicationContext()).getAppComponent().inject(this);
        User user = authUserUtils.authenticate();
        if (user != null) {
            logger.info("user authenticated");
            alarmManagerUtils.setupUserStatisticRepeatingService();
        }
    }
}
