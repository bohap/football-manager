package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 29.07.2016.
 */
public class UserRepository extends BaseRepository<User, Integer> {

    private Base64Utils base64Utils;
    private String COLUMN_NAME;
    private String COLUMN_EMAIL;
    private String COLUMN_PASSWORD;
    private String COLUMN_CREATED_AT;
    private String COLUMN_UPDATED_AT;

    public UserRepository(Context context, MainSQLiteOpenHelper dbHelper,
                          Base64Utils base64Utils) {
        this.dbHelper = dbHelper;
        this.base64Utils = base64Utils;
        this.TABLE_NAME = context.getString(R.string.users_table_name);
        this.COLUMN_ID = context.getString(R.string.users_column_id);
        this.COLUMN_NAME = context.getString(R.string.users_column_name);
        this.COLUMN_EMAIL = context.getString(R.string.users_column_email);
        this.COLUMN_PASSWORD = context.getString(R.string.users_column_password);
        this.COLUMN_CREATED_AT = context.getString(R.string.users_column_created_at);
        this.COLUMN_UPDATED_AT = context.getString(R.string.users_column_updated_at);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD,
                COLUMN_CREATED_AT, COLUMN_UPDATED_AT};
    }

    /**
     * Map the cursor to a new User.
     *
     * @param cursor database cursor containing the values
     * @return new User with cursor values
     */
    @Override
    protected User mapCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
        user.setPassword(base64Utils
                .decode(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))));
        user.setCreatedAt(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_CREATED_AT))));
        user.setUpdatedAt(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_UPDATED_AT))));
        return user;
    }

    /**
     * Put user values into a HashMap.
     *
     * @param user User containing the values
     * @return HashMap in which the users table columns is map with user values
     */
    Map<String, String> putValues(User user) {
        Map<String, String> params = new HashMap<>();
        params.put(COLUMN_ID, user.getId().toString());
        params.put(COLUMN_NAME, user.getName());
        params.put(COLUMN_EMAIL, user.getEmail());
        params.put(COLUMN_PASSWORD, base64Utils.encode(user.getPassword()));
        params.put(COLUMN_CREATED_AT, DateUtils.format(user.getCreatedAt()));
        params.put(COLUMN_UPDATED_AT, DateUtils.format(user.getUpdatedAt()));
        return params;
    }

    /**
     * Get all users in the database.
     *
     * @return List of all users.
     */
    public List<User> getAll() {
        return super.getMultiple();
    }

    /**
     * Find a user by his id.
     *
     * @param id user id
     * @return User or null if the user don't exists
     */
    public User get(int id) {
        return super.getOneById(id);
    }

    /**
     * Find a user by his email.
     *
     * @param email user email
     * @return User or null if the user with the given email don't exists
     */
    public User getByEmail(String email) {
        return super.getOne(COLUMN_EMAIL, email);
    }

    /**
     * Get the number of users.
     *
     * @return number of users
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new user.
     *
     * @param user User to be stored
     * @return success of the operation
     */
    public boolean store(User user) {
        return super.store(this.putValues(user));
    }

    /**
     * Update a existing user.
     *
     * @param user User to be setChanged
     * @return success of the operation
     */
    public boolean update(User user) {
        return super.update(this.putValues(user), user);
    }

    /**
     * Delete a user by his id.
     *
     * @param id User id
     * @return success of the operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }
}