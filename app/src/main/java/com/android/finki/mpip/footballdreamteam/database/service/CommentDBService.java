package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.CommentRepository;
import com.android.finki.mpip.footballdreamteam.exception.CommentException;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 02.08.2016.
 */
public class CommentDBService {

    private Logger logger = LoggerFactory.getLogger(CommentDBService.class);
    private CommentRepository repository;
    private UserDBService userDBService;
    private LineupDBService lineupDBService;
    private int connections = 0;

    public CommentDBService(CommentRepository repository, UserDBService userDBService,
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
     * Valida that the Comment data is correct.
     *
     * @param comment Comment to be validated
     */
    private void validateData(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("comment can't be null");
        }
        if (comment.getId() == null) {
            throw new IllegalArgumentException("comment id ca't be null");
        }
        if (comment.getId() < 1) {
            throw new IllegalArgumentException("comment id must be greater than 0");
        }
        if (comment.getBody() == null) {
            throw new IllegalArgumentException("comment body can't be null");
        }
    }

    /**
     * Get all comments.
     *
     * @return List of all comments
     */
    public List<Comment> getAll() {
        return repository.getAll();
    }

    /**
     * Find a comment by his id.
     *
     * @param id comment id
     * @return Comment or null if the id don't exists
     */
    public Comment get(int id) {
        return repository.get(id);
    }

    /**
     * Get the number of comments.
     *
     * @return number of comments
     */
    public long count() {
        return repository.count();
    }

    /**
     * Checks if exists a comment with the given id.
     *
     * @param id comment id
     * @return whatever the comment exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Checks if the given comment exists.
     *
     * @param comment Comment to be checked
     * @return whatever the comment exists
     */
    public boolean exists(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("comment can't be null");
        }
        return this.exists(comment.getId());
    }

    /**
     * Store a new comment.
     *
     * @param comment Comment to be stored
     * @return stored Comment
     */
    public Comment store(Comment comment) {
        this.validateData(comment);
        if (this.exists(comment.getId())) {
            throw new PrimaryKeyConstraintException(String
                    .format("can't save comment, comment with id %d already exists",
                            comment.getId()));
        }
        if (!userDBService.exists(comment.getUserId())) {
            throw new ForeignKeyConstraintException(String
                    .format("can't save comment, comment user with id %d don't exists",
                            comment.getUserId()));
        }
        if (!lineupDBService.exists(comment.getLineupId())) {
            throw new ForeignKeyConstraintException(String
                    .format("can't save comment, comment lineup with id %d don't exists",
                            comment.getLineupId()));
        }

        boolean result = repository.store(comment);
        if (!result) {
            throw new CommentException("error occurred while saving the comment");
        }
        return comment;
    }

    /**
     * Update a existing comment.
     *
     * @param comment Comment to be setChanged
     * @return setChanged Comment
     */
    public Comment update(Comment comment) {
        this.validateData(comment);
        if (!this.exists(comment.getId())) {
            throw new IllegalArgumentException(String
                    .format("can't onUpdateSuccess comment, comment with id %d don't exists",
                            comment.getId()));
        }
        boolean result = repository.update(comment);
        if (!result) {
            throw new CommentException("error occurred while updating the comment");
        }
        return comment;
    }

    /**
     * Delete a existing comment.
     *
     * @param id comment id
     */
    public void delete(int id) {
        if (!this.exists(id)) {
            throw new IllegalArgumentException(String
                    .format("can't delete comment, comment with id %d don't exists", id));
        }
        boolean result = repository.delete(id);
        if (!result) {
            throw new CommentException("error occurred while deleting the comment");
        }
    }

    /**
     * Delete a existing comment.
     *
     * @param comment Comment to be deleted
     */
    public void delete(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("comment can't be null");
        }
        this.delete(comment.getId());
    }

    /**
     * Get all comments that the given user has wrote.
     *
     * @param userId user id
     * @return List of all comments that the user has wrote
     */
    public List<Comment> getUserComments(int userId) {
        return repository.getUserComments(userId);
    }

    /**
     * Get all comments that the given user has wrote.
     *
     * @param user User
     * @return List of all comments that the given user has wrote
     */
    public List<Comment> getUserComments(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user can't be null");
        }
        return this.getUserComments(user.getId());
    }

    /**
     * Get all comments that were wrote on the lineup.
     *
     * @param lineupId lineup id
     * @return List of all comments that were wrote on the lineup
     */
    public List<Comment> getLineupComments(int lineupId) {
        return repository.getLineupComments(lineupId);
    }

    /**
     * Get all comments that were wrote on the lineup.
     *
     * @param lineup Lineup
     * @return List of all comments that were wrote on the lineup
     */
    public List<Comment> getLineupComments(Lineup lineup) {
        if (lineup == null) {
            throw new IllegalArgumentException("lineup can't be null");
        }
        return this.getLineupComments(lineup.getId());
    }
}
