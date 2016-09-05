package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Borce on 17.08.2016.
 */
public class ListPositionPlayersFragmentPresenter {

    private static Logger logger = LoggerFactory.getLogger(ListPositionPlayersFragment.class);
    private ListPositionPlayersFragment fragment;
    private PlayerDBService playerDBService;
    private List<Player> players;
    private PositionUtils.POSITION_PLACE place;

    public ListPositionPlayersFragmentPresenter(ListPositionPlayersFragment fragment,
                                                PlayerDBService playerDBService) {
        this.fragment = fragment;
        this.playerDBService = playerDBService;
    }

    /**
     * Called when the fragment has been created.
     *
     * @param args fragment arguments
     */
    public void onFragmentCreated(Bundle args) {
        if (args == null) {
            String message = "bundle can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        Serializable serializable = args
                .getSerializable(ListPositionPlayersFragment.getPlaceKey());
        if (serializable == null || ! (serializable instanceof PositionUtils.POSITION_PLACE)) {
            String message = "position place must be set";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        int[] playersToExclude = args.getIntArray(ListPositionPlayersFragment
                .getExcludeLayersKey());
        if (playersToExclude == null) {
            String message = "players to exclude must be set";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.place = (PositionUtils.POSITION_PLACE)serializable;
        this.getPlayers(playersToExclude);
    }

    /**
     * Set the positions place.
     *
     * @param playersToExclude players ids that should be excluded from the response
     */
    public void getPlayers(int[] playersToExclude) {
        playerDBService.open();
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
                String message = "unknown position place";
                logger.error(message);
                throw new IllegalArgumentException(message);
        }
        playerDBService.close();
    }

    /**
     * Called when the view is created to set the list view adapter.
     */
    public void onViewCreated() {
        if (place == null) {
            String message = "place os still not set";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        fragment.setAdapter(this.players);
        switch (place) {
            case KEEPERS:
                fragment.setPositionPlace("Keepers");
                break;
            case DEFENDERS:
                fragment.setPositionPlace("Defenders");
                break;
            case MIDFIELDERS:
                fragment.setPositionPlace("Midfielders");
                break;
            case ATTACKERS:
                fragment.setPositionPlace("Attackers");
                break;
        }
    }
}
