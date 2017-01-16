package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 04.08.2016.
 */
abstract class Repository<T> {

    protected MainSQLiteOpenHelper dbHelper;
    protected SQLiteDatabase database;
    protected String TABLE_NAME;
    protected String[] COLUMNS;

    protected abstract T mapCursor(Cursor cursor);

    /**
     * Open a new database connection.
     */
    public void open() {
        if (this.database == null) {
            this.database = dbHelper.getWritableDatabase();
        }
    }

    /**
     * Close the database connection.
     */
    public void close() {
        this.database = null;
    }

    /**
     * Checks if there is a open connection to the database.
     *
     * @return whatever there is s open connection to the database
     */
    public boolean isOpen() {
        return this.database != null;
    }

    /**
     * Get multiple records from the table.
     *
     * @param cursor database cursor containing the records
     * @return multiple records from the table
     */
    private List<T> getMultipleRecords(Cursor cursor) {
        List<T> result = new ArrayList<>();
        cursor.moveToFirst();
        while (! cursor.isAfterLast()) {
            result.add(this.mapCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    /**
     * Get one record from the table.
     *
     * @param cursor database cursor containing the record
     * @return single record from the table
     */
    private T getOneRecord(Cursor cursor) {
        T result = null;
        if (cursor.moveToFirst()) {
            result = this.mapCursor(cursor);
        }
        cursor.close();
        return result;
    }

    /**
     * Get multiple records from the database.
     *
     * @param where selection clause
     * @param whereArgs selection arguments
     * @param orderBy order by clause
     * @param limit number of records to be returned
     * @return multiple records from the table
     */
    List<T> getMultiple(String where, String[] whereArgs,
                        String orderBy, String limit) {
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, where, whereArgs,
                null, null, orderBy, limit);
        return this.getMultipleRecords(cursor);
    }

    /**
     * Get multiple records from the table.
     *
     * @return multiple records from the table
     */
    List<T> getMultiple() {
        return this.getMultiple(null, null, null, null);
    }

    /**
     * Get multiple records from the table.
     *
     * @param raw query to be executed
     * @param params query params
     * @return multiple records from the table
     */
    List<T> getMultiple(String raw, String[] params) {
        Cursor cursor = this.database.rawQuery(raw, params);
        return this.getMultipleRecords(cursor);
    }

    /**
     * Get multiple records from the table.
     *
     * @param raw query to be executed
     * @param where selection query
     * @param whereArgs selection arguments
     * @return multiple records from the table
     */
    List<T> getMultiple(String raw, String where, String[] whereArgs) {
        raw += " where " + where;
        return this.getMultiple(raw, whereArgs);
    }

    /**
     * Group the records from the table by the given groupBy clause.
     *
     * @param groupBy groupBy clause
     * @param where selection clause
     * @param whereArgs selection arguments
     * @param orderBy order by clause
     * @param limit number of records to be returned
     * @return grouped records from the table
     */
    List<T> getGrouped(String groupBy, String where,
                       String[] whereArgs, String orderBy, String limit) {
        List<T> result = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, where, whereArgs,
                groupBy, null, orderBy, limit);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()) {
            result.add(this.mapCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    /**
     * Group the records from the table by the given group by clause.
     *
     * @param groupBy group by clause
     * @return grouped records from the table
     */
    List<T> getGrouped(String groupBy) {
        return this.getGrouped(groupBy, null, null, null, null);
    }

    /**
     * Find a record from the table by the given value.
     *
     * @param column name of the columns on which the selection will be
     * @param value column value
     * @return T or null if the value don't exists
     */
    T getOne(String column, String value) {
        String where = String.format("%s = ? COLLATE NOCASE", column);
        String[] whereArgs = {value};
        return this.getOne(where, whereArgs);
    }

    /**
     * Find a record in the database by the where clause.
     *
     * @param where selection clause
     * @param whereArgs selection arguments
     * @return T or null if a record can't be find
     */
    T getOne(String where, String[] whereArgs) {
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, where, whereArgs, null, null, null);
        return this.getOneRecord(cursor);
    }

    /**
     * Find a record in the table by the given value.
     *
     * @param raw query to be executed
     * @param column name of the columns on which the selection will be
     * @param value column value
     * @return T or null if the id don't exists
     */
    T getOne(String raw, String column, String value) {
        String where = String.format("%s.%s = ?", TABLE_NAME, column);
        String[] whereArgs = {value};
        return this.getOne(raw, where, whereArgs);
    }

    /**
     * Find a record in the table by the where clause.
     *
     * @param raw query to be executed
     * @param where selection clause
     * @param whereArgs selection arguments
     * @return T or null if the id don't exists
     */
    T getOne(String raw, String where, String[] whereArgs) {
        raw += " where " + where;
        Cursor cursor = database.rawQuery(raw, whereArgs);
        return this.getOneRecord(cursor);
    }

    /**
     * Create a new record in the table.
     *
     * @param params Map containing the values that will be stored
     * @return success of the operation
     */
    boolean store(Map<String, String> params) {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> param : params.entrySet()) {
            values.put(param.getKey(), param.getValue());
        }
        long result = database.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    /**
     * Update a existing record in the table.
     *
     * @param params Map containing the values to be stored that will be stored
     * @param where selection string
     * @param whereArgs selection arguments
     * @return success of the operation
     */
    boolean update(Map<String, String> params, String where, String[] whereArgs) {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> param : params.entrySet()) {
            values.put(param.getKey(), param.getValue());
        }
        int effect = database.update(TABLE_NAME, values, where, whereArgs);
        return effect != 0;
    }

    /**
     * Delete a record from the table.
     *
     * @param where selection string
     * @param whereArgs selection arguments
     * @return success of the operation
     */
    boolean delete(String where, String[] whereArgs) {
        int effect = database.delete(TABLE_NAME, where, whereArgs);
        return effect > 0;
    }

    /**
     * Get the number of records in the table.
     *
     * @return number of records int he table
     */
    long countRecords() {
        return DatabaseUtils.queryNumEntries(database, TABLE_NAME);
    }
}