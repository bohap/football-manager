package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.CommentRepository;
import com.android.finki.mpip.footballdreamteam.exception.CommentException;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Comment;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Borce on 02.08.2016.
 */
public class CommentDBServiceUnitTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private UserDBService userDBService;

    @Mock
    private LineupDBService lineupDBService;

    @Mock
    private CommentDBService service;

    private final int userId = 1;
    private User user = new User(userId, "User", "user@user.co", null, null, null);
    private final int lineupId = 1;
    private Lineup lineup = new Lineup(lineupId, userId, null, null);
    private final int commentId = 1;
    private Comment comment = new Comment(commentId, userId, lineupId, "Comment body", null, null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new CommentDBService(repository, userDBService, lineupDBService);
    }

    /**
     * Test that he exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(commentId)).thenReturn(comment);
        assertTrue(service.exists(commentId));
    }

    /**
     * Test that he exists method returns false on un existing id.
     */
    @Test
    public void testNotExists() {
        when(repository.get(commentId)).thenReturn(null);
        assertFalse(service.exists(comment));
    }

    /**
     * Test the behavior on exists method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsOnNull() {
        service.exists(null);
    }

    /**
     * Test the behavior on store method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNull() {
        service.store(null);
    }

    /**
     * Test the behavior on store method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnInvalidId() {
        service.store(new Comment(0, 1, 1, "", null, null));
    }

    /**
     * Test the behavior on store method called with null body.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullBody() {
        service.store(new Comment(1, 1, 1, null, null, null));
    }

    /**
     * Test the behavior on store method called with existing id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(commentId)).thenReturn(comment);
        service.store(comment);
    }

    /**
     * Test the behavior on store method called with un existing user id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingUserId() {
        when(repository.get(commentId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(false);
        service.store(comment);
    }

    /**
     * Test the behavior on store method called with un existing lineup id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingLineupId() {
        when(repository.get(commentId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(false);
        service.store(comment);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = CommentException.class)
    public void testFailedStore() {
        when(repository.get(commentId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(repository.store(comment)).thenReturn(false);
        service.store(comment);
    }

    /**
     * Test the behavior on store method when storing the record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(commentId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(repository.store(comment)).thenReturn(true);
        Comment stored = service.store(comment);
        assertNotNull(stored);
        assertEquals(commentId, stored.getId().intValue());
        assertEquals(userId, stored.getUserId());
        assertEquals(lineupId, stored.getLineupId());
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNull() {
        service.update(null);
    }

    /**
     * Test the behavior on onUpdateSuccess method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnInvalidId() {
        service.update(new Comment(0, 1, 1, "", null, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null body.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullBody() {
        service.update(new Comment(1, 1, 1, null, null, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingId() {
        when(repository.get(commentId)).thenReturn(null);
        service.update(comment);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database failed.
     */
    @Test(expected = CommentException.class)
    public void testFailedUpdate() {
        when(repository.get(commentId)).thenReturn(comment);
        when(repository.update(comment)).thenReturn(false);
        service.update(comment);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(commentId)).thenReturn(comment);
        when(repository.update(comment)).thenReturn(true);
        Comment updated = service.update(comment);
        assertNotNull(updated);
        assertEquals(commentId, updated.getId().intValue());
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(commentId)).thenReturn(null);
        service.delete(commentId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database failed.
     */
    @Test(expected = CommentException.class)
    public void testFailedDelete() {
        when(repository.get(commentId)).thenReturn(comment);
        when(repository.delete(commentId)).thenReturn(false);
        service.delete(commentId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(commentId)).thenReturn(comment);
        when(repository.delete(commentId)).thenReturn(true);
        service.delete(commentId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }

    /**
     * Test tha behavior on getUserComments called with null
     * param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUserCommentsOnNull() {
        service.getUserComments(null);
    }

    /**
     * Test the behavior on getLineupComments called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLineupCommentsOnNull() {
        service.getLineupComments(null);
    }
}