package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LineupRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 02.08.2016.
 */
public class LineupDBServiceUnitTest {

    @Mock
    private LineupRepository repository;

    @Mock
    private UserDBService userDBService;

    private LineupDBService service;

    private final int userId = 1;
    private final int lineupId = 1;
    private Lineup lineup = new Lineup(lineupId, userId, null, null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new LineupDBService(repository, userDBService);
    }

    /**
     * Test the exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(lineupId)).thenReturn(lineup);
        assertTrue(service.exists(lineupId));
    }

    /**
     * Test the exists method returns false on un exiting id.
     */
    @Test
    public void testNotExists() {
        when(repository.get(lineupId)).thenReturn(null);
        assertFalse(service.exists(lineupId));
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
        service.store(new Lineup(0, userId));
    }

    /**
     * Test the behavior on store method called with existing id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(lineupId)).thenReturn(lineup);
        service.store(lineup);
    }

    /**
     * Test the behavior on store method called with un existing user id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreWithUnExistingUserId() {
        when(repository.get(lineupId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(false);
        service.store(lineup);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = LineupException.class)
    public void testFailedStore() {
        when(repository.get(lineupId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(true);
        when(repository.store(lineup)).thenReturn(false);
        service.store(lineup);
    }

    /**
     * Test the behavior on store method when storing the record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(lineupId)).thenReturn(null);
        when(userDBService.exists(userId)).thenReturn(true);
        when(repository.store(lineup)).thenReturn(true);
        Lineup stored = service.store(lineup);
        assertSame(lineup, stored);
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
        service.update(new Lineup(0, userId));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingId() {
        when(repository.get(lineupId)).thenReturn(null);
        service.update(lineup);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database failed.
     */
    @Test(expected = LineupException.class)
    public void testFailedUpdate() {
        when(repository.get(lineupId)).thenReturn(lineup);
        when(repository.update(lineup)).thenReturn(false);
        service.update(lineup);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(lineupId)).thenReturn(lineup);
        when(repository.update(lineup)).thenReturn(true);
        Lineup updated = service.update(lineup);
        assertSame(lineup, updated);
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(lineupId)).thenReturn(null);
        service.delete(lineupId);
    }

    /**
     * Test the behavior on delete method when deleting the record in the database failed.
     */
    @Test(expected = LineupException.class)
    public void testFailedDelete() {
        when(repository.get(lineupId)).thenReturn(lineup);
        when(repository.delete(lineupId)).thenReturn(false);
        service.delete(lineupId);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(lineupId)).thenReturn(lineup);
        when(repository.delete(lineupId)).thenReturn(true);
        service.delete(lineupId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }

    /**
     * Test the behavior on getUserLineups method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetUserLineupsOnNull() {
        service.getUserLineups(null);
    }
}