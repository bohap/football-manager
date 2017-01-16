package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
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
 * Created by Borce on 05.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LikeRepositoryTest {

    private LikeRepository repository;
    private UserRepository userRepository;
    private LineupRepository lineupRepository;

    private Calendar calendar = new GregorianCalendar(2016, 7, 5, 9, 20, 0);
    private Date date = calendar.getTime();
    private final int user1Id = 124;
    private User user1 = new User(user1Id, "User 1", "user@user.com", "password", date, date);
    private final int user2Id = 45;
    private User user2 = new User(user2Id, "User 2", "user2@user.com", "password", date, date);
    private final int lineup1Id = 324;
    private Lineup lineup1 = new Lineup(lineup1Id, user1Id);
    private final int lineup2Id = 24;
    private Lineup lineup2 = new Lineup(lineup2Id, user1Id);
    private final int NUMBER_OF_LIKES = 3;
    private LineupLike like1 = new LineupLike(user1, lineup1, date);
    private LineupLike like2 = new LineupLike(user1, lineup2, date);
    private LineupLike like3 = new LineupLike(user2, lineup2, date);
    private LineupLike unExistingLike = new LineupLike(user2, lineup1, calendar.getTime());
    private List<LineupLike> user1likes = Arrays.asList(like1, like2);
    private List<LineupLike> line2Likes = Arrays.asList(like2, like3);

    /**
     * Initialize the repositories, open the connection and seed the tables.
     */
    @Before
    public void setup() {
        Base64Utils base64Utils = new Base64Utils();
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        userRepository = new UserRepository(context, dbHelper, base64Utils);
        lineupRepository = new LineupRepository(context, dbHelper);
        repository = new LikeRepository(context, dbHelper);
        /* Open the connections */
        userRepository.open();
        lineupRepository.open();
        repository.open();
        /* Seed the tables */
        userRepository.store(user1);
        userRepository.store(user2);
        lineupRepository.store(lineup1);
        lineupRepository.store(lineup2);
        repository.store(like1);
        repository.store(like2);
        repository.store(like3);
    }

    /**
     * Close the database connection.
     */
    @After
    public void shutdown() {
        repository.close();
        lineupRepository.close();
        userRepository.close();
    }

    /**
     * Assert that the list are same.
     *
     * @param expectedList expected list of likes
     * @param actualList   actual list of likes
     * @param checkUser    whatever the like user should be checked
     */
    private void assertList(List<LineupLike> expectedList, List<LineupLike> actualList,
                            boolean checkUser) {
        assertEquals(expectedList.size(), actualList.size());
        int count = 0;
        for (LineupLike expected : expectedList) {
            for (LineupLike actual : actualList) {
                if (expected.equals(actual)) {
                    this.assertLike(expected, actual, checkUser);
                    count++;
                }
            }
        }
        assertEquals(expectedList.size(), count);
    }

    /**
     * Assert that the given like data is valid.
     *
     * @param compare   actual like
     * @param like      like to be compared
     * @param checkUser whatever the user should be checked
     */
    private void assertLike(LineupLike compare, LineupLike like, boolean checkUser) {
        assertTrue(compare.same(like));
        if (checkUser) {
            User user = like.getUser();
            assertNotNull(user);
            assertEquals(compare.getUserId(), user.getId().intValue());
            assertEquals(compare.getUser().getName(), user.getName());
        }
    }

    /**
     * Test that getAll method will return all likes.
     */
    @Test
    public void testGetAll() {
        List<LineupLike> likes = repository.getAll();
        this.assertList(Arrays.asList(like1, like2, like3), likes, true);
    }

    /**
     * Test the get method will return a existing like.
     */
    @Test
    public void testGet() {
        LineupLike like = repository.get(like1.getUserId(), like1.getLineupId());
        this.assertLike(like1, like, true);
    }

    /**
     * Test that get method will return null when the like don;t exists.
     */
    @Test
    public void testGetOnUnExistingLike() {
        assertNull(repository.get(unExistingLike.getUserId(), unExistingLike.getLineupId()));
    }

    /**
     * Test that the count method will return the number of likes.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_LIKES, repository.count());
    }

    /**
     * Test that the store method will create a new like for the lineup from the user.
     */
    @Test
    public void testStore() {
        assertTrue(repository.store(unExistingLike));
        assertEquals(NUMBER_OF_LIKES + 1, repository.count());
        LineupLike like = repository.get(unExistingLike.getUserId(), unExistingLike.getLineupId());
        this.assertLike(unExistingLike, like, true);
    }

    /**
     * Test that store method will return false when inserting the result in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(like1));
        assertEquals(NUMBER_OF_LIKES, repository.count());
    }

    /**
     * Test that delete method will delete the user like from the lineup.
     */
    @Test
    public void testDelete() {
        int userId = like1.getUserId();
        int lineupId = like1.getLineupId();
        assertTrue(repository.delete(userId, lineupId));
        assertEquals(NUMBER_OF_LIKES - 1, repository.count());
        assertNull(repository.get(userId, lineupId));
    }

    /**
     * Test that delete method will return false when deleting the like from the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingLike.getUserId(), unExistingLike.getLineupId()));
        assertEquals(NUMBER_OF_LIKES, repository.count());
    }

    /**
     * Test that getLineupLikes will return all likes that are for the lineup.
     */
    @Test
    public void getLineupLikes() {
        List<LineupLike> likes = repository.getLineupLikes(lineup2Id);
        this.assertList(line2Likes, likes, true);
    }

    /**
     * Test that getUserLikes will return all likes that the user has made.
     */
    @Test
    public void getUserLikes() {
        List<LineupLike> likes = repository.getUserLikes(user1Id);
        this.assertList(user1likes, likes, false);
    }
}