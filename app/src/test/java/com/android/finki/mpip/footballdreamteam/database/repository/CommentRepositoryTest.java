package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Comment;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 01.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class CommentRepositoryTest {

    private CommentRepository repository;
    private UserRepository userRepository;
    private LineupRepository lineupRepository;

    private Calendar calendar = new GregorianCalendar(2016, 8, 1, 16, 11, 10);
    private Date date = calendar.getTime();
    private final int user1Id = 1;
    private User user1 = new User(user1Id, "User 1", "user@user-1.com", "password", date, date);
    private final int user2Id = 2;
    private User user2 = new User(user2Id, "User 2", "user@user-2.com", "password", date, date);
    private final int lineup1Id = 1;
    private Lineup lineup1 = new Lineup(lineup1Id, user1Id, date, date);
    private final int lineup2Id = 2;
    private Lineup lineup2 = new Lineup(lineup2Id, user2Id, date, date);
    private final int NUMBER_OF_COMMENTS = 3;
    private final int comment1Id = 1;
    private Comment comment1 = new Comment(comment1Id, user1Id, lineup1Id, "Comment", date, date);
    private final int comment2Id = 2;
    private Comment comment2 = new Comment(comment2Id, user2Id, lineup1Id, "Comment", date, date);
    private final int comment3Id = 3;
    private Comment comment3 = new Comment(comment3Id, user1Id, lineup2Id, "Comment", date, date);
    private final int unExistingCommentId = 4;
    private Comment unExistingComment =
            new Comment(unExistingCommentId, user2Id, lineup2Id, "Comment", date, date);
    private List<Comment> user1Comments = Arrays.asList(comment1, comment3);
    private List<Comment> lineup1Comments = Arrays.asList(comment1, comment2);

    /**
     * Create a new instances of the repositories, open the connections and seed the tables.
     */
    @Before
    public void setup() {
        Base64Utils base64Utils = new Base64Utils();
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        repository = new CommentRepository(context, dbHelper);
        userRepository = new UserRepository(context, dbHelper, base64Utils);
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
     * @param expectedList expected list of comments
     * @param actualList   actual list of comments
     * @param checkUser    whatever the comment user should be asserted
     */
    private void assertList(List<Comment> expectedList, List<Comment> actualList,
                            boolean checkUser) {
        assertEquals(expectedList.size(), actualList.size());
        int count = 0;
        for (Comment expected : expectedList) {
            for (Comment actual : actualList) {
                if (expected.equals(actual)) {
                    this.assertComment(expected, actual, checkUser);
                    count++;
                }
            }
        }
        assertEquals(expectedList.size(), count);
    }

    /**
     * Assert that the given comment data is valid.
     *
     * @param compare   actual comment
     * @param comment   Comment to be compared
     * @param checkUser whatever the user should be asserted
     */
    private void assertComment(Comment compare, Comment comment, boolean checkUser) {
        assertTrue(comment.same(comment));
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
        this.assertList(Arrays.asList(comment1, comment2, comment3), comments, true);
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
        this.assertList(user1Comments, comments, false);
    }

    /**
     * Test that getLineupComments will return all comment that were wrote on the lineup.
     */
    @Test
    public void testGetLineupComments() {
        List<Comment> comments = repository.getLineupComments(lineup1Id);
        this.assertList(lineup1Comments, comments, true);
    }
}
