package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 31.07.2016.
 */
public class PositionRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private PositionRepository repository;

    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private Position position = new Position(1, "Position");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new PositionRepository(context, dbHelper);
        this.initContext();
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.positions_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.positions_column_name)).thenReturn(COLUMN_NAME);
    }

    /**
     * Mock the cursor to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))).thenReturn(position.getId());
        when(cursor.getColumnIndex(COLUMN_NAME)).thenReturn(1);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NAME))).thenReturn(position.getName());
    }

    /**
     * Test that the mapCursor method will correctly map the values into a new Position
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        Position mapped = repository.mapCursor(cursor);
        assertTrue(position.same(mapped));
    }

    /**
     * Test that the putValues will correctly map the position into a HaspMap.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(position);
        assertNotNull(map);
        assertEquals(position.getId().toString(), map.get(COLUMN_ID));
        assertEquals(position.getName(), map.get(COLUMN_NAME));
    }
}
