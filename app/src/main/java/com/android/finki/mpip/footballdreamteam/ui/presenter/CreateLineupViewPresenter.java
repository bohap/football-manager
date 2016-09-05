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
public class CreateLineupViewPresenter implements Callback<LineupResponse> {

    private static final Logger logger = LoggerFactory.getLogger(CreateLineupViewPresenter.class);
    private CreatedLineupView view;
    private LineupApi api;
    private LineupDBService lineupDBService;
    private LineupPlayerDBService lineupPlayerDBService;
    private LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_4_2;
    private List<LineupPlayer> players;

    private boolean formationValid = false;
    private boolean changed = false;

    public CreateLineupViewPresenter(CreatedLineupView view, LineupApi api,
                                     LineupDBService lineupDBService,
                                     LineupPlayerDBService lineupPlayerDBService) {
        this.view = view;
        this.api = api;
        this.lineupDBService = lineupDBService;
        this.lineupPlayerDBService = lineupPlayerDBService;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setFormationValid(boolean formationValid) {
        this.formationValid = formationValid;
    }

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
        if (!this.formation.equals(formation)) {
            this.formation = formation;
            view.changeFormation(formation, view.getPlayersOrdered());
        }
    }

    /**
     * Send a request to store the lineup.
     *
     */
    public void store() {
        view.showStoring();
        this.players = view.getLineupPlayers();
        api.store(this.createLineupRequest(players)).enqueue(this);
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
        if (response.isSuccessful()) {
            Lineup lineup = response.body().getLineup();
            if (lineup == null) {
                String message = "lineup can't be null";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            view.showStoringSuccessful(lineup);
            lineupDBService.open();
            boolean lineupSavingError = false;
            if (!lineupDBService.exists(lineup.getId())) {
                try {
                    lineupDBService.store(lineup);
                } catch (RuntimeException exp) {
                    logger.error("error occurred while saving the lineup");
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
        } else {
            view.showStoringFailed();
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
        view.showStoringFailed();
        //Check the exception
    }
}