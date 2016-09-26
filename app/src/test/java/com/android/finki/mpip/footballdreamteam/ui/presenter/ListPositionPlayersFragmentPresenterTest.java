package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Borce on 19.08.2016.
 */
public class ListPositionPlayersFragmentPresenterTest {

    @Mock
    private ListPositionPlayersView view;

    @Mock
    private PlayerDBService playerDBService;

    @Mock
    private Bundle args;

    private ListPositionPlayersViewPresenter presenter;
    private int[] playersToExclude = {1, 35, 2356};
    private List<Player> players = Arrays.asList(new Player(), new Player(), new Player());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(playerDBService.getGoalkeepers(any(int[].class))).thenReturn(players);
        when(playerDBService.getDefenders(any(int[].class))).thenReturn(players);
        when(playerDBService.getMidfielders(any(int[].class))).thenReturn(players);
        when(playerDBService.getAttackers(any(int[].class))).thenReturn(players);
        presenter = new ListPositionPlayersViewPresenter(view, playerDBService);
    }

    /**
     * Mock the view arguments.
     *
     * @param place player place on the field
     */
    private void initArgs(PositionUtils.POSITION_PLACE place) {
        when(args.getSerializable(ListPositionPlayersView.PLACE_KEY)).thenReturn(place);
        when(args.getIntArray(ListPositionPlayersView.EXCLUDE_LAYERS_KEY))
                .thenReturn(playersToExclude);
    }

    /**
     * Test the behavior on onViewCreated called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithNull() {
        presenter.onViewCreated(null);
    }

    /**
     * Test the behavior on onViewCreated when position place bundle key is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithNotSetPlaceBundleKey() {
        when(args.getSerializable(ListPositionPlayersView.PLACE_KEY)).thenReturn(null);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior on onViewCreated when exclude players bundle key is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithNotSetExcludePlayersKey() {
        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.ATTACKERS;
        when(args.getSerializable(ListPositionPlayersView.PLACE_KEY)).thenReturn(place);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onVIewLayoutCreated is called and the players is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewLayoutCreatedWhenPlayersIsNotYetSet() {
        presenter.onViewLayoutCreated();
    }

    /**
     * Test the behavior on onViewCreated called with position place KEEPERS.
     */
    @Test
    public void testOnViewCreatedWithKeepersPositionPlace() {
        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.KEEPERS;
        this.initArgs(place);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).getGoalkeepers(playersToExclude);
        inOrder.verify(playerDBService).close();
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
        verify(view).onPlayersLoaded(players, place.getName());
    }

    /**
     * Test the behavior on onViewCreated called with position place DEFENDERS.
     */
    @Test
    public void testOnViewCreatedWithDefendersPositionPlace() {
        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.DEFENDERS;
        this.initArgs(place);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).getDefenders(playersToExclude);
        inOrder.verify(playerDBService).close();
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
        verify(view).onPlayersLoaded(players, place.getName());
    }

    /**
     * Test the behavior on onViewCreated called with position place MIDFIELDERS.
     */
    @Test
    public void testOnViewCreatedWithMidfieldersPositionPlace() {
        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.MIDFIELDERS;
        this.initArgs(place);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).getMidfielders(playersToExclude);
        inOrder.verify(playerDBService).close();
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
        verify(view).onPlayersLoaded(players, place.getName());
    }

    /**
     * Test the behavior on onViewCreated called with position place KEEPERS.
     */
    @Test
    public void testOnViewCreatedWithAttackersPositionPlace() {
        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.ATTACKERS;
        this.initArgs(place);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        InOrder inOrder = inOrder(playerDBService);
        inOrder.verify(playerDBService).open();
        inOrder.verify(playerDBService).getAttackers(playersToExclude);
        inOrder.verify(playerDBService).close();
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
        verify(view).onPlayersLoaded(players, place.getName());
    }
}