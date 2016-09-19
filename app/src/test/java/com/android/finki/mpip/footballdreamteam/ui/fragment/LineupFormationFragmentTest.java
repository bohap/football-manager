package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragmentTest.TestActivity.ON_INVALID_FORMATION_TOAST;
//import static com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragmentTest.TestActivity.ON_VALID_FORMATION_TOAST;
//import static com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragmentTest.TestActivity.SHOW_LIST_POSITION_PLAYERS_TOAST;
//import static com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragmentTest.TestActivity.SHOW_PLAYER_DETAILS_TOAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 18.08.2016.
 */
@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LineupFormationFragmentTest {

//    @Mock
//    private LineupFormationViewComponent component;
//
//    @Mock
//    private LineupFormationViewPresenter presenter;
//
//    private LineupFormationFragment fragment;
//    private List<Player> players = Arrays.asList(new Player(),
//            new Player(), new Player(), new Player(), new Player(), new Player(), new Player(),
//            new Player(), new Player(), new Player(), new Player());
//    private LineupPlayers lineupPlayers = new LineupPlayers(players);
//    private LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_3_3;
//
//    private final int[] positions_4_4_2 = {R.id.keeper, R.id.leftBack, R.id.rightBack,
//            R.id.leftCentreBack, R.id.rightCentreBack, R.id.leftCentreMidfield,
//            R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
//            R.id.rightCentreForward};
//    private final int[] not_positions_4_4_2 = {R.id.centreCentreBack, R.id.centreCentreMidfield,
//            R.id.attackingMidfield, R.id.centreCentreForward};
//    private final int[] positions_3_2_3_2 = {R.id.keeper, R.id.centreCentreBack,
//            R.id.leftCentreBack, R.id.rightCentreBack, R.id.leftCentreMidfield,
//            R.id.attackingMidfield, R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing,
//            R.id.leftCentreForward, R.id.rightCentreForward};
//    private final int[] not_positions_3_2_3_2 = {R.id.leftBack, R.id.rightBack,
//            R.id.centreCentreMidfield, R.id.centreCentreForward};
//    private final int[] positions_4_2_3_1 = {R.id.keeper, R.id.leftBack, R.id.rightBack,
//            R.id.leftCentreBack, R.id.rightCentreBack, R.id.leftCentreMidfield,
//            R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.attackingMidfield,
//            R.id.centreCentreForward};
//    private final int[] not_positions_4_2_3_1 = {R.id.centreCentreBack, R.id.centreCentreMidfield,
//            R.id.leftCentreForward, R.id.rightCentreForward};
//    private final int[] positions_4_4_3 = {R.id.keeper, R.id.leftBack, R.id.rightBack,
//            R.id.leftCentreBack, R.id.rightCentreBack, R.id.leftCentreMidfield,
//            R.id.rightCentreMidfield, R.id.centreCentreMidfield, R.id.leftCentreForward,
//            R.id.rightCentreForward, R.id.centreCentreForward};
//    private final int[] not_positions_4_3_3 = {R.id.centreCentreBack, R.id.attackingMidfield,
//            R.id.leftWing, R.id.rightWing};
//
//    private Map<Integer, Player> mappedFormation4_4_2;
//    private Map<Integer, Player> mappedFormation3_2_3_2;
//    private Map<Integer, Player> mappedFormation4_2_3_1;
//    private Map<Integer, Player> mappedFormation4_3_3;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        MockApplication application = (MockApplication) RuntimeEnvironment.application;
//        application.setLineupFormationFragmentComponent(component);
//        this.mockDependencies();
//
//        /* Generate the map with players for each formation. */
//        mappedFormation4_4_2 = new HashMap<>();
//        int id = 0;
//        for (int position : positions_4_4_2) {
//            if (id % 3 == 0) {
//                mappedFormation4_4_2.put(position, new Player());
//            } else {
//                mappedFormation4_4_2.put(position, new Player(id, "Player " + id));
//                id++;
//            }
//        }
//        mappedFormation3_2_3_2 = new HashMap<>();
//        id = 0;
//        for (int position : positions_3_2_3_2) {
//            if (id % 3 == 0) {
//                mappedFormation3_2_3_2.put(position, new Player());
//            } else {
//                mappedFormation3_2_3_2.put(position, new Player(id, "Player " + id));
//                id++;
//            }
//        }
//        mappedFormation4_2_3_1 = new HashMap<>();
//        id = 0;
//        for (int position : positions_4_2_3_1) {
//            if (id % 3 == 0) {
//                mappedFormation4_2_3_1.put(position, new Player());
//            } else {
//                mappedFormation4_2_3_1.put(position, new Player(id, "Player " + id));
//                id++;
//            }
//        }
//        mappedFormation4_3_3 = new HashMap<>();
//        id = 0;
//        for (int position : positions_4_4_3) {
//            if (id % 3 == 0) {
//                mappedFormation4_3_3.put(position, new Player());
//            } else {
//                mappedFormation4_3_3.put(position, new Player(id, "Player " + id));
//                id++;
//            }
//        }
//    }
//
//    /**
//     * Mock the dependencies for the fragment.
//     */
//    private void mockDependencies() {
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                LineupFormationFragment fragment = (LineupFormationFragment)
//                        invocation.getArguments()[0];
//                fragment.setPresenter(presenter);
//                return null;
//            }
//        }).when(component).inject(any(LineupFormationFragment.class));
//    }
//
//    /**
//     * Test the behavior when newInstance is called with invalid players size.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testNewInstanceWithInvalidLineupPlayerSize() {
//        LineupPlayers lineupPlayers = new LineupPlayers(Arrays
//                .asList(new Player(), new Player()));
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//    }
//
//    /**
//     * Test that fragment is successfully created when newInstance is called with LineupPlayers.
//     */
//    @Test
//    public void testFragmentIsCreatedWithLineupPlayer() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment);
//        Bundle args = fragment.getArguments();
//        assertNotNull(args);
//        assertSame(lineupPlayers, args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY));
//        verify(presenter).onViewCreated(args);
//    }
//
//    /**
//     * Test the behavior when new instance is called with null formation
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testNewInstanceOnNullFormation() {
//        LineupFormationFragment.newInstance(null, new ArrayList<Player>());
//    }
//
//    /**
//     * Test the behavior when newInstance is called with null list of players.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testNewInstanceOnNullListOfPlayers() {
//        LineupFormationFragment.newInstance(LineupUtils.FORMATION.F_4_4_2, null);
//    }
//
//    /**
//     * Test that fragment is successfully created when newInstance is called with formation.
//     */
//    @Test
//    public void testFragmentIsCreatedWithFormationAndListOfPlayers() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        fragment = LineupFormationFragment.newInstance(formation, players);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment);
//        Bundle args = fragment.getArguments();
//        assertNotNull(args);
//        assertSame(formation, args.getSerializable(LineupFormationFragment.FORMATION_KEY));
//        Serializable ser = args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY);
//        assertTrue(ser instanceof LineupPlayers);
//        assertSame(players, ((LineupPlayers) ser).getPlayers());
//        verify(presenter).onViewCreated(args);
//    }
//
//    /**
//     * Assert that the view children  are correctly injected by
//     * ButterKnife for the given formation.
//     *
//     * @param fragment     LineupFormationFragment to be checked
//     * @param checkBinding whatever the view binding with the player should be checked
//     * @param formation    Lineup formation
//     * @param map          Map containing the players
//     */
//    private void assertFragmentView(LineupFormationFragment fragment,
//                                    LineupUtils.FORMATION formation, boolean checkBinding,
//                                    Map<Integer, Player> map) {
//        int[] positions;
//        int[] notPositions;
//        switch (formation) {
//            case F_4_4_2:
//                positions = positions_4_4_2;
//                notPositions = not_positions_4_4_2 ;
//                break;
//            case F_3_2_3_2:
//                positions = positions_3_2_3_2;
//                notPositions = not_positions_3_2_3_2;
//                break;
//            case F_4_2_3_1:
//                positions = positions_4_2_3_1;
//                notPositions = not_positions_4_2_3_1;
//                break;
//            case F_4_3_3:
//                positions = positions_4_4_3;
//                notPositions = not_positions_4_3_3;
//                break;
//            default:
//                throw new IllegalArgumentException("invalid formation");
//        }
//
//        View view = fragment.getView();
//        assertNotNull(view);
//
//        for (int position : positions) {
//            TextView txtView = (TextView) view.findViewById(position);
//            assertNotNull(txtView);
//            if (checkBinding) {
//                this.assertViewBinding(txtView, fragment, map);
//            }
//        }
//        for (int position : notPositions) {
//            TextView txtView = (TextView) view.findViewById(position);
//            assertNull(txtView);
//        }
//    }
//
//    /**
//     * Assert that the view is correctly binned with the player.
//     *
//     * @param view     view to be checked
//     * @param fragment Fragment containing the view
//     * @param map      Map containing the players
//     */
//    private void assertViewBinding(TextView view, LineupFormationFragment fragment,
//                                   Map<Integer, Player> map) {
//        Player player = map.get(view.getId());
//        if (player.getName() == null) {
//            assertEquals("", view.getText());
//        } else {
//            assertEquals(player.getName(), view.getText());
//        }
//        assertSame(fragment, shadowOf(view).getOnClickListener());
//    }
//
//    /**
//     * Test that the correct view is inflated when lineup formation is 4-4-2.
//     */
//    @Test
//    public void testOnViewCreatedOn4_4_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        View view = fragment.getView();
//        assertNotNull(view);
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_4_2, false, null);
//    }
//
//    /**
//     * Test that the correct view is inflated when the lineup formation is 3-2-3-2.
//     */
//    @Test
//    public void testOnViewCreatedOn3_2_3_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
//        fragment = LineupFormationFragment.newInstance(LineupUtils.FORMATION.F_3_2_3_2);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        View view = fragment.getView();
//        assertNotNull(view);
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_3_2_3_2, false, null);
//    }
//
//    /**
//     * Test that the correct view is inflated when the lineup formation is 4-2-3-1.
//     */
//    @Test
//    public void testOnViewCreatedOn4_2_3_1Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        View view = fragment.getView();
//        assertNotNull(view);
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_2_3_1, false, null);
//    }
//
//    /**
//     * Test that the correct view is inflated when the formation is 4-3-3.
//     */
//    @Test
//    public void testOnVIewCreatedOn4_3_3Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        View view = fragment.getView();
//        assertNotNull(view);
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_3_3, false, null);
//    }
//
//    /**
//     * Test the behavior on onCreateView when the presenter provides invalid formation.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnViewCreatedWithInvalidFormation() {
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//    }
//
//    /**
//     * Test that the view is correctly binned with the players when the formation is 4-4-2.
//     */
//    @Test
//    public void testBindPlayersOn4_4_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Integer id = (Integer) invocation.getArguments()[0];
//                Player player = mappedFormation4_4_2.get(id);
//                if (player == null) {
//                    throw new IllegalArgumentException
//                            ("error getPlayerAt called with un existing id");
//                }
//                return player.getName();
//            }
//        }).when(presenter).getPlayerAt(anyInt());
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        fragment.bindPlayers();
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_4_2,
//                true, mappedFormation4_4_2);
//    }
//
//    /**
//     * Test that the view is correctly binned with the players when the formation is 3-2-3-2.
//     */
//    @Test
//    public void testBindPlayersOn3_2_3_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Integer id = (Integer) invocation.getArguments()[0];
//                Player player = mappedFormation3_2_3_2.get(id);
//                if (player == null) {
//                    throw new IllegalArgumentException
//                            ("error getPlayerAt called with un existing id");
//                }
//                return player.getName();
//            }
//        }).when(presenter).getPlayerAt(anyInt());
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        fragment.bindPlayers();
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_3_2_3_2,
//                true, mappedFormation3_2_3_2);
//    }
//
//    /**
//     * Test that the view is correctly binned with the players when the formation is 4-2-3-1.
//     */
//    @Test
//    public void testBindPlayersOn4_2_3_1Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Integer id = (Integer) invocation.getArguments()[0];
//                Player player = mappedFormation4_2_3_1.get(id);
//                if (player == null) {
//                    throw new IllegalArgumentException
//                            ("error getPlayerAt called with un existing id");
//                }
//                return player.getName();
//            }
//        }).when(presenter).getPlayerAt(anyInt());
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        fragment.bindPlayers();
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_2_3_1,
//                true, mappedFormation4_2_3_1);
//    }
//
//    /**
//     * Test that the view is correctly binned with the players when the formation is 4-3-3.
//     */
//    @Test
//    public void testBindPlayersOn4_3_3Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                Integer id = (Integer) invocation.getArguments()[0];
//                Player player = mappedFormation4_3_3.get(id);
//                if (player == null) {
//                    throw new IllegalArgumentException
//                            ("error getPlayerAt called with un existing id");
//                }
//                return player.getName();
//            }
//        }).when(presenter).getPlayerAt(anyInt());
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        fragment.bindPlayers();
//        this.assertFragmentView(fragment, LineupUtils.FORMATION.F_4_3_3,
//                true, mappedFormation4_3_3);
//    }
//
//    /**
//     * Test the behavior when onClick method is called on 4-4-2 formation.
//     */
//    @Test
//    public void testOnClickOn4_4_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        fragment = LineupFormationFragment.newInstance(LineupUtils.FORMATION.F_4_4_2);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment.getView());
//        fragment.bindPlayers();
//        for (int position : positions_4_4_2) {
//            View view = fragment.getView().findViewById(position);
//            assertNotNull(view);
//            view.performClick();
//            verify(presenter).onPlayerClick(position);
//        }
//    }
//
//    /**
//     * Test the behavior when on onClick is called on 3-2-3-2 formation.
//     */
//    @Test
//    public void testOnClickOn3_2_3_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment.getView());
//        fragment.bindPlayers();
//        for (int position : positions_3_2_3_2) {
//            View view = fragment.getView().findViewById(position);
//            assertNotNull(view);
//            view.performClick();
//            verify(presenter).onPlayerClick(position);
//        }
//    }
//
//    /**
//     * Test the behavior when on onClick is called on 4-2-3-1 formation.
//     */
//    @Test
//    public void testOnClickOn4_2_3_1Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment.getView());
//        fragment.bindPlayers();
//        for (int position : positions_4_2_3_1) {
//            View view = fragment.getView().findViewById(position);
//            assertNotNull(view);
//            view.performClick();
//            verify(presenter).onPlayerClick(position);
//        }
//    }
//
//    /**
//     * Test the behavior when onClick method is called on 4-3-3 formation.
//     */
//    @Test
//    public void testOnClickOn4_3_3Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment);
//        assertNotNull(fragment.getView());
//        fragment.bindPlayers();
//        for (int position : positions_4_4_3) {
//            View view = fragment.getView().findViewById(position);
//            assertNotNull(view);
//            view.performClick();
//            verify(presenter).onPlayerClick(position);
//        }
//    }
//
//    /**
//     * Test the behavior when showListPositionPlayersView is called.
//     */
//    @Test
//    public void testShowListPositionPlayersFragment() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment, TestActivity.class, 0);
//        fragment.showListPositionPlayersView(null, null);
//        assertEquals(SHOW_LIST_POSITION_PLAYERS_TOAST, ShadowToast.getTextOfLatestToast());
//    }
//
//    /**
//     * Test the behavior when showPlayerDetailsDialog is called.
//     */
//    @Test
//    public void testShowPlayerDetailsDialog() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment, TestActivity.class, 0);
////        fragment.showPlayerDetailsDialog(1, true);
//        assertEquals(SHOW_PLAYER_DETAILS_TOAST, ShadowToast.getTextOfLatestToast());
//    }
//
//    /**
//     * Test the behavior when lineupInvalid is called.
//     */
//    @Test
//    public void testFormationInvalid() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment, TestActivity.class, 0);
////        fragment.lineupInvalid();
//        assertEquals(ON_INVALID_FORMATION_TOAST, ShadowToast.getTextOfLatestToast());
//    }
//
//    /**
//     * Test the behavior when lineupValid is called.
//     */
//    @Test
//    public void testFormationValid() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startVisibleFragment(fragment, TestActivity.class, 0);
////        fragment.lineupValid();
//        assertEquals(ON_VALID_FORMATION_TOAST, ShadowToast.getTextOfLatestToast());
//    }
//
//    /**
//     * Test activity used so that the method that calls the listener can be verified.
//     */
//    public static class TestActivity extends AppCompatActivity
//            implements LineupFormationFragment.Listener {
//
//        static final String SHOW_LIST_POSITION_PLAYERS_TOAST = "Show List position players";
//        static final String SHOW_PLAYER_DETAILS_TOAST = "Show player details";
//        static final String ON_VALID_FORMATION_TOAST = "On Valid Formation";
//        static final String ON_INVALID_FORMATION_TOAST = "On Invalid Formation";
//
//        @Override
//        public void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
//                                                    int[] playersToExclude) {
//            Toast.makeText(this, SHOW_LIST_POSITION_PLAYERS_TOAST, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void showPlayerDetailsDialog(int id, boolean editable) {
//            Toast.makeText(this, SHOW_PLAYER_DETAILS_TOAST, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void showValidLineup() {
//            Toast.makeText(this, ON_VALID_FORMATION_TOAST, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void showInvalidLineup() {
//            Toast.makeText(this, ON_INVALID_FORMATION_TOAST, Toast.LENGTH_SHORT).show();
//        }
//    }
}