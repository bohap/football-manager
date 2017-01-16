package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.AsyncTask;

import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 30.08.2016.
 */
public class StorePositionsTask extends AsyncTask<Position, Void, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(StorePositionsTask.class);
    private PositionDBService dbService;
    private Listener listener;

    public StorePositionsTask(PositionDBService dbService) {
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
     * Save the positions on a new worker thread.
     *
     * @param positions array of positions to be saved
     * @return whatever the saving is successful
     */
    @Override
    protected Boolean doInBackground(Position... positions) {
        logger.info("doInBackground");
        dbService.open();
        for (Position position : positions) {
            try {
                if (!dbService.exists(position.getId())) {
                    dbService.store(position);
                }
            } catch (RuntimeException exc) {
                logger.error("error occurred while saving the position");
                exc.printStackTrace();
                dbService.close();
                return false;
            }
        }
        dbService.close();
        return true;
    }

    /**
     * Called after the doInBackground has finished.
     *
     * @param success whatever the saving is successful or no
     */
    @Override
    protected void onPostExecute(Boolean success) {
        logger.info("onPostExecute");
        if (listener != null) {
            if (success) {
                listener.onPositionsSavingSuccess();
            } else {
                listener.onPositionsSavingFailed();
            }
        }
    }

    /**
     * Called when the task is canceled.
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

        void onPositionsSavingSuccess();

        void onPositionsSavingFailed();
    }
}
