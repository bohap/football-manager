package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 20.08.2016.
 */
public class PlayerDetailsDialogPresenterTest {

    @Mock
    private PlayerDetailsDialog dialog;

    @Mock
    private PlayerDBService dbService;

    @Mock
    private Bundle args;

    private PlayerDetailsDialogPresenter presenter;

    private final int year = 2016, month = 8, day = 20;
    private final Calendar calendar = new GregorianCalendar(year, month, day);
    private final Player player = new Player(1, new Team(1, "Team"),
            new Position(1, "Position"), "Player", null, calendar.getTime(), 0);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new PlayerDetailsDialogPresenter(dialog, dbService);
    }

    /**
     * Test the behavior on onDialogCreated called with not set player id bundle key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnDialogCreatedOnNotSetPlayerId() {
        presenter.onDialogCreated(args);
    }

    /**
     * Test the behavior on onDialogCreated called with invalid player id bundle key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnDialogCreatedWithInvalidPlayerId() {
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1)).thenReturn(0);
        presenter.onDialogCreated(args);
    }

    /**
     * Test the behavior on getPlayer when player id don't exists in the database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerOnUnExistingPlayerId() {
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1))
                .thenReturn(player.getId());
        presenter.onDialogCreated(args);
        verify(dbService).open();
        verify(dbService).close();
        verify(dbService).get(player.getId());
    }

    /**
     * Test the behavior on getPlayer when the player team is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerOnPlayerNullTeam() {
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1))
                .thenReturn(player.getId());
        when(dbService.get(player.getId())).thenReturn(player);
        player.setTeam(null);
        presenter.onDialogCreated(args);
        verify(dbService).open();
        verify(dbService).close();
        verify(dbService).get(player.getId());
    }

    /**
     * Test the behavior on getPlayer when player position is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerWithPlayerNullPosition() {
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1))
                .thenReturn(player.getId());
        when(dbService.get(player.getId())).thenReturn(player);
        player.setPosition(null);
        presenter.onDialogCreated(args);
        verify(dbService).open();
        verify(dbService).close();
        verify(dbService).get(player.getId());
    }

    /**
     * Test that getPlayer works when all data is valid.
     */
    @Test
    public void testGetPlayer() {
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1))
                .thenReturn(player.getId());
        when(dbService.get(player.getId())).thenReturn(player);
        presenter.onDialogCreated(args);
        verify(dbService).open();
        verify(dbService).close();
        verify(dbService).get(player.getId());
    }

    /**
     * Test the behavior when loadComments is called.
     */
    @Test
    public void testOnViewCreated() {
        String name = player.getName();
        String team = player.getTeam().getName();
        String age = String.valueOf(DateUtils.getYearDiff(player.getDateOfBirth()));
        String position = player.getPosition().getName();
        when(args.getInt(PlayerDetailsDialog.getBundlePlayerIdKey(), -1))
                .thenReturn(player.getId());
        when(args.getBoolean(PlayerDetailsDialog.getBundleEditableKey(), false))
                .thenReturn(true);
        when(dbService.get(player.getId())).thenReturn(player);
        presenter.onDialogCreated(args);
        presenter.onViewCreated();
        verify(dialog).bindPlayer(name, team, age, position, true);
    }
}