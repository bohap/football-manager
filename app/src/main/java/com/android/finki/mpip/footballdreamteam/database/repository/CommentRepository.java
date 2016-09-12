package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 01.08.2016.
 */
public class CommentRepository extends BaseRepository<Comment, Integer> {

    private String COLUMN_USER_ID;
    private String COLUMN_LINEUP_ID;
    private String COLUMNS_BODY;
    private String COLUMN_CREATED_AT;
    private String COLUMN_UPDATED_AT;
    private String USERS_TABLE_NAME;
    private String USERS_COLUMN_ID;
    private String USERS_COLUMN_NAME;

    public CommentRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.lineups_comments_table_name);
        this.COLUMN_ID = context.getString(R.string.lineups_comments_column_id);
        this.COLUMN_USER_ID = context.getString(R.string.lineups_comments_column_user_id);
        this.COLUMN_LINEUP_ID = context.getString(R.string.lineups_comments_column_lineup_id);
        this.COLUMNS_BODY = context.getString(R.string.lineups_comments_column_body);
        this.COLUMN_CREATED_AT = context.getString(R.string.lineups_comments_column_created_at);
        this.COLUMN_UPDATED_AT = context.getString(R.string.lineups_comments_column_updated_at);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_USER_ID, COLUMN_LINEUP_ID,
                COLUMNS_BODY, COLUMN_CREATED_AT, COLUMN_UPDATED_AT};
        this.USERS_TABLE_NAME = context.getString(R.string.users_table_name);
        this.USERS_COLUMN_ID = context.getString(R.string.users_column_id);
        this.USERS_COLUMN_NAME = context.getString(R.string.users_column_name);
    }

    /**
     * Map the cursor values into a new comment.
     *
     * @param cursor database cursor containing the values
     * @return new Comment containing the cursor values
     */
    @Override
    protected Comment mapCursor(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        comment.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        comment.setLineupId(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID)));
        comment.setBody(cursor.getString(cursor.getColumnIndex(COLUMNS_BODY)));
        comment.setCreatedAt(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_CREATED_AT))));
        comment.setUpdatedAt(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_UPDATED_AT))));

        String column = String.format("%s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME);
        int index = cursor.getColumnIndex(column);
        if (index != -1) {
            User user = new User(comment.getUserId(), cursor.getString(index));
            comment.setUser(user);
        }
        return comment;
    }

    /**
     * Put the comment values into a HashMap.
     *
     * @param comment Comment containing the values
     * @return HashMap in which the table columns are mapped with comment values
     */
    Map<String, String> putValues(Comment comment) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_ID, comment.getId().toString());
        map.put(COLUMN_USER_ID, String.valueOf(comment.getUserId()));
        map.put(COLUMN_LINEUP_ID, String.valueOf(comment.getLineupId()));
        map.put(COLUMNS_BODY, comment.getBody());
        map.put(COLUMN_CREATED_AT, DateUtils.format(comment.getCreatedAt()));
        map.put(COLUMN_UPDATED_AT, DateUtils.format(comment.getUpdatedAt()));
        return map;
    }

    /**
     * Get the sql query that will also get the user and lineup with every comment.
     *
     * @param user indicates if the user should be taken
     * @return sql query
     */
    private String getQuery(boolean user) {
        StringBuilder query = new StringBuilder();
        query.append(String.format("select %s.* ", TABLE_NAME));
        if (user) {
            query.append(String.format(", %s.%s as %s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME,
                    USERS_TABLE_NAME, USERS_COLUMN_NAME));
        }
        query.append(String.format(" from %s" , TABLE_NAME));
        if (user) {
            query.append(String.format(" inner join %s on %s.%s = %s.%s", USERS_TABLE_NAME,
                    TABLE_NAME, COLUMN_USER_ID, USERS_TABLE_NAME, USERS_COLUMN_ID));
        }
        return query.toString();
    }

    /**
     * Get all comments in the database.
     *
     * @return List of all comments
     */
    public List<Comment> getAll() {
        return super.getMultiple(this.getQuery(true), null);
    }

    /**
     * Find a comment by bis id.
     *
     * @param id comment id
     * @return Comment or null if the id don't exists
     */
    public Comment get(int id) {
        return super.getOneById(this.getQuery(true), id);
    }

    /**
     * Get the number of comments.
     *
     * @return number of comment in the database
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new comment in the database.
     *
     * @param comment Comment ot be stored
     * @return result of the store operation
     */
    public boolean store(Comment comment) {
        return super.store(this.putValues(comment));
    }

    /**
     * Update a existing comment.
     *
     * @param comment Comment to be setChanged
     * @return result of the onUpdateSuccess operation
     */
    public boolean update(Comment comment) {
        return super.update(this.putValues(comment), comment);
    }

    /**
     * Delete a exiting comment.
     *
     * @param id comment id
     * @return result of the delete operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }

    /**
     * Get all comments that the given user has created.
     *
     * @param userId user id
     * @return List of comments that the user has created
     */
    public List<Comment> getUserComments(int userId) {
        String where = String.format("%s = ?", COLUMN_USER_ID);
        String[] whereArgs = {String.valueOf(userId)};
        return super.getMultiple(where, whereArgs, null, null);
    }

    /**
     * Get all comment that were wrote for the lineup.
     *
     * @param lineupId lineup id
     * @return List of all comment that were wrote on the lineup
     */
    public List<Comment> getLineupComments(int lineupId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_LINEUP_ID);
        String[] whereArgs = {String.valueOf(lineupId)};
        return super.getMultiple(this.getQuery(true), where, whereArgs);
    }
}