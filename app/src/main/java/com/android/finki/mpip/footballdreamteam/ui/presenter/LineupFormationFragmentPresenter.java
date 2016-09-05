package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.utility.ArrayUtils;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PlayerUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 13.08.2016.
 */
public class LineupFormationFragmentPresenter {

    private static Logger logger = LoggerFactory.getLogger(LineupFormationFragmentPresenter.class);

    private LineupFormationFragment fragment;
    private PositionDBService positionDBService;
    private LineupUtils lineupUtils;
    private PlayerUtils playerUtils;
    private PositionUtils positionUtils;
    private LineupPlayerValidator validator;

    private Map<Integer, Player> mappedPlayers;
    private LineupUtils.FORMATION formation;
    private boolean editable = false;
    private int selectedPositionResourceId = -1;

    public LineupFormationFragmentPresenter(LineupFormationFragment fragment,
                                            PositionDBService positionDBService,
                                            LineupUtils lineupUtils, PlayerUtils playerUtils,
                                            PositionUtils positionUtils,
                                            LineupPlayerValidator validator) {
        this.fragment = fragment;
        this.positionDBService = positionDBService;
        this.lineupUtils = lineupUtils;
        this.playerUtils = playerUtils;
        this.positionUtils = positionUtils;
        this.validator = validator;
    }

