package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 31.07.2016.
 */
public class TeamRepository extends BaseRepository<Team, Integer> {

    private String COLUMN_NAME;
    private String COLUMN_SHORT_NAME;
    private String COLUMN_SQUAD_MARKET_VALUE;

    public TeamRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.teams_table_name);
        this.COLUMN_ID = context.getString(R.string.teams_column_id);
        this.COLUMN_NAME = context.getString(R.string.teams_column_name);
        this.COLUMN_SHORT_NAME = context.getString(R.string.teams_column_short_name);
        this.COLUMN_SQUAD_MARKET_VALUE = context
                .getString(R.string.teams_column_squad_market_value);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME,
                COLUMN_SHORT_NAME, COLUMN_SQUAD_MARKET_VALUE};
    }

    /**
     * Map the Cursor to a new User.
     *
     * @param cursor database cursor containing the values
     * @return new Team with cursor values
     */
    @Override
    protected Team mapCursor(Cursor cursor) {
        Team team = new Team();
        team.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        team.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        team.setShortName(cursor.getString(cursor.getColumnIndex(COLUMN_SHORT_NAME)));
        team.setSquadMarketValue(cursor.getString(cursor
                .getColumnIndex(COLUMN_SQUAD_MARKET_VALUE)));
        return team;
    }

    /**
     * Put team values into a HashMap
     *
     * @param team Team containing the values
     * @return HashMap in wich the table columns is mapped with the team values
     */
    Map<String, String> putValues(Team team) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_ID, team.getId().toString());
        map.put(COLUMN_NAME, team.getName());
        map.put(COLUMN_SHORT_NAME, team.getShortName());
        map.put(COLUMN_SQUAD_MARKET_VALUE, team.getSquadMarketValue());
        return map;
    }

    /**
     * Get all teams in the database.
     *
     * @return List of all teams
     */
    public List<Team> getAll() {
        return super.getMultiple();
    }

    /**
     * Find a team by his id.
     *
     * @param id team id
     * @return Team or null if the id don;t exists
     */
    public Team get(int id) {
        return super.getOneById(id);
    }

    /**
     * Find a team by his name.
     *
     * @param name team name
     * @return Team or null if the name don't exists.
     */
    public Team getByName(String name) {
        return super.getOne(COLUMN_NAME, name);
    }

    /**
     * Get the number of teams.
     *
     * @return number of teams
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new team in the database.
     *
     * @param team Team to be stored
     * @return success of the operation
     */
    public boolean store(Team team) {
        return super.store(this.putValues(team));
    }

    /**
     * Update a existing team.
     *
     * @param team Team to be setChanged
     * @return success of the operation
     */
    public boolean update(Team team) {
        return super.update(this.putValues(team), team);
    }

    /**
     * Delete the team from the database.
     *
     * @param id id of the team to be deleted
     * @return success of the operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }
}
