package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LineupRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 02.08.2016.
 */
public class LineupDBService {

    private Logger logger = LoggerFactory.getLogger(LineupDBService.class);
    private LineupRepository repository;
    private UserDBService userDBService;

    public LineupDBService(LineupRepository repository, UserDBService userDBService) {
        this.repository = repository;
        this.userDBService = userDBService;
    }

    /**
     * Open a new connection to the database.
     */
    public void open() {
        if (!repository.isOpen()) {
            repository.open();
        }
        userDBService.open();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (repository.isOpen()) {
            repository.close();
        }
        userDBService.close();
    }

    /**
     * Validate that hte lineup data is correct.
     *
     * @param lineup Lineup to be valdiated
     */
    private void validateData(Lineup lineup) {
        if (lineup == null) {
            String message = "lineup can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (lineup.getId() == null) {
            String message = "lineup id can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (lineup.getId() < 1) {
            String message = "lineup id must be greater than 0";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all lineups.
     *
     * @return List of all lineups
     */
    public List<Lineup> getAll() {
        return repository.getAll();
    }

    /**
     * Find a lineup by her id.
     *
     * @param id lineup id
     * @return Lineup or null if hte lineup don't exists
     */
    public Lineup get(int id) {
        return repository.get(id);
    }

    /**
     * Get the number of lineup.
     *
     * @return number of lineups
     */
    public long count() {
        return repository.count();
    }

    /**
     * Checks if exists a lineup with the given id.
     *
     * @param id lineup id
     * @return whatever the lineup exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Checks if the given lineup exists.
     *
     * @param lineup Lineup ti be checked
     * @return whatever the lineup exists
     */
    public boolean exists(Lineup lineup) {
        if (lineup == null) {
            String message = "lineup can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(lineup.getId());
    }

    /**
     * Store a new lineup,
     *
     * @param lineup lineup to be stored
     * @return stored lineup
     */
    public Lineup store(Lineup lineup) {
        this.validateData(lineup);
        if (this.exists(lineup)) {
            String message = String.format("can't save lineup, lineup with " +
                    "id %d already exists", lineup.getId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }
        /* Check if the user id exists */
        if (lineup.getUserId() < 1 && lineup.getUser() != null) {
            lineup.setUserId(lineup.getUser().getId());
        }
        if (!userDBService.exists(lineup.getUserId())) {
            String message = String.format("can't save lineup, lineup user " +
                    "id %d don't exists", lineup.getUserId());
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }

        boolean result = repository.store(lineup);
        if (!result) {
            String message = "error occurred while saving the lineup";
            logger.error(message);
            throw new LineupException(message);
        }
        return lineup;
    }

    /**
     * Update a existing lineup.
     *
     * @param lineup Lineup to be setChanged
     * @return setChanged Lineup
     */
    public Lineup update(Lineup lineup) {
        this.validateData(lineup);
        if (!this.exists(lineup)) {
            String message = String.format("can't onUpdateSuccess lineup, lineup with id " +
                    "%d don't exists", lineup.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(lineup);
        if (!result) {
            String message = "error occurred while updating the lineup";
            logger.error(message);
            throw new LineupException(message);
        }
        return lineup;
    }

    /**
     * Delete a existing lineup.
     *
     * @param id lineup id
     */
    public void delete(int id) {
        if (!this.exists(id)) {
            String message = String.format("can't delete lineup, lineup with " +
                    "id %d con't exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(id);
        if (!result) {
            String message = "error occurred while deleting the lineup";
            logger.error(message);
            throw new LineupException(message);
        }
    }

    /**
     * Delete a existing lineup.
     *
     * @param lineup Lineup to be deleted
     */
    public void delete(Lineup lineup) {
        if (lineup == null) {
            String message = "lineup can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(lineup.getId());
    }

    /**
     * Get all lineups that the user has made.
     *
     * @param userId user id
     * @return List of all lineups that the user has made
     */
    public List<Lineup> getUserLineups(int userId) {
        return repository.getUserLineups(userId);
    }

    /**
     * Get all lineups that the user has made.
     *
     * @param user User
     * @return List of all lineups that the user has made
     */
    public List<Lineup> getUserLineups(User user) {
        if (user == null) {
            String message = "user can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.getUserLineups(user.getId());
    }
}
