package com.android.finki.mpip.footballdreamteam.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.finki.mpip.footballdreamteam.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Borce on 29.07.2016.
 */
public class MainSQLiteOpenHelper extends SQLiteOpenHelper {

    private Context context;
    private String CREATE_SQL;
    private String DROP_SQL;

    public MainSQLiteOpenHelper(Context context) {
        super(context, context.getString(R.string.db_name), null,
              context.getResources().getInteger(R.integer.db_version));
        this.context = context;
        this.CREATE_SQL = context.getString(R.string.create_sql);
        this.DROP_SQL = context.getString(R.string.drop_sql);
    }

    /**
     * Called on every connection opening.
     *
     * @param db database on which the commands will be executed
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        /* This query needs to be executed if we want to have foreign key constraint enabled */
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Called only if the database is not already created.
     *
     * @param sqLiteDatabase database on which the commands will be executed
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.executeStatements(this.CREATE_SQL, sqLiteDatabase);
    }

    /**
     * Called when the old version of the database is not matching with the new version.
     *
     * @param sqLiteDatabase database on which the commands will be executed
     * @param oldVersion database old version
     * @param newVersion database new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            this.executeStatements(this.DROP_SQL, sqLiteDatabase);
            this.executeStatements(this.CREATE_SQL, sqLiteDatabase);
        }
    }

    /**
     * Execute all statements from the given file name.
     *
     * @param fileName sql file name
     * @param database database on which the queries will be executed
     */
    private void executeStatements(String fileName, SQLiteDatabase database) {
        List<String> statements = this.extractSqlStatements(fileName);
        for (String statement: statements) {
            database.execSQL(statement);
        }
    }

    /**
     * Extract all sql statements fromt he given file name.
     *
     * @param fileName name of the file
     * @return List of all sql statements in the file
     */
    private List<String> extractSqlStatements(String fileName) {
        String statements = "";
        AssetManager manager = context.getAssets();
        InputStream stream = null;
        try {
            stream = manager.open(fileName);
            int i = 0;
            while ((i = stream.read()) != -1) {
                statements += (char)i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        List<String> result = new ArrayList<>();
        Collections.addAll(result, statements.split(";"));
        return result;
    }
}
