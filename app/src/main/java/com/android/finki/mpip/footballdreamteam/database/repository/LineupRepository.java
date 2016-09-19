package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 01.08.2016.
 */
public class LineupRepository extends BaseRepository<Lineup, Integer> {

    private String COLUMN_USER_ID;
    private String COLUMN_CREATED_AT;
    private String COLUMN_UPDATED_AT;

    public LineupRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.lineups_table_name);
        this.COLUMN_ID = context.getString(R.string.lineups_column_id);
        this.COLUMN_USER_ID = context.getString(R.string.lineups_column_user_id);
        this.COLUMN_CREATED_AT = context.getString(R.string.lineups_column_created_at);
        this.COLUMN_UPDATED_AT = context.getString(R.string.lineups_column_updated_at);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_USER_ID,
                COLUMN_CREATED_AT, COLUMN_UPDATED_AT};
    }

    /**
     * Map the cursor values into a new Lineup.
     *
     * @param cursor database cursor containing the values
     * @return new Lineup containing the cursor values
     */
    @Override
    protected Lineup mapCursor(Cursor cursor) {
        Lineup lineup = new Lineup();
        lineup.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        lineup.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
        lineup.setCreatedAt(DateUtils
                .parse(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT))));
        lineup.setUpdatedAt(DateUtils
                .parse(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT))));
        return lineup;
    }

    /**
     * Put the lineup values into a hash map.
     *
     * @param lineup Lineup containing the values
     * @return HashMap in which the table columns are mapped with the lineup values
     */
    Map<String, String> putValues(Lineup lineup) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_ID, lineup.getId().toString());
        map.put(COLUMN_USER_ID, String.valueOf(lineup.getUserId()));
        map.put(COLUMN_CREATED_AT, DateUtils.format(lineup.getCreatedAt()));
        map.put(COLUMN_UPDATED_AT, DateUtils.format(lineup.getUpdatedAt()));
        return map;
    }

    /**
     * Get all lineups in the database.
     *
     * @return List of all lineups
     */
    public List<Lineup> getAll() {
        return super.getMultiple();
    }

    /**
     * Find a lineup by the her id.
     *
     * @param id lineup id
     * @return Lineup or null if the lineup don't exists
     */
    public Lineup get(int id) {
        return super.getOneById(id);
    }

    /**
     * Get the number of lineups.
     *
     * @return number of lineups in the database
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new lineup.
     *
     * @param lineup Lineup to be stored
     * @return result of the store operation
     */
    public boolean store(Lineup lineup) {
        return super.store(this.putValues(lineup));
    }

    /**
     * Update a existing lineup.
     *
     * @param lineup Lineup to be setChanged
     * @return result of the onUpdateSuccess operation
     */
    public boolean update(Lineup lineup) {
        return super.update(this.putValues(lineup), lineup);
    }

    /**
     * Delete a existing lineup.
     *
     * @param id lineup id
     * @return result of the delete operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }

    /**
     * Get all lineup for the given user.
     *
     * @param userId user id
     * @return List of all lineups that th euser has created
     */
    public List<Lineup> getUserLineups(int userId) {
        String where = String.format("%s = ?", COLUMN_USER_ID);
        String[] whereArgs = {String.valueOf(userId)};
        return super.getMultiple(where, whereArgs, null, null);
    }
}
