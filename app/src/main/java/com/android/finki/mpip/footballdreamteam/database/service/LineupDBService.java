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
    private int connections = 0;

    public LineupDBService(LineupRepository repository, UserDBService userDBService) {
        this.repository = repository;
        this.userDBService = userDBService;
    }

    /**
     * Open a new connection to the database.
     */
    public void open() {
        synchronized (this) {
            logger.info("open");
            connections++;
            if (!repository.isOpen()) {
                repository.open();
            }
        }
        userDBService.open();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        synchronized (this) {
            if (repository.isOpen()) {
                logger.info("close");
                connections--;
                if (connections == 0) {
                    repository.close();
                }
            }
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
            throw new IllegalArgumentException("lineup can't be null");
        }
        if (lineup.getId() == null) {
            throw new IllegalArgumentException("lineup id can't be null");
        }
        if (lineup.getId() < 1) {
            throw new IllegalArgumentException("lineup id must be greater than 0");
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
            throw new IllegalArgumentException("lineup can't be null");
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
            throw new PrimaryKeyConstraintException(String
                    .format("can't save lineup, lineup with id %d already exists",
                            lineup.getId()));
        }
        if (!userDBService.exists(lineup.getUserId())) {
            throw new ForeignKeyConstraintException(String
                    .format("can't save lineup, lineup user id %d don't exists",
                            lineup.getUserId()));
        }

        boolean result = repository.store(lineup);
        if (!result) {
            throw new LineupException("error occurred while saving the lineup");
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
            throw new IllegalArgumentException(String
                    .format("can't onUpdateSuccess lineup, lineup with id %d don't exists",
                            lineup.getId()));
        }
        boolean result = repository.update(lineup);
        if (!result) {
            throw new LineupException("error occurred while updating the lineup");
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
            throw new IllegalArgumentException(String
                    .format("can't delete lineup, lineup with id %d con't exists", id));
        }
        boolean result = repository.delete(id);
        if (!result) {
            throw new LineupException("error occurred while deleting the lineup");
        }
    }

    /**
     * Delete a existing lineup.
     *
     * @param lineup Lineup to be deleted
     */
    public void delete(Lineup lineup) {
        if (lineup == null) {
            throw new IllegalArgumentException("lineup can't be null");
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
            throw new IllegalArgumentException("user can't be null");
        }
        return this.getUserLineups(user.getId());
    }
}
