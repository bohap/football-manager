package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 16.08.2016.
 */
public class LineupPlayersViewPresenter extends BasePresenter {

    private static Logger logger = LoggerFactory.getLogger(LineupPlayersViewPresenter.class);
    private LineupPlayersView view;
    private User user;
    private Lineup lineup;
    private LineupApi api;
    private LineupDBService lineupDBService;
    private LineupPlayerDBService lineupPlayerDBService;
    private LineupPlayerValidator validator;
    private Call<List<Player>> lineupPlayersCall;
    private Call<LineupResponse> updateLineupCall;
    private boolean loadRequestSending = false;
    private boolean updateRequestSending = false;
    private boolean viewLayoutCreated = false;
    private boolean lineupValid = false;
    private boolean changed = false;
    private boolean lineupUpdateFailed = false;
    private LineupUtils.FORMATION formation;

    public LineupPlayersViewPresenter(LineupPlayersView view, User user,
                                      LineupApi api, LineupDBService lineupDBService,
                                      LineupPlayerDBService lineupPlayerDBService,
                                      LineupPlayerValidator validator) {
        this.view = view;
        this.user = user;
        this.api = api;
        this.lineupDBService = lineupDBService;
        this.lineupPlayerDBService = lineupPlayerDBService;
        this.validator = validator;
    }

    /**
     * Called when the view is created.
     *
     * @param args view arguments
     */
    public void onViewCreated(Bundle args) {
        this.extractLineup(args);
        this.loadPlayers();
    }

    /**
     * Called when the view layout is crated.
     */
    public void onViewLayoutCreated() {
        this.viewLayoutCreated = true;
        if (loadRequestSending) {
            view.showLoading();
        }
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        if (lineupPlayersCall != null) {
            lineupPlayersCall.cancel();
        }
        if (updateLineupCall != null) {
            updateLineupCall.cancel();
        }
    }

    /**
     * Extract the lineup data from the view intent.
     *
     * @param args view params
     */
    void extractLineup(Bundle args) {
        if (args == null) {
            throw new IllegalArgumentException("view arguments can't be null");
        }
        Serializable serializable = args.getSerializable(LineupPlayersView.LINEUP_BUNDLE_KEY);
        if (serializable == null || !(serializable instanceof Lineup)) {
            throw new IllegalArgumentException("lineup must be provided for the presenter");
        }
        this.lineup = (Lineup) serializable;
        if (this.lineup.getId() < 1) {
            throw new IllegalArgumentException(
                    String.format("invalid lineup id, %d", lineup.getId()));
        }
    }

    /**
     * Get the lineup for the players.
     *
     * @return players lineup
     */
    public Lineup getLineup() {
        return lineup;
    }

    /**
     * Check if the authenticated user can edit the lineup.
     *
     * @return whatever the user can edit the lineup
     */
    public boolean canEditLineup() {
        int lineupUserId = this.lineup.getUserId();
        if (lineupUserId < 1 && lineup.getUser() != null) {
            lineupUserId = lineup.getUser().getId();
        }
        return lineupUserId == user.getId();
    }

    /**
     * Update the lineup changed situation.
     *
     * @param changed whatever the lineup is changed or not
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Checks if the lineup has been changed.
     *
     * @return whatever the lineup has been changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Called when the lineup formation has been changed.
     *
     * @param lineupValid value indicating whatever the lineup is valid or not
     */
    public void setLineupValid(boolean lineupValid) {
        this.lineupValid = lineupValid;
    }

    /**
     * Checks if the lineup is valid.
     *
     * @return whatever the lineup is valid
     */
    public boolean isLineupValid() {
        return lineupValid;
    }

    /**
     * Check if the request for updating the lineup failed.
     *
     * @return  whatever the update request failed
     */
    public boolean isLineupUpdateFailed() {
        return lineupUpdateFailed;
    }

