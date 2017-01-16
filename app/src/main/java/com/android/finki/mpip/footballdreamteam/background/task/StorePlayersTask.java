package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.AsyncTask;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.exception.PlayerException;
import com.android.finki.mpip.footballdreamteam.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 30.08.2016.
 */
public class StorePlayersTask extends AsyncTask<Player, Void, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(StorePlayersTask.class);
    private PlayerDBService dbService;
    private Listener listener;

    public StorePlayersTask(PlayerDBService dbService) {
        this.dbService = dbService;
        this.listener = null;
    }

    /**
     * Set the listener for the task.
     *
     * @param listener task listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Save the players on a new worker thread.
     *
     * @param players   array of players to be saved
     * @return          whatever the saving is successful
     */
    @Override
    protected Boolean doInBackground(Player... players) {
        logger.info("doInBackground");
        dbService.open();
        for (Player player : players) {
            try {
                if (!dbService.exists(player.getId())) {
                    dbService.store(player);
                }
            } catch (PlayerException exp) {
                logger.error("error occurred while saving the players");
                exp.printStackTrace();
                dbService.close();
                return false;
            }
        }
        dbService.close();
        return true;
    }

    /**
     * Called when doInBackground has finished.
     *
     * @param success whatever the saving is successful or not
     */
    @Override
    protected void onPostExecute(Boolean success) {
        logger.info("onPostExecute");
        if (listener != null) {
            if (success) {
                listener.onPlayersSavingSuccess();
            } else {
                listener.onPlayersSavingFailed();
            }
        }
    }

    /**
     * Called when the task in canceled.
     */
    @Override
    protected void onCancelled() {
        logger.info("onCanceled");
        super.onCancelled();
    }

    /**
     * Listener user fro communication with the classes calling the task.
     */
    public interface Listener {

        void onPlayersSavingSuccess();

        void onPlayersSavingFailed();
    }
}
