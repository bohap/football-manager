package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Borce on 17.08.2016.
 */
public class PlayerDetailsDialogPresenter {

    private static Logger logger = LoggerFactory.getLogger(PlayerDetailsDialogPresenter.class);
    private PlayerDetailsDialog dialog;
    private PlayerDBService playerDBService;
    private Player player;
    private boolean editable;

    public PlayerDetailsDialogPresenter(PlayerDetailsDialog dialog,
                                        PlayerDBService playerDBService) {
        this.dialog = dialog;
        this.playerDBService = playerDBService;
    }

    /**
     * Called when the dialog is created.
     *
     * @param args dialog arguments
     */
    public void onDialogCreated(Bundle args) {
        int playerId = args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1);
        if (playerId < 1) {
            String message = "player id not provided";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.editable = args.getBoolean(PlayerDetailsDialog.getBundleEditableKey(), false);
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
     * Called when the dialog view is created.
     */
    public void onViewCreated() {
        dialog.bindPlayer(player.getName(), player.getTeam().getName(),
                String.valueOf(DateUtils.getYearDiff(player.getDateOfBirth())),
                player.getPosition().getName(), editable);
    }
}
