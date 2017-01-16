package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 05.08.2016.
 */
public class LikeRepository extends Repository<LineupLike> {

    private String COLUMN_USER_ID;
    private String COLUMN_LINEUP_ID;
    private String COLUMN_CREATED_AT;
    private String USERS_TABLE_NAME;
    private String USERS_COLUMN_ID;
    private String USERS_COLUMN_NAME;

    public LikeRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.lineups_likes_table_name);
        this.COLUMN_USER_ID = context.getString(R.string.lineup_likes_user_id);
        this.COLUMN_LINEUP_ID = context.getString(R.string.lineup_likes_lineup_id);
        this.COLUMN_CREATED_AT = context.getString(R.string.lineup_likes_created_at);
        this.USERS_TABLE_NAME = context.getString(R.string.users_table_name);
        this.USERS_COLUMN_ID = context.getString(R.string.users_column_id);
        this.USERS_COLUMN_NAME = context.getString(R.string.users_column_name);
    }

    /**
     * Map the cursor values into a new LineupLike.
     *
     * @param cursor database cursor containing the values
     * @return LineupLike containing the cursor values
     */
    @Override
    protected LineupLike mapCursor(Cursor cursor) {
        LineupLike like = new LineupLike();
        like.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        like.setLineupId(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID)));
        like.setCreatedAt(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_CREATED_AT))));
        String column = String.format("%s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME);
        int index = cursor.getColumnIndex(column);
        if (index != -1) {
            User user = new User(like.getUserId(), cursor.getString(index));
            like.setUser(user);
        }
        return like;
    }

    /**
     * Map the LineupLike values into a HashMap.
     *
     * @param like LineupLike containing the values
     * @return HashMap where table columns are mapped with like values
     */
    Map<String, String> putValues(LineupLike like) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_USER_ID, String.valueOf(like.getUserId()));
        map.put(COLUMN_LINEUP_ID, String.valueOf(like.getLineupId()));
        map.put(COLUMN_CREATED_AT, DateUtils.format(like.getCreatedAt()));
        return map;
    }

    /**
     * Create a sql query that will also take the user name with every select query.
     *
     * @param user whatever the user should be retrived
     * @return sql query
     */
    private String getQuery(boolean user) {
        StringBuilder query = new StringBuilder();
        query.append(String.format("select %s.*", TABLE_NAME));
        if (user) {
            query.append(String.format(", %s.%s as %s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME,
                    USERS_TABLE_NAME, USERS_COLUMN_NAME));
        }
        query.append(String.format(" from %s", TABLE_NAME));
        if (user) {
            query.append(String.format(" inner join %s on %s.%s = %s.%s", USERS_TABLE_NAME,
                    TABLE_NAME, COLUMN_USER_ID, USERS_TABLE_NAME, USERS_COLUMN_ID));
        }
        return query.toString();
    }

    /**
     * Get all likes in the lineups.
     *
     * @return List of all likes
     */
    public List<LineupLike> getAll() {
        return super.getMultiple(this.getQuery(true), null);
    }

    /**
     * Find a like by the lineup and user id.
     *
     * @param userId   user id
     * @param lineupId lineup id
     * @return Like or null if the record don't exists
     */
    public LineupLike get(int userId, int lineupId) {
        String where = String.format("%s.%s = ? and %s.%s = ?", TABLE_NAME, COLUMN_USER_ID,
                TABLE_NAME, COLUMN_LINEUP_ID);
        String[] whereArgs = {String.valueOf(userId), String.valueOf(lineupId)};
        return super.getOne(this.getQuery(true), where, whereArgs);
    }

    /**
     * Get the number of likes on all lineups.
     *
     * @return number of likes
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new like to the lineup from the user.
     *
     * @param like LineupLike containing the values
     * @return success of the operation
     */
    public boolean store(LineupLike like) {
        return super.store(this.putValues(like));
    }

    /**
     * Delete a like from tbe user on the lineup.
     *
     * @param userId   user id
     * @param lineupId lineup id
     * @return success of the operation
     */
    public boolean delete(int userId, int lineupId) {
        String where = String.format("%s = ? and %s = ?", COLUMN_USER_ID, COLUMN_LINEUP_ID);
        String[] whereArgs = {String.valueOf(userId), String.valueOf(lineupId)};
        return super.delete(where, whereArgs);
    }

    /**
     * Get all likes for the given lineup.
     *
     * @param lineupId lineup id
     * @return List of all likes that are for the given lineup
     */
    public List<LineupLike> getLineupLikes(int lineupId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_LINEUP_ID);
        String[] whereArgs = {String.valueOf(lineupId)};
        return super.getMultiple(this.getQuery(true), where, whereArgs);
    }

    /**
     * Get all likes for the given user.
     *
     * @param userId user id
     * @return List of all like that were made from the given user
     */
    public List<LineupLike> getUserLikes(int userId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_USER_ID);
        String[] whereArgs = {String.valueOf(userId)};
        return super.getMultiple(where, whereArgs, null, null);
    }
}
