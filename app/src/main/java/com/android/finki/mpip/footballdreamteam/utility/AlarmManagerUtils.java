package com.android.finki.mpip.footballdreamteam.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.service.UserStatisticService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 14.09.2016.
 */
public class AlarmManagerUtils {

    private static final Logger logger = LoggerFactory.getLogger(AlarmManagerUtils.class);
    public static final int USER_STATISTIC_SERVICE_REQUEST_CODE = 0;
    public static final int USER_STATISTIC_SERVICE_REPEATING_MILLS =  10 * 60 * 1000;
    private Context context;
    private AlarmManager manager;
    private SharedPreferences preferences;
    private String userStatisticLastCheckedKey;

    public AlarmManagerUtils(Context context, AlarmManager manager,
                             SharedPreferences preferences) {
        this.context = context;
        this.manager = manager;
        this.preferences = preferences;
        this.userStatisticLastCheckedKey =
                context.getString(R.string.preference_user_statistic_service_last_check_mills);
    }

    /**
     * Get the pending intent for the UserStatisticService.
     *
     * @return pending intent for the UserStatisticService
     */
    private PendingIntent getUserStatisticServicePendingIntent() {
        Intent intent = new Intent(context, UserStatisticService.class);
        return PendingIntent.getService(context, USER_STATISTIC_SERVICE_REQUEST_CODE, intent, 0);
    }

    /**
     * Setup the alarm with pending intent for the UserStatisticService.
     * @see UserStatisticService
     */
    public void setupUserStatisticRepeatingService() {
        logger.info("setupUserStatisticRepeatingService");
        long lastChecked = preferences.getLong(userStatisticLastCheckedKey, 0);
        if (System.currentTimeMillis() - lastChecked > 2 * USER_STATISTIC_SERVICE_REPEATING_MILLS) {
            logger.info("starting repeating alarm for the user statistic service");
            long triggerAtMills = SystemClock.elapsedRealtime() + 10000;
            manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMills,
                    USER_STATISTIC_SERVICE_REPEATING_MILLS,
                    this.getUserStatisticServicePendingIntent());
        } else {
            logger.info("repeating alarm for the user statistic service already started");
        }
    }

    /**
     * Cancel the repeating alarm for the UserStatisticService.
     * @see UserStatisticService
     */
    public void cancelUserStatisticRepeatingService() {
        logger.info("cancelUserStatisticRepeatingService");
        manager.cancel(this.getUserStatisticServicePendingIntent());
    }
}
