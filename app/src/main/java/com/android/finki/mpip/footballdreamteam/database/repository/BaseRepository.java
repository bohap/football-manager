package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.android.finki.mpip.footballdreamteam.model.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 29.07.2016.
 */
abstract class BaseRepository<T extends BaseModel<K>, K> extends Repository<T> {

    String COLUMN_ID;

    /**
     * Find a record from the table by his primary key.
     *
     * @param id primary key value
     * @return T or null if the id don't exists
     */
    T getOneById(K id) {
        return super.getOne(COLUMN_ID, id.toString());
    }

    /**
     * Find a record in the table by his primary key.
     *
     * @param raw query to be executed
     * @param id primary key value
     * @return T or null if the id don't exists
     */
    T getOneById(String raw, K id) {
        return super.getOne(raw, COLUMN_ID, id.toString());
    }

    /**
     * Update a existing record in the table.
     *
     * @param params Map containing the values to be stored that will be stored
     * @param model T model that is updating
     * @return success of the operation
     */
    boolean update(Map<String, String> params, T model) {
        String where = String.format("%s = ?", COLUMN_ID);
        String[] whereArgs = {model.getId().toString()};
        return super.update(params, where, whereArgs);
    }

    /**
     * Delete a record from the table.
     *
     * @param id id of the record that will be deleted
     * @return success of the operation
     */
    boolean delete(K id) {
        String where = String.format("%s = ?", COLUMN_ID);
        String[] whereArgs = {id.toString()};
        return super.delete(where, whereArgs);
    }
}