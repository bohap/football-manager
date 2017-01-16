package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.ui.component.PlayerDetailsView;
import com.android.finki.mpip.footballdreamteam.utility.DateUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 20.08.2016.
 */
public class PlayerDetailsViewPresenterTest {

    @Mock
    private PlayerDetailsView view;

    @Mock
    private PlayerDBService dbService;

    @Mock
    private Bundle args;

    private PlayerDetailsViewPresenter presenter;
    private final Calendar calendar = new GregorianCalendar(2016, 8, 20);
    private Date date = calendar.getTime();
    private final Player player = new Player(1, new Team(1, "Team"),
            new Position(1, "Position"), "Player", null, date, 0);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new PlayerDetailsViewPresenter(view, dbService);
    }

    /**
     * Test the behavior on onViewCreated called with not set player id bundle key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnDialogCreatedOnNotSetPlayerId() {
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior on onViewCreated called with invalid player id bundle key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithInvalidPlayerId() {
        when(args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1)).thenReturn(0);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior on getPlayer when player id don't exists in the database.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerOnUnExistingPlayerId() {
        when(args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1)).thenReturn(player.getId());
        presenter.onViewCreated(args);
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).get(player.getId());
        inOrder.verify(dbService).close();
    }

    /**
     * Test the behavior on getPlayer when the player team is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerOnPlayerNullTeam() {
        when(args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1)).thenReturn(player.getId());
        when(dbService.get(player.getId())).thenReturn(player);
        player.setTeam(null);
        presenter.onViewCreated(args);
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).get(player.getId());
        inOrder.verify(dbService).close();
    }

    /**
     * Test the behavior on getPlayer when player position is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerWithPlayerNullPosition() {
        when(args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1)).thenReturn(player.getId());
        when(dbService.get(player.getId())).thenReturn(player);
        player.setPosition(null);
        presenter.onViewCreated(args);
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).get(player.getId());
        inOrder.verify(dbService).close();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called.
     */
    @Test
    public void testOnViewLayoutCreated() {
        String name = player.getName();
        String team = player.getTeam().getName();
        String age = String.valueOf(DateUtils.getYearDiff(player.getDateOfBirth()));
        String position = player.getPosition().getName();
        when(args.getInt(PlayerDetailsView.BUNDLE_PLAYER_ID_KEY, -1)).thenReturn(player.getId());
        when(args.getBoolean(PlayerDetailsView.BUNDLE_EDITABLE_KEY, false)).thenReturn(true);
        when(dbService.get(player.getId())).thenReturn(player);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        InOrder inOrder = inOrder(dbService);
        inOrder.verify(dbService).open();
        inOrder.verify(dbService).get(player.getId());
        inOrder.verify(dbService).close();
        verify(view).bindPlayer(name, team, age, position, true);
    }
}