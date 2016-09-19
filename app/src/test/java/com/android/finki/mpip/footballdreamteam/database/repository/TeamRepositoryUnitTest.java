package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Borce on 31.07.2016.
 */
public class TeamRepositoryUnitTest {

    @Mock
    private Context context;

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Cursor cursor;

    private TeamRepository repository;

    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private String COLUMN_SHORT_NAME = "short_name";
    private String COLUMN_SQUAD_MARKET_VALUE = "smv";

    private Team team = new Team(1, "Team", "TM", "200000$");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new TeamRepository(context, dbHelper);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.teams_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.teams_column_name)).thenReturn(COLUMN_NAME);
        when(context.getString(R.string.teams_column_short_name)).thenReturn(COLUMN_SHORT_NAME);
        when(context.getString(R.string.teams_column_squad_market_value))
                .thenReturn(COLUMN_SQUAD_MARKET_VALUE);
    }

    /**
     * Mock the cursor to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))).thenReturn(team.getId());
        when(cursor.getColumnIndex(COLUMN_NAME)).thenReturn(1);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NAME))).thenReturn(team.getName());
        when(cursor.getColumnIndex(COLUMN_SHORT_NAME)).thenReturn(2);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_SHORT_NAME)))
                .thenReturn(team.getShortName());
        when(cursor.getColumnIndex(COLUMN_SQUAD_MARKET_VALUE)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_SQUAD_MARKET_VALUE)))
                .thenReturn(team.getSquadMarketValue());
    }

    /**
     * Test that the cursor value are correctly mapped into a new Team.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        Team mapped = repository.mapCursor(cursor);
        assertTrue(team.same(mapped));
    }

    /**
     * Test that putValues method correctly maps the team values into a Map.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(team);
        assertNotNull(map);
        assertEquals(team.getId().toString(), map.get(COLUMN_ID));
        assertEquals(team.getName(), map.get(COLUMN_NAME));
        assertEquals(team.getShortName(), map.get(COLUMN_SHORT_NAME));
        assertEquals(team.getSquadMarketValue(), map.get(COLUMN_SQUAD_MARKET_VALUE));
    }
}