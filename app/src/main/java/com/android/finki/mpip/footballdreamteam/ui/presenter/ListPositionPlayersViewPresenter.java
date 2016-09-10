package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
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
    private PositionUtils.POSITION_PLACE place;

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
        Serializable serializable = args
                .getSerializable(ListPositionPlayersView.PLACE_KEY);
        if (serializable == null || ! (serializable instanceof PositionUtils.POSITION_PLACE)) {
            throw new IllegalArgumentException("position place must be set");
        }
        int[] playersToExclude = args.getIntArray(ListPositionPlayersView.EXCLUDE_LAYERS_KEY);
        if (playersToExclude == null) {
            throw new IllegalArgumentException("players to exclude must be set");
        }
        this.place = (PositionUtils.POSITION_PLACE)serializable;
        this.getPlayers(playersToExclude);
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        if (place == null) {
            throw new IllegalArgumentException("place is not set");
        }
        view.setAdapter(this.players);
        switch (place) {
            case KEEPERS:
                view.setPositionPlace("Keepers");
                break;
            case DEFENDERS:
                view.setPositionPlace("Defenders");
                break;
            case MIDFIELDERS:
                view.setPositionPlace("Midfielders");
                break;
            case ATTACKERS:
                view.setPositionPlace("Attackers");
                break;
        }
    }

    /**
     * Set the positions place.
     *
     * @param playersToExclude players ids that should be excluded from the response
     */
    private void getPlayers(int[] playersToExclude) {
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
}
