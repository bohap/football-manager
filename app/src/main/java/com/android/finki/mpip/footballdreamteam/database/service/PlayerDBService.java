package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.PlayerRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.PlayerException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.utility.ArrayUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 01.08.2016.
 */
public class PlayerDBService {

    private Logger logger = LoggerFactory.getLogger(PlayerDBService.class);
    private PlayerRepository repository;
    private TeamDBService teamDBService;
    private PositionDBService positionDBService;

    public PlayerDBService(PlayerRepository repository, TeamDBService teamDBService,
                           PositionDBService positionDBService) {
        this.repository = repository;
        this.teamDBService = teamDBService;
        this.positionDBService = positionDBService;
    }

    /**
     * Open a new connection to the database.
     */
    public void open() {
        if (! repository.isOpen()) {
            repository.open();
        }
        teamDBService.open();
        positionDBService.open();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (repository.isOpen()) {
            repository.close();
        }
        teamDBService.close();
        positionDBService.close();
    }

    /**
     * Validate that the player data is correct.
     *
     * @param player Player to be validated
     */
    private void validateData(Player player) {
        if (player == null) {
            String message = "player can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (player.getId() == null) {
            String message = "player id can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (player.getId() < 1) {
            String message = "player id must be greater then 0";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (player.getName() == null) {
            String message = "player name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all players.
     *
     * @return List of all players
     */
    public List<Player> getAll() {
        return repository.getAll();
    }

    /**
     * Find a player by his id.
     *
     * @param id player id
     * @return Player or null if the id don't exists
     */
    public Player get(int id) {
        return repository.get(id);
    }

    /**
     * Fina a player by his name.
     *
     * @param name player name
     * @return Player or null if the name don;t exists
     */
    public Player getByName(String name) {
        return repository.getByName(name);
    }

    /**
     * Get the number od players.
     *
     * @return number of players
     */
    public long count() {
        return repository.count();
    }

    /**
     * Checks if exists a player with the given id.
     *
     * @param id player id
     * @return whatever the player exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Checks if the given player exists.
     *
     * @param player Player to be checked
     * @return whatever the player exists
     */
    public boolean exists(Player player) {
        if (player == null) {
            String message = "player can'e be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(player.getId());
    }

    /**
     * Store a new player.
     *
     * @param player Player to be stored
     * @return stored Player
     */
    public Player store(Player player) {
        this.validateData(player);
        if (this.exists(player)) {
            String message = String.format("can't save player, " +
                    "id %d already exists", player.getId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }
        /* Check if the team id exists */
        if (player.getTeamId() < 1 && player.getTeam() != null) {
            player.setTeamId(player.getTeam().getId());
        }
        if (! teamDBService.exists(player.getTeamId())) {
            String message = String.format("can't save player, team with " +
                    "id %d don't exists", player.getTeamId());
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        /* Check if the position id exists */
        if (player.getPositionId() < 1 && player.getPosition() != null) {
            player.setPositionId(player.getPosition().getId());
        }
        if (! positionDBService.exists(player.getPositionId())) {
            String message = String.format("can't save player, position with " +
                    "id %d don't exists", player.getPositionId());
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }

        boolean result = repository.store(player);
        if (! result) {
            String message = "error occurred while storing the player";
            logger.error(message);
            throw new PlayerException(message);
        }
        return player;
    }

    /**
     * Update a existing player.
     *
     * @param player Player to be setChanged
     * @return setChanged Player
     */
    public Player update(Player player) {
        this.validateData(player);
        if (! this.exists(player)) {
            String message = String.format("can't update player, player with " +
                    "id %d don't exists", player.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(player);
        if (! result) {
            String message = "error occurred while updating the player";
            logger.error(message);
            throw new PlayerException(message);
        }
        return player;
    }

    /**
     * Delete a existing player.
     *
     * @param id player id
     */
    public void delete(int id) {
        if (! this.exists(id)) {
            String message = String.format("can't delete player, player " +
                    "with id %d don;t exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(id);
        if (! result) {
            String message = "error occurred while deleting the player";
            logger.error(message);
            throw new PlayerException(message);
        }
    }

    /**
     * Delete a existing player.
     *
     * @param player Player to be deleted
     */
    public void delete(Player player) {
        if (player == null) {
            String message = "player can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(player.getId());
    }

    /**
     * Get all players the plays for the given team.
     *
     * @param teamId team id
     * @return List of players that plays for the given team
     */
    public List<Player> getTeamPlayers(int teamId) {
        return repository.getTeamPlayers(teamId);
    }

    /**
     * Get all players that plays for the given team.
     *
     * @param team Team
     * @return List of all players that plays for the given team
     */
    public List<Player> getTeamPlayers(Team team) {
        if (team == null) {
            String message = "team can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.getTeamPlayers(team.getId());
    }

    /**
     * Get all players that plays on the given position.
     *
     * @param positionId position id
     * @return List of players that plays on the given position
     */
    public List<Player> getPositionPlayers(int positionId) {
        return repository.getPositionPlayers(positionId);
    }

    /**
     * Get all players that plays on the given position.
     *
     * @param position Position
     * @return List of all players that plays on the given position
     */
    public List<Player> getPositionPlayers(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.getPositionPlayers(position.getId());
    }

    /**
     * Get all players that are goalkeepers.
     *
     * @param playersToExclude array of players ids to exclude from the response
     * @return all goalkeepers
     */
    public List<Player> getGoalkeepers(int[] playersToExclude) {
        int positionId = positionDBService.getGoalkeeperId();
        return repository.getPositionPlayers(playersToExclude, positionId);
    }

    /**
     * Get all players that are defenders.
     *
     * @param playersToExclude array of players ids to exclude from the response
     * @return all defenders
     */
    public List<Player> getDefenders(int[] playersToExclude) {
        int[] positionsIds = positionDBService.getDefendersIds();
        return repository.getPositionPlayers(playersToExclude, ArrayUtils.toInteger(positionsIds));
    }

    /**
     * Get all players that are midfielders.
     *
     * @param playersToExclude array of players ids to exclude form the response
     * @return all midfielders
     */
    public List<Player> getMidfielders(int[] playersToExclude) {
        int[] positionsIds = positionDBService.getMidfieldersIds();
        return repository.getPositionPlayers(playersToExclude, ArrayUtils.toInteger(positionsIds));
    }

    /**
     * Get all players that are attackers.
     *
     * @param playersToExclude array of players ids to exclude from the response
     * @return all attackers
     */
    public List<Player> getAttackers(int[] playersToExclude) {
        int[] positionsIds = positionDBService.getAttackersIds();
        return repository.getPositionPlayers(playersToExclude, ArrayUtils.toInteger(positionsIds));
    }
}
