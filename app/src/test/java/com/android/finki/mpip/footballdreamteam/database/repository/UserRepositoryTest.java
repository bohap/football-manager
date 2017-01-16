package com.android.finki.mpip.footballdreamteam.database.repository;

import android.app.Application;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 30.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class UserRepositoryTest {

    private UserRepository repository;
    private Base64Utils base64Utils = new Base64Utils();

    private Calendar calendar = new GregorianCalendar(2016, 6, 31, 14, 50, 10);
    private Date date = calendar.getTime();
    private final int NUMBER_OF_USERS = 3;
    private final int user1Id = 1;
    private User user1 = new User(user1Id, "User 1", "user@user-1.com", "password", date, date);
    private final int user2Id = 2;
    private User user2 = new User(user2Id, "User 1", "user@user-2.com", "password", date, date);
    private final int user3Id = 3;
    private User user3 = new User(user3Id, "User 1", "user@user-3.com", "password", date, date);
    private final int unExistingUserId = 4;
    private User unExistingUser =
            new User(unExistingUserId, "User 4", "user@user-4.com", "password", date, date);

    /**
     * Create a new instance of the UserRepository, open the connection and seed the table.
     */
    @Before
    public void setup() {
        Application application = RuntimeEnvironment.application;
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(application.getBaseContext());
        repository = new UserRepository(application.getBaseContext(), dbHelper, base64Utils);
        repository.open();
        repository.store(user1);
        repository.store(user2);
        repository.store(user3);
    }

    /**
     * Close the database connection.
     */
    @After
    public void shutdown() {
        repository.close();
    }

    /**
     * Test that the getAll method returns all users in the database.
     */
    @Test
    public void testGetAll() {
        List<User> users = repository.getAll();
        assertNotNull(users);
        assertEquals(NUMBER_OF_USERS, users.size());
        int count = 0;
        for (User expected : Arrays.asList(user1, user2, user3)) {
            for (User actual : users) {
                if (expected.equals(actual)) {
                    assertTrue(expected.same(actual));
                    count++;
                }
            }
        }
        assertEquals(NUMBER_OF_USERS, count);
    }

    /**
     * Test that the get method works on existing user id.
     */
    @Test
    public void testGet() {
        User user = repository.get(user1Id);
        assertTrue(user1.same(user));
    }

    /**
     * Test that get method will return null on un existing user id.
     */
    @Test
    public void testGetOnUnExistingId() {
        assertNull(repository.get(unExistingUserId));
    }

    /**
     * Test the the getByEmail works when exists user with the given email.
     */
    @Test
    public void testGetByEmail() {
        String email = user2.getEmail();
        User user = repository.getByEmail(email);
        assertTrue(user2.same(user));
    }

    /**
     * Test that the getByEmail returns null when don't exists user with the given email.
     */
    @Test
    public void testGetByEmailOnUnExistingEmail() {
        String email = unExistingUser.getEmail();
        assertNull(repository.getByEmail(email));
    }

    /**
     * Test that the count returns the correct number of users.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_USERS, repository.count());
    }

    /**
     * Test that the store create a new user in the database.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingUser);
        assertTrue(result);
        assertEquals(NUMBER_OF_USERS + 1, repository.count());
        User user = repository.get(unExistingUserId);
        assertTrue(unExistingUser.same(user));
    }

    /**
     * Test that store method returns false when storing the record int he database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(user1));
        assertEquals(NUMBER_OF_USERS, repository.count());
    }

    /**
     * Test that the onUpdateSuccess method onUpdateSuccess the user data in the database.
     */
    @Test
    public void testUpdate() {
        String email = unExistingUser.getEmail();
        user3.setEmail(email);
        boolean result = repository.update(user3);
        assertTrue(result);
        assertEquals(NUMBER_OF_USERS, repository.count());
        User user = repository.get(user3Id);
        assertTrue(user3.same(user));
    }

    /**
     * Test that onUpdateSuccess method returns false when updating the record in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingUser));
        assertEquals(NUMBER_OF_USERS, repository.count());
    }

    /**
     * Test that the delete method removes the user from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(user1Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_USERS - 1, repository.count());
        assertNull(repository.get(user1Id));
    }

    /**
     * Test that the delete method will return false when deleting the record in the
     * database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingUserId));
        assertEquals(NUMBER_OF_USERS, repository.count());
    }
}