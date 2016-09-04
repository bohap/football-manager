package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.Logger;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Borce on 31.07.2016.
 */
public class UserRepositoryUnitTest {

    @Mock
    private Context context;

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Cursor cursor;

    @Mock
    private Resources resources;

    private UserRepository repository;

    private int year = 2016, month = 7, day = 31, hour = 16, minute = 45, second = 30 ;
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);
    private String sDate = DateUtils.format(calendar.getTime());

    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private String COLUMN_EMAIL = "email";
    private String COLUMN_CREATED_AT = "created_at";
    private String COLUMN_UPDATED_AT = "updated_at";
    private String COLUMN_JWT_TOKEN = "jwt_token";
    private User user = new User(1, "User", "user@user.com", calendar.getTime(),
            calendar.getTime(), null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        repository = new UserRepository(context, dbHelper);
    }

    /**
     * Mock the context to return specific values on called methods.
     */
    private void initContext() {
        when(context.getString(R.string.users_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.users_column_name)).thenReturn(COLUMN_NAME);
        when(context.getString(R.string.users_column_email)).thenReturn(COLUMN_EMAIL);
        when(context.getString(R.string.users_column_created_at)).thenReturn(COLUMN_CREATED_AT);
        when(context.getString(R.string.users_column_updated_at)).thenReturn(COLUMN_UPDATED_AT);
        when(context.getString(R.string.users_column_jwt_token)).thenReturn(COLUMN_JWT_TOKEN);
    }

    /**
     * Mock the cursor to return specific values on called methods.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID))).thenReturn(user.getId());
        when(cursor.getColumnIndex(COLUMN_NAME)).thenReturn(1);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NAME))).thenReturn(user.getName());
        when(cursor.getColumnIndex(COLUMN_EMAIL)).thenReturn(2);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))).thenReturn(user.getEmail());
        when(cursor.getColumnIndex(COLUMN_CREATED_AT)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT))).thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_UPDATED_AT)).thenReturn(4);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT))).thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_JWT_TOKEN)).thenReturn(5);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_JWT_TOKEN))).thenReturn(null);
    }

    /**
     * Test that the cursor values is correctly mapped into a new user.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        User mapped = repository.mapCursor(cursor);
        assertNotNull(mapped);
        assertEquals(user.getId(), mapped.getId());
        assertEquals(user.getName(), mapped.getName());
        assertEquals(user.getEmail(), mapped.getEmail());
        assertEquals(sDate, DateUtils.format(mapped.getCreatedAt()));
        assertEquals(sDate, DateUtils.format(mapped.getUpdatedAt()));
        assertEquals(user.getJwtToken(), mapped.getJwtToken());
    }

    /**
     * Test that putValues method correctly maps the User values onto a Map.
     */
    @Test
    public void testPutValues() {
        Map<String, String> map = repository.putValues(user);
        assertNotNull(map);
        assertEquals(user.getId().toString(), map.get(COLUMN_ID));
        assertEquals(user.getName(), map.get(COLUMN_NAME));
        assertEquals(user.getEmail(), map.get(COLUMN_EMAIL));
        assertEquals(sDate, map.get(COLUMN_CREATED_AT));
        assertEquals(sDate, map.get(COLUMN_UPDATED_AT));
        assertEquals(user.getJwtToken(), map.get(COLUMN_JWT_TOKEN));
    }
}