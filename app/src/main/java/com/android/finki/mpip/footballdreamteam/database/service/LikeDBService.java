package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LikeRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LikeException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 05.08.2016.
 */
public class LikeDBService {

    private Logger logger = LoggerFactory.getLogger(LikeDBService.class);
    private LikeRepository repository;
    private UserDBService userDBService;
    private LineupDBService lineupDBService;
    private int connections = 0;

    public LikeDBService(LikeRepository repository, UserDBService userDBService,
                         LineupDBService lineupDBService) {
        this.repository = repository;
        this.userDBService = userDBService;
        this.lineupDBService = lineupDBService;
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
        lineupDBService.open();
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
        lineupDBService.close();
    }

    /**
     * Validate that the given LineupLike data is valid.
     *
     * @param like LineupLike to be validated
     */
    private void validateData(LineupLike like) {
        if (like == null) {
            String message = "like can;t be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all likes to all lineups from all users.
     *
     * @return List of all likes on lineups
     */
    public List<LineupLike> getAll() {
        return repository.getAll();
    }

    /**
     * Find a like by the lineup and user id.
     *
     * @param userId   user id
     * @param lineupId lineup id
     * @return LineupLike or null if the like don't exists
     */
    public LineupLike get(int userId, int lineupId) {
        return repository.get(userId, lineupId);
    }

    /**
     * Get the number of all likes.
     *
     * @return number of likes
     */
    public long count() {
        return repository.count();
    }

    /**
     * Checks if the given user has liked the lineup.
     *
     * @param userId   user id
     * @param lineupId lineup id
     * @return whatever the user has liked the lineup
     */
    public boolean exists(int userId, int lineupId) {
        return this.get(userId, lineupId) != null;
    }

    /**
     * Checks if the given like exists.
     *
     * @param like LineupLike to be checked
     * @return whatever the user has liked the lineup
     */
    public boolean exists(LineupLike like) {
        if (like == null) {
            String message = "like can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(like.getUserId(), like.getLineupId());
    }

    /**
     * Store a new like to the lineup from the user.
     *
     * @param like LineupLike to be stored
     * @return stored LineupLike
     */
    public LineupLike store(LineupLike like) {
        this.validateData(like);
        /* Check if the user id exists */
        if (like.getUserId() < 1 && like.getUser() != null) {
            like.setUserId(like.getUser().getId());
        }
        if (!userDBService.exists(like.getUserId())) {
            String message = String.format("user with id %d don't exists", like.getUserId());
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        /* CHeck if the lineup id exists */
        if (like.getLineupId() < 1 && like.getLineup() != null) {
            like.setLineupId(like.getLineup().getId());
        }
        if (!lineupDBService.exists(like.getLineupId())) {
            String message = String.format("lineup with id %d don;t exists", like.getLineupId());
            logger.error(message);
            throw new ForeignKeyConstraintException(message);
        }
        if (this.exists(like)) {
            String message = String
                    .format("like from user %d on lineup with id %d already exists",
                            like.getUserId(), like.getLineupId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }

        boolean result = repository.store(like);
        if (!result) {
            String message = "error occurred while saving the like";
            logger.error(message);
            throw new LikeException(message);
        }
        return like;
    }

    /**
     * Delete a like on the lineup from the user.
     *
     * @param userId   user id
     * @param lineupId lineup id
     */
    public void delete(int userId, int lineupId) {
        if (!this.exists(userId, lineupId)) {
            String message = String.format("like from user with id %d on lineup %d don;t exists",
                    userId, lineupId);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(userId, lineupId);
        if (!result) {
            String message = "error occurred while deleting the like";
            logger.error(message);
            throw new LikeException(message);
        }
    }

    /**
     * Delete the given like.
     *
     * @param like Like to be deleted
     */
    public void delete(LineupLike like) {
        if (like == null) {
            String message = "like can;t be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}