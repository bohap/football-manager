package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerRepository extends Repository<LineupPlayer> {

    private String COLUMN_LINEUP_ID;
    private String COLUMN_PLAYER_ID;
    private String COLUMN_POSITION_ID;
    private String COLUMN_CREATED_AT;
    private String COLUMN_UPDATED_AT;
    private String PLAYERS_TABLE_NAME;
    private String PLAYERS_COLUMN_ID;
    private String PLAYERS_COLUMN_NAME;
    private String POSITIONS_TABLE_NAME;
    private String POSITIONS_COLUMN_ID;
    private String POSITIONS_COLUMN_NAME;

    public LineupPlayerRepository(Context context, MainSQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.TABLE_NAME = context.getString(R.string.lineups_players_table_name);
        this.COLUMN_LINEUP_ID = context.getString(R.string.lineups_players_column_lineup_id);
        this.COLUMN_PLAYER_ID = context.getString(R.string.lineups_players_column_player_id);
        this.COLUMN_POSITION_ID = context.getString(R.string.lineups_players_column_position_id);
        this.COLUMN_CREATED_AT = context.getString(R.string.lineups_players_column_created_at);
        this.COLUMN_UPDATED_AT = context.getString(R.string.lineups_players_column_updated_at);
        this.COLUMNS = new String[]{COLUMN_LINEUP_ID, COLUMN_PLAYER_ID, COLUMN_POSITION_ID,
                COLUMN_CREATED_AT, COLUMN_UPDATED_AT};
        this.PLAYERS_TABLE_NAME = context.getString(R.string.players_table_name);
        this.PLAYERS_COLUMN_ID = context.getString(R.string.players_column_id);
        this.PLAYERS_COLUMN_NAME = context.getString(R.string.players_column_name);
        this.POSITIONS_TABLE_NAME = context.getString(R.string.positions_table_name);
        this.POSITIONS_COLUMN_ID = context.getString(R.string.positions_column_id);
        this.POSITIONS_COLUMN_NAME = context.getString(R.string.positions_column_name);
    }

    /**
     * Map the cursor data to a new LineupPlayer.
     *
     * @param cursor database cursor containing the values
     * @return LineupPlayer containing cursor data
     */
    protected LineupPlayer mapCursor(Cursor cursor) {
        LineupPlayer lineupPlayer = new LineupPlayer();
        lineupPlayer.setLineupId(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID)));
        lineupPlayer.setPlayerId(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYER_ID)));
        lineupPlayer.setPositionId(cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION_ID)));
        lineupPlayer.setCreatedAt(DateUtils.parse(cursor.getString(cursor
                .getColumnIndex(COLUMN_CREATED_AT))));
        lineupPlayer.setUpdatedAt(DateUtils.parse(cursor.getString(cursor
                .getColumnIndex(COLUMN_UPDATED_AT))));

        String column = String.format("%s_%s", PLAYERS_TABLE_NAME, PLAYERS_COLUMN_NAME);
        int index = cursor.getColumnIndex(column);
        if (index != -1) {
            Player player = new Player(lineupPlayer.getPlayerId(), cursor.getString(index));
            lineupPlayer.setPlayer(player);
        }
        column = String.format("%s_%s", POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME);
        index = cursor.getColumnIndex(column);
        if (index != -1) {
            Position position = new Position(lineupPlayer.getPositionId(),
                    cursor.getString(index));
            lineupPlayer.setPosition(position);
        }
        return lineupPlayer;
    }

    /**
     * Put the LineupPlayer values into a HashMap.
     *
     * @param lineupPlayer LineupPlayer containing the values
     * @return HashMap where table columns are mapped with LineupPlayer values
     */
    Map<String, String> putValues(LineupPlayer lineupPlayer) {
        Map<String, String> map = new HashMap<>();
        map.put(COLUMN_LINEUP_ID, String.valueOf(lineupPlayer.getLineupId()));
        map.put(COLUMN_PLAYER_ID, String.valueOf(lineupPlayer.getPlayerId()));
        map.put(COLUMN_POSITION_ID, String.valueOf(lineupPlayer.getPositionId()));
        map.put(COLUMN_CREATED_AT, DateUtils.format(lineupPlayer.getCreatedAt()));
        map.put(COLUMN_UPDATED_AT, DateUtils.format(lineupPlayer.getUpdatedAt()));
        return map;
    }

    /**
     * Get the query that will also take player name and position name with every select.
     *
     * @param player   whatever the player should be taken
     * @param position whatever the position should be taken
     * @return sql query
     */
    private String getQuery(boolean player, boolean position) {
        StringBuilder query = new StringBuilder();
        query.append(String.format("select %s.* ", TABLE_NAME));
        if (player) {
            query.append(String.format(", %s.%s as %s_%s ", PLAYERS_TABLE_NAME,
                    PLAYERS_COLUMN_NAME, PLAYERS_TABLE_NAME, PLAYERS_COLUMN_NAME));
        }
        if (position) {
            query.append(String.format(", %s.%s as %s_%s ", POSITIONS_TABLE_NAME,
                    POSITIONS_COLUMN_NAME, POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME));
        }
        query.append(String.format(" from %s", TABLE_NAME));
        if (player) {
            query.append(String.format(" inner join %s on %s.%s = %s.%s ", PLAYERS_TABLE_NAME,
                    TABLE_NAME, COLUMN_PLAYER_ID, PLAYERS_TABLE_NAME, PLAYERS_COLUMN_ID));
        }
        if (position) {
            query.append(String.format(" inner join %s on %s.%s = %s.%s ", POSITIONS_TABLE_NAME,
                    TABLE_NAME, COLUMN_POSITION_ID, POSITIONS_TABLE_NAME, POSITIONS_COLUMN_ID));
        }
        return query.toString();
    }

    /**
     * Get all players that are in a lineup.
     *
     * @return List of all lineups players and their position
     */
    public List<LineupPlayer> getAll() {
        return super.getMultiple(this.getQuery(true, true), null);
    }

    /**
     * Find a player in hte lineup by the lineup and player id.
     *
     * @param lineupId lineup id
     * @param playerId player id
     * @return LineupPlayer or null if the record can;t be find
     */
    public LineupPlayer get(int lineupId, int playerId) {
        String where = String.format("%s = ? and %s = ?", COLUMN_LINEUP_ID, COLUMN_PLAYER_ID);
        String[] whereArgs = {String.valueOf(lineupId), String.valueOf(playerId)};
        return super.getOne(this.getQuery(true, true), where, whereArgs);
    }

    /**
     * Get the number of players that are in a lineup.
     *
     * @return number of records in tht table
     */
    public long count() {
        return super.countRecords();
    }

    /**
     * Store a new player for the lineup.
     *
     * @param lineupPlayer LineupPlayer to be stored
     * @return success of the operation
     */
    public boolean store(LineupPlayer lineupPlayer) {
        return super.store(this.putValues(lineupPlayer));
    }

    /**
     * Update a existing player in the lineup.
     *
     * @param lineupPlayer LineupPlayer to be setChanged
     * @return success of the operation
     */
    public boolean update(LineupPlayer lineupPlayer) {
        String where = String.format("%s = ? and %s = ?", COLUMN_LINEUP_ID, COLUMN_PLAYER_ID);
        String[] whereArgs = {String.valueOf(lineupPlayer.getLineupId()),
                String.valueOf(lineupPlayer.getPlayerId())};
        return super.update(this.putValues(lineupPlayer), where, whereArgs);
    }

    /**
     * Delete e existing player from the lineup.
     *
     * @param lineupId lineup id
     * @param playerId player id
     * @return success of the operation
     */
    public boolean delete(int lineupId, int playerId) {
        String where = String.format("%s = ? and %s = ?", COLUMN_LINEUP_ID, COLUMN_PLAYER_ID);
        String[] whereArgs = {String.valueOf(lineupId), String.valueOf(playerId)};
        return super.delete(where, whereArgs);
    }

    /**
     * Get all players that are in the given lineup.
     *
     * @param lineupId lineup id
     * @return List of LineupPlayers that are for the given lineup
     */
    public List<LineupPlayer> getLineupPlayers(int lineupId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_LINEUP_ID);
        String[] whereArgs = {String.valueOf(lineupId)};
        return super.getMultiple(this.getQuery(true, true), where, whereArgs);
    }

    /**
     * Get all lineups in which the given player is put.
     *
     * @param playerId player id
     * @return List of LineupPlayer that contains the player id
     */
    public List<LineupPlayer> getPlayerLineups(int playerId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_PLAYER_ID);
        String[] whereArgs = {String.valueOf(playerId)};
        return super.getMultiple(this.getQuery(false, true), where, whereArgs);
    }

    /**
     * Get all players that have the given position in a lineup.
     *
     * @param positionId position id
     * @return List of LineupPlayer that contains the position id
     */
    public List<LineupPlayer> getPositionLineups(int positionId) {
        String where = String.format("%s.%s = ?", TABLE_NAME, COLUMN_POSITION_ID);
        String[] whereArgs = {String.valueOf(positionId)};
        return super.getMultiple(this.getQuery(true, false), where, whereArgs);
    }
}