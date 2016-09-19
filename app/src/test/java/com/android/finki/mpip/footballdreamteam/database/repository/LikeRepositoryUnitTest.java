package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Ignore;
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
 * Created by Borce on 05.08.2016.
 */
public class LikeRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private LikeRepository repository;

    private String COLUMN_USER_ID = "user_id";
    private String COLUMN_LINEUP_ID = "lineup_id";
    private String COLUMN_CREATED_AT = "created_at";
    private String USERS_TABLE_NAME = "users";
    private String USERS_COLUMN_NAME = "name";
    private Calendar calendar = new GregorianCalendar(2016, 7, 5, 9, 3, 10);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.format(date);
    private final int userId = 1;
    private User user = new User(userId, "User");
    private final int lineupId = 1;
    private Lineup lineup = new Lineup(lineupId, userId);
    private LineupLike like = new LineupLike(user, lineup, date);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new LikeRepository(context, dbHelper);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.lineup_likes_user_id)).thenReturn(COLUMN_USER_ID);
        when(context.getString(R.string.lineup_likes_lineup_id)).thenReturn(COLUMN_LINEUP_ID);
        when(context.getString(R.string.lineup_likes_created_at)).thenReturn(COLUMN_CREATED_AT);
        when(context.getString(R.string.users_table_name)).thenReturn(USERS_TABLE_NAME);
        when(context.getString(R.string.users_column_name)).thenReturn(USERS_COLUMN_NAME);
    }

    /**
     * Mock the context to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_USER_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))).thenReturn(userId);
        when(cursor.getColumnIndex(COLUMN_LINEUP_ID)).thenReturn(1);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID))).thenReturn(lineupId);
        when(cursor.getColumnIndex(COLUMN_CREATED_AT)).thenReturn(2);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT))).thenReturn(sDate);
        String column = String.format("%s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(column))).thenReturn(user.getName());
    }

    /**
     * Test that mapCursor method will correctly map the cursor into a new LineupLike.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        LineupLike mapped = repository.mapCursor(cursor);
        assertTrue(like.same(mapped));
        User mUser = mapped.getUser();
        assertTrue(user.same(mUser));
    }

    /**
     * Test that putValues method will correctly map the LineupLike into a HashMap.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(like);
        assertNotNull(map);
        assertEquals(String.valueOf(userId), map.get(COLUMN_USER_ID));
        assertEquals(String.valueOf(lineupId), map.get(COLUMN_LINEUP_ID));
        assertEquals(sDate, map.get(COLUMN_CREATED_AT));
    }
}
