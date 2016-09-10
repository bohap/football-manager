package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Borce on 22.08.2016.
 */
public class CreateLineupViewPresenter extends BasePresenter implements Callback<LineupResponse> {

    private static final Logger logger = LoggerFactory.getLogger(CreateLineupViewPresenter.class);
    private CreatedLineupView view;
    private LineupApi api;
    private LineupDBService lineupDBService;
    private LineupPlayerDBService lineupPlayerDBService;
    private LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_4_2;
    private List<LineupPlayer> players;
    private Call<LineupResponse> call;
    private boolean viewLayoutCreated = false;
    private boolean formationValid = false;
    private boolean changed = false;
    private boolean requestSending = false;

    public CreateLineupViewPresenter(CreatedLineupView view, LineupApi api,
                                     LineupDBService lineupDBService,
                                     LineupPlayerDBService lineupPlayerDBService) {
        this.view = view;
        this.api = api;
        this.lineupDBService = lineupDBService;
        this.lineupPlayerDBService = lineupPlayerDBService;
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        this.viewLayoutCreated = true;
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        logger.info("onViewLayoutDestroyed");
        this.viewLayoutCreated = false;
    }

    /**
     * Called when the view is destroyed.
     */
    public void onViewDestroyed() {
        logger.info("onViewDestroyed");
        if (call != null) {
            call.cancel();
        }
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
     * CAlled when the lineup formation has been changed.
     *
     * @param valid whatever the formation is valid or not
     */
    public void setFormationValid(boolean valid) {
        this.formationValid = valid;
    }

    /**
     * Checks if the formation is valid.
     *
     * @return whatever the formation is valid
     */
    public boolean isFormationValid() {
        return formationValid;
    }

    /**
     * Get the lineup formation.
     *
     * @return lineup formation
     */
    public LineupUtils.FORMATION getFormation() {
        return this.formation;
    }

    /**
     * Update the view formation.
     *
     * @param formation new lineup formation
     */
    public void updateFormation(LineupUtils.FORMATION formation) {
        if (!this.formation.equals(formation) && viewLayoutCreated) {
            this.formation = formation;
            view.changeFormation(formation, view.getPlayersOrdered());
        }
    }

    /**
     * Send a request to store the lineup.
     *
     */
    public void store() {
        if (viewLayoutCreated && !requestSending) {
            logger.info("store lineup request");
            requestSending = true;
            view.showStoring();
            this.players = view.getLineupPlayers();
            call = api.store(this.createLineupRequest(players));
            call.enqueue(this);
        }
    }

    /**
     * Create a LineupRequest from the List of players.
     *
     * @param players List of players
     * @return new LineupRequest
     */
    private LineupRequest createLineupRequest(List<LineupPlayer> players) {
        LineupRequest request = new LineupRequest();
        List<LineupRequest.Content> requestPlayers = new ArrayList<>();
        for (LineupPlayer player : players) {
            int playerId = player.getPlayerId();
            if (playerId < 1) {
                String message = String.format("invalid player id, %d", playerId);
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            int positionId = player.getPositionId();
            if (positionId < 1) {
                String message = String.format("invalid position id, %d", positionId);
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            requestPlayers.add(new LineupRequest.Content(playerId, positionId));
        }
        request.setPlayers(requestPlayers);
        return request;
    }

    /**
     * Called when storing the lineup is successful.
     *
     * @param call     retrofit call
     * @param response server response
     */
    @Override
    public void onResponse(Call<LineupResponse> call, Response<LineupResponse> response) {
        logger.info("store lineup request success");
        requestSending = false;
        this.call = null;

        Lineup lineup = response.body().getLineup();
        if (lineup == null) {
            throw new IllegalArgumentException("lineup is be null");
        }
        lineupDBService.open();
        boolean lineupSavingError = false;
        if (!lineupDBService.exists(lineup.getId())) {
            try {
                lineupDBService.store(lineup);
            } catch (RuntimeException exp) {
                logger.error("error occurred while saving the lineup");
                exp.printStackTrace();
                lineupSavingError = true;
            } finally {
                lineupDBService.close();
            }
        } else {
            lineupDBService.close();
        }
        if (!lineupSavingError) {
            lineupPlayerDBService.open();
            try {
                lineupPlayerDBService.storePlayers(players);
            } catch (RuntimeException exp) {
                logger.error("error occurred while saving the players");
            } finally {
                lineupPlayerDBService.close();
            }
        }
        if (viewLayoutCreated) {
            view.showStoringSuccessful(lineup);
        }
    }

    /**
     * Called when storing the lineup failed.
     *
     * @param call retrofit call
     * @param t    exception that has been thrown
     */
    @Override
    public void onFailure(Call<LineupResponse> call, Throwable t) {
        logger.info("store lienup request failed");
        requestSending = false;
        if (call.isCanceled()) {
            logger.info("store lineup request canceled");
        } else {
            t.printStackTrace();
            if (viewLayoutCreated) {
                view.showStoringFailed();
                super.onRequestFailed(view, t);
            }
        }
        this.call = null;
    }
}