package com.android.finki.mpip.footballdreamteam.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.UsersApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupPlayersActivity;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by Borce on 13.09.2016.
 */
public class UserStatisticService extends IntentService {

    private static final Logger logger = LoggerFactory.getLogger(UserStatisticService.class);
    private static final String name = "USER_STATISTIC";
    private SharedPreferences preferences;
    private NotificationManager notificationManager;
    private String key;
    private UsersApi api;
    private User user;

    public UserStatisticService() {
        super(name);
    }

    /**
     * Set the application shared preferences.
     *
     * @param preferences application shared preferences
     */
    @Inject
    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Set the application notification manager.
     *
     * @param notificationManager application notification manager
     */
    @Inject
    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    /**
     * Set the UserApi.
     *
     * @param api instance of UserApi
     */
    @Inject
    public void setApi(UsersApi api) {
        this.api = api;
    }

    /**
     * Set the instance of the authenticated user.
     *
     * @param user authenticated user
     */
    @Inject
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Called when the background service is ready to be created.
     */
    @Override
    public void onCreate() {
        logger.info("onCreate");
        super.onCreate();
        MainApplication application = (MainApplication) this.getApplication();
        application.createAuthComponent();
        application.getAuthComponent().plus().inject(this);
        this.key = this.getString(R.string.preference_user_statistic_service_last_check_mills);
    }

    /**
     * Called before the background service is destroyed.
     */
    @Override
    public void onDestroy() {
        logger.info("onDestroy");
        super.onDestroy();
    }

    /**
     * Main method that is executed on a new worker thread and where the service logic lies.
     *
     * @param intent the value passed to startService when the service is created
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        logger.info("loading statistic");
        long mills = System.currentTimeMillis();
        long lastChecked = preferences.getLong(key, mills);
        preferences.edit().putLong(key, mills).apply();
        try {
            Lineup lineup = api.statistic(lastChecked).execute().body();
            logger.info("statistics loaded");
            if (lineup.getId() > 0 && (lineup.getLikesCount() > 0 ||
                    lineup.getCommentsCount() > 0)) {
                logger.info("add notification");
                this.addNotification(lineup);
            }
        } catch (IOException e) {
            logger.info("error occurred while loading the statistic");
            e.printStackTrace();
        }
    }

    /**
     * Create a new notification.
     *
     * @param lineup lineup for which the notification will be created
     */
    private void addNotification(Lineup lineup) {
        int id = (int) System.currentTimeMillis();
        String ticker = this.getString(R.string.notification_ticker);
        String title = this.getString(R.string.notification_title);
        String body = this.getString(R.string.notification_body,
                lineup.getLikesCount(), lineup.getCommentsCount());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker(ticker)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setSmallIcon(R.drawable.lineup)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent(lineup));
        notificationManager.notify(id, builder.build());
    }

    /**
     * Get the
     *
     * @param lineup lineup that will be in the pending intent
     */
    private PendingIntent getPendingIntent(Lineup lineup) {
        lineup.setUser(user);
        Intent intent = new Intent(this, LineupPlayersActivity.class);
        intent.putExtra(LineupPlayersView.LINEUP_BUNDLE_KEY, lineup);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LineupPlayersActivity.class);
        stackBuilder.addNextIntent(intent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