    /**
     * Called when the fragment is created.
     *
     * @param args Fragment arguments
     */
    public void onFragmentCreated(Bundle args) {
        if (args == null) {
            throw new IllegalArgumentException("bundle argument can't be null");
        }
        Serializable serializable = args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY);
        if ((serializable instanceof LineupPlayers)) {
            LineupPlayers lineupPlayers = (LineupPlayers) serializable;
            this.setPlayers(lineupPlayers);
        } else {
            serializable = args.getSerializable(LineupFormationFragment.FORMATION_KEY);
            if (!(serializable instanceof LineupUtils.FORMATION)) {
                throw new IllegalArgumentException(
                        "neither player of formation provided for fragment");
            }
            LineupUtils.FORMATION formation = (LineupUtils.FORMATION) serializable;
            serializable = args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY);
            if (!(serializable instanceof LineupPlayers)) {
                throw new IllegalArgumentException("players for the formation are not provided");
            }
            List<Player> players = ((LineupPlayers) serializable).getPlayers();
            if (players == null) {
                throw new IllegalArgumentException("List of players must be provided");
            }
            this.setFormation(formation, players);
        }
    }

    /**
     * Set the players that will be in the lineup. The formation is determined from players.
     *
     * @param lineupPlayers all players in the lineup with value indicating
     *                      whatever the lineup is editable
     */
    private void setPlayers(LineupPlayers lineupPlayers) {
        if (lineupPlayers.getPlayers().size() != 11) {
            String message = String.format("invalid players size, required 11, got %d",
                    lineupPlayers.getPlayers().size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        positionDBService.open();
        List<Position> positions = new ArrayList<>();
        for (Player player : lineupPlayers.getPlayers()) {
            if (player.getLineupPlayer() == null) {
                positionDBService.close();
                throw new IllegalArgumentException("lineup player must be set");
            }
            int positionId = player.getLineupPositionId();
            /* We need access to the player position that is used in LineupUtils */
            Position position = positionDBService.get(positionId);
            if (position == null) {
                positionDBService.close();
                throw new IllegalArgumentException(String
                        .format("un existing position id, %d", positionId));
            }
            player.getLineupPlayer().setPosition(position);
            positions.add(position);
        }
        positionDBService.close();

        lineupPlayers.setPositions(positions);
        lineupUtils.mapPlayers(lineupPlayers);
        this.editable = lineupPlayers.isEditable();
        this.formation = lineupPlayers.getFormation();
        this.mappedPlayers = lineupPlayers.getMappedPlayers();
    }

    /**
     * Set the lineup formation, and put the list of players in the lineup.
     *
     * @param formation lineup formation
     * @param players players that are already in the lineup
     */
    private void setFormation(LineupUtils.FORMATION formation, List<Player> players) {
        this.formation = formation;
        this.editable = true;
        positionDBService.open();
        for (Player player : players) {
            if (player.getLineupPlayer() == null) {
                positionDBService.close();
                throw new IllegalArgumentException("lineup player must be set");
            }
            if (player.getLineupPlayer().getPosition() == null) {
                int positionId = player.getLineupPositionId();
                /* We need access to the player position that is used in LineupUtils */
                Position position = positionDBService.get(positionId);
                if (position == null) {
                    positionDBService.close();
                    throw new IllegalArgumentException(String
                            .format("un existing position id, %d", positionId));
                }
                player.getLineupPlayer().setPosition(position);
            }
        }
        this.mappedPlayers = lineupUtils.generateMap(formation,
                players, positionDBService.mapPositions());
        positionDBService.close();
        List<LineupPlayer> lineupPlayers = this.getLineupPlayers();
        if (validator.validate(lineupPlayers)) {
            fragment.lineupValid();
        } else {
            fragment.lineupInvalid();
        }
    }

    /**
     * Get the formation for the given lineup.
     *
     * @return Lineup formation
     */
    public LineupUtils.FORMATION getFormation() {
        return this.formation;
    }

    /**
     * Called when the fragment view has been created.
     */
    public void onViewCreated() {
        fragment.bindPlayers();
    }

    /**
     * Get the goalkeeper name in the lineup.
     *
     * @return goalkeeper name
     */
    public String getPlayerAt(int positionId) {
        if (mappedPlayers == null) {
            throw new IllegalArgumentException("map for the players is not yet generated");
        }
        Player player = mappedPlayers.get(positionId);
        if (player == null) {
            throw new IllegalArgumentException(String
                    .format("can't find player in the lineup on positions %s", positionId));
        }
        if (player.getId() == 0 || player.getName() == null) {
            return "";
        }
        return playerUtils.getLastName(player.getName());
    }

    /**
     * Handle click on the player.
     *
     * @param positionResourceId android position resource id
     */
    public void onPlayerClick(int positionResourceId) {
        if (mappedPlayers == null) {
            throw new IllegalArgumentException("map for the players is not yet generated");
        }
        Player player = mappedPlayers.get(positionResourceId);
        if (player == null) {
            throw new IllegalArgumentException(String
                    .format("can't find player at position %d", positionResourceId));
        }
        int playerId = player.getId();
        if (playerId > 0) {
            fragment.showPlayerDetailsDialog(playerId, editable);
        } else if (playerId == 0) {
            PositionUtils.POSITION_PLACE place = positionUtils.getPositionPlace(positionResourceId);
            List<Integer> playersToExclude = new ArrayList<>();
            for (Map.Entry<Integer, Player> entry : mappedPlayers.entrySet()) {
                Player mapPlayer = entry.getValue();
                if (mapPlayer.getId() > 0) {
                    playersToExclude.add(mapPlayer.getId());
                }
            }
            fragment.showListPositionPlayersFragment(place, ArrayUtils.toInt(playersToExclude));
        } else {
            throw new IllegalArgumentException(String.format("invalid player id, %d", playerId));
        }
        this.selectedPositionResourceId = positionResourceId;
    }

    /**
     * Update the selected lineup position with the new player.
     *
     * @param player new player on the position
     */
    public void updateLineupPosition(Player player) {
        if (selectedPositionResourceId == -1) {
            throw new IllegalArgumentException("position not selected");
        }

        positionDBService.open();
        Map<PositionUtils.POSITION, Integer> mappedPositions = positionDBService.mapPositions();
        positionDBService.close();
        int positionId = positionUtils.getPositionId(selectedPositionResourceId, mappedPositions);
        player.setLineupPlayer(new LineupPlayer(0, player.getId(), positionId));
        mappedPlayers.put(selectedPositionResourceId, player);

        fragment.bindPlayers();
        selectedPositionResourceId = -1;
        List<LineupPlayer> lineupPlayers = this.getLineupPlayers();
        if (validator.validate(lineupPlayers)) {
            fragment.lineupValid();
        } else {
            fragment.lineupInvalid();
        }
    }

    /**
     * Remove the selected player from the lineup.
     */
    public void removeSelectedPlayer() {
        if (selectedPositionResourceId == -1) {
            throw new IllegalArgumentException("position not selected");
        }
        mappedPlayers.put(selectedPositionResourceId, new Player());
        selectedPositionResourceId = -1;
        fragment.bindPlayers();
        fragment.lineupInvalid();
    }

    /**
     * Called when select a player is canceled.
     */
    public void onPlayerSelectCanceled() {
        if (selectedPositionResourceId == -1) {
            throw new IllegalArgumentException("position not selected");
        }
        selectedPositionResourceId = -1;
        fragment.lineupInvalid();
    }

    /**
     * Convert the players in the map to a List of LineupPlayer.
     *
     * @return List of LineupPlayer
     */
    public List<LineupPlayer> getLineupPlayers() {
        return lineupUtils.getLineupPlayers(mappedPlayers);
    }

    /**
     * Get the players in the lineup ordered by their position.
     *
     * @return List of players in the lineup
     */
    public List<Player> getPlayersOrdered() {
        return lineupUtils.orderPlayers(mappedPlayers);
    }
}