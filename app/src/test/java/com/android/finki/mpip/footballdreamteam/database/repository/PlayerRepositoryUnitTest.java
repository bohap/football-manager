package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Borce on 31.07.2016.
 */
public class PlayerRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private PlayerRepository repository;

    private String COLUMN_ID = "id";
    private String COLUMN_TEAM_ID = "team_id";
    private String COLUMN_POSITION_ID = "position_id";
    private String COLUMN_NAME = "name";
    private String COLUMN_NATIONALITY = "nationality";
    private String COLUMN_DATE_OF_BIRTH = "data_of_birth";
    private String TEAMS_TABLE_NAME = "teams";
    private String TEAMS_COLUMN_NAME = "name";
    private String POSITIONS_TABLE_NAME = "positions";
    private String POSITIONS_COLUMN_NAME = "name";
    private Calendar calendar = new GregorianCalendar(2016, 6, 31);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.format(date);
    private Team team = new Team(1, "Team");
    private Position position = new Position(1, "position");
    private Player player = new Player(1, team, position, "Player", "Nationality", date, 0);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new PlayerRepository(context, dbHelper);
    }

    private void initContext() {
        when(context.getString(R.string.players_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.players_column_team_id)).thenReturn(COLUMN_TEAM_ID);
        when(context.getString(R.string.players_column_position_id))
                .thenReturn(COLUMN_POSITION_ID);
        when(context.getString(R.string.players_column_name)).thenReturn(COLUMN_NAME);
        when(context.getString(R.string.players_column_nationality))
                .thenReturn(COLUMN_NATIONALITY);
        when(context.getString(R.string.players_column_date_of_birth))
                .thenReturn(COLUMN_DATE_OF_BIRTH);
        when(context.getString(R.string.teams_table_name)).thenReturn(TEAMS_TABLE_NAME);
        when(context.getString(R.string.teams_column_id)).thenReturn("id");
        when(context.getString(R.string.teams_column_name)).thenReturn(TEAMS_COLUMN_NAME);
        when(context.getString(R.string.positions_table_name)).thenReturn(POSITIONS_TABLE_NAME);
        when(context.getString(R.string.positions_column_id)).thenReturn("id");
        when(context.getString(R.string.positions_column_name)).thenReturn(POSITIONS_COLUMN_NAME);
    }

    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))).thenReturn(player.getId());
        when(cursor.getColumnIndex(COLUMN_TEAM_ID)).thenReturn(1);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_TEAM_ID)))
                .thenReturn(player.getTeamId());
        when(cursor.getColumnIndex(COLUMN_POSITION_ID)).thenReturn(2);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION_ID)))
                .thenReturn(player.getPositionId());
        when(cursor.getColumnIndex(COLUMN_NAME)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NAME))).thenReturn(player.getName());
        when(cursor.getColumnIndex(COLUMN_NATIONALITY)).thenReturn(4);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NATIONALITY)))
                .thenReturn(player.getNationality());
        when(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH)).thenReturn(5);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH)))
                .thenReturn(sDate);
        String column = String.format("%s_%s", TEAMS_TABLE_NAME, TEAMS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(6);
        when(cursor.getString(cursor.getColumnIndex(column))).thenReturn(team.getName());
        column = String.format("%s_%s", POSITIONS_TABLE_NAME, POSITIONS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(7);
        when(cursor.getString(cursor.getColumnIndex(column))).thenReturn(position.getName());
    }

    /**
     * Test that the mapCursor method correctly maps the cursor values into a Player.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        Player mapped = repository.mapCursor(cursor);
        assertTrue(player.same(mapped));
        Team mTeam = mapped.getTeam();
        assertTrue(team.same(mTeam));
        Position mPosition = mapped.getPosition();
        assertTrue(position.same(mPosition));
    }

    /**
     * Test that the putValues method successfully maps Player values into a HashMap.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(player);
        assertEquals(player.getId().toString(), map.get(COLUMN_ID));
        assertEquals(String.valueOf(player.getTeamId()), map.get(COLUMN_TEAM_ID));
        assertEquals(String.valueOf(player.getPositionId()), map.get(COLUMN_POSITION_ID));
        assertEquals(player.getName(), map.get(COLUMN_NAME));
        assertEquals(player.getNationality(), map.get(COLUMN_NATIONALITY));
        assertEquals(sDate, map.get(COLUMN_DATE_OF_BIRTH));
    }
}
