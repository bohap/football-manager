package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.User;
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
 * Created by Borce on 01.08.2016.
 */
public class CommentRepositoryUnitTest {

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Context context;

    @Mock
    private Cursor cursor;

    private CommentRepository repository;

    private Calendar calendar = new GregorianCalendar(2016, 7, 1, 15, 30, 0);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.format(calendar.getTime());
    private String COLUMN_ID = "id";
    private String COLUMN_USER_ID = "user_id";
    private String COLUMN_LINEUP_ID = "lineup_id";
    private String COLUMN_BODY = "body";
    private String COLUMN_CREATED_AT = "created_at";
    private String COLUMN_UPDATED_AT = "updated_at";
    private String USERS_TABLE_NAME = "users";
    private String USERS_COLUMN_NAME = "name";
    private final int userId = 1;
    private User user = new User(userId, "User");
    private Comment comment = new Comment(1, userId, 1, "Body", date, date);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new CommentRepository(context, dbHelper);
    }

    /**
     * Mock the Context to return specific values on method calls.
     */
    private void initContext() {
        when(context.getString(R.string.lineups_comments_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.lineups_comments_column_user_id))
                .thenReturn(COLUMN_USER_ID);
        when(context.getString(R.string.lineups_comments_column_lineup_id))
                .thenReturn(COLUMN_LINEUP_ID);
        when(context.getString(R.string.lineups_comments_column_body)).thenReturn(COLUMN_BODY);
        when(context.getString(R.string.lineups_comments_column_created_at))
                .thenReturn(COLUMN_CREATED_AT);
        when(context.getString(R.string.lineups_comments_column_updated_at))
                .thenReturn(COLUMN_UPDATED_AT);
        when(context.getString(R.string.users_table_name)).thenReturn(USERS_TABLE_NAME);
        when(context.getString(R.string.users_column_id)).thenReturn("id");
        when(context.getString(R.string.users_column_name)).thenReturn(USERS_COLUMN_NAME);
    }

    /**
     * Mock the Cursor to return specific values on method calls.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
                .thenReturn(comment.getId());
        when(cursor.getColumnIndex(COLUMN_USER_ID)).thenReturn(1);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)))
                .thenReturn(comment.getUserId());
        when(cursor.getColumnIndex(COLUMN_LINEUP_ID)).thenReturn(2);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_LINEUP_ID)))
                .thenReturn(comment.getLineupId());
        when(cursor.getColumnIndex(COLUMN_BODY)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)))
                .thenReturn(comment.getBody());
        when(cursor.getColumnIndex(COLUMN_CREATED_AT)).thenReturn(4);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)))
                .thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_UPDATED_AT)).thenReturn(5);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT)))
                .thenReturn(sDate);
        String column = String.format("%s_%s", USERS_TABLE_NAME, USERS_COLUMN_NAME);
        when(cursor.getColumnIndex(column)).thenReturn(6);
        when(cursor.getString(cursor.getColumnIndex(column)))
                .thenReturn(user.getName());
    }

    /**
     * Test that mapCursor method will successfully map cursor values into a new Comment.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        Comment mapped = repository.mapCursor(cursor);
        assertNotNull(mapped);
        assertTrue(comment.same(mapped));
        User mUser = mapped.getUser();
        assertTrue(user.same(mUser));
    }

    /**
     * Test that putValues method will successfully put comment values into a HashMap.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(comment);
        assertNotNull(map);
        assertEquals(comment.getId().toString(), map.get(COLUMN_ID));
        assertEquals(String.valueOf(comment.getUserId()), map.get(COLUMN_USER_ID));
        assertEquals(String.valueOf(comment.getLineupId()), map.get(COLUMN_LINEUP_ID));
        assertEquals(comment.getBody(), map.get(COLUMN_BODY));
        assertEquals(sDate, map.get(COLUMN_CREATED_AT));
        assertEquals(sDate, map.get(COLUMN_UPDATED_AT));
    }
}
