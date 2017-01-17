package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.helpers.SerializableList;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView;
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

import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.FORMATION_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_EDITABLE_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_PLAYERS_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LIST_PLAYERS_KEY;

/**
 * Created by Borce on 13.08.2016.
 */
public class LineupFormationViewPresenter {

    private static Logger logger = LoggerFactory.getLogger(LineupFormationViewPresenter.class);
    private LineupFormationView view;
    private PositionDBService positionDBService;
    private LineupUtils lineupUtils;
    private PlayerUtils playerUtils;
    private PositionUtils positionUtils;
    private LineupPlayerValidator validator;

    private Map<Integer, Player> mappedPlayers;
    private LineupUtils.FORMATION formation;
    private boolean editable = false;
    private int selectedPositionResourceId = -1;
    private boolean viewLayoutCreated = false;

    public LineupFormationViewPresenter(LineupFormationView view,
                                        PositionDBService positionDBService,
                                        LineupUtils lineupUtils, PlayerUtils playerUtils,
                                        PositionUtils positionUtils,
                                        LineupPlayerValidator validator) {
        this.view = view;
        this.positionDBService = positionDBService;
        this.lineupUtils = lineupUtils;
        this.playerUtils = playerUtils;
        this.positionUtils = positionUtils;
        this.validator = validator;
    }

    /**
     * Called when the view is created.
     *
     * @param args view arguments
     */
    public void onViewCreated(Bundle args) {
        logger.info("onViewCreated");
        if (args == null) {
            throw new IllegalArgumentException("bundle argument can't be null");
        }
        if (!this.extractLineupPlayers(args) && !this.extractFormation(args)) {
            throw new IllegalArgumentException("neither player of formation provided for view");
        }
    }

    /**
     * Extract the lineup players from the given bundle.
     *
     * @param args view arguments
     * @return whatever the lineup players are extracted from the arguments
     */
    @SuppressWarnings("unchecked")
    private boolean extractLineupPlayers(Bundle args) {
        Serializable serializable =
                args.getSerializable(LINEUP_PLAYERS_KEY);
        if (serializable instanceof SerializableList) {
            List list = ((SerializableList) serializable).getList();
            if (list == null) {
                throw new IllegalArgumentException("list of players can't be null");
            }
            for (Object aList : list) {
                if (!(aList instanceof Player)) {
                    throw new IllegalArgumentException("players list contains invalid argument");
                }
            }
            this.editable = args.getBoolean(LINEUP_EDITABLE_KEY, false);
            this.setPositions();
            this.setPlayers(list);
            return true;
        }
        return false;
    }

    /**
     * Extract the formation from the given bundle.
     *
     * @param args view arguments
     * @return whatever the formation is extracted from the arguments
     */
    @SuppressWarnings("unchecked")
    private boolean extractFormation(Bundle args) {
        Serializable serializable = args.getSerializable(FORMATION_KEY);
        if (serializable instanceof LineupUtils.FORMATION) {
            LineupUtils.FORMATION formation = (LineupUtils.FORMATION) serializable;
            serializable = args.getSerializable(LIST_PLAYERS_KEY);
            if (!(serializable instanceof SerializableList)) {
                throw new IllegalArgumentException("players for the formation are not provided");
            }
            List list = ((SerializableList) serializable).getList();
            if (list == null) {
                throw new IllegalArgumentException("list of players can't be null");
            }
            for (Object aList : list) {
                if (!(aList instanceof Player)) {
                    throw new IllegalArgumentException("players list contains invalid argument");
                }
            }
            this.setPositions();
            this.setFormation(formation, list);
            return true;
        }
        return false;
    }

    /**
     * Get all positions that are stored in the local database and pass them to the positions
     * utils.
     */
    private void setPositions() {
        positionDBService.open();
        positionUtils.setPositions(positionDBService.getAll());
        positionDBService.close();
    }

    /**
     * Called when the view layout is created.
     */
    public void onViewLayoutCreated() {
        logger.info("onViewLayoutCreated");
        this.viewLayoutCreated = true;
        this.updateView();
    }

    /**
     * Called when the view layout is destroyed.
     */
    public void onViewLayoutDestroyed() {
        this.viewLayoutCreated = false;
    }

    /**
     * Update the view with the new data.
     */
    private void updateView() {
        if (viewLayoutCreated) {
            view.bindPlayers();
            List<LineupPlayer> lineupPlayers = this.getLineupPlayers();
            if (validator.validate(lineupPlayers)) {
                view.showValidLineup();
            } else {
                view.showInvalidLineup();
            }
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

    /**
     * Set the players that will be in the lineup. The formation is determined from players.
     *
     * @param players List of players in the lineup
     */
    private void setPlayers(List<Player> players) {
        if (players.size() != 11) {
            String msg = String.format("invalid players size, required 11, got %d",
                                        players.size());
            throw new IllegalArgumentException(msg);
        }
        this.formation = lineupUtils.getFormation(players);
        this.mappedPlayers = lineupUtils.mapPlayers(formation, players);
        this.updateView();
    }

    /**
     * Set the lineup formation, and put the list of players in the lineup.
     *
     * @param formation lineup formation
     * @param players   players that are already in the lineup
     */
    private void setFormation(LineupUtils.FORMATION formation, List<Player> players) {
        this.formation = formation;
        this.editable = true;
        this.mappedPlayers = lineupUtils.generateMap(formation, players);
        this.updateView();
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
            String msg = String.format("can't find player in the lineup on positions %s",
                                        positionId);
            throw new IllegalArgumentException(msg);
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
     * @param startX             player x position in the layout
     * @param startY             player y position in the layout
     */
    public void onPlayerClick(int positionResourceId, int startX, int startY) {
        if (mappedPlayers == null) {
            throw new IllegalArgumentException("map for the players is not yet generated");
        }
        Player player = mappedPlayers.get(positionResourceId);
        if (player == null) {
            String msg = String.format("can't find player at position %d", positionResourceId);
            throw new IllegalArgumentException(msg);
        }
        if (viewLayoutCreated) {
            int playerId = player.getId();
            if (playerId > 0) {
                view.showPlayerDetailsView(playerId, editable);
            } else if (playerId == 0) {
                PositionUtils.POSITION_PLACE place =
                        positionUtils.getPositionResourceIdPlace(positionResourceId);
                List<Integer> playersToExclude = new ArrayList<>();
                for (Map.Entry<Integer, Player> entry : mappedPlayers.entrySet()) {
                    Player mapPlayer = entry.getValue();
                    if (mapPlayer.getId() > 0) {
                        playersToExclude.add(mapPlayer.getId());
                    }
                }
                view.showListPositionPlayersView(place, ArrayUtils.toInt(playersToExclude),
                                                 startX, startY);
            } else {
                String msg = String.format("invalid player id, %d", playerId);
                throw new IllegalArgumentException(msg);
            }
            this.selectedPositionResourceId = positionResourceId;
        }
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
        int positionId = positionUtils.getPositionId(selectedPositionResourceId);
        player.setLineupPlayer(new LineupPlayer(0, player.getId(), positionId));
        mappedPlayers.put(selectedPositionResourceId, player);
        selectedPositionResourceId = -1;
        this.updateView();
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
        if (viewLayoutCreated) {
            view.bindPlayers();
            view.showInvalidLineup();
        }
    }

    /**
     * Called when select a player is canceled.
     */
    public void onPlayerSelectCanceled() {
        if (selectedPositionResourceId == -1) {
            throw new IllegalArgumentException("position not selected");
        }
        selectedPositionResourceId = -1;
        if (viewLayoutCreated) {
            view.showInvalidLineup();
        }
    }
}