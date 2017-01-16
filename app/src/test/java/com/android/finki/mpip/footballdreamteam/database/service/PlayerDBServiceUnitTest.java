package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.PlayerRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.PlayerException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 01.08.2016.
 */
public class PlayerDBServiceUnitTest {

    @Mock
    private PlayerRepository repository;

    @Mock
    private TeamDBService teamDBService;

    @Mock
    private PositionDBService positionDBService;

    private PlayerDBService service;

    private final int teamId = 1;
    private Team team = new Team(teamId, "Team 1", "TM", "20000$");
    private final int positionId = 1;
    private Position position = new Position(positionId, "Position 1");
    private final int playerId = 1;
    private Player player = new Player(playerId, team, position, "Player", "Nationality", null);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new PlayerDBService(repository, teamDBService, positionDBService);
    }

    /**
     * Test that the exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(playerId)).thenReturn(player);
        assertTrue(service.exists(playerId));
    }

    /**
     * Test that the exists method returns false on un existing id.
     */
    @Test
    public void testExistsOnUnExistingId() {
        when(repository.get(playerId)).thenReturn(null);
        assertFalse(service.exists(playerId));
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
        service.store(new Player(0, ""));
    }

    /**
     * Test the behavior on store method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullName() {
        service.store(new Player(1, null));
    }

    /**
     * Test the behavior on store method called with existing id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(playerId)).thenReturn(player);
        service.store(player);
    }

    /**
     * Test the behavior on store method called with un existing team id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingTeamId() {
        when(repository.get(playerId)).thenReturn(null);
        when(teamDBService.exists(teamId)).thenReturn(false);
        service.store(player);
    }

    /**
     * Test the behavior on store method called with un existing position id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingPositionId() {
        when(repository.get(playerId)).thenReturn(null);
        when(teamDBService.exists(teamId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(false);
        service.store(player);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = PlayerException.class)
    public void testFailedStore() {
        when(repository.get(playerId)).thenReturn(null);
        when(teamDBService.exists(teamId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(player)).thenReturn(false);
        service.store(player);
    }

    /**
     * Test the behavior on store method when storing the record in database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(playerId)).thenReturn(null);
        when(teamDBService.exists(teamId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(player)).thenReturn(true);
        Player stored = service.store(player);
        assertSame(player, stored);
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
        service.update(new Player(0, ""));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullName() {
        service.update(new Player(1, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExitingId() {
        when(repository.get(playerId)).thenReturn(null);
        service.update(player);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database failed.
     */
    @Test(expected = PlayerException.class)
    public void testFailedUpdate() {
        when(repository.get(playerId)).thenReturn(player);
        when(repository.update(player)).thenReturn(false);
        service.update(player);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database is successful;
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(playerId)).thenReturn(player);
        when(repository.update(player)).thenReturn(true);
        Player updated = service.update(player);
        assertSame(player, updated);
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(playerId)).thenReturn(null);
        service.delete(playerId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database failed.
     */
    @Test(expected = PlayerException.class)
    public void testFailedDelete() {
        when(repository.get(playerId)).thenReturn(player);
        when(repository.delete(playerId)).thenReturn(false);
        service.delete(playerId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(playerId)).thenReturn(player);
        when(repository.delete(playerId)).thenReturn(true);
        service.delete(playerId);
    }

    /**
     * Test the behavior when delete method is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }

    /**
     * Test the behavior on getTeamPlayers when called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetTeamPlayers() {
        service.getTeamPlayers(null);
    }

    /**
     * Test the behavior on getPositionsPlayers method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionPlayers() {
        service.getPositionPlayers(null);
    }
}