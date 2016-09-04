package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 31.07.2016.
 */
public class PositionRepository extends BaseRepository<Position, Integer> {

    private String COLUMN_NAME;

    public PositionRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.positions_table_name);
        this.COLUMN_ID = context.getString(R.string.positions_column_id);
        this.COLUMN_NAME = context.getString(R.string.positions_column_name);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME};
    }

    /**
     * Map the cursor values to a new Position.
     *
     * @param cursor database cursor containing the values
     * @return Position containing the cursor values
     */
    @Override
    protected Position mapCursor(Cursor cursor) {
        Position position = new Position();
        position.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        position.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        return position;
    }

    /**
     * Put the position values into a HashMap.
     *
     * @param position Position containing the values
     * @return HashMap in which table columns are mapped with position values
     */
    Map<String, String> putValues(Position position) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_ID, position.getId().toString());
        map.put(COLUMN_NAME, position.getName());
        return map;
    }

    /**
     * Get all positions.
     *
     * @return List of all positions in the database
     */
    public List<Position> getAll() {
        return super.getMultiple();
    }

    /**
     * Find a position by her id.
     *
     * @param id position id
     * @return Position or null if position don't exists
     */
    public Position get(int id) {
        return super.getOneById(id);
    }

    /**
     * Find a position by her name.
     *
     * @param name position name
     * @return Position or null if position with given name don't exists
     */
    public Position getByName(String name) {
        return super.getOne(COLUMN_NAME, name);
    }

    /**
     * Get the number of positions.
     *
     * @return number of positions
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new position.
     *
     * @param position Position to be stored
     * @return result of the store operation
     */
    public boolean store(Position position) {
        return super.store(this.putValues(position));
    }

    /**
     * Update a existing position.
     *
     * @param position Position to be setChanged
     * @return result of the update operation
     */
    public boolean update(Position position) {
        return super.update(this.putValues(position), position);
    }

    /**
     * Delete a existing position.
     *
     * @param id position id
     * @return result of the delete operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }

    /**
     * Search for positions that contains the given name.
     *
     * @param names array os name to be searched
     * @return List of positions that contains the given name
     *
     */
    public List<Position> searchByName(String... names) {
        StringBuilder where = new StringBuilder();
        List<String> whereArgs = new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                where.append(" or ");
            }
            where.append(String.format("%s LIKE ?", COLUMN_NAME));
            whereArgs.add("%" + names[i] + "%");
        }
        return super.getMultiple(where.toString(), whereArgs
                .toArray(new String[names.length]), null, null);
    }
}
