package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Borce on 01.08.2016.
 */
public class LineupRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private LineupRepository repository;

    private int year = 2016, month = 8, day = 1, hour = 14, minute = 15, second = 10;
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
    private String sDate = String.format("%02d-%02d-%02d %02d:%02d:%02d",
            year, month, day, hour, minute, second);
    private String COLUMN_ID = "id";
    private String COLUMN_USER_ID = "user_id";
    private String COLUMNS_CREATED_AT = "created_at";
    private String COLUMN_UPDATED_AT = "updated_at";
    private Lineup lineup = new Lineup(1, 1, calendar.getTime(), calendar.getTime());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new LineupRepository(context, dbHelper);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.lineups_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.lineups_column_user_id)).thenReturn(COLUMN_USER_ID);
        when(context.getString(R.string.lineups_column_created_at)).thenReturn(COLUMNS_CREATED_AT);
        when(context.getString(R.string.lineups_column_updated_at)).thenReturn(COLUMN_UPDATED_AT);
    }

    /**
     * Mock the cursor to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))).thenReturn(lineup.getId());
        when(cursor.getColumnIndex(COLUMN_USER_ID)).thenReturn(1);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))).thenReturn(lineup.getUserId());
        when(cursor.getColumnIndex(COLUMNS_CREATED_AT)).thenReturn(2);
        when(cursor.getString(cursor.getColumnIndex(COLUMNS_CREATED_AT))).thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_UPDATED_AT)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT))).thenReturn(sDate);
    }

    /**
     * Test that the mapCursor method will correctly mapped the cursor values into Lineup.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        Lineup mapped = repository.mapCursor(cursor);
        assertNotNull(mapped);
        assertEquals(lineup.getId(), mapped.getId());
        assertEquals(lineup.getUser(), mapped.getUser());
        assertEquals(sDate, DateUtils.format(mapped.getCreatedAt()));
        assertEquals(sDate, DateUtils.format(mapped.getUpdatedAt()));
    }

    /**
     * Test that putValues method will correctly map the Lineup values into a HashMap.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(lineup);
        assertNotNull(map);
        assertEquals(lineup.getId().toString(), map.get(COLUMN_ID));
        assertEquals(String.valueOf(lineup.getUserId()), map.get(COLUMN_USER_ID));
        assertEquals(sDate, map.get(COLUMNS_CREATED_AT));
        assertEquals(sDate, map.get(COLUMN_UPDATED_AT));
    }
}
