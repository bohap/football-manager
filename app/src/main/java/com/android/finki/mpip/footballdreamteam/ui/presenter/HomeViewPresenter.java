package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.content.Context;
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
import com.android.finki.mpip.footballdreamteam.ui.component.HomeView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 09.08.2016.
 */
public class HomeViewPresenter extends BasePresenter implements StoreTeamsTask.Listener,
                                                                StorePositionsTask.Listener,
                                                                StorePlayersTask.Listener {

    private Logger logger = LoggerFactory.getLogger(HomeViewPresenter.class);
    private HomeView view;
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
    private Call<List<Team>> teamCall;
    private Call<List<Position>> positionCall;
    private Call<List<Player>> playerCall;
    private boolean requestSending = false;
    private boolean viewLayoutCreated = false;
    private boolean isInfoDialogShowed = false;
    private boolean storeTeamsTaskExecuting = false;
    private boolean storePositionsTaskExecuting = false;
    private boolean storePlayersTaskExecuting = false;

    public HomeViewPresenter(HomeView view, SharedPreferences preferences, Context context,
                             TeamApi teamApi, PositionApi positionApi, PlayerApi playerApi,
                             StoreTeamsTask storeTeamsTask, StorePositionsTask storePositionsTask,
                             StorePlayersTask storePlayersTask) {
        this.view = view;
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
        this.TEAMS_LOADED_KEY = context.getString(R.string.preference_teams_loaded_key);
        this.POSITIONS_LOADED_KEY = context.getString(R.string.preference_positions_loaded_key);
        this.PLAYERS_LOADED_KEY = context.getString(R.string.preference_players_loaded_key);
    }

    /**
     * Called when the view is created.
     */
    public void onViewCreated() {
        logger.info("onViewCreated");
        this.loadData();
    }

    /**
     * Called when the view is visible to the user.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCrated");
        this.viewLayoutCreated = true;
        if (requestSending) {
            view.showInitialDataLoading();
            if (!isInfoDialogShowed) {
                isInfoDialogShowed = true;
                view.showInitialDataInfoDialog();
            }
        }
    }

    /**
     * Load the teams, positions and players data from the server.
     */
    public void loadData() {
        this.loadTeams();
    }

    /**
     * Called when the http request failed.
     *
     * @param t exception that has been thrown
     */
    private void requestFailed(Throwable t) {
        t.printStackTrace();
        super.onRequestFailed(view, t);
    }

    /**
     * Load the teams from the server.
     */
    private void loadTeams() {
        if (!this.isTeamsLoaded()) {
            if (!requestSending) {
                logger.info("sending index teams request");
                requestSending = true;
                if (viewLayoutCreated) {
                    view.showInitialDataLoading();
                    view.showTeamsLoading();
                    if (!isInfoDialogShowed) {
                        isInfoDialogShowed = true;
                        view.showInitialDataInfoDialog();
                    }
                }
                teamCall = teamApi.index(null, null, null);
                teamCall.enqueue(new Callback<List<Team>>() {
                    @Override
                    public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                        onTeamsLoadingSuccess(response);
                    }

                    @Override
                    public void onFailure(Call<List<Team>> call, Throwable t) {
                        onTeamsLoadingFailed(call, t);
                    }
                });
            }
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
     * Called when loading the teams is successful.
     *
     * @param response server response
     */
    private void onTeamsLoadingSuccess(Response<List<Team>> response) {
        logger.info("teams loading success");
        requestSending = false;
        teamCall = null;
        List<Team> teams = response.body();
        storeTeamsTaskExecuting = true;
        storeTeamsTask.execute(teams.toArray(new Team[teams.size()]));
        if (viewLayoutCreated) {
            view.showTeamsStoring();
        }
    }

    /**
     * Called when loading the teams failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onTeamsLoadingFailed(Call<List<Team>> call, Throwable t) {
        logger.info("teams loading failed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("teams loading request canceled");
        } else {
            if (viewLayoutCreated) {
                view.showTeamsLoadingFailed();
                requestFailed(t);
            }
        }
        teamCall = null;
    }

    /**
     * Called when saving the teams in the database is successful.
     */
    @Override
    public void onTeamsSavingSuccess() {
        logger.info("onTeamsSavingSuccess");
        preferences.edit().putBoolean(TEAMS_LOADED_KEY, true).apply();
        storeTeamsTaskExecuting = false;
        loadPositions();
    }

    /**
     * Called when saving the teams in the database failed.
     */
    @Override
    public void onTeamsSavingFailed() {
        logger.info("onTeamsSavingFailed");
        storeTeamsTaskExecuting = false;
        if (viewLayoutCreated) {
            view.showTeamsStoringFailed();
        }
    }

    /**
     * Load the position from the server.
     */
    private void loadPositions() {
        if (!this.isPositionsLoaded()) {
            if (!requestSending) {
                logger.info("sending index positions request");
                requestSending = true;
                if (viewLayoutCreated) {
                    view.showInitialDataLoading();
                    view.showPositionsLoading();
                    if (!isInfoDialogShowed) {
                        isInfoDialogShowed = true;
                        view.showInitialDataInfoDialog();
                    }
                }
                positionCall = positionApi.index(null, null, null);
                positionCall.enqueue(new Callback<List<Position>>() {
                    @Override
                    public void onResponse(Call<List<Position>> call,
                                           Response<List<Position>> response) {
                        onPositionsLoadingSuccess(response);
                    }

                    @Override
                    public void onFailure(Call<List<Position>> call, Throwable t) {
                        onPositionsLoadingFailed(call, t);
                    }
                });
            }
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
     * Called when loading the positions is successful.
     *
     * @param response server response
     */
    private void onPositionsLoadingSuccess(Response<List<Position>> response) {
        logger.info("positions loading success");
        requestSending = false;
        positionCall = null;
        List<Position> positions = response.body();
        storePositionsTaskExecuting = true;
        storePositionsTask.execute(positions.toArray(new Position[positions.size()]));
        if (viewLayoutCreated) {
            view.showPositionsStoring();
        }
    }

    /**
     * Called when loading the positions failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onPositionsLoadingFailed(Call<List<Position>> call, Throwable t) {
        logger.info("positions loading failed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("positions loading request canceled");
        } else {
            if (viewLayoutCreated) {
                view.showPositionsLoadingFailed();
                requestFailed(t);
            }
        }
        positionCall = null;
    }

    /**
     * Called when saving the positions in the database is successful.
     */
    @Override
    public void onPositionsSavingSuccess() {
        logger.info("onPositionsSavingSuccess");
        preferences.edit().putBoolean(POSITIONS_LOADED_KEY, true).apply();
        storePositionsTaskExecuting = false;
        loadPlayers();
    }

    /**
     * Called when saving the positions in the database is successful.
     */
    @Override
    public void onPositionsSavingFailed() {
        logger.info("onPositionsSavingFailed");
        storePositionsTaskExecuting = false;
        if (viewLayoutCreated) {
            view.showPositionsStoringFailed();
        }
    }

    /**
     * Load the players from the server.
     */
    private void loadPlayers() {
        if (!this.isPlayersLoaded()) {
            if (!requestSending) {
                logger.info("sending index players request");
                requestSending = true;
                if (viewLayoutCreated) {
                    view.showInitialDataLoading();
                    view.showPlayersLoading();
                    if (!isInfoDialogShowed) {
                        isInfoDialogShowed = true;
                        view.showInitialDataInfoDialog();
                    }
                }
                playerCall = playerApi.index(false, null, null);
                playerCall.enqueue(new Callback<List<Player>>() {
                    @Override
                    public void onResponse(Call<List<Player>> call,
                                           Response<List<Player>> response) {
                        onPlayersLoadingSuccess(response);
                    }

                    @Override
                    public void onFailure(Call<List<Player>> call, Throwable t) {
                        onPlayersLoadingFailed(call, t);
                    }
                });
            }
        } else {
            view.showInitialDataLoadingSuccess();
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
     * Called when loading the players is successful.
     *
     * @param response server response
     */
    private void onPlayersLoadingSuccess(Response<List<Player>> response) {
        logger.info("players loading success");
        requestSending = false;
        playerCall = null;
        List<Player> players = response.body();
        storePlayersTaskExecuting = true;
        storePlayersTask.execute(players.toArray(new Player[players.size()]));
        if (viewLayoutCreated) {
            view.showPlayersStoring();
        }
    }

    /**
     * Called when loading the players failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onPlayersLoadingFailed(Call<List<Player>> call, Throwable t) {
        logger.info("players loading failed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("players loading request canceled");
        } else {
            if (viewLayoutCreated) {
                view.showPlayersLoadingFailed();
                requestFailed(t);
            }
        }
        playerCall = null;
    }

    /**
     * Called when saving the players in the database is successful.
     */
    @Override
    public void onPlayersSavingSuccess() {
        logger.info("onPlayersSavingSuccess");
        preferences.edit().putBoolean(PLAYERS_LOADED_KEY, true).apply();
        storePlayersTaskExecuting = false;
        if (viewLayoutCreated) {
            view.showInitialDataLoadingSuccess();
        }
    }

    /**
     * Called when storing the players in the database failed.
     */
    @Override
    public void onPlayersSavingFailed() {
        logger.info("onPlayersSavingFailed");
        storePlayersTaskExecuting = false;
        if (viewLayoutCreated) {
            view.showPlayersStoringFailed();
        }
    }

    /**
     * Called when the view is not anymore visible.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called before the view is destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (teamCall != null) {
            teamCall.cancel();
        }
        if (positionCall != null) {
            positionCall.cancel();
        }
        if (playerCall != null) {
            playerCall.cancel();
        }
        if (storeTeamsTaskExecuting) {
            storeTeamsTask.cancel(true);
        }
        if (storePositionsTaskExecuting) {
            storePositionsTask.cancel(true);
        }
        if (storePlayersTaskExecuting) {
            storePlayersTask.cancel(true);
        }
    }
}