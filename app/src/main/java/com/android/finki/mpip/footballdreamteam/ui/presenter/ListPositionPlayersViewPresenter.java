package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 17.08.2016.
 */
public class ListPositionPlayersViewPresenter {

    private static Logger logger = LoggerFactory.getLogger(ListPositionPlayersViewPresenter.class);
    private ListPositionPlayersView view;
    private PlayerDBService playerDBService;
    private List<Player> players;
    private String txtPlace;

    public ListPositionPlayersViewPresenter(ListPositionPlayersView view,
                                            PlayerDBService playerDBService) {
        this.view = view;
        this.playerDBService = playerDBService;
    }

    /**
     * Called when the view has been created.
     *
     * @param args view arguments
     */
    public void onViewCreated(Bundle args) {
        logger.info("onViewCreated");
        if (args == null) {
            throw new IllegalArgumentException("bundle can't be null");
        }
        Serializable serializable = args.getSerializable(ListPositionPlayersView.PLACE_KEY);
        if (!(serializable instanceof PositionUtils.POSITION_PLACE)) {
            throw new IllegalArgumentException("position place must be set");
        }
        int[] playersToExclude = args.getIntArray(ListPositionPlayersView.EXCLUDE_LAYERS_KEY);
        if (playersToExclude == null) {
            throw new IllegalArgumentException("players to exclude must be set");
        }
        PositionUtils.POSITION_PLACE place = (PositionUtils.POSITION_PLACE)serializable;
        this.getPlayers(place, playersToExclude);
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        if (this.players == null) {
            throw new IllegalArgumentException("players nto set");
        }
        view.onPlayersLoaded(this.players, txtPlace);
    }

    /**
     * Set the positions place.
     *
     * @param place the player place on the field
     * @param playersToExclude players ids that should be excluded from the response
     */
    private void getPlayers(PositionUtils.POSITION_PLACE place, int[] playersToExclude) {
        playerDBService.open();
        this.txtPlace = place.getName();
        switch (place) {
            case KEEPERS:
                this.players = playerDBService.getGoalkeepers(playersToExclude);
                break;
            case DEFENDERS:
                this.players = playerDBService.getDefenders(playersToExclude);
                break;
            case MIDFIELDERS:
                this.players = playerDBService.getMidfielders(playersToExclude);
                break;
            case ATTACKERS:
                this.players = playerDBService.getAttackers(playersToExclude);
                break;
            default:
                throw new IllegalArgumentException("unknown position place");
        }
        playerDBService.close();
    }

    /**
     * Get all players that are loaded from the database.
     *
     * @return all loaded players
     */
    public List<Player> getPlayers() {
        return players;
    }
}
