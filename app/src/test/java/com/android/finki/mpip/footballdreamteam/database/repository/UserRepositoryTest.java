package com.android.finki.mpip.footballdreamteam.database.repository;

import android.app.Application;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Borce on 30.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class UserRepositoryTest {

    private UserRepository repository;

    private int year = 2016, month = 7, day = 31, hour = 14, minute = 50, second = 10;
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);

    private final int NUMBER_OF_USERS = 3;
    private final int user1Id = 1;
    private User user1 = new User(user1Id, "User 1", "user@user-1.com", "pass",
            calendar.getTime(), calendar.getTime());
    private final int user2Id = 2;
    private User user2 = new User(user2Id, "User 1", "user@user-2.com", "pass",
            calendar.getTime(), calendar.getTime());
    private final int user3Id = 3;
    private User user3 = new User(user3Id, "User 1", "user@user-3.com", "pass",
            calendar.getTime(), calendar.getTime());
    private final int unExistingUserId = 4;
    private User unExistingUser = new User(unExistingUserId, "User 4", "user@user-4.com", "pass",
            calendar.getTime(), calendar.getTime());

    /**
     * Create a new instance of the UserRepository, open the connection and seed the table.
     */
    @Before
    public void setup() {
        Application application = RuntimeEnvironment.application;
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(application.getBaseContext());
        repository = new UserRepository(application.getBaseContext(), dbHelper);
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
     * Assert that the given user data is valid.
     *
     * @param compare actual user
     * @param user User to be compared
     */
    private void assertUser(User compare, User user) {
        assertNotNull(user);
        assertEquals(compare.getId(), user.getId());
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
        for (User user : users) {
            if (user.getId().equals(user1Id) || user.getId().equals(user2Id)
                    || user.getId().equals(user3Id)) {
                count++;
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
        this.assertUser(user1, user);
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
        String email = user1.getEmail();
        User user = repository.getByEmail(email);
        this.assertUser(user1, user);
        assertEquals(email, user.getEmail());
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
        this.assertUser(unExistingUser, user);
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
        user1.setEmail(email);
        boolean result = repository.update(user1);
        assertTrue(result);
        assertEquals(NUMBER_OF_USERS, repository.count());
        User user = repository.get(user1Id);
        this.assertUser(user1, user);
        assertEquals(email, user.getEmail());
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