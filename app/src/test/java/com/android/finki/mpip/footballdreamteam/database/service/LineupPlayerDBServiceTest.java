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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
    private LineupPlayer lineupPlayer1 = new LineupPlayer(lineupId, playerId, positionId);
    private LineupPlayer lineupPlayer2 = new LineupPlayer(2, 2, 2);
    private LineupPlayer lineupPlayer3 = new LineupPlayer(3, 3, 3);
    private LineupPlayer lineupPlayer4 = new LineupPlayer(4, 4, 4);
    private LineupPlayer lineupPlayer5 = new LineupPlayer(5, 5, 5);
    private LineupPlayer lineupPlayer6 = new LineupPlayer(6, 6, 6);
    private List<LineupPlayer> lineupPlayers = Arrays.asList(lineupPlayer1, lineupPlayer2,
            lineupPlayer3, lineupPlayer4, lineupPlayer5, lineupPlayer6);

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
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
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
        service.store(lineupPlayer1);
    }

    /**
     * Test the behavior on store method called with un existing player id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingPlayerId() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(false);
        service.store(lineupPlayer1);
    }

    /**
     * Test the behavior on store method called with un existing position id.
     */
    @Test(expected = ForeignKeyConstraintException.class)
    public void testStoreOnUnExistingPositionId() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(false);
        service.store(lineupPlayer1);
    }

    /**
     * Test the behavior on store method called with existing primary key.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExitingPrimaryKey() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
        service.store(lineupPlayer1);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = LineupPlayerException.class)
    public void testFailedStore() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(lineupPlayer1)).thenReturn(false);
        service.store(lineupPlayer1);
    }

    /**
     * Test the behavior on store method when storing the record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(lineupDBService.exists(lineupId)).thenReturn(true);
        when(playerDBService.exists(playerId)).thenReturn(true);
        when(positionDBService.exists(positionId)).thenReturn(true);
        when(repository.store(lineupPlayer1)).thenReturn(true);
        LineupPlayer stored = service.store(lineupPlayer1);
        assertSame(lineupPlayer1, stored);
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing primary key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingPrimaryKey() {
        when(repository.get(lineupId, playerId)).thenReturn(null);
        service.update(lineupPlayer1);
    }

    /**
     * Test the behavior on onUpdateSuccess method when inserting the record in the database failed.
     */
    @Test(expected = LineupPlayerException.class)
    public void testFailedUpdate() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
        when(repository.update(lineupPlayer1)).thenReturn(false);
        service.update(lineupPlayer1);
    }

    /**
     * Test the behavior on onUpdateSuccess method when inserting the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
        when(repository.update(lineupPlayer1)).thenReturn(true);
        Date date = new Date();
        lineupPlayer1.setUpdatedAt(date);
        LineupPlayer updated = service.update(lineupPlayer1);
        assertSame(lineupPlayer1, updated);
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
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
        when(repository.delete(lineupId, playerId)).thenReturn(false);
        service.delete(lineupId, playerId);
    }

    /**
     * Test the behavior on delete method when deleting the record in the database failed.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayer1);
        when(repository.delete(lineupId, playerId)).thenReturn(true);
        service.delete(lineupId, playerId);
    }

    /**
     * Test the behavior when delete players is called with empty list.
     */
    @Test
    public void testDeletePlayersOnEmptyList() {
        assertTrue(service.deletePlayers(new ArrayList<LineupPlayer>()));
        verify(repository, never()).delete(anyInt(), anyInt());
    }

    /**
     * Test the behavior on deletePlayers when deleting the first player in the list failed.
     */
    @Test
    public void testDeletePlayersWhenDeletingTheFirstPlayerFailed() {
        int lineupId = lineupPlayers.get(0).getLineupId();
        int playerId = lineupPlayers.get(0).getPlayerId();
        when(repository.get(lineupId, playerId)).thenReturn(new LineupPlayer());
        when(repository.delete(lineupId, playerId)).thenReturn(false);
        assertFalse(service.deletePlayers(lineupPlayers));
        verify(repository, times(1)).delete(lineupId, playerId);
    }

    /**
     * Test the behavior on deletePlayers when deleting not the first player failed.
     */
    @Test
    public void testDeletePlayersWhenDeletingNotTheFirstPlayerFailed() {
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        int index = lineupPlayers.size() - 1;
        int lineupId;
        int playerId;
        for (int i = 0; i <= index; i++) {
            lineupId = lineupPlayers.get(i).getLineupId();
            playerId = lineupPlayers.get(i).getPlayerId();
            if (i == index) {
                when(repository.delete(lineupId, playerId)).thenReturn(false);
            } else {
                when(repository.delete(lineupId, playerId)).thenReturn(true);
            }
        }
        assertFalse(service.deletePlayers(lineupPlayers));
        for (int i = 0; i <= index; i++) {
            lineupId = lineupPlayers.get(i).getLineupId();
            playerId = lineupPlayers.get(i).getPlayerId();
            verify(repository).delete(lineupId, playerId);
        }
    }

    /**
     * Test the behavior on deletePlayers when all players are deleted successfully.
     */
    @Test
    public void testSuccessDeletePlayers() {
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        when(repository.delete(anyInt(), anyInt())).thenReturn(true);
        assertTrue(service.deletePlayers(lineupPlayers));
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            verify(repository).delete(lineupPlayer.getLineupId(), lineupPlayer.getPlayerId());
        }
    }

    /**
     * Test the behavior on storePlayers called with empty list.
     */
    @Test
    public void testStorePlayersOnEmptyList() {
        assertTrue(service.storePlayers(new ArrayList<LineupPlayer>()));
        verify(repository, never()).store(any(LineupPlayer.class));
        verify(repository, never()).delete(anyInt(), anyInt());
    }

    /**
     * Test the behavior on storePlayers when storing the first player failed.
     */
    @Test
    public void testStorePlayersWhenStoringTheFirstPlayersFailed() {
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(null);
        when(repository.store(lineupPlayers.get(0))).thenReturn(false);
        assertFalse(service.storePlayers(lineupPlayers));
        verify(repository).store(lineupPlayers.get(0));
        verify(repository, never()).delete(anyInt(), anyInt());
    }

    /**
     * Test the behavior on storePlayers when storing not the first player failed.
     */
    @Test
    public void testStorePlayersWhenStoringNotTheFirstPlayerFailed() {
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.delete(anyInt(), anyInt())).thenReturn(true);
        final int index = lineupPlayers.size() - 1;
        int lineupId;
        int playerId;
        for (int i = 0; i <= index; i++) {
            final int finalLineupId = lineupPlayers.get(i).getLineupId();
            final int finalPlayerId = lineupPlayers.get(i).getPlayerId();
            when(repository.get(finalLineupId, finalPlayerId)).thenReturn(null);
            final int finalI = i;
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    when(repository.get(finalLineupId, finalPlayerId))
                            .thenReturn(lineupPlayers.get(finalI));
                    return finalI != index;
                }
            }).when(repository).store(lineupPlayers.get(i));
        }
        assertFalse(service.storePlayers(lineupPlayers));
        for (int i = 0; i <= index; i++) {
            lineupId = lineupPlayers.get(i).getLineupId();
            playerId = lineupPlayers.get(i).getPlayerId();
            verify(repository).store(lineupPlayers.get(i));
            if (i < index) {
                verify(repository).delete(lineupId, playerId);
            }
        }
    }

    /**
     * Test the behavior on storePlayers when all players in the list are store successfully.
     */
    @Test
    public void testSuccessStorePlayers() {
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.get(anyInt(), anyInt())).thenReturn(null);
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        assertTrue(service.storePlayers(lineupPlayers));
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            verify(repository).store(lineupPlayer);
        }
        verify(repository, never()).delete(anyInt(), anyInt());
    }

    /**
     * Test the behavior on updatePlayers called with empty list.
     */
    @Test
    public void testUpdatePlayersOnEmptyList() {
        assertTrue(service.updatePlayers(new ArrayList<LineupPlayer>()));
        verify(repository, never()).update(any(LineupPlayer.class));
    }

    /**
     * Test the behavior on updatePlayers when updating the first player failed.
     */
    @Test
    public void testUpdatePlayersWhenUpdatingTheFirstPlayerFailed() {
        int lineupId = lineupPlayers.get(0).getLineupId();
        int playerId = lineupPlayers.get(0).getPlayerId();
        when(repository.get(lineupId, playerId)).thenReturn(lineupPlayers.get(0));
        when(repository.update(lineupPlayers.get(0))).thenReturn(false);
        assertFalse(service.updatePlayers(lineupPlayers));
        verify(repository).update(lineupPlayers.get(0));
    }

    /**
     * Test the behavior on updatePlayers when updating not the first player failed.
     */
    @Test
    public void testUpdatePlayersWhenUpdatingNotTheFirstPlayerFailed() {
        final int index = lineupPlayers.size() - 1;
        for (int i = 0; i <= index; i++) {
            int lineupId = lineupPlayers.get(i).getLineupId();
            int playerId = lineupPlayers.get(i).getPlayerId();
            when(repository.get(lineupId, playerId)).thenReturn(lineupPlayers.get(i));
            if (i == index) {
                when(repository.update(lineupPlayers.get(i))).thenReturn(false);
            } else {
                when(repository.update(lineupPlayers.get(i))).thenReturn(true);
            }
        }
        assertFalse(service.updatePlayers(lineupPlayers));
        for (int i = 0; i <= index; i++) {
            verify(repository).update(lineupPlayers.get(i));
        }
    }

    /**
     * Test the behavior on updatePlayers when all playes in the list are updated successfully.
     */
    @Test
    public void testSuccessUpdatePlayers() {
        when(repository.get(anyInt(), anyInt())).thenReturn(new LineupPlayer());
        when(repository.update(any(LineupPlayer.class))).thenReturn(true);
        assertTrue(service.updatePlayers(lineupPlayers));
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            verify(repository).update(lineupPlayer);
        }
    }

    /**
     * Test the behavior when syncPlayers is called with un existing lineup id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSyncPlayersOnUnExistingLineup() {
        service.syncPlayers(0, lineupPlayers);
    }

    /**
     * Init the mocks for the syncPlayers method.
     */
    private List<List<LineupPlayer>> mockForSyncPlayers() {
        List<List<LineupPlayer>> result = new ArrayList<>();
        final LineupPlayer tLineupPlayer1 = new LineupPlayer(100, 100, 100);
        final LineupPlayer tLineupPlayer2 = new LineupPlayer(101, 101, 101);
        List<LineupPlayer> existingPlayers = Arrays.asList(lineupPlayer1, lineupPlayer4,
                tLineupPlayer1, tLineupPlayer2);
        result.add(Arrays.asList(lineupPlayer2, lineupPlayer3, lineupPlayer5, lineupPlayer6));
        result.add(Arrays.asList(lineupPlayer1, lineupPlayer4));
        result.add(Arrays.asList(tLineupPlayer1, tLineupPlayer2));
        when(lineupDBService.exists(anyInt())).thenReturn(true);
        when(playerDBService.exists(anyInt())).thenReturn(true);
        when(positionDBService.exists(anyInt())).thenReturn(true);
        when(repository.getLineupPlayers(lineupId)).thenReturn(existingPlayers);
        for (LineupPlayer player : existingPlayers) {
            when(repository.get(player.getLineupId(), player.getPlayerId())).thenReturn(player);
        }
        return result;
    }

    /**
     * Test the behavior on syncPlayers when storing some of the existing player failed.
     */
    @Test
    public void testSyncPlayersWhenStoringThePlayerFailed() {
        List<List<LineupPlayer>> mock = this.mockForSyncPlayers();
        List<LineupPlayer> playersToBeStored = mock.get(0);
        List<LineupPlayer> playersToBeUpdated = mock.get(1);
        List<LineupPlayer> playersToBeDeleted = mock.get(2);
        when(repository.store(any(LineupPlayer.class))).thenReturn(false);
        assertFalse(service.syncPlayers(lineupId, this.lineupPlayers));
        verify(repository).store(playersToBeStored.get(0));
        for (int i = 1; i < playersToBeStored.size(); i++) {
            verify(repository, never()).store(playersToBeStored.get(i));
        }
        for (LineupPlayer player : playersToBeUpdated) {
            verify(repository, never()).update(player);
        }
        for (LineupPlayer player : playersToBeDeleted) {
            verify(repository, never()).delete(player.getLineupId(), player.getPlayerId());
        }
    }

    /**
     * Test the behavior on syncPlayers when updating some of the players in the list failed.
     */
    @Test
    public void testSyncPlayersWhenUpdatingThePlayerFailed() {
        List<List<LineupPlayer>> mock = this.mockForSyncPlayers();
        List<LineupPlayer> playersToBeStored = mock.get(0);
        List<LineupPlayer> playersToBeUpdated = mock.get(1);
        List<LineupPlayer> playersToBeDeleted = mock.get(2);
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        when(repository.update(any(LineupPlayer.class))).thenReturn(false);
        assertFalse(service.syncPlayers(lineupId, this.lineupPlayers));
        for (LineupPlayer player : playersToBeStored) {
            verify(repository).store(player);
        }
        verify(repository).update(playersToBeUpdated.get(0));
        for (int i = 1; i < playersToBeUpdated.size(); i++) {
            verify(repository, never()).update(playersToBeUpdated.get(i));
        }
        for (LineupPlayer player : playersToBeDeleted) {
            verify(repository, never()).delete(player.getLineupId(), player.getPlayerId());
        }
    }

    /**
     * Test the behavior on syncPlayers when deleting some of the player in the list failed.
     */
    @Test
    public void testSyncPlayersWhenDeletingThePlayerFailed() {
        List<List<LineupPlayer>> mock = this.mockForSyncPlayers();
        List<LineupPlayer> playersToBeStored = mock.get(0);
        List<LineupPlayer> playersToBeUpdated = mock.get(1);
        List<LineupPlayer> playersToBeDeleted = mock.get(2);
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        when(repository.update(any(LineupPlayer.class))).thenReturn(true);
        when(repository.delete(anyInt(), anyInt())).thenReturn(false);
        assertFalse(service.syncPlayers(lineupId, this.lineupPlayers));
        for (LineupPlayer player : playersToBeStored) {
            verify(repository).store(player);
        }
        for (LineupPlayer player : playersToBeUpdated) {
            verify(repository).update(player);
        }
        verify(repository).delete(playersToBeDeleted.get(0).getLineupId(),
                playersToBeDeleted.get(0).getPlayerId());
        for (int i = 1; i < playersToBeDeleted.size(); i++) {
            int lineupId = playersToBeDeleted.get(i).getLineupId();
            int playerId = playersToBeDeleted.get(i).getPlayerId();
            verify(repository, never()).delete(lineupId, playerId);
        }
    }

    /**
     * Test the behavior on syncPlayers when syncing the players is successful.
     */
    @Test
    public void testSuccessSyncPlayers() {
        List<List<LineupPlayer>> mock = this.mockForSyncPlayers();
        List<LineupPlayer> playersToBeStored = mock.get(0);
        List<LineupPlayer> playersToBeUpdated = mock.get(1);
        List<LineupPlayer> playersToBeDeleted = mock.get(2);
        when(repository.store(any(LineupPlayer.class))).thenReturn(true);
        when(repository.update(any(LineupPlayer.class))).thenReturn(true);
        when(repository.delete(anyInt(), anyInt())).thenReturn(true);
        assertTrue(service.syncPlayers(lineupId, this.lineupPlayers));
        for (LineupPlayer player : playersToBeStored) {
            verify(repository).store(player);
        }
        for (LineupPlayer player : playersToBeUpdated) {
            verify(repository).update(player);
        }
        for (LineupPlayer player : playersToBeDeleted) {
            verify(repository).delete(player.getLineupId(), player.getPlayerId());
        }
    }
}
