package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.UserRepository;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UserException;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 01.08.2016.
 */
public class UserDBService {

    private Logger logger = LoggerFactory.getLogger(UserDBService.class);
    private UserRepository repository;
    private int connections = 0;

    public UserDBService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Open a connection to the database.
     */
    public void open() {
        synchronized (this) {
            logger.info("open");
            connections++;
            if (!repository.isOpen()) {
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
     * Validate that given user data is correct.
     *
     * @param user User to be validated
     */
    private void validateData(User user) {
        if (user == null) {
            String message = "user can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (user.getId() == null) {
            String message = "user id can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (user.getId() < 1) {
            String message = "user id must be greater that 0";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (user.getName() == null) {
            String message = "user name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (user.getEmail() == null) {
            String message = "user email can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Get all users in the database.
     *
     * @return List of all users.
     */
    public List<User> getAll() {
        return repository.getAll();
    }

    /**
     * Find a user by his id.
     *
     * @param id user id
     * @return User or null if the user don't exists
     */
    public User get(int id) {
        return repository.get(id);
    }

    /**
     * Find a user by his email.
     *
     * @param email user email
     * @return User or null if the user with the given email don't exists
     */
    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    /**
     * Get the number of users.
     *
     * @return number of users
     */
    public long count() {
        return repository.count();
    }

    /**
     * Check if exists a user with the given id.
     *
     * @param id user
     * @return whatever the user exists
     */
    public boolean exists(int id) {
        return this.get(id) != null;
    }

    /**
     * Check if the given user exists in the database.
     *
     * @param user User to be checked
     * @return whatever the user exists
     */
    public boolean exists(User user) {
        if (user == null) {
            String message = "user can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        return this.exists(user.getId());
    }

    /**
     * Store a new user.
     *
     * @param user User to be stored
     * @return stored User
     */
    public User store(User user) {
        this.validateData(user);
        if (this.exists(user)) {
            String message = String.format("can't save user, user with " +
                    "id %d already exists", user.getId());
            logger.error(message);
            throw new PrimaryKeyConstraintException(message);
        }
        if (this.getByEmail(user.getEmail()) != null) {
            String message = String.format("can't save user, user with a email " +
                    "%s already exists", user.getEmail());
            logger.error(message);
            throw new UniqueFieldConstraintException(message);
        }

        boolean result = repository.store(user);
        if (! result) {
            String message = "error occurred while saving the user";
            logger.error(message);
            throw new UserException(message);
        }
        return user;
    }

    /**
     * Update a existing user.
     *
     * @param user User to be setChanged
     * @return setChanged User
     */
    public User update(User user) {
        this.validateData(user);
        if (! this.exists(user)) {
            String message = String.format("can't onUpdateSuccess user, user with id " +
                    "%d don't exists", user.getId());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.update(user);
        if (! result) {
            String message = "error occurred while updating the user";
            logger.error(message);
            throw new UserException(message);
        }
        return user;
    }

    /**
     * Delete a user by his id.
     *
     * @param id User id
     */
    public void delete(int id) {
        if (! this.exists(id)) {
            String message = String.format("can;t delete user, user with id %d don't exists", id);
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = repository.delete(id);
        if (! result) {
            String message = "error occurred while deleting the user";
            logger.error(message);
            throw new UserException(message);
        }
    }

    /**
     * Delete the given user.
     *
     * @param user User to be deleted
     */
    public void delete(User user) {
        if (user == null) {
            String message = "user can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.delete(user.getId());
    }
}
