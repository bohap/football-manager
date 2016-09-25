package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.AsyncTask;

import com.android.finki.mpip.footballdreamteam.database.service.TeamDBService;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 29.08.2016.
 */
public class StoreTeamsTask extends AsyncTask<Team, Void, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(StoreTeamsTask.class);
    private TeamDBService dbService;
    private Listener listener;

    public StoreTeamsTask(TeamDBService dbService) {
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
     * Save the teams on a new worker thread.
     *
     * @param teams array of teams to be saved
     * @return whatever the teams saving is successful
     */
    @Override
    protected final Boolean doInBackground(Team... teams) {
        logger.info("doInBackground");
        dbService.open();
        for (Team team : teams) {
            try {
                if (!dbService.exists(team.getId())) {
                    dbService.store(team);
                }
            } catch (RuntimeException exp) {
                logger.error("error occurred while saving the team");
                exp.printStackTrace();
                dbService.close();
                return false;
            }
        }
        dbService.close();
        return true;
    }

    /**
     * Called when doInBackgroundMethod has finished.
     *
     * @param success whatever the saving is successful
     */
    @Override
    protected void onPostExecute(Boolean success) {
        logger.info("onPostExecute");
        if (listener != null) {
            if (success) {
                listener.onTeamsSavingSuccess();
            } else {
                listener.onTeamsSavingFailed();
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

        void onTeamsSavingSuccess();

        void onTeamsSavingFailed();
    }
}
