package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 31.07.2016.
 */
public class PlayerRepository extends BaseRepository<Player, Integer> {

    private String COLUMN_TEAM_ID;
    private String COLUMN_POSITION_ID;
    private String COLUMN_NAME;
    private String COLUMN_NATIONALITY;
    private String COLUMN_DATE_OF_BIRTH;
    private String TEAMS_TABLE_NAME;
    private String TEAMS_COLUMN_ID;
    private String TEAMS_COLUMN_NAME;
    private String POSITIONS_TABLE_NAME;
    private String POSITIONS_COLUMN_ID;
    private String POSITIONS_COLUMN_NAME;


    public PlayerRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.players_table_name);
        this.COLUMN_ID = context.getString(R.string.players_column_id);
        this.COLUMN_TEAM_ID = context.getString(R.string.players_column_team_id);
        this.COLUMN_POSITION_ID = context.getString(R.string.players_column_position_id);
        this.COLUMN_NAME = context.getString(R.string.players_column_name);
        this.COLUMN_NATIONALITY = context.getString(R.string.players_column_nationality);
        this.COLUMN_DATE_OF_BIRTH = context.getString(R.string.players_column_date_of_birth);
        this.COLUMNS = new String[]{COLUMN_ID, COLUMN_TEAM_ID, COLUMN_POSITION_ID, COLUMN_NAME,
                COLUMN_NATIONALITY, COLUMN_DATE_OF_BIRTH};
        this.TEAMS_TABLE_NAME = context.getString(R.string.teams_table_name);
        this.TEAMS_COLUMN_ID = context.getString(R.string.teams_column_id);
        this.TEAMS_COLUMN_NAME = context.getString(R.string.teams_column_name);
        this.POSITIONS_TABLE_NAME = context.getString(R.string.positions_table_name);
        this.POSITIONS_COLUMN_ID = context.getString(R.string.positions_column_id);
        this.POSITIONS_COLUMN_NAME = context.getString(R.string.positions_column_name);
    }

    /**
     * Map the cursor values to a new Player.
     *
     * @param cursor database cursor containing the values
     * @return Player containing the cursor values
     */
    @Override
    protected Player mapCursor(Cursor cursor) {
        Player player = new Player();
        player.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        player.setTeamId(cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_ID)));
        player.setPositionId(cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION_ID)));
        player.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        player.setNationality(cursor.getString(cursor.getColumnIndex(COLUMN_NATIONALITY)));
        player.setDateOfBirth(DateUtils.parse(cursor
                .getString(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH))));

        String column = String.format("%s_%s", TEAMS_TABLE_NAME, TEAMS_COLUMN_NAME);
        int index = cursor.getColumnIndex(column);
        if (index != -1) {
            Team team = new Team(player.getTeamId(), cursor.getString(index));
            player.setTeam(team);
        }
        column = String.format("%s_%s", POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME);
        index = cursor.getColumnIndex(column);
        if (index != -1) {
            Position position = new Position(player.getPositionId(), cursor.getString(index));
            player.setPosition(position);
        }
        return player;
    }

    /**
     * Map the players values into a HaspMap.
     *
     * @param player Player containing the values.
     * @return HaspMap in which the table columns are mapped with player values
     */
    Map<String, String> putValues(Player player) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_ID, player.getId().toString());
        map.put(COLUMN_TEAM_ID, String.valueOf(player.getTeamId()));
        map.put(COLUMN_POSITION_ID, String.valueOf(player.getPositionId()));
        map.put(COLUMN_NAME, player.getName());
        map.put(COLUMN_NATIONALITY, player.getNationality());
        map.put(COLUMN_DATE_OF_BIRTH, DateUtils.format(player.getDateOfBirth()));
        return map;
    }

    /**
     * Create a sql query that will also get team and position for the player.
     *
     * @param team indicates if the team should be taken
     * @param  position indicates if hte position should be taken
     * @return sql query
     */
    private String getQuery(boolean team, boolean position) {
        StringBuilder query = new StringBuilder();
        query.append(String.format("select %s.*", TABLE_NAME));
        if (team) {
            query.append(String.format(", %s.%s as %s_%s ", TEAMS_TABLE_NAME, TEAMS_COLUMN_NAME,
                    TEAMS_TABLE_NAME, TEAMS_COLUMN_NAME));
        }
        if (position) {
            query.append(String.format(", %s.%s as %s_%s ", POSITIONS_TABLE_NAME,
                    POSITIONS_COLUMN_NAME, POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME));
        }
        query.append(String.format("from %s ", TABLE_NAME));
        if (team) {
            query.append(String.format("inner join %s on %s.%s = %s.%s ", TEAMS_TABLE_NAME,
                    TABLE_NAME, COLUMN_TEAM_ID, TEAMS_TABLE_NAME, TEAMS_COLUMN_ID));
        }
        if (position) {
            query.append(String.format("inner join %s on %s.%s = %s.%s ", POSITIONS_TABLE_NAME,
                    TABLE_NAME, COLUMN_POSITION_ID, POSITIONS_TABLE_NAME, POSITIONS_COLUMN_ID));
        }
        return query.toString();
    }

    /**
     * Get all players.
     *
     * @return List of all players
     */
    public List<Player> getAll() {
        String query = this.getQuery(true, true);
        return super.getMultiple(query, null);
    }

    /**
     * Find a player by his id.
     *
     * @param id player id
     * @return Player or null if the id don't exists
     */
    public Player get(int id) {
        return super.getOneById(this.getQuery(true, true), id);
    }

    /**
     * Fina a player by his name.
     *
     * @param name player name
     * @return Player or null if the name don;t exists
     */
    public Player getByName(String name) {
        return super.getOne(this.getQuery(true, true), COLUMN_NAME, name);
    }

    /**
     * Get the number of players.
     *
     * @return number of players
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new player.
     *
     * @param player Player to be stored
     * @return result of the store operation
     */
    public boolean store(Player player) {
        return super.store(this.putValues(player));
    }

    /**
     * Update a existing player.
     *
     * @param player Player ot be setChanged
     * @return result of the update operation.
     */
    public boolean update(Player player) {
        return super.update(this.putValues(player), player);
    }

    /**
     * Delete a existing player.
     *
     * @param id player id
     * @return result of the delete operation
     */
    public boolean delete(int id) {
        return super.delete(id);
    }

    /**
     * Get all players that players for the given team.
     *
     * @param teamId team id
     * @return List of players that plays for the given team
     */
    public List<Player> getTeamPlayers(int teamId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_TEAM_ID);
        String[] whereArgs = {String.valueOf(teamId)};
        return super.getMultiple(this.getQuery(false, true), where, whereArgs);
    }

    /**
     * Get all players that have a position id tha that is in the given array of positions ids.
     *
     * @param positionsIds array of positions id
     * @return List of Players
     */
    public List<Player> getPositionPlayers(Integer... positionsIds) {
        StringBuilder where = new StringBuilder();
        String[] whereArgs = new String[positionsIds.length];
        for (int i = 0; i < positionsIds.length; i++) {
            if (i > 0) {
                where.append(" or ");
            }
            where.append(String.format("%s.%s = ?", TABLE_NAME, COLUMN_POSITION_ID));
            whereArgs[i] = String.valueOf(positionsIds[i]);
        }
        return super.getMultiple(this.getQuery(true, false), where.toString(), whereArgs);
    }

    /**
     * Get all players that have a position id that is in the given array of positions ids and
     * their id is not in the array of players to exclude.
     *
     * @param playersToExclude arrays of players ids
     * @param positionsIds array of positions ids
     * @return List if Players
     */
    public List<Player> getPositionPlayers(int[] playersToExclude, Integer... positionsIds) {
        StringBuilder where = new StringBuilder();
        String[] whereArgs = new String[playersToExclude.length + positionsIds.length];
        int argsCount = 0;
        where.append("(");
        for (int i = 0; i < playersToExclude.length; i++) {
            if (i > 0) {
                where.append(" and ");
            }
            where.append(String.format("%s.%s != ?", TABLE_NAME, COLUMN_ID));
            whereArgs[argsCount++] = String.valueOf(playersToExclude[i]);
        }
        where.append(") and (");
        for (int i = 0; i < positionsIds.length; i++) {
            if (i > 0) {
                where.append(" or ");
            }
            where.append(String.format("%s.%s = ?", TABLE_NAME, COLUMN_POSITION_ID));
            whereArgs[argsCount++] = String.valueOf(positionsIds[i]);
        }
        where.append(")");
        return super.getMultiple(this.getQuery(true, false), where.toString(), whereArgs);
    }
}