    /**
     * Load the lineup players from the server.
     */
    public void loadPlayers() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup not set");
        }
        if (!loadRequestSending) {
            logger.info("sending load players request");
            loadRequestSending = true;
            if (viewLayoutCreated) {
                view.showLoading();
            }
            lineupPlayersCall = api.players(lineup.getId(), null, null);
            lineupPlayersCall.enqueue(new Callback<List<Player>>() {
                @Override
                public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                    onPlayerLoadingSuccess(response);
                }

                @Override
                public void onFailure(Call<List<Player>> call, Throwable t) {
                    onPlayersLoadingFailed(call, t);
                }
            });
        }
    }

    /**
     * Called when loading the players data is successful.
     *
     * @param response server response
     */
    private void onPlayerLoadingSuccess(Response<List<Player>> response) {
        logger.info("load players request success");
        loadRequestSending = false;
        lineupPlayersCall = null;
        if (viewLayoutCreated) {
            view.showLoadingSuccess(response.body());
        }
    }

    /**
     * Called when loading the players data failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onPlayersLoadingFailed(Call<List<Player>> call, Throwable t) {
        logger.info("load players request failed");
        loadRequestSending = false;
        if (call.isCanceled()) {
            logger.info("load players request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showLoadingFailed();
                super.onRequestFailed(view, t);
            }
        }
        lineupPlayersCall = null;
    }

    /**
     * Send a request to update the lineup players.
     */
    public void update() {
        if (this.lineup == null) {
            throw new IllegalArgumentException("lineup data is not yet sey");
        }
        if (!this.changed) {
            throw new IllegalArgumentException("onUpdateSuccess called but data was not changed");
        }
        final List<LineupPlayer> lineupPlayers = view.getLineupPlayers();
        if (!validator.validate(lineupPlayers)) {
            throw new IllegalArgumentException("lineup players are not valid");
        }
        if (!updateRequestSending) {
            logger.info("sending lineup onUpdateSuccess request");
            updateRequestSending = true;
            if (viewLayoutCreated) {
                view.showUpdating();
            }
            updateLineupCall =
                    api.update(lineup.getId(), this.createdLineupRequest(lineupPlayers));
            updateLineupCall.enqueue(new Callback<LineupResponse>() {
                @Override
                public void onResponse(Call<LineupResponse> call,
                                       Response<LineupResponse> response) {
                    onUpdateSuccess(lineupPlayers);
                }

                @Override
                public void onFailure(Call<LineupResponse> call, Throwable t) {
                    onUpdateFailed(call, t);
                }
            });
        }
    }

    /**
     * Create a LineupRequest for the given List of LineupPlayer.
     *
     * @param lineupPlayers List of players in the lineup
     * @return LineupRequest
     */
    private LineupRequest createdLineupRequest(List<LineupPlayer> lineupPlayers) {
        LineupRequest request = new LineupRequest();
        List<LineupRequest.Content> players = new ArrayList<>();
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            int playerId = lineupPlayer.getPlayerId();
            int positionId = lineupPlayer.getPositionId();
            players.add(new LineupRequest.Content(playerId, positionId));
        }
        request.setPlayers(players);
        return request;
    }

    /**
     * Called when updating the lineup is successful.
     *
     * @param lineupPlayers players in the lineup
     */
    private void onUpdateSuccess(List<LineupPlayer> lineupPlayers) {
        logger.info("lineup onUpdateSuccess request success");
        updateRequestSending = false;
        changed = false;
        lineupUpdateFailed = false;
        updateLineupCall = null;

        boolean lineupExistsInDatabase = false;
        boolean lineupSavingError = false;
        lineupDBService.open();
        lineup.setUpdatedAt(new Date());
        try {
            if (lineupDBService.exists(lineup)) {
                lineupDBService.update(lineup);
                lineupExistsInDatabase = true;
            } else {
                lineupDBService.store(lineup);
            }
        } catch (RuntimeException exp) {
            String message = "error occurred while changing the lineup in the database";
            logger.error(message);
            lineupSavingError = true;
        } finally {
            lineupDBService.close();
        }
        if (!lineupSavingError) {
            /* Save the players for the lineup. */
            String message = "error occurred while changing the lineup players in the database";
            lineupPlayerDBService.open();
            for (LineupPlayer player : lineupPlayers) {
                player.setLineupId(lineup.getId());
            }
            try {
                boolean result;
                if (lineupExistsInDatabase) {
                    result = lineupPlayerDBService.syncPlayers(lineup.getId(), lineupPlayers);
                } else {
                    result = lineupPlayerDBService.storePlayers(lineupPlayers);
                }
                if (!result) {
                    logger.error(message);
                }
            } catch (RuntimeException exp) {
                logger.error(message);
            } finally {
                lineupPlayerDBService.close();
            }
        }
        if (viewLayoutCreated) {
            view.showUpdatingSuccess();
        }
    }

    /**
     * Called when updating the lineup failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    private void onUpdateFailed(Call<LineupResponse> call, Throwable t) {
        logger.info("lineup onUpdateSuccess request failed");
        lineupUpdateFailed = true;
        updateRequestSending = false;
        if (call.isCanceled()) {
            logger.info("lineup onUpdateSuccess call canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showUpdatingFailed();
                super.onRequestFailed(view, t);
            }
        }
        updateLineupCall = null;
    }

    /**
     * Update the view formation.
     *
     * @param formation new formation
     */
    public void updateFormation(LineupUtils.FORMATION formation) {
        logger.info("updateFormation");
        if (this.formation == null) {
            this.formation = view.getFormation();
        }
        if (this.formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }
        if (!this.formation.equals(formation) && viewLayoutCreated) {
            view.changeFormation(formation, view.getPlayersOrdered());
            this.formation = formation;
        }
    }
}