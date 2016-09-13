package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.PositionRepository;
import com.android.finki.mpip.footballdreamteam.exception.PositionException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 01.08.2016.
 */
public class PositionDBService {

    private Logger logger = LoggerFactory.getLogger(PositionDBService.class);
    private PositionRepository repository;
    private int connections = 0;

    public PositionDBService(PositionRepository repository) {
        this.repository = repository;
    }

    /**
     * Open a new connection to the database.
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
     * Validate the given position data is correct.
     *
     * @param position Position to be validated
     */
    private void validateData(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getId() == null) {
            String message = "position id can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getId() < 1) {
            String message = "position id must be greater then 0";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getName() == null) {
            String message = "position name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all positions.
     *
     * @return List of all positions
     */
    public List<Position> getAll() {
        return repository.getAll();
    }

    /**
     * Find a position by his id.
     *
     * @param id position id
     * @return Position or null if the id don't exists
     */
    public Position get(int id) {
        return repository.get(id);
    }

    /**
     * Find a position by her name.
     *
     * @param name position name
     * @return Position or null if position with given name don't exists
     */
    public Position getByName(String name) {
        return repository.getByName(name);
    }

    /**
     * Get the number of positions.
     *
     * @return number of positions
     */
    public long count() {
        return repository.count();
    }

    /**
     * Checks if exists a position with the given id.
     *
     * @param id position id
     * @return whatever the position exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Checks if the given position exists.
     *
     * @param position Position to be checked
     * @return whatever the position exists
     */
    public boolean exists(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(position.getId());
    }

    /**
     * Store a new position.
     *
     * @param position Position to be stored
     * @return stored Position
     */
    public Position store(Position position) {
        this.validateData(position);
        if (this.exists(position.getId())) {
            String message = String.format("can't save position, position with " +
                    "id %d already exists", position.getId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }
        if (this.getByName(position.getName()) != null) {
            String message = String.format("can't save position, position with name " +
                    "%s already exists", position.getName());
            logger.error(message);
            throw new UniqueFieldConstraintException(message);
        }
        boolean result = repository.store(position);
        if (! result) {
            String message = "error occurred while saving the position";
            logger.error(message);
            throw new PositionException(message);
        }
        return position;
    }

    /**
     * Update a existing position.
     *
     * @param position Position to be setChanged
     * @return setChanged Position
     */
    public Position update(Position position) {
        this.validateData(position);
        if (! this.exists(position.getId())) {
            String message = String.format("can't onUpdateSuccess position, position with " +
                    "id %d don't exists", position.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(position);
        if (! result) {
            String message = "error occurred while updating the position";
            logger.error(message);
            throw new PositionException(message);
        }
        return position;
    }

    /**
     * Delete a existing position.
     *
     * @param id position id
     */
    public void delete(int id) {
        if (! this.exists(id)) {
            String message = String.format("can't delete position, position with " +
                    "id %d don't exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(id);
        if (! result) {
            String message = "error occurred while deleting the position";
            logger.error(message);
            throw new PositionException(message);
        }
    }

    /**
     * Delete a existing position.
     *
     * @param position Position to be delted
     */
    public void delete(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(position.getId());
    }

    /**
     * Find a position with the given name or throw a exception if the name don't exists.
     *
     * @param name position name
     * @return position id
     */
    private int getId(String name) {
        Position position = repository.getByName(name);
        if (position == null) {
            String message = String.format("can't find position %s", name);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return position.getId();
    }

    /**
     * Get the id of the position goalkeeper.
     *
     * @return goalkeeper position id
     */
    public int getGoalkeeperId() {
        return this.getId(PositionUtils.POSITION.KEEPER.getName());
    }

    /**
     * Get the id of the position centre back.
     *
     * @return centre back position id
     */
    public int getCentreBackId() {
        return this.getId(PositionUtils.POSITION.CENTRE_BACK.getName());
    }

    /**
     * Get the id of the position left back.
     *
     * @return left back position id
     */
    public int getLeftBackId() {
        return this.getId(PositionUtils.POSITION.LEFT_BACK.getName());
    }

    /**
     * Get the id of the position right back.
     *
     * @return right back position id
     */
    public int getRightBackId() {
        return this.getId(PositionUtils.POSITION.RIGHT_BACK.getName());
    }

    /**
     * Get the id of the position defensive midfield.
     *
     * @return defensive midfield position id
     */
    public int getDefensiveMidfieldId() {
        return this.getId(PositionUtils.POSITION.DEFENSIVE_MIDFIELD.getName());
    }

    /**
     * Get the id of the position centre midfield.
     *
     * @return centre midfield position id.
     */
    public int getCentreMidfieldId() {
        return this.getId(PositionUtils.POSITION.CENTRE_MIDFIELD.getName());
    }

    /**
     * Get the id of the position attacking midfield.
     *
     * @return attacking midfield position id
     */
    public int getAttackingMidfieldId() {
        return this.getId(PositionUtils.POSITION.ATTACKING_MIDFIELD.getName());
    }

    /**
     * Get the id of the position left wing.
     *
     * @return left wing position id
     */
    public int getLeftWingId() {
        return this.getId(PositionUtils.POSITION.LEFT_WING.getName());
    }

    /**
     * Get the id of the position right wing.
     *
     * @return right wing position id
     */
    public int getRightWingId() {
        return this.getId(PositionUtils.POSITION.RIGHT_WING.getName());
    }

    /**
     * Get the id of the position centre forward.
     *
     * @return centre forward position id
     */
    public int getCentreForwardId() {
        return this.getId(PositionUtils.POSITION.CENTRE_FORWARD.getName());
    }

    /**
     * Get the id of the position secondary forward.
     *
     * @return secondary forward position id
     */
    public int getSecondaryForwardId() {
        return this.getId(PositionUtils.POSITION.SECONDARY_FORWARD.getName());
    }

    /**
     * Get the ids of the positions that are in the defense.
     *
     * @return positions ids
     */
    public int[] getDefendersIds() {
        return this.mapIds(repository.searchByName("back"));
    }

    /**
     * Get the ids of the positions that are in the midfield..
     *
     * @return positions ids
     */
    public int[] getMidfieldersIds() {
        return this.mapIds(repository.searchByName("midfield", "wing"));
    }

    /**
     * Get the ids of the positions that are in the forward.
     *
     * @return positions ids
     */
    public int[] getAttackersIds() {
        return this.mapIds(repository.searchByName("forward"));
    }

    /**
     * Map the positions ids to a array of integer.
     *
     * @param positions List of positions ot be mapped
     * @return array of positions ids
     */
    private int[] mapIds(List<Position> positions) {
        int[] ids = new int[positions.size()];
        int i = 0;
        for (Position position : positions) {
            ids[i++] = position.getId();
        }
        return ids;
    }

    /**
     * Created a map where the key is a POSITION and the value is the position id.
     *
     * @return mapped positions
     */
    public Map<PositionUtils.POSITION, Integer> mapPositions() {
        //TODO change the key to be android resource id (some key will have the same value?)
        Map<PositionUtils.POSITION, Integer> result = new HashMap<>();
        result.put(PositionUtils.POSITION.KEEPER, this.getGoalkeeperId());
        result.put(PositionUtils.POSITION.CENTRE_BACK, this.getCentreBackId());
        result.put(PositionUtils.POSITION.LEFT_BACK, this.getLeftBackId());
        result.put(PositionUtils.POSITION.RIGHT_BACK, this.getRightBackId());
        result.put(PositionUtils.POSITION.DEFENSIVE_MIDFIELD, this.getDefensiveMidfieldId());
        result.put(PositionUtils.POSITION.CENTRE_MIDFIELD, this.getCentreMidfieldId());
        result.put(PositionUtils.POSITION.ATTACKING_MIDFIELD, this.getAttackingMidfieldId());
        result.put(PositionUtils.POSITION.LEFT_WING, this.getLeftWingId());
        result.put(PositionUtils.POSITION.RIGHT_WING, this.getRightWingId());
        result.put(PositionUtils.POSITION.CENTRE_FORWARD, this.getCentreForwardId());
        result.put(PositionUtils.POSITION.SECONDARY_FORWARD, this.getSecondaryForwardId());
        return result;
    }
}