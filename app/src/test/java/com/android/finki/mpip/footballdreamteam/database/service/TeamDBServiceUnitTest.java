package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.TeamRepository;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.TeamException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created by Borce on 01.08.2016.
 */
public class TeamDBServiceUnitTest {

    @Mock
    private TeamRepository repository;

    private TeamDBService service;

    private final int teamId = 1;
    private Team team = new Team(teamId, "Team 1", "TM", "2000$");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new TeamDBService(repository);
    }

    /**
     * Test that the exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(teamId)).thenReturn(team);
        assertTrue(service.exists(teamId));
    }

    /**
     * Test that the exist method returns false on un existing id.
     */
    @Test
    public void testNotExists() {
        when(repository.get(teamId)).thenReturn(null);
        assertFalse(service.exists(team));
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
     * Test the behavior on store method called wiht invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnInvalidId() {
        service.store(new Team(0, ""));
    }

    /**
     * Test the behavior on store method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullName() {
        service.store(new Team(1, null));
    }

    /**
     * Test the behavior on store method called with existing team id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(teamId)).thenReturn(team);
        service.store(team);
    }

    /**
     * Test the behavior on store method called with existing team name.
     */
    @Test(expected = UniqueFieldConstraintException.class)
    public void testStoreOnExistingName() {
        when(repository.get(teamId)).thenReturn(null);
        when(repository.getByName(team.getName())).thenReturn(team);
        service.store(team);
    }

    /**
     * Test the behavior on store method when inserting the record in the database failed.
     */
    @Test(expected = RuntimeException.class)
    public void testFailedStore() {
        when(repository.get(teamId)).thenReturn(null);
        when(repository.getByName(team.getName())).thenReturn(null);
        when(repository.store(team)).thenReturn(false);
        service.store(team);
    }

    /**
     * Test the behavior on store method when inserting hte record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(teamId)).thenReturn(null);
        when(repository.getByName(team.getName())).thenReturn(null);
        when(repository.store(team)).thenReturn(true);
        Team stored = service.store(team);
        assertNotNull(stored);
        assertEquals(teamId, stored.getId().intValue());
        assertEquals(team.getName(), stored.getName());
    }

    /**
     * Test the behavior on update method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNull() {
        service.update(null);
    }

    /**
     * Test the behavior on update method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnInvalidId() {
        service.update(new Team(0, ""));
    }

    /**
     * Test the behavior on update method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullName() {
        service.update(new Team(1, null));
    }

    /**
     * Test the behavior on update method called with un existing team id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingId() {
        when(repository.get(teamId)).thenReturn(null);
        service.update(team);
    }

    /**
     * Test the behavior on update method when updating the record in the database failed.
     */
    @Test(expected = TeamException.class)
    public void testFailedUpdate() {
        when(repository.get(teamId)).thenReturn(team);
        when(repository.update(team)).thenReturn(false);
        service.update(team);
    }

    /**
     * Test the behavior on update method when updating the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(teamId)).thenReturn(team);
        when(repository.update(team)).thenReturn(true);
        Team updated = service.update(team);
        assertNotNull(updated);
        assertEquals(teamId, updated.getId().intValue());
        assertEquals(team.getName(), updated.getName());
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(teamId)).thenReturn(null);
        service.delete(teamId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database failed.
     */
    @Test(expected = RuntimeException.class)
    public void testFailedDelete() {
        when(repository.get(teamId)).thenReturn(team);
        when(repository.delete(teamId)).thenReturn(false);
        service.delete(teamId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(teamId)).thenReturn(team);
        when(repository.delete(teamId)).thenReturn(true);
        service.delete(teamId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }
}