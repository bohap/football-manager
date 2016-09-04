package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.LineupPlayerRepository;
import com.android.finki.mpip.footballdreamteam.exception.ForeignKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.LineupPlayerException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerDBServiceTest {

    @Mock
    private LineupDBService lineupDBService;

    @Mock
    private PlayerDBService playerDBService;

    @Mock
    private PositionDBService positionDBService;

    @Mock
    private LineupPlayerRepository repository;

    private LineupPlayerDBService service;

    private final int lineupId = 1;
    private final int playerId = 1;
    private final int positionId = 2;
    private LineupPlayer lineupPlayer = new LineupPlayer(lineupId, playerId, positionId);
    private List<LineupPlayer> lineupPlayers = Arrays.asList(
            new LineupPlayer(1, 1, 1), new LineupPlayer(2, 2, 2), new LineupPlayer(3, 3, 3));


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new LineupPlayerDBService(repository, lineupDBService,
                playerDBService, positionDBService);
    }

    /**
     * Test that the exists method return true when the lineup player exists.
     */
    @Test
    public void testExists() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        assertTrue(service.exists(lineupId, playerId));
    }

    /**
     * Test that the exists method return false when the lineup player don't exists.
     */
    @Test
    public void testNotExists() {
        when(repository.get(lineupId, playerId)).thenReturn(null);
        assertFalse(service.exists(lineupId, playerId));
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
     * Test the behavior on store method called with un existing lineup id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingLineupId() {
        when(lineupDBService.exists(lineupId)).thenReturn(false);
        service.store(lineupPlayer);
    }

    /**
     * Test the behavior on store method called with un existing player id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingPlayerId() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(false);
        service.store(lineupPlayer);
    }

    /**
     * Test the behavior on store method called with un existing position id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingPositionId() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(false);
        service.store(lineupPlayer);
    }

    /**
     * Test the behavior on store method called with existing primary key.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExitingPrimaryKey() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        service.store(lineupPlayer);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = LineupPlayerException.class)
    public void testFailedStore() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(lineupPlayer)).thenReturn(false);
        service.store(lineupPlayer);
    }

    /**
     * Test the behavior on store method when storing the record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(lineupPlayer)).thenReturn(true);
        LineupPlayer stored = service.store(lineupPlayer);
        assertNotNull(stored);
        assertEquals(lineupId, stored.getLineupId());
        assertEquals(playerId, stored.getPlayerId());
        assertEquals(positionId, stored.getPositionId());
    }

    /**
     * Test the behavior on update method called with un existing primary key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingPrimaryKey() {
        when(repository.get(lineupId, playerId)).thenReturn(null);
        service.update(lineupPlayer);
    }

    /**
     * Test the behavior on update method when inserting the record in the database failed.
     */
    @Test(expected = LineupPlayerException.class)
    public void testFailedUpdate() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        when(repository.update(lineupPlayer)).thenReturn(false);
        service.update(lineupPlayer);
    }

    /**
     * Test the behavior on update method when inserting the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        when(repository.update(lineupPlayer)).thenReturn(true);
        Date date = new Date();
        lineupPlayer.setUpdatedAt(date);
        LineupPlayer updated = service.update(lineupPlayer);
        assertNotNull(updated);
        assertEquals(lineupId, updated.getLineupId());
        assertEquals(playerId, updated.getPlayerId());
        assertEquals(positionId, updated.getPositionId());
        assertEquals(date, updated.getUpdatedAt());
    }

    /**
     * Test the behavior on delete method called with un existing primary key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingPrimaryKey() {
        when(repository.get(lineupId, playerId)).thenReturn(null);
        service.delete(lineupId, playerId);
    }

    /**
     * Test the behavior on delete method when deleting the record in the database failed.
     */
    @Test(expected = LineupPlayerException.class)
    public void testFailedDelete() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        when(repository.delete(lineupId, playerId)).thenReturn(false);
        service.delete(lineupId, playerId);
    }

    /**
     * Test the behavior on delete method when deleting the record in the database failed.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer);
        when(repository.delete(lineupId, playerId)).thenReturn(true);
        service.delete(lineupId, playerId);
    }

    @Test
    public void testStorePlayerOnFailedStore() {
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(null);
        when(repository.store(any(LineupPlayer.class))).thenReturn(false);
        assertFalse(service.storePlayers(lineupPlayers));
    }

    /**
     * Test that storePlayers method passes.
     */
    @Test
    public void testSuccessStorePlayers() {
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(null);
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        assertTrue(service.storePlayers(lineupPlayers));
    }

    /**
     * Update the players in the lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePlayersOnUnExistingLineupId() {
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        service.updatePlayers(1, lineupPlayers);
    }

    /**
     * Test the behavior on deletePlayers method when deleting the player failed.
     */
    @Test
    public void testFailedDeletePlayers() {
        LineupPlayer lineupPlayer1 = lineupPlayers.get(0);
        when(repository.get(lineupPlayer1.getLineupId(), lineupPlayer1.getPositionId()))
                .thenReturn(null);
        assertFalse(service.deletePlayers(lineupPlayers));
    }

    /**
     * Test that deletePlayers method passes.
     */
    @Test
    public void testSuccessDeletePlayers() {
        LineupPlayer lineupPlayer1 = lineupPlayers.get(0);
        when(repository.get(lineupPlayer1.getLineupId(), lineupPlayer1.getPositionId()))
                .thenReturn(lineupPlayer1);
        when(repository.delete(lineupPlayer1.getLineupId(), lineupPlayer1.getPositionId()))
                .thenReturn(true);
        LineupPlayer lineupPlayer2 = lineupPlayers.get(1);
        when(repository.get(lineupPlayer2.getLineupId(), lineupPlayer2.getPositionId()))
                .thenReturn(lineupPlayer2);
        when(repository.delete(lineupPlayer2.getLineupId(), lineupPlayer2.getPositionId()))
                .thenReturn(true);
        LineupPlayer lineupPlayer3 = lineupPlayers.get(2);
        when(repository.get(lineupPlayer3.getLineupId(), lineupPlayer3.getPositionId()))
                .thenReturn(lineupPlayer3);
        when(repository.delete(lineupPlayer3.getLineupId(), lineupPlayer3.getPositionId()))
                .thenReturn(true);
        assertTrue(service.deletePlayers(lineupPlayers));
    }

    /**
     * Test the behavior on updatePlayers when storing the player data failed.
     */
    @Test
    public void testUpdatePlayersOnFailedPlayerStore() {
        final List<LineupPlayer> list1 = Arrays.asList(new LineupPlayer(1, 1, 1),
                new LineupPlayer(2, 2, 2), new LineupPlayer(3, 3, 3));
        final List<LineupPlayer> list2 = Arrays.asList(new LineupPlayer(4, 4, 4),
                new LineupPlayer(5, 5, 5));
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(null);
        when(repository.store(any(LineupPlayer.class))).thenReturn(false);
        when(repository.getLineupPlayers(anyInt())).thenReturn(list2);
        assertFalse(service.updatePlayers(1, list1));
    }

    /**
     * Test the behavior on updatePlayers when updating the player failed.
     */
    @Test
    public void testUpdatePlayersOnFailedPlayersUpdate() {
        final List<LineupPlayer> list1 = Arrays.asList(new LineupPlayer(3, 3, 3),
                new LineupPlayer(2, 2, 2));
        final List<LineupPlayer> list2 = Arrays.asList(new LineupPlayer(3, 3, 3),
                new LineupPlayer(2, 2, 2));
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        when(repository.getLineupPlayers(anyInt())).thenReturn(list2);
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        assertFalse(service.updatePlayers(1, list1));
    }

    /**
     * Test the behavior on updatePlayers when deleting the players failed.
     */
    @Test
    public void testUpdatePlayersOnFailedDelete() {
        final List<LineupPlayer> list1 = Arrays.asList(new LineupPlayer(2, 2, 2),
                new LineupPlayer(3, 3, 3));
        final List<LineupPlayer> list2 = Arrays.asList(new LineupPlayer(4, 4, 4),
                new LineupPlayer(3, 3, 3), new LineupPlayer(2, 2, 2));
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(repository.update(any(LineupPlayer.class))).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        when(repository.delete(anyInt(), anyInt())).thenReturn(false);
        when(repository.getLineupPlayers(anyInt())).thenReturn(list2);
        assertFalse(service.updatePlayers(1, list1));
    }

    /**
     * Test that updatePlayer method works.
     */
    @Test
    public void testUpdatePlayers() {
        final List<LineupPlayer> list1 = Arrays.asList(new LineupPlayer(1, 1, 1),
                new LineupPlayer(2, 2, 2), new LineupPlayer(3, 3, 3));
        final List<LineupPlayer> list2 = Arrays.asList(new LineupPlayer(2, 2, 2),
                new LineupPlayer(4, 4, 4));
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(1, 1)).thenReturn(null);
        when(repository.get(3, 3)).thenReturn(null);
        when(repository.get(2, 2)).thenReturn(new LineupPlayer());
        when(repository.get(4, 4)).thenReturn(new LineupPlayer());
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        when(repository.update(any(LineupPlayer.class))).thenReturn(true);
        when(repository.delete(anyInt(), anyInt())).thenReturn(true);
        when(repository.getLineupPlayers(anyInt())).thenReturn(list2);
        assertTrue(service.updatePlayers(1, list1));
        verify(repository).store(list1.get(0));
        verify(repository).store(list1.get(2));
        verify(repository).update(list1.get(1));
        verify(repository).delete(4, 4);
    }
}
