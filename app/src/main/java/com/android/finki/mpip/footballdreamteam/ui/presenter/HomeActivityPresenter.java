package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.background.task.StorePlayersTask;
import com.android.finki.mpip.footballdreamteam.background.task.StorePositionsTask;
import com.android.finki.mpip.footballdreamteam.background.task.StoreTeamsTask;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 09.08.2016.
 */
public class HomeActivityPresenter implements StoreTeamsTask.Listener,
        StorePositionsTask.Listener, StorePlayersTask.Listener {

    private Logger logger = LoggerFactory.getLogger(HomeActivityPresenter.class);

    private HomeActivity activity;
    private SharedPreferences preferences;
    private TeamApi teamApi;
    private PositionApi positionApi;
    private PlayerApi playerApi;
    private StoreTeamsTask storeTeamsTask;
    private StorePositionsTask storePositionsTask;
    private StorePlayersTask storePlayersTask;

    private String TEAMS_LOADED_KEY;
    private String POSITIONS_LOADED_KEY;
    private String PLAYERS_LOADED_KEY;
    private String AUTH_USER_ID_KEY;

    private boolean isInfoDialogShowed = false;
    private boolean mainViewVisible = false;

    public HomeActivityPresenter(HomeActivity activity, SharedPreferences preferences,
                                 TeamApi teamApi, PositionApi positionApi, PlayerApi playerApi,
                                 StoreTeamsTask storeTeamsTask,
                                 StorePositionsTask storePositionsTask,
                                 StorePlayersTask storePlayersTask) {
        this.activity = activity;
        this.preferences = preferences;
        this.teamApi = teamApi;
        this.positionApi = positionApi;
        this.playerApi = playerApi;
        this.storeTeamsTask = storeTeamsTask;
        this.storePositionsTask = storePositionsTask;
        this.storePlayersTask = storePlayersTask;

        this.storePositionsTask.setListener(this);
        this.storeTeamsTask.setListener(this);
        this.storePlayersTask.setListener(this);

        this.TEAMS_LOADED_KEY = activity.getString(R.string.preference_teams_loaded_key);
        this.POSITIONS_LOADED_KEY = activity.getString(R.string.preference_positions_loaded_key);
        this.PLAYERS_LOADED_KEY = activity.getString(R.string.preference_players_loaded_key);
        this.AUTH_USER_ID_KEY = activity.getString(R.string.preference_auth_user_id_key);
    }

    /**
     * Set the visibility of the main view.
     *
     * @param mainViewVisible new main view visibility
     */
    public void setMainViewVisible(boolean mainViewVisible) {
        this.mainViewVisible = mainViewVisible;
    }

    /**
     * Checks if the main view is visible.
     *
     * @return whatever the main view is visible
     */
    public boolean isMainViewVisible() {
        return mainViewVisible;
    }

    /**
     * Called when the http request failed.
     *
     * @param t exception that has been thrown
     */
    private void requestFailed(Throwable t) {
        t.printStackTrace();
        activity.showErrorLoading();
        if (t instanceof SocketTimeoutException) {
            activity.showConnectionTimeoutMessage();
        } else {
            activity.showServerErrorMessage();
        }
    }

    /**
     * Called when the activity is created.
     */
    public void loadData() {
        this.loadTeams();
    }

    /**
     * Load the teams from the server.
     */
    private void loadTeams() {
        if (!this.isTeamsLoaded()) {
            logger.info("sending index teams request");
            activity.showInitialDataLoading();
            if (!isInfoDialogShowed) {
                isInfoDialogShowed = true;
                activity.showInfoDialog();
            }
            Call<List<Team>> call = teamApi.index(null, null, null);
            call.enqueue(new Callback<List<Team>>() {

                @Override
                public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                    if (response.isSuccessful()) {
                        logger.info("teams loading success");
                        List<Team> teams = response.body();
                        storeTeamsTask.execute(teams.toArray(new Team[teams.size()]));
                    } else {
                        logger.info("teams loading failed");
                        activity.showErrorLoading();
                    }
                }

                @Override
                public void onFailure(Call<List<Team>> call, Throwable t) {
                    logger.info("teams loading failed");
                    requestFailed(t);
                }
            });
        } else {
            this.loadPositions();
        }
    }

    /**
     * Checks if the teams are loaded from the server.
     *
     * @return whatever the teams are loaded
     */
    private boolean isTeamsLoaded() {
        return preferences.getBoolean(TEAMS_LOADED_KEY, false);
    }

    /**
     * Called when saving the teams in the database is successful.
     */
    @Override
    public void onTeamsSavingSuccess() {
        preferences.edit().putBoolean(TEAMS_LOADED_KEY, true).apply();
        loadPositions();
    }

    /**
     * Called when saving the teams in the database failed.
     */
    @Override
    public void onTeamsSavingFailed() {
        activity.showErrorLoading();
    }

    /**
     * Load the position from the server.
     */
    private void loadPositions() {
        if (!this.isPositionsLoaded()) {
            logger.info("sending index positions request");
            activity.showInitialDataLoading();
            if (!isInfoDialogShowed) {
                isInfoDialogShowed = true;
                activity.showInfoDialog();
            }
            Call<List<Position>> call = positionApi.index(null, null, null);
            call.enqueue(new Callback<List<Position>>() {
                @Override
                public void onResponse(Call<List<Position>> call,
                                       Response<List<Position>> response) {
                    if (response.isSuccessful()) {
                        logger.info("positions loading success");
                        List<Position> positions = response.body();
                        storePositionsTask.execute(positions.toArray(new Position[positions.size()]));
                    } else {
                        activity.showErrorLoading();
                    }
                }

                @Override
                public void onFailure(Call<List<Position>> call, Throwable t) {
                    logger.info("positions loading failed");
                    requestFailed(t);
                }
            });
        } else {
            this.loadPlayers();
        }
    }

    /**
     * Checks if the positions are loaded from the server.
     *
     * @return whatever the positions are loaded
     */
    private boolean isPositionsLoaded() {
        return preferences.getBoolean(POSITIONS_LOADED_KEY, false);
    }

    /**
     * Called when saving the positions in the database is successful.
     */
    @Override
    public void onPositionsSavingSuccess() {
        preferences.edit().putBoolean(POSITIONS_LOADED_KEY, true).apply();
        loadPlayers();
    }

    /**
     * Called when saving the positions in the database is successful.
     */
    @Override
    public void onPositionsSavingFailed() {
        activity.showErrorLoading();
    }

    /**
     * Load the players from the server.
     */
    private void loadPlayers() {
        if (!this.isPlayersLoaded()) {
            logger.info("sending index players request");
            activity.showInitialDataLoading();
            if (!isInfoDialogShowed) {
                isInfoDialogShowed = true;
                activity.showInfoDialog();
            }
            Call<List<Player>> call = playerApi.index(false, null, null);
            call.enqueue(new Callback<List<Player>>() {
                @Override
                public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                    if (response.isSuccessful()) {
                        logger.info("players loading success");
                        List<Player> players = response.body();
                        storePlayersTask.execute(players.toArray(new Player[players.size()]));
                    } else {
                        activity.showErrorLoading();
                    }
                }

                @Override
                public void onFailure(Call<List<Player>> call, Throwable t) {
                    logger.info("players loading failed");
                    requestFailed(t);
                }
            });
        } else {
            activity.showSuccessLoading();
        }
    }

    /**
     * Checks if the players has been loaded.
     *
     * @return whatever the players has been loaded
     */
    private boolean isPlayersLoaded() {
        return preferences.getBoolean(PLAYERS_LOADED_KEY, false);
    }

    /**
     * Called when saving the players in the database is successful.
     */
    @Override
    public void onPlayersSavingSuccess() {
        preferences.edit().putBoolean(PLAYERS_LOADED_KEY, true).apply();
        activity.showSuccessLoading();
    }

    /**
     * Called when storing the players in the database failed.
     */
    @Override
    public void onPlayersSavingFailed() {
        activity.showErrorLoading();
    }


    /**
     * Remove the auth user data from the database.
     */
    public void logout() {
        preferences.edit().putInt(AUTH_USER_ID_KEY, -1).apply();
    }
}