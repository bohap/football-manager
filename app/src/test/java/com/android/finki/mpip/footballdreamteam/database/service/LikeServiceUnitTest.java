package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LikeRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LikeException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.LineupLike;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 05.08.2016.
 */
public class LikeServiceUnitTest {

    @Mock
    private LikeRepository repository;

    @Mock
    private UserDBService userDBService;

    @Mock
    private LineupDBService lineupDBService;

    private LikeDBService service;

    private final int userId = 64;
    private final int lineupId = 25;
    private LineupLike like = new LineupLike(userId, lineupId);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new LikeDBService(repository, userDBService, lineupDBService);
    }

    /**
     * Test that exists method return true when the like exists.
     */
    @Test
    public void testExists() {
        when(repository.get(userId, lineupId)).thenReturn(like);
        assertTrue(service.exists(userId, lineupId));
    }

    /**
     * Test that exists method will return false when the like don;t exists.
     */
    @Test
    public void testNotExists() {
        when(repository.get(userId, lineupId)).thenReturn(null);
        assertFalse(service.exists(userId, lineupId));
    }

    /**
     * Test the behavior on exists method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsOnNull() {
        service.exists(null);
    }

    /**
     * Test the behavior on store method called with null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnUnNull() {
        service.store(null);
    }

    /**
     * Test the behavior on store method called un existing user id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingUserId() {
        when(userDBService.exists(userId)).thenReturn(false);
        service.store(like);
    }

    /**
     * Test the behavior on store method called un existing lineup id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingLineupId() {
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(false);
        service.store(like);
    }

    /**
     * Test the behavior on store method called with existing primary key.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnUnExistingPrimaryKey() {
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(repository.get(userId, lineupId)).thenReturn(like);
        service.store(like);
    }

    /**
     * Test the behavior on store method when storing hte record in the database failed.
     */
    @Test(expected = LikeException.class)
    public void testFailedStore() {
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(repository.get(userId, lineupId)).thenReturn(null);
        when(repository.store(like)).thenReturn(false);
        service.store(like);
    }

    /**
     * Test the behavior on store method when storing hte record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(userDBService.exists(userId)).thenReturn(true);
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(repository.get(userId, lineupId)).thenReturn(null);
        when(repository.store(like)).thenReturn(true);
        LineupLike stored = service.store(like);
        assertSame(like, stored);
    }

    /**
     * Test the behavior on delete method called with un existing like.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingLike() {
        when(repository.get(userId, lineupId)).thenReturn(null);
        service.delete(userId, lineupId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database failed.
     */
    @Test(expected = LikeException.class)
    public void testFailedDelete() {
        when(repository.get(userId, lineupId)).thenReturn(like);
        when(repository.delete(userId, lineupId)).thenReturn(false);
        service.delete(userId, lineupId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(userId, lineupId)).thenReturn(like);
        when(repository.delete(userId, lineupId)).thenReturn(true);
        service.delete(userId, lineupId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }
}