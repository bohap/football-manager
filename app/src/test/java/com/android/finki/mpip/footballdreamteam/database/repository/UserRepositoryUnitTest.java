package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;
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
public class UserRepositoryUnitTest {

    @Mock
    private Context context;

    @Mock
    private MainSQLiteOpenHelper dbHelper;

    @Mock
    private Cursor cursor;

    @Mock
    private Base64Utils base64Utils;

    @Mock
    private Resources resources;

    private UserRepository repository;

    private Calendar calendar = new GregorianCalendar(2016, 6, 31, 16, 45, 30);
    private Date date = calendar.getTime();
    private String sDate = DateUtils.format(date);
    private final String originalPassword = "original";
    private final String encryptedPassword = "encrypted";

    private String COLUMN_ID = "id";
    private String COLUMN_NAME = "name";
    private String COLUMN_EMAIL = "email";
    private String COLUMN_PASSWORD = "originalPassword";
    private String COLUMN_CREATED_AT = "created_at";
    private String COLUMN_UPDATED_AT = "updated_at";
    private User user = new User(1, "User", "user@user.com", originalPassword, date, date);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initContext();
        when(base64Utils.encode(originalPassword)).thenReturn(encryptedPassword);
        when(base64Utils.decode(encryptedPassword)).thenReturn(originalPassword);
        repository = new UserRepository(context, dbHelper, base64Utils);
    }

    /**
     * Mock the context to return specific values on called methods.
     */
    private void initContext() {
        when(context.getString(R.string.users_column_id)).thenReturn(COLUMN_ID);
        when(context.getString(R.string.users_column_name)).thenReturn(COLUMN_NAME);
        when(context.getString(R.string.users_column_email)).thenReturn(COLUMN_EMAIL);
        when(context.getString(R.string.users_column_password)).thenReturn(COLUMN_PASSWORD);
        when(context.getString(R.string.users_column_created_at)).thenReturn(COLUMN_CREATED_AT);
        when(context.getString(R.string.users_column_updated_at)).thenReturn(COLUMN_UPDATED_AT);
    }

    /**
     * Mock the cursor to return specific values on called methods.
     */
    private void initCursor() {
        when(cursor.getColumnIndex(COLUMN_ID)).thenReturn(0);
        when(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)))
                .thenReturn(user.getId());
        when(cursor.getColumnIndex(COLUMN_NAME)).thenReturn(1);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)))
                .thenReturn(user.getName());
        when(cursor.getColumnIndex(COLUMN_EMAIL)).thenReturn(2);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)))
                .thenReturn(user.getEmail());
        when(cursor.getColumnIndex(COLUMN_PASSWORD)).thenReturn(3);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)))
                .thenReturn(encryptedPassword);
        when(cursor.getColumnIndex(COLUMN_CREATED_AT)).thenReturn(4);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)))
                .thenReturn(sDate);
        when(cursor.getColumnIndex(COLUMN_UPDATED_AT)).thenReturn(5);
        when(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT)))
                .thenReturn(sDate);
    }

    /**
     * Test that the cursor values is correctly mapped into a new user.
     */
    @Test
    public void testMapCursor() {
        this.initCursor();
        User mapped = repository.mapCursor(cursor);
        assertTrue(user.same(mapped));
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
        assertEquals(encryptedPassword, map.get(COLUMN_PASSWORD));
        assertEquals(sDate, map.get(COLUMN_CREATED_AT));
        assertEquals(sDate, map.get(COLUMN_UPDATED_AT));
    }
}