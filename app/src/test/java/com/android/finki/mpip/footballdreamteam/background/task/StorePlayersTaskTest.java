package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.exception.PlayerException;
import com.android.finki.mpip.footballdreamteam.model.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 30.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class StorePlayersTaskTest {

    @Mock
    private PlayerDBService playerDBService;

    @Mock
    private StorePlayersTask.Listener listener;

    private StorePlayersTask task;

    private Player[] players = {new Player(1, "Player 1"), new Player(2, "Player 2")};

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = new StorePlayersTask(playerDBService);
        task.setListener(listener);
    }

    /**
     * Test the behavior when storing the players failed.
     */
    @Test
    public void testStoreFailed() {
        when(playerDBService.exists(anyInt())).thenReturn(false);
        doThrow(new PlayerException("")).when(playerDBService).store(any(Player.class));
        task.execute(players);
        Robolectric.flushBackgroundThreadScheduler();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).exists(players[0].getId());
        inOrder.verify(playerDBService).store(players[0]);
        inOrder.verify(playerDBService).close();
        verify(listener).onPlayersSavingFailed();
    }

    /**
     * Test the behavior when storing the players is successful.
     */
    @Test
    public void testStoreSuccessful() {
        when(playerDBService.exists(players[0].getId())).thenReturn(true);
        when(playerDBService.exists(players[1].getId())).thenReturn(false);
        task.execute(players);
        Robolectric.flushBackgroundThreadScheduler();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).exists(players[0].getId());
        inOrder.verify(playerDBService, never()).store(players[0]);
        inOrder.verify(playerDBService).exists(players[1].getId());
        inOrder.verify(playerDBService).store(players[1]);
        inOrder.verify(playerDBService).close();
        verify(listener).onPlayersSavingSuccess();
    }
}
