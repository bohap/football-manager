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
    private int connections = 0;

    public TeamDBService(TeamRepository repository) {
        this.repository = repository;
    }

    /**
     * Open a connection to the database.
     */
    public void open() {
        synchronized (this) {
            logger.info("open");
            connections++;
            if (! repository.isOpen()) {
                repository.open();
            }
        }
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
    }

    /**
     * Validate the given team data is correct.
     *
     * @param team Team to be validated
     */
    private void validateData(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        if (team.getId() == null) {
            throw new IllegalArgumentException("user id can't be null");
        }
        if (team.getId() < 1) {
            throw new IllegalArgumentException("user id must be greater then 1");
        }
        if (team.getName() == null) {
            throw new IllegalArgumentException("user name can't be null");
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
            throw new IllegalArgumentException("team can't be null");
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
            throw new PrimaryKeyConstraintException(String
                    .format("can't save team, team with id %d already exists", team.getId()));
        }
        if (this.getByName(team.getName()) != null) {
            throw new UniqueFieldConstraintException(String
                    .format("can't save team, team with name %s already exists", team.getName()));
        }
        boolean result = repository.store(team);
        if (! result) {
            throw new TeamException("error occurred while saving the team");
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
            throw new IllegalArgumentException(String
                    .format("can't onUpdateSuccess team, team with id %d already exists",
                            team.getId()));
        }
        boolean result = repository.update(team);
        if (! result) {
            throw new TeamException("error occurred while updating the team");
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
            throw new IllegalArgumentException(String
                    .format("can't delete team, team with id %d already exists", id));
        }
        boolean result = repository.delete(id);
        if (! result) {
            throw new TeamException("error occurred while deleting the team");
        }
    }

    /**
     * Delete a existing team.
     *
     * @param team Team to be deleted
     */
    public void delete(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("team can't be null");
        }
        this.delete(team.getId());
    }
}