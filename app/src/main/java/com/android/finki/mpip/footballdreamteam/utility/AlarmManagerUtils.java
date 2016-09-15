package com.android.finki.mpip.footballdreamteam.utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.android.finki.mpip.footballdreamteam.service.UserStatisticService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 14.09.2016.
 */
public class AlarmManagerUtils {

    private static final Logger logger = LoggerFactory.getLogger(AlarmManagerUtils.class);
    private static final int USER_STATISTIC_SERVICE_REQUEST_CODE = 0;
    private static final int USER_STATISTIC_SERVICE_REPEATING_MILLS = 10 * 60 * 1000;
    private Context context;
    private AlarmManager manager;

    public AlarmManagerUtils(Context context, AlarmManager manager) {
        this.context = context;
        this.manager = manager;
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
     *
     * @see UserStatisticService
     */
    public void setupUserStatisticRepeatingService() {
        logger.info("setupUserStatisticRepeatingService");
        long triggerAtMills = SystemClock.elapsedRealtime() + 10000;
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMills,
                USER_STATISTIC_SERVICE_REPEATING_MILLS, this.getUserStatisticServicePendingIntent());
    }

    /**
     * Cancel the repeating alarm for the UserStatisticService.
     *
     * @see UserStatisticService
     */
    public void cancelUserStatisticRepeatingService() {
        logger.info("cancelUserStatisticRepeatingService");
        manager.cancel(this.getUserStatisticServicePendingIntent());
    }
}
