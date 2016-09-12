package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
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

import static org.junit.Assert.*;

/**
 * Created by Borce on 01.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CommentRepositoryTest {

    private CommentRepository repository;
    private UserRepository userRepository;
    private LineupRepository lineupRepository;

    private final int year = 2016, month = 8, day = 1, hour = 16, minute = 11, second = 10;
    private Calendar calendar = new GregorianCalendar(year, month - 1, day, hour, minute, second);

    private Calendar calendar1 = new GregorianCalendar(year, month - 1, day, hour + 4, minute, second);

    private final int NUMBER_OF_USER_1_COMMENTS = 2;
    private final int user1Id = 1;
    private User user1 = new User(user1Id, "User 1", "user@user-1.com",
            calendar1.getTime(), calendar.getTime(), null);
    private final int NUMBER_OF_USER_2_COMMENTS = 1;
    private final int user2Id = 2;
    private User user2 = new User(user2Id, "User 2", "user@user-2.com",
            calendar.getTime(), calendar.getTime(), null);

    private final int NUMBER_OF_LINEUP_1_COMMENTS = 2;
    private final int lineup1Id = 1;
    private Lineup lineup1 = new Lineup(lineup1Id, user1Id,
            calendar.getTime(), calendar.getTime());
    private final int NUMBER_OF_LINEUP_2_COMMENTS = 1;
    private final int lineup2Id = 2;
    private Lineup lineup2 = new Lineup(lineup2Id, user2Id,
            calendar.getTime(), calendar.getTime());

    private final int NUMBER_OF_COMMENTS = 3;
    private final int comment1Id = 1;
    private Comment comment1 = new Comment(comment1Id, user1Id, lineup1Id,
            "Comment", calendar.getTime(), calendar.getTime());
    private final int comment2Id = 2;
    private Comment comment2 = new Comment(comment2Id, user2Id, lineup1Id,
            "Comment", calendar.getTime(), calendar.getTime());
    private final int comment3Id = 3;
    private Comment comment3 = new Comment(comment3Id, user1Id, lineup2Id,
            "Comment", calendar.getTime(), calendar.getTime());
    private final int unExistingCommentId = 4;
    private Comment unExistingComment = new Comment(unExistingCommentId, user2Id, lineup2Id,
            "Comment", calendar.getTime(), calendar.getTime());

    /**
     * Create a new instances of the repositories, open the connections and seed the tables.
     */
    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        repository = new CommentRepository(context, dbHelper);
        userRepository = new UserRepository(context, dbHelper);
        lineupRepository = new LineupRepository(context, dbHelper);
        userRepository.open();
        lineupRepository.open();
        repository.open();
        userRepository.store(user1);
        userRepository.store(user2);
        lineupRepository.store(lineup1);
        lineupRepository.store(lineup2);
        repository.store(comment1);
        repository.store(comment2);
        repository.store(comment3);
    }

    /**
     * Close the database connections.
     */
    @After
    public void shutdown() {
        repository.close();
        userRepository.close();
        lineupRepository.close();
    }

    /**
     * Assert theat the given array of comments is valid.
     *
     * @param number expected number of comments
     * @param comments List of comments
     * @param checkUser whatever the comment user should be asserted
     */
    private void assertComments(int number, List<Comment> comments, boolean checkUser) {
        assertNotNull(comments);
        assertEquals(number, comments.size());
        int count = 0;
        for (Comment comment : comments) {
            if (comment.getId().equals(comment1Id)) {
                this.assertComment(comment1, comment, checkUser);
                count++;
            } else if (comment.getId().equals(comment2Id)) {
                this.assertComment(comment2, comment, checkUser);
                count++;
            } else if (comment.getId().equals(comment3Id)) {
                this.assertComment(comment3, comment, checkUser);
                count++;
            }
        }
        assertEquals(number, count);
    }

    /**
     * Assert that the given comment data is valid.
     *
     * @param compare actual comment
     * @param comment Comment to be compared
     * @param checkUser whatever the user should be asserted
     */
    private void assertComment(Comment compare, Comment comment, boolean checkUser) {
        assertNotNull(comment);
        assertEquals(comment.getId(), comment.getId());
        if (checkUser) {
            User user = comment.getUser();
            assertNotNull(user);
            assertEquals(compare.getUserId(), user.getId().intValue());
        }
    }

    /**
     * Test that getAll method will return all comments.
     */
    @Test
    public void testGetAll() {
        List<Comment> comments = repository.getAll();
        this.assertComments(NUMBER_OF_COMMENTS, comments, true);
    }

    /**
     * Test that get method will return the comment when the id exists.
     */
    @Test
    public void testGet() {
        Comment comment = repository.get(comment1Id);
        this.assertComment(comment1, comment, true);
    }

    /**
     * Test that get method will return null when the don't exists.
     */
    @Test
    public void testGetOnUnExistingId() {
        assertNull(repository.get(unExistingCommentId));
    }

    /**
     * Test that the count method will return the number of comment.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_COMMENTS, repository.count());
    }

    /**
     * Test that store method will create a new comment in the database.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingComment);
        assertTrue(result);
        assertEquals(NUMBER_OF_COMMENTS + 1, repository.count());
        Comment comment = repository.get(unExistingCommentId);
        this.assertComment(unExistingComment, comment, true);
    }

    /**
     * Test that the store method will return false when storing the record in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(comment1));
        assertEquals(NUMBER_OF_COMMENTS, repository.count());
    }

    /**
     * Test that onUpdateSuccess method will onUpdateSuccess the comment data.
     */
    @Test
    public void testUpdate() {
        String body = "Comment setChanged";
        comment1.setBody(body);
        boolean result = repository.update(comment1);
        assertTrue(result);
        assertEquals(NUMBER_OF_COMMENTS, repository.count());
        Comment comment = repository.get(comment1Id);
        this.assertComment(comment1, comment, false);
        assertEquals(body, comment.getBody());
    }

    /**
     * Test that onUpdateSuccess method will return false when updating hte record in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingComment));
        assertEquals(NUMBER_OF_COMMENTS, repository.count());
    }

    /**
     * Test that the delete method will delete the comment from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(comment1Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_COMMENTS - 1, repository.count());
        assertNull(repository.get(comment1Id));
    }

    /**
     * Test that delete method will return false when deleting the record from the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingCommentId));
        assertEquals(NUMBER_OF_COMMENTS, repository.count());
    }

    /**
     * Test that getUserComments will return all comments that the user has created.
     */
    @Test
    public void testGetUserComments() {
        List<Comment> comments = repository.getUserComments(user1Id);
        this.assertComments(NUMBER_OF_USER_1_COMMENTS, comments, false);
    }

    /**
     * Test that getLineupComments will return all comment that were wrote on the lineup.
     */
    @Test
    public void testGetLineupComments() {
        List<Comment> comments = repository.getLineupComments(lineup1Id);
        this.assertComments(NUMBER_OF_LINEUP_1_COMMENTS, comments, true);
    }
}
