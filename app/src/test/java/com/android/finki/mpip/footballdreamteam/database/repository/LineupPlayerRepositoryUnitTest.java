package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private LineupPlayerRepository repository;

    private Calendar calendar = new GregorianCalendar(2016, 7, 4, 10, 37, 10);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.format(date);
    private String COLUMN_LINEUP_ID = "lineup_id";
    private String COLUMN_PLAYER_ID = "player_id";
    private String COLUMN_POSITION_ID = "position_id";
    private String COLUMN_CREATED_AT = "created_at";
    private String COLUMN_UPDATED_AT = "updated_at";
    private String PLAYERS_TABLE_NAME = "players";
    private String PLAYERS_COLUMN_NAME = "name";
    private String POSITIONS_TABLE_NAME = "positions";
    private String POSITIONS_COLUMN_NAME = "name";
    private Lineup lineup = new Lineup(1, 1);
    private Player player = new Player(1, "Player");
    private Position position = new Position(3, "Position");
    private LineupPlayer lineupPlayer = new LineupPlayer(lineup, player, position, date, date);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new LineupPlayerRepository(context, dbHelper);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.lineups_players_column_lineup_id))
                .thenReturn(COLUMN_LINEUP_ID);
        when(context.getString(R.string.lineups_players_column_player_id))
                .thenReturn(COLUMN_PLAYER_ID);
        when(context.getString(R.string.lineups_players_column_position_id))
                .thenReturn(COLUMN_POSITION_ID);
        when(context.getString(R.string.lineups_players_column_created_at))
                .thenReturn(COLUMN_CREATED_AT);
        when(context.getString(R.string.lineups_players_column_updated_at))
                .thenReturn(COLUMN_UPDATED_AT);
        when(context.getString(R.string.players_table_name)).thenReturn(PLAYERS_TABLE_NAME);
        when(context.getString(R.string.players_table_name)).thenReturn(PLAYERS_TABLE_NAME);
        when(context.getString(R.string.players_column_id)).thenReturn("id");
        when(context.getString(R.string.players_column_name)).thenReturn(PLAYERS_COLUMN_NAME);
        when(context.getString(R.string.positions_table_name)).thenReturn(POSITIONS_TABLE_NAME);
        when(context.getString(R.string.positions_column_id)).thenReturn("id");
        when(context.getString(R.string.positions_column_name)).thenReturn(POSITIONS_COLUMN_NAME);
    }

    /**
     * Mock the cursor to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_LINEUP_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID)))
                .thenReturn(lineupPlayer.getLineupId());
        when(cursor.getColumnIndex(COLUMN_PLAYER_ID)).thenReturn(1);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYER_ID)))
                .thenReturn(lineupPlayer.getPlayerId());
        when(cursor.getColumnIndex(COLUMN_POSITION_ID)).thenReturn(2);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION_ID)))
                .thenReturn(lineupPlayer.getPositionId());
        when(cursor.getColumnIndex(COLUMN_CREATED_AT)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)))
                .thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_UPDATED_AT)).thenReturn(4);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT)))
                .thenReturn(sDate);
        String column = String.format("%s_%s", PLAYERS_TABLE_NAME, PLAYERS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(5);
        when(cursor.getString(cursor.getColumnIndex(column)))
                .thenReturn(player.getName());
        column = String.format("%s_%s", POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(6);
        when(cursor.getString(cursor.getColumnIndex(column)))
                .thenReturn(position.getName());
    }

    /**
     * Test that mapCursor method will correctly map the cursor values into a new LineupPlayer.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        LineupPlayer mapped = repository.mapCursor(cursor);
        assertNotNull(mapped);
        assertTrue(lineupPlayer.same(mapped));
        Player mPlayer = mapped.getPlayer();
        assertTrue(player.same(mPlayer));
        Position mPosition = mapped.getPosition();
        assertTrue(position.same(mPosition));
    }

    /**
     * Test that putValues method will map LineupPlayer values into a HashMap
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(lineupPlayer);
        assertNotNull(map);
        assertEquals(String.valueOf(lineupPlayer.getLineupId()), map.get(COLUMN_LINEUP_ID));
        assertEquals(String.valueOf(lineupPlayer.getPlayerId()), map.get(COLUMN_PLAYER_ID));
        assertEquals(String.valueOf(lineupPlayer.getPositionId()), map.get(COLUMN_POSITION_ID));
        assertEquals(sDate, map.get(COLUMN_CREATED_AT));
        assertEquals(sDate, map.get(COLUMN_UPDATED_AT));
    }
}
