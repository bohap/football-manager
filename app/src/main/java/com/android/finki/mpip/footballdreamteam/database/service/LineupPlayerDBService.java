package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LineupPlayerRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LineupPlayerException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.utility.ListUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerDBService {

    private Logger logger = LoggerFactory.getLogger(LineupPlayerDBService.class);
    private LineupPlayerRepository repository;
    private LineupDBService lineupDBService;
    private PlayerDBService playerDBService;
    private PositionDBService positionDBService;

    public LineupPlayerDBService(LineupPlayerRepository repository,
                                 LineupDBService lineupDBService,
                                 PlayerDBService playerDBService,
                                 PositionDBService positionDBService) {
        this.repository = repository;
        this.lineupDBService = lineupDBService;
        this.playerDBService = playerDBService;
        this.positionDBService = positionDBService;
    }

    /**
     * Open a new connection to the database.
     */
    public void open() {
        if (!repository.isOpen()) {
            repository.open();
        }
        lineupDBService.open();
        playerDBService.open();
        positionDBService.open();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (repository.isOpen()) {
            repository.close();
        }
        lineupDBService.close();
        playerDBService.close();
        positionDBService.close();
    }

    /**
     * Validate that the given LineupPlayer data is valid.
     *
     * @param lineupPlayer LineupPlayer to be validated
     */
    private void validateData(LineupPlayer lineupPlayer) {
        if (lineupPlayer == null) {
            String message = "lineupPlayer can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all players that are in a lineup.
     *
     * @return List of all LineupPlayer's
     */
    public List<LineupPlayer> getAll() {
        return repository.getAll();
    }

    /**
     * Find a player in the lineup by the lineup and player id.
     *
     * @param lineupId lineup id
     * @param playerId player id
     * @return LineupPlayer or null if the record don;t exists
     */
    public LineupPlayer get(int lineupId, int playerId) {
        return repository.get(lineupId, playerId);
    }

    /**
     * Checks if the given player is in the lineup.
     *
     * @param lineupId lineup id
     * @param playerId player id
     * @return true or false
     */
    public boolean exists(int lineupId, int playerId) {
        return this.get(lineupId, playerId) != null;
    }

    /**
     * Checks if the given player is in the lineup.
     *
     * @param lineupPlayer LineupPlayer to be checked
     * @return true or false
     */
    public boolean exists(LineupPlayer lineupPlayer) {
        if (lineupPlayer == null) {
            String message = "lineupPlayer can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(lineupPlayer.getLineupId(), lineupPlayer.getPlayerId());
    }

    /**
     * Store a new player for the lineup.
     *
     * @param lineupPlayer LineupPlayer to be stored
     * @return stored LineupPlayer
     */
    public LineupPlayer store(LineupPlayer lineupPlayer) {
        this.validateData(lineupPlayer);
        /* Check is lineup id exists */
        if (lineupPlayer.getLineupId() < 1 && lineupPlayer.getLineup() != null) {
            lineupPlayer.setLineupId(lineupPlayer.getLineup().getId());
        }
        int lineupId = lineupPlayer.getLineupId();
        if (!lineupDBService.exists(lineupPlayer.getLineupId())) {
            String message = String.format("can't save lineup player, lineup with " +
                    "id %d don't exists", lineupId);
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        /* Check if player id exists */
        if (lineupPlayer.getPlayerId() < 1 && lineupPlayer.getPlayer() != null) {
            lineupPlayer.setPlayerId(lineupPlayer.getPlayer().getId());
        }
        int playerId = lineupPlayer.getPlayerId();
        if (!playerDBService.exists(playerId)) {
            String message = String.format("can't save lineup player, player with " +
                    "id %d don't exists", playerId);
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        /* Check if position id exists */
        if (lineupPlayer.getPositionId() < 1 && lineupPlayer.getPosition() != null) {
            lineupPlayer.setPositionId(lineupPlayer.getPositionId());
        }
        int positionId = lineupPlayer.getPositionId();
        if (!positionDBService.exists(positionId)) {
            String message = String.format("can't save lineup player, player with " +
                    "id %d don't exists", positionId);
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        if (this.exists(lineupPlayer)) {
            String message = String.format("can't addLike player in the lineup, lineup with " +
                    "id %d already has the player with id %d", lineupId, playerId);
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }

        boolean result = repository.store(lineupPlayer);
        if (!result) {
            String message = "error occurred while saving the lineup player";
            logger.error(message);
            throw new LineupPlayerException(message);
        }
        return lineupPlayer;
    }

    /**
     * Update a existing player in the lineup.
     *
     * @param lineupPlayer LineupPlayer to be setChanged
     * @return setChanged LineupPlayer
     */
    public LineupPlayer update(LineupPlayer lineupPlayer) {
        if (! this.exists(lineupPlayer)) {
            String message = String.format("can't onUpdateSuccess, lineupPlayer with " +
                            "lineup id %d and player id %d don't exists", lineupPlayer.getLineupId(),
                    lineupPlayer.getPlayerId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(lineupPlayer);
        if (!result) {
            String message = "error occurred while updating the lineup player";
            logger.error(message);
            throw new LineupPlayerException(message);
        }
        return lineupPlayer;
    }

    /**
     * Delete a existing player from the lineup.
     *
     * @param lineupId lineup id
     * @param playerId player id
     */
    public void delete(int lineupId, int playerId) {
        if (!this.exists(lineupId, playerId)) {
            String message = String.format("lineup player can't delete, lineupPlayer " +
                    "with lineup id %d and player with id %d don't exists", lineupId, playerId);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(lineupId, playerId);
        if (!result) {
            String message = "error occurred while deleting the lineup";
            logger.error(message);
            throw new LineupPlayerException(message);
        }
    }

    /**
     * Delete a existing player from the lineup.
     *
     * @param lineupPlayer LineupPlayer ot be deleted
     */
    public void delete(LineupPlayer lineupPlayer) {
        if (lineupPlayer == null) {
            String message = "lineupPlayer can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(lineupPlayer.getLineupId(), lineupPlayer.getPlayerId());
    }

    /**
     * Add multiple players in the lineup.
     *
     * @param lineupPlayers ListPlayers to be added
     * @return success if the operation
     */
    public boolean storePlayers(List<LineupPlayer> lineupPlayers) {
        List<LineupPlayer> stored = new ArrayList<>();
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            try {
                LineupPlayer store = this.store(lineupPlayer);
                stored.add(store);
            } catch (PrimaryKeyConstraintException | ForeignKeyConstraintException
                    | LineupPlayerException exp) {
                exp.printStackTrace();
                this.deletePlayers(stored);
                return false;
            }
        }
        return true;
    }

    /**
     * Update the data about the lineup players.
     *
     * @param lineupPlayers List of players in the lineup
     * @return whatever the onUpdateSuccess is successful
     */
    private boolean updatePlayers(List<LineupPlayer> lineupPlayers) {
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            try {
                this.update(lineupPlayer);
            } catch (RuntimeException exp) {
                logger.error("error occurred while updating the players");
                return false;
            }
        }
        return true;
    }

    /**
     * Update the players for the given lineup.
     *
     * @param lineupId lineup id
     * @param lineupPlayers List of players in the lineup
     * @return whatever the operation is successful
     */
    public boolean updatePlayers(int lineupId, List<LineupPlayer> lineupPlayers) {
        if (!lineupDBService.exists(lineupId)) {
            String message = "updatePlayers called with un existing lineup";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        List<LineupPlayer> currentLineupPlayers = repository.getLineupPlayers(lineupId);
        List<LineupPlayer> playersToStore = ListUtils
                .notInTheList(lineupPlayers, currentLineupPlayers);
        List<LineupPlayer> playersToUpdate = ListUtils
                .inTheList(lineupPlayers, currentLineupPlayers);
        List<LineupPlayer> playersToDelete = ListUtils
                .notInTheList(currentLineupPlayers, lineupPlayers);
        boolean result = this.storePlayers(playersToStore);
        if (!result) {
            return false;
        }
        result = this.updatePlayers(playersToUpdate);
        return result && this.deletePlayers(playersToDelete);
    }

    /**
     * Delete multiple players from the lineup.
     *
     * @param lineupPlayers LineupPlayers to be deleted
     */
    public boolean deletePlayers(List<LineupPlayer> lineupPlayers) {
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            try {
                this.delete(lineupPlayer);
            } catch (IllegalArgumentException | LineupPlayerException exp) {
                exp.printStackTrace();
                return false;
            }
        }
        return true;
    }
}