package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.TeamRepository;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.TeamException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 01.08.2016.
 */
public class TeamDBService {

    private Logger logger = LoggerFactory.getLogger(TeamDBService.class);
    private TeamRepository repository;

    public TeamDBService(TeamRepository repository) {
        this.repository = repository;
    }

    /**
     * Open a connection to the database.
     */
    public void open() {
        if (! repository.isOpen()) {
            repository.open();
        }
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (repository.isOpen()) {
            repository.close();
        }
    }

    /**
     * Validate the given team data is correct.
     *
     * @param team Team to be validated
     */
    private void validateData(Team team) {
        if (team == null) {
            String message = "user can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (team.getId() == null) {
            String message = "user id can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (team.getId() < 1) {
            String message = "user id must be greater then 1";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (team.getName() == null) {
            String message = "user name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all teams.
     *
     * @return List of all users
     */
    public List<Team> getAll() {
        return repository.getAll();
    }

    /**
     * Find a team by his id.
     *
     * @param id team id
     * @return Team or null if the id don;t exists
     */
    public Team get(int id) {
        return repository.get(id);
    }

    /**
     * Find a team by his name.
     *
     * @param name team name
     * @return Team or null if the name don't exists.
     */
    public Team getByName(String name) {
        return repository.getByName(name);
    }

    /**
     * Get the number of teams.
     *
     * @return number of teams
     */
    public long count() {
        return repository.count();
    }

    /**
     * Check if exists a team with the given id.
     *
     * @param id team id
     * @return whatever the team exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Check if the given team exists.
     *
     * @param team Team to the checked
     * @return whatever the team exists
     */
    public boolean exists(Team team) {
        if (team == null) {
            String message = "team can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(team.getId());
    }

    /**
     * Store a new team in the database.
     *
     * @param team Team to be stored
     * @return stored Team
     */
    public Team store(Team team) {
        this.validateData(team);
        if (this.exists(team)) {
            String message = String.format("can't save team, team with id " +
                    "%d already exists", team.getId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }
        if (this.getByName(team.getName()) != null) {
            String message = String.format("can't save team, team with name " +
                    "%s already exists", team.getName());
            logger.error(message);
            throw new UniqueFieldConstraintException(message);
        }
        boolean result = repository.store(team);
        if (! result) {
            String message = "error occurred while saving the team";
            logger.error(message);
            throw new TeamException(message);
        }
        return team;
    }

    /**
     * Update a existing team.
     *
     * @param team Team to be setChanged
     * @return setChanged Team
     */
    public Team update(Team team) {
        this.validateData(team);
        if (! this.exists(team)) {
            String message = String.format("can't onUpdateSuccess team, team with " +
                    "id %d already exists", team.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(team);
        if (! result) {
            String message = "error occurred while updating the team";
            logger.error(message);
            throw new TeamException(message);
        }
        return team;
    }

    /**
     * Delete the team from the database.
     *
     * @param id id of the team to be deleted
     */
    public void delete(int id) {
        if (! this.exists(id)) {
            String message = String.format("can't delete team, team with " +
                    "id %d already exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(id);
        if (! result) {
            String message = "error occurred while deleting the team";
            logger.error(message);
            throw new TeamException(message);
        }
    }

    /**
     * Delete a existing team.
     *
     * @param team Team to be deleted
     */
    public void delete(Team team) {
        if (team == null) {
            String message = "team can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(team.getId());
    }
}