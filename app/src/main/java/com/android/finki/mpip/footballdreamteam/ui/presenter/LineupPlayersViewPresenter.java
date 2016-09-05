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
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupPlayersActivity;
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
public class LineupPlayersViewPresenter implements Callback<List<Player>> {

    private static Logger logger = LoggerFactory.getLogger(LineupPlayersViewPresenter.class);
    private LineupPlayersView view;
    private User user;
    private Lineup lineup;
    private LineupApi api;
    private LineupDBService lineupDBService;
    private LineupPlayerDBService lineupPlayerDBService;
    private LineupPlayerValidator validator;

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
     * Called when the lineup players has been changed.
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

    public boolean isLineupUpdateFailed() {
        return lineupUpdateFailed;
    }

    /**
     * Load the lineup players from the server.
     *
     * @param args view param
     */
    public void loadPlayers(Bundle args) {
        this.extractLineup(args);
        logger.info("sending load players request");
        api.players(lineup.getId(), null, null).enqueue(this);
        view.showLoading();
    }

    /**
     * Extract the lineup data from the view intent.
     *
     * @param args view params
     */
    void extractLineup(Bundle args) {
        if (args == null) {
            String message = "view arguments can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        Serializable serializable = args.getSerializable(LineupPlayersActivity.LINEUP_BUNDLE_KEY);
        if (serializable == null || !(serializable instanceof Lineup)) {
            String message = "lineup must be provided for the presenter";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.lineup = (Lineup) serializable;
        if (this.lineup.getId() < 1) {
            String message = String.format("invalid lineup id, %d", lineup.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Called when loading the players data is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
        if (response.isSuccessful()) {
            logger.info("load players request success");
            view.showLoadingSuccess(response.body());
            if (this.canEditLineup()) {
                view.showBtnChangeFormation();
            }
        } else {
            logger.info("load players request failed");
            view.showLoadingFailed();
        }
    }

    /**
     * Called when loading the players data failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    @Override
    public void onFailure(Call<List<Player>> call, Throwable t) {
        logger.info("load players request failed");
        view.showLoadingFailed();
        //TODO
//        if (t instanceof SocketTimeoutException) {
//            view.showConnectionTimeoutMessage();
//        } else {
//            view.showServerErrorMessage();
//        }
    }

    /**
     * Send a request to update the lineup players.
     *
     */
    public void update() {
        if (this.lineup == null) {
            String message = "lineup data is not yet sey";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        final List<LineupPlayer> lineupPlayers = view.getLineupPlayers();
        if (!validator.validate(lineupPlayers)) {
            String message = "lineup players are not valid";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (!this.changed) {
            String message = "update called but data was not changed";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        view.showUpdating();
        logger.info("sending lineup update request");
        api.update(lineup.getId(), this.createdLineupRequest(lineupPlayers))
                .enqueue(new Callback<LineupResponse>() {
                    //TODO server respond with invalid json result on first request
                    @Override
                    public void onResponse(Call<LineupResponse> call,
                                           Response<LineupResponse> response) {
                        if (response.isSuccessful()) {
                            logger.info("lineup update request success");
                            lineupUpdateFailed = false;
                            changed = false;
                            updateSuccess(lineupPlayers);
                        } else {
                            logger.info("lineup update request failed");
                            lineupUpdateFailed = true;
                            view.showUpdatingFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<LineupResponse> call, Throwable t) {
                        logger.info("lineup update request failed");
                        lineupUpdateFailed = true;
                        updateFailed(t);
                    }
                });
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
     */
    private void updateSuccess(List<LineupPlayer> lineupPlayers) {
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
                    result = lineupPlayerDBService.updatePlayers(lineup.getId(), lineupPlayers);
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
        view.showUpdatingSuccess();
    }

    /**
     * Called when updating the lineup failed.
     *
     * @param t exception that has been thrown
     */
    private void updateFailed(Throwable t) {
        view.showUpdatingFailed();
        t.printStackTrace();
        //TODO
//        if (t instanceof SocketTimeoutException) {
//            view.showConnectionTimeoutMessage();
//        } else {
//            view.showServerErrorMessage();
//        }
    }

    /**
     * Update the view formation.
     *
     * @param formation new formation
     */
    public void updateFormation(LineupUtils.FORMATION formation) {
        if (this.formation == null) {
            this.formation = view.getFormation();
        }
        if (this.formation == null) {
            String message = "formation can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (!this.formation.equals(formation)) {
            view.changeFormation(formation, view.getPlayersOrdered());
            this.formation = formation;
        }
    }
}