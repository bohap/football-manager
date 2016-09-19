package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 01.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LineupRepositoryTest {

    private LineupRepository repository;
    private UserRepository userRepository;

    private Calendar calendar = new GregorianCalendar(2016, 7, 1, 14, 15, 10);
    private Date date = calendar.getTime();

    private final int user1Id = 1;
    private User user1 = new User(user1Id, "User", "user@user1.com", "pass", date, date);
    private final int user2Id = 2;
    private User user2 = new User(user2Id, "User", "user@user2.com", "pass", date, date);

    private final int NUMBER_OF_LINEUPS = 3;
    private final int lineup1Id = 1;
    private Lineup lineup1 = new Lineup(lineup1Id, user1Id, date, date);
    private final int lineup2Id = 2;
    private Lineup lineup2 = new Lineup(lineup2Id, user1Id, date, date);
    private final int lineup3Id = 3;
    private Lineup lineup3 = new Lineup(lineup3Id, user2Id, date, date);
    private final int unExistingLineupId = 4;
    private Lineup unExistingLineup = new Lineup(unExistingLineupId, user2Id, date, date);
    private List<Lineup> user1Lineups = Arrays.asList(lineup1, lineup2);

    /**
     * Create a new instances of the repositories, open the connection and seed the tables.
     */
    @Before
    public void setup() {
        Base64Utils base64Utils = new Base64Utils();
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        userRepository = new UserRepository(context, dbHelper, base64Utils);
        repository = new LineupRepository(context, dbHelper);
        userRepository.open();
        repository.open();
        /* Seed the users table */
        userRepository.store(user1);
        userRepository.store(user2);
        /* Seed the lineups table */
        repository.store(lineup1);
        repository.store(lineup2);
        repository.store(lineup3);
    }

    /**
     * Close the database connection.
     */
    @After
    public void shutdown() {
        userRepository.close();
        repository.close();
    }

    /**
     * Assert that the lists are same.
     *
     * @param expectedList expected list of lineups
     * @param actualList actual list of lineups
     */
    private void assertList(List<Lineup> expectedList, List<Lineup> actualList) {
        assertEquals(expectedList.size(), actualList.size());
        int count = 0;
        for (Lineup expected : expectedList) {
            for (Lineup actual : actualList) {
                if (expected.equals(actual)) {
                    assertTrue(expected.same(actual));
                    count++;
                }
            }
        }
        assertEquals(expectedList.size(), count);
    }

    /**
     * Test that getAll method will return all lineups in the database.
     */
    @Test
    public void testGetAll() {
        List<Lineup> lineups = repository.getAll();
        this.assertList(Arrays.asList(lineup1, lineup2, lineup3), lineups);
    }

    /**
     * Test the get method will return a lineup when the id exists.
     */
    @Test
    public void testGet() {
        Lineup lineup = repository.get(lineup1Id);
        assertTrue(lineup1.same(lineup));
    }

    /**
     * Test tha get method will return a null when the id don't exists.
     */
    @Test
    public void testGetOnUnExistingId() {
        assertNull(repository.get(unExistingLineupId));
    }

    /**
     * Test that count method will return the number of lineup in the database,
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_LINEUPS, repository.count());
    }

    /**
     * Test that store method will store a new lineup in the database.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingLineup);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS + 1, repository.count());
        Lineup lineup = repository.get(unExistingLineupId);
        assertTrue(unExistingLineup.same(lineup));
    }

    /**
     * Test that the store method will return false when storing the
     * record in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(lineup1));
        assertEquals(NUMBER_OF_LINEUPS, repository.count());
    }

    /**
     * Test that onUpdateSuccess method will successfully onUpdateSuccess a lineup in the database.
     */
    @Test
    public void testUpdate() {
        calendar.add(Calendar.HOUR, 1);
        Date date = calendar.getTime();
        lineup2.setUpdatedAt(date);
        boolean result = repository.update(lineup2);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS, repository.count());
        Lineup lineup = repository.get(lineup2Id);
        assertTrue(lineup2.same(lineup));
    }

    /**
     * Test that the onUpdateSuccess method will return false when updating
     * the record in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingLineup));
        assertEquals(NUMBER_OF_LINEUPS, repository.count());
    }

    /**
     * Test that delete method will successfully delete a lineup from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(lineup3Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS - 1, repository.count());
        assertNull(repository.get(lineup3Id));
    }

    /**
     * Test that the delete method will return false when deleting the record
     * from the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingLineupId));
        assertEquals(NUMBER_OF_LINEUPS, repository.count());
    }

    /**
     * Test that getUserLineups method will return all lineups that the given user has created.
     */
    @Test
    public void testGetUserLineups() {
        List<Lineup> lineups = repository.getUserLineups(user1Id);
        this.assertList(user1Lineups, lineups);
    }
}
