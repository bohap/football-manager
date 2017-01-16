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
            throw new IllegalArgumentException("position can't be null");
        }
        if (position.getId() == null) {
            throw new IllegalArgumentException("position id can't be null");
        }
        if (position.getId() < 1) {
            throw new IllegalArgumentException("position id must be greater then 0");
        }
        if (position.getName() == null) {
            throw new IllegalArgumentException("position name can't be null");
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
            throw new IllegalArgumentException("position can't be null");
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
            throw new PrimaryKeyConstraintException(String
                    .format("can't save position, position with id %d already exists",
                            position.getId()));
        }
        if (this.getByName(position.getName()) != null) {
            throw new UniqueFieldConstraintException(String
                    .format("can't save position, position with name %s already exists",
                            position.getName()));
        }
        boolean result = repository.store(position);
        if (! result) {
            throw new PositionException("error occurred while saving the position");
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
            throw new IllegalArgumentException(String
                    .format("can't onUpdateSuccess position, position with id %d don't exists",
                            position.getId()));
        }
        boolean result = repository.update(position);
        if (! result) {
            throw new PositionException("error occurred while updating the position");
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
            throw new IllegalArgumentException(String
                    .format("can't delete position, position with id %d don't exists", id));
        }
        boolean result = repository.delete(id);
        if (! result) {
            throw new PositionException("error occurred while deleting the position");
        }
    }

    /**
     * Delete a existing position.
     *
     * @param position Position to be delted
     */
    public void delete(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position can't be null");
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
            throw new IllegalArgumentException(String.format("can't find position %s", name));
        }
        return position.getId();
    }

    /**
     * Get the id of the position goalkeeper.
     *
     * @return goalkeeper position id
     */
    public int getGoalkeeperId() {
        return this.getId(PositionUtils.POSITION_TYPE.KEEPER.getName());
    }

    /**
     * Get the id of the position centre back.
     *
     * @return centre back position id
     */
    public int getCentreBackId() {
        return this.getId(PositionUtils.POSITION_TYPE.CENTRE_BACK.getName());
    }

    /**
     * Get the id of the position left back.
     *
     * @return left back position id
     */
    public int getLeftBackId() {
        return this.getId(PositionUtils.POSITION_TYPE.LEFT_BACK.getName());
    }

    /**
     * Get the id of the position right back.
     *
     * @return right back position id
     */
    public int getRightBackId() {
        return this.getId(PositionUtils.POSITION_TYPE.RIGHT_BACK.getName());
    }

    /**
     * Get the id of the position defensive midfield.
     *
     * @return defensive midfield position id
     */
    public int getDefensiveMidfieldId() {
        return this.getId(PositionUtils.POSITION_TYPE.DEFENSIVE_MIDFIELD.getName());
    }

    /**
     * Get the id of the position centre midfield.
     *
     * @return centre midfield position id.
     */
    public int getCentreMidfieldId() {
        return this.getId(PositionUtils.POSITION_TYPE.CENTRE_MIDFIELD.getName());
    }

    /**
     * Get the id of the position attacking midfield.
     *
     * @return attacking midfield position id
     */
    public int getAttackingMidfieldId() {
        return this.getId(PositionUtils.POSITION_TYPE.ATTACKING_MIDFIELD.getName());
    }

    /**
     * Get the id of the position left wing.
     *
     * @return left wing position id
     */
    public int getLeftWingId() {
        return this.getId(PositionUtils.POSITION_TYPE.LEFT_WING.getName());
    }

    /**
     * Get the id of the position right wing.
     *
     * @return right wing position id
     */
    public int getRightWingId() {
        return this.getId(PositionUtils.POSITION_TYPE.RIGHT_WING.getName());
    }

    /**
     * Get the id of the position centre forward.
     *
     * @return centre forward position id
     */
    public int getCentreForwardId() {
        return this.getId(PositionUtils.POSITION_TYPE.CENTRE_FORWARD.getName());
    }

    /**
     * Get the id of the position secondary forward.
     *
     * @return secondary forward position id
     */
    public int getSecondaryForwardId() {
        return this.getId(PositionUtils.POSITION_TYPE.SECONDARY_FORWARD.getName());
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
     * Created a map where the key is a POSITION_TYPE and the value is the position id.
     *
     * @return mapped positions
     */
    public Map<PositionUtils.POSITION_TYPE, Integer> mapPositions() {
        Map<PositionUtils.POSITION_TYPE, Integer> result = new HashMap<>();
        result.put(PositionUtils.POSITION_TYPE.KEEPER, this.getGoalkeeperId());
        result.put(PositionUtils.POSITION_TYPE.CENTRE_BACK, this.getCentreBackId());
        result.put(PositionUtils.POSITION_TYPE.LEFT_BACK, this.getLeftBackId());
        result.put(PositionUtils.POSITION_TYPE.RIGHT_BACK, this.getRightBackId());
        result.put(PositionUtils.POSITION_TYPE.DEFENSIVE_MIDFIELD, this.getDefensiveMidfieldId());
        result.put(PositionUtils.POSITION_TYPE.CENTRE_MIDFIELD, this.getCentreMidfieldId());
        result.put(PositionUtils.POSITION_TYPE.ATTACKING_MIDFIELD, this.getAttackingMidfieldId());
        result.put(PositionUtils.POSITION_TYPE.LEFT_WING, this.getLeftWingId());
        result.put(PositionUtils.POSITION_TYPE.RIGHT_WING, this.getRightWingId());
        result.put(PositionUtils.POSITION_TYPE.CENTRE_FORWARD, this.getCentreForwardId());
        result.put(PositionUtils.POSITION_TYPE.SECONDARY_FORWARD, this.getSecondaryForwardId());
        return result;
    }
}