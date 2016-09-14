package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * Created by Borce on 19.08.2016.
 */
public class ListPositionPlayersFragmentPresenterTest {

    @Mock
    private ListPositionPlayersFragment fragment;

    @Mock
    private PlayerDBService playerDBService;

    @Mock
    private Bundle args;

    private ListPositionPlayersViewPresenter presenter;

    private int[] playersToExclude = {1, 35, 2356};

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ListPositionPlayersViewPresenter
                (fragment, playerDBService);
    }

    /**
     * Test the behavior on onViewCreated called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnFragmentCreatedWithNull() {
        presenter.onViewCreated(null);
    }

    /**
     * Test the behavior on onViewCreated when position place bundle key is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnFragmentCreatedWithNotSetPlaceBundleKey() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey())).thenReturn(null);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior on onViewCreated when exclude players bundle key is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnFragmnetCreatedWithNotSetExcludePlayersKey() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.KEEPERS);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior on onViewCreated called with position place KEEPERS.
     */
    @Test
    public void testOnFragmentCreatedWithKeepersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.KEEPERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
        verify(playerDBService).open();
        verify(playerDBService).close();
        verify(playerDBService).getGoalkeepers(playersToExclude);
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
    }

    /**
     * Test the behavior on onViewCreated called with position place DEFENDERS.
     */
    @Test
    public void testOnFragmentCreatedWithDefendersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.DEFENDERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
        verify(playerDBService).open();
        verify(playerDBService).close();
        verify(playerDBService).getDefenders(playersToExclude);
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
    }

    /**
     * Test the behavior on onViewCreated called with position place MIDFIELDERS.
     */
    @Test
    public void testOnFragmentCreatedWithMidfieldersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.MIDFIELDERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
        verify(playerDBService).open();
        verify(playerDBService).close();
        verify(playerDBService).getMidfielders(playersToExclude);
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getAttackers(any(int[].class));
    }

    /**
     * Test the behavior on onViewCreated called with position place ATTACKERS.
     */
    @Test
    public void testOnFragmentCreatedWithAttackersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.ATTACKERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
        verify(playerDBService).open();
        verify(playerDBService).close();
        verify(playerDBService).getAttackers(playersToExclude);
        verify(playerDBService, never()).getGoalkeepers(any(int[].class));
        verify(playerDBService, never()).getDefenders(any(int[].class));
        verify(playerDBService, never()).getMidfielders(any(int[].class));
    }

    /**
     * Test the behavior on loadComments when position place is not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWhenPositionPlaceIsNotSet() {
//        presenter.onViewCreated();
    }

    /**
     * Test the behavior on loadComments when position place is keepers.
     */
    @Test
    public void testOnViewCreatedWithKeepersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.KEEPERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
//        presenter.onViewCreated();
        verify(fragment).setAdapter(anyListOf(Player.class));
        verify(fragment).setPositionPlace("Keepers");
    }

    /**
     * Test the behavior on loadComments when position place is defenders.
     */
    @Test
    public void testOnViewCreatedWithDefendersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.DEFENDERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
//        presenter.onViewCreated();
        verify(fragment).setAdapter(anyListOf(Player.class));
        verify(fragment).setPositionPlace("Defenders");
    }

    /**
     * Test the behavior on loadComments when position place is midfielders.
     */
    @Test
    public void testOnViewCreatedWithMidfieldersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.MIDFIELDERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
//        presenter.onViewCreated();
        verify(fragment).setAdapter(anyListOf(Player.class));
        verify(fragment).setPositionPlace("Midfielders");
    }

    /**
     * Test the behavior on loadComments when position place is attackers.
     */
    @Test
    public void testOnViewCreatedWithAttackersPositionPlace() {
//        when(args.getSerializable(ListPositionPlayersFragment.getPlaceKey()))
//                .thenReturn(PositionUtils.POSITION_PLACE.ATTACKERS);
//        when(args.getIntArray(ListPositionPlayersFragment.getExcludeLayersKey()))
//                .thenReturn(playersToExclude);
        presenter.onViewCreated(args);
//        presenter.onViewCreated();
        verify(fragment).setAdapter(anyListOf(Player.class));
        verify(fragment).setPositionPlace("Attackers");
    }
}