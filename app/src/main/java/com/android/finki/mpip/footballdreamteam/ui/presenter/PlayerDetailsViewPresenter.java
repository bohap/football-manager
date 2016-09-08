package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.PlayerDetailsView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 17.08.2016.
 */
public class PlayerDetailsViewPresenter {

    private static Logger logger = LoggerFactory.getLogger(PlayerDetailsViewPresenter.class);
    private PlayerDetailsView view;
    private PlayerDBService playerDBService;
    private Player player;
    private boolean editable;

    public PlayerDetailsViewPresenter(PlayerDetailsView view,
                                      PlayerDBService playerDBService) {
        this.view = view;
        this.playerDBService = playerDBService;
    }

    /**
     * Called when the view is created.
     *
     * @param args view arguments
     */
    public void onDialogCreated(Bundle args) {
        int playerId = args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1);
        if (playerId < 1) {
            String message = "player id not provided";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.editable = args.getBoolean(PlayerDetailsView.BUNDLE_EDITABLE_KEY, false);
        this.getPlayer(playerId);
    }

    /**
     * Get the player data.
     *
     * @param id player id
     */
    private void getPlayer(int id) {
        playerDBService.open();
        player = playerDBService.get(id);
        playerDBService.close();
        if (player == null) {
            String message = String.format("player with id %d don't exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (player.getTeam() == null) {
            String message = "player team is null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (player.getPosition() == null) {
            String message = "player position is null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Called when the view view is created.
     */
    public void onViewCreated() {
        view.bindPlayer(player.getName(), player.getTeam().getName(),
                String.valueOf(DateUtils.getYearDiff(player.getDateOfBirth())),
                player.getPosition().getName(), editable);
    }
}
