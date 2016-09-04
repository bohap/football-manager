package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Calendar;
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

    private final int year = 2016, month = 8, day = 5, hour = 9, minute = 20, second = 0;
    private Calendar calendar = new GregorianCalendar(year, month, day, hour, minute, second);

    private final int NUMBER_OF_USER_1_LIKES = 2;
    private final int user1Id = 124;
    private User user1 = new User(user1Id, "User 1");
    private final int NUMBER_OF_USER_2_LIKES = 1;
    private final int user2Id = 45;
    private User user2 = new User(user2Id, "User 2");

    private final int NUMBER_OF_LINEUP_1_LIKES = 1;
    private final int lineup1Id = 324;
    private Lineup lineup1 = new Lineup(lineup1Id, user1Id);
    private final int NUMBER_OF_LINEUP_2_LIKES = 2;
    private final int lineup2Id = 24;
    private Lineup lineup2 = new Lineup(lineup2Id, user1Id);

    private final int NUMBER_OF_LIKES = 3;
    private LineupLike like1 = new LineupLike(user1, lineup1, calendar.getTime());
    private LineupLike like2 = new LineupLike(user1, lineup2, calendar.getTime());
    private LineupLike like3 = new LineupLike(user2, lineup2, calendar.getTime());
    private LineupLike unExistingLike = new LineupLike(user2, lineup1, calendar.getTime());

    /**
     * Initialize the repositories, open the connection and seed the tables.
     */
    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        userRepository = new UserRepository(context, dbHelper);
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
     * Assert that the given List of likes is valid.
     *
     * @param number    expected number of likes
     * @param likes     List of LineupLike
     * @param checkUser whatever the like user should be checked
     */
    private void assertLikes(int number, List<LineupLike> likes, boolean checkUser) {
        assertNotNull(likes);
        assertEquals(number, likes.size());
        int count = 0;
        for (LineupLike like : likes) {
            int userId = like.getUserId();
            int lineupId = like.getLineupId();
            if (like1.getUserId() == userId && like1.getLineupId() == lineupId) {
                this.assertLike(like1, like, checkUser);
                count++;
            } else if (like2.getUserId() == userId && like2.getLineupId() == lineupId) {
                this.assertLike(like2, like, checkUser);
                count++;
            } else if (like3.getUserId() == userId && like3.getLineupId() == lineupId) {
                this.assertLike(like3, like, checkUser);
                count++;
            }
        }
        assertEquals(number, count);
    }

    /**
     * Assert that the given like data is valid.
     *
     * @param compare   actual like
     * @param like      like to be compared
     * @param checkUser whatever the user should be checked
     */
    private void assertLike(LineupLike compare, LineupLike like, boolean checkUser) {
        assertNotNull(like);
        assertEquals(compare.getLineupId(), like.getLineupId());
        assertEquals(compare.getUserId(), like.getUserId());
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
        this.assertLikes(NUMBER_OF_LIKES, likes, true);
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
        this.assertLikes(NUMBER_OF_LINEUP_2_LIKES, likes, true);
    }

    /**
     * Test that getUserLikes will return all likes that the user has made.
     */
    @Test
    public void getUserLikes() {
        List<LineupLike> likes = repository.getUserLikes(user1Id);
        this.assertLikes(NUMBER_OF_USER_1_LIKES, likes, false);
    }
}