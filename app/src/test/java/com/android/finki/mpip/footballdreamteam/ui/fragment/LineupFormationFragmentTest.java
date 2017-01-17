package com.android.finki.mpip.footballdreamteam.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.helpers.SerializableList;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.FORMATION_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_EDITABLE_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_PLAYERS_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LIST_PLAYERS_KEY;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_PLACE;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.formation_3_2_3_2_resourcesIds;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.formation_4_2_3_1_resourcesIds;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.formation_4_3_3_resourcesIds;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.formation_4_4_2_resourcesIds;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.resourcesIds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 18.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class LineupFormationFragmentTest {

    @Mock
    private LineupFormationViewComponent component;

    @Mock
    private LineupFormationViewPresenter presenter;

    @Mock
    private static BaseFragment.Listener bfListener;

    @Mock
    private static LineupFormationFragment.Listener lffListener;

    private LineupFormationFragment fragment;

    private List<Player> players = Arrays.asList(new Player(),
            new Player(), new Player(), new Player(), new Player(), new Player(), new Player(),
            new Player(), new Player(), new Player(), new Player());
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();

        /* Generate the map with players for each formation. */
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
    }

    /**
     * Mock the dependencies for the fragment.
     */
    private void mockDependencies() {
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setLineupFormationFragmentComponent(component);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupFormationFragment fragment =
                        (LineupFormationFragment) invocation.getArguments()[0];
                fragment.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(LineupFormationFragment.class));
    }

    /**
     * Test the behavior when newInstance is called with null list of players.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNullListPlayersParam() {
        LineupFormationFragment.newInstance(null, false);
    }

    /**
     * Test the behavior when newInstance is called with invalid players size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithInvalidLineupPlayerSize() {
        List<Player> players = Arrays.asList(new Player(), new Player());
        LineupFormationFragment.newInstance(players, true);
    }

    /**
     * TEst that newInstance will correctly put the List of players and the editable oeption
     * in the fragment arguments.
     */
    @Test
    public void testNewInstanceWithListOfPlayers() {
        when(presenter.getFormation()).thenReturn(FORMATION.F_4_4_2);
        final boolean editable = true;
        fragment = LineupFormationFragment.newInstance(players, editable);
        SupportFragmentTestUtil.startFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        SerializableList serList = (SerializableList) args.get(LINEUP_PLAYERS_KEY);
        assertNotNull(serList);
        assertSame(serList.getList(), players);
        assertSame(editable, args.getBoolean(LINEUP_EDITABLE_KEY));
        verify(presenter).onViewCreated(args);
    }

    /**
     * Test the behavior when new instance is called with null formation
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWIthFormationAndListAndNullFormation() {
        LineupFormationFragment.newInstance(null, new ArrayList<Player>());
    }

    /**
     * Test the behavior when newInstance is called with null list of players.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithFOrmationAndListAndNullListOfPlayers() {
        LineupFormationFragment.newInstance(FORMATION.F_4_4_2, null);
    }

    /**
     * Test that fragment is successfully created when newInstance is called with formation.
     */
    @Test
    public void testNewInstanceWithFormationAndListOfPlayers() {
        when(presenter.getFormation()).thenReturn(FORMATION.F_4_4_2);
        final FORMATION formation = FORMATION.F_3_2_3_2;
        fragment = LineupFormationFragment.newInstance(formation, players);
        SupportFragmentTestUtil.startFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(formation, args.getSerializable(FORMATION_KEY));
        SerializableList serList = (SerializableList) args.getSerializable(LIST_PLAYERS_KEY);
        assertNotNull(serList);
        assertSame(players, serList.getList());
        verify(presenter).onViewCreated(args);
    }

    /**
     * Test the behavior when newInstance is called only with formation and the
     * formation is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithOnlyANullFormation() {
        LineupFormationFragment.newInstance(null);
    }

    /**
     * Test that fragment is successfully created when called with only a formation.
     */
    @Test
    public void testNewInstanceWithOnlyAFormation() {
        when(presenter.getFormation()).thenReturn(FORMATION.F_4_4_2);
        final FORMATION formation = FORMATION.F_3_2_3_2;
        fragment = LineupFormationFragment.newInstance(formation);
        SupportFragmentTestUtil.startFragment(fragment);
        Bundle args = fragment.getArguments();
        assertNotNull(args);
        assertSame(formation, args.getSerializable(FORMATION_KEY));
        verify(presenter).onViewCreated(args);
    }

    /**
     * Test the behavior when fragment is created with a activity class that implements
     * BaseFragment.Listener.
     */
    @Test
    public void testFragmentIsCreatedWithBaseFragmentListener() {
        when(presenter.getFormation()).thenReturn(FORMATION.F_4_4_2);
        fragment = LineupFormationFragment.newInstance(FORMATION.F_3_2_3_2);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        verify(bfListener).onFragmentActive();
    }

    /**
     * Assert that the view children  are correctly injected by
     * ButterKnife for the given formation.
     *
     * @param fragment     LineupFormationFragment to be checked
     * @param checkBinding whatever the view binding with the player should be checked
     * @param formation    Lineup formation
     * @param map          Map containing the players
     */
    private void assertFragmentView(LineupFormationFragment fragment, FORMATION formation,
                                    boolean checkBinding, Map<Integer, Player> map) {
        int[] positions;
        ArrayList<Integer> notPositions;
        switch (formation) {
            case F_4_4_2:
                positions = formation_4_4_2_resourcesIds;
                break;
            case F_3_2_3_2:
                positions = formation_3_2_3_2_resourcesIds;
                break;
            case F_4_2_3_1:
                positions = formation_4_2_3_1_resourcesIds;
                break;
            case F_4_3_3:
                positions = formation_4_3_3_resourcesIds;
                break;
            default:
                throw new IllegalArgumentException("invalid formation");
        }
        notPositions = this.getDifference(resourcesIds, positions);

        View view = fragment.getView();
        assertNotNull(view);

        for (int position : positions) {
            TextView txtView = (TextView) view.findViewById(position);
            assertNotNull(txtView);
            if (checkBinding) {
                this.assertViewBinding(txtView, fragment, map);
            }
        }

        for (int position : notPositions) {
            TextView txtView = (TextView) view.findViewById(position);
            assertNull(txtView);
        }
    }

    /**
     * Get all the elements that are in the array 1 and not in the array 2.
     *
     * @param res1 first array with resource ids
     * @param res2 second array with resource ids
     * @return array difference
     */
    private ArrayList<Integer> getDifference(int[] res1, int[] res2) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int aRes1 : res1) {
            int j;
            for (j = 0; j < res2.length; j++) {
                if (aRes1 == res2[j]) {
                    break;
                }
            }
            if (j == res2.length) {
                result.add(aRes1);
            }
        }
        return result;
    }

    /**
     * Assert that the view is correctly binned with the player.
     *
     * @param view     view to be checked
     * @param fragment Fragment containing the view
     * @param map      Map containing the players
     */
    private void assertViewBinding(TextView view, LineupFormationFragment fragment,
                                   Map<Integer, Player> map) {
        Player player = map.get(view.getId());
        if (player.getName() == null) {
            assertEquals("", view.getText());
        } else {
            assertEquals(player.getName(), view.getText());
        }

        assertSame(fragment, shadowOf(view).getOnClickListener());
        view.performClick();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        verify(presenter).onPlayerClick(view.getId(), location[0], location[1]);
    }

    /**
     * Test that the correct view is inflated when lineup formation is 4-4-2.
     */
    @Test
    public void testOnViewCreatedOn4_4_2Formation() {
        final FORMATION formation = FORMATION.F_4_3_3;
        when(presenter.getFormation()).thenReturn(formation);
        fragment = LineupFormationFragment.newInstance(formation);
        SupportFragmentTestUtil.startFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        this.assertFragmentView(fragment, formation, false, null);
    }

    /**
     * Test that the correct view is inflated when the lineup formation is 3-2-3-2.
     */
    @Test
    public void testOnViewCreatedOn3_2_3_2Formation() {
        final FORMATION formation = FORMATION.F_3_2_3_2;
        when(presenter.getFormation()).thenReturn(formation);
        fragment = LineupFormationFragment.newInstance(formation);
        SupportFragmentTestUtil.startFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        this.assertFragmentView(fragment, formation, false, null);
    }

    /**
     * Test that the correct view is inflated when the lineup formation is 4-2-3-1.
     */
    @Test
    public void testOnViewCreatedOn4_2_3_1Formation() {
        final FORMATION formation = FORMATION.F_4_2_3_1;
        when(presenter.getFormation()).thenReturn(formation);
        fragment = LineupFormationFragment.newInstance(formation);
        SupportFragmentTestUtil.startFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        this.assertFragmentView(fragment, formation, false, null);
    }

    /**
     * Test that the correct view is inflated when the formation is 4-3-3.
     */
    @Test
    public void testOnVIewCreatedOn4_3_3Formation() {
        final FORMATION formation = FORMATION.F_4_3_3;
        when(presenter.getFormation()).thenReturn(formation);
        fragment = LineupFormationFragment.newInstance(formation);
        SupportFragmentTestUtil.startFragment(fragment);
        View view = fragment.getView();
        assertNotNull(view);
        this.assertFragmentView(fragment, formation, false, null);
    }

    /**
     * Test the behavior on onCreateView when the presenter provides invalid formation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithInvalidFormation() {
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment);
    }

    /**
     * Generate a HashMap for the given formation and mock the presenter to return specific
     * value for the method getPlayerAt.
     *
     * @param formation   formation for which the map will be generated
     * @param resourceIds all the resource ids that are in that formation
     * @return generated HashMap
     */
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Player> generatePlayersMap(FORMATION formation, int[] resourceIds) {
        when(presenter.getFormation()).thenReturn(formation);
        final Map<Integer, Player> result = new HashMap<>();
        int i = 0;
        for (int resourceId : resourceIds) {
            if (i % 3 == 0) {
                result.put(resourceId, new Player(i, "Player" + i));
            } else {
                result.put(resourceId, new Player());
                i++;
            }
        }

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer resourceId = (Integer) invocation.getArguments()[0];
                Player player = result.get(resourceId);
                if (player == null) {
                    String msg = "error getPlayerAt called with un existing id";
                    throw new IllegalArgumentException(msg);
                }
                return player.getName();
            }
        }).when(presenter).getPlayerAt(anyInt());

        return result;
    }

    /**
     * Test that the view is correctly binned with the players when the formation is 4-4-2.
     */
    @Test
    public void testBindPlayersOn4_4_2Formation() {
        final FORMATION formation = FORMATION.F_4_4_2;
        Map<Integer, Player> map =
                this.generatePlayersMap(formation, formation_4_4_2_resourcesIds);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.bindPlayers();
        this.assertFragmentView(fragment, formation, true, map);
    }

    /**
     * Test that the view is correctly binned with the players when the formation is 3-2-3-2.
     */
    @Test
    public void testBindPlayersOn3_2_3_2Formation() {
        final FORMATION formation = FORMATION.F_3_2_3_2;
        Map<Integer, Player> map =
                this.generatePlayersMap(formation, formation_3_2_3_2_resourcesIds);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.bindPlayers();
        this.assertFragmentView(fragment, formation, true, map);
    }

    /**
     * Test that the view is correctly binned with the players when the formation is 4-2-3-1.
     */
    @Test
    public void testBindPlayersOn4_2_3_1Formation() {
        final FORMATION formation = FORMATION.F_4_2_3_1;
        Map<Integer, Player> map =
                this.generatePlayersMap(formation, formation_4_2_3_1_resourcesIds);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.bindPlayers();
        this.assertFragmentView(fragment, formation, true, map);
    }

    /**
     * Test that the view is correctly binned with the players when the formation is 4-3-3.
     */
    @Test
    public void testBindPlayersOn4_3_3Formation() {
        final FORMATION formation = FORMATION.F_4_3_3;
        Map<Integer, Player> map =
                this.generatePlayersMap(formation, formation_4_3_3_resourcesIds);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment);
        fragment.bindPlayers();
        this.assertFragmentView(fragment, formation, true, map);
    }

//    /**
//     * Test the behavior when onClick method is called on 4-4-2 formation.
//     */
//    @Test
//    public void testOnClickOn4_4_2Formation() {
//        final FORMATION formation = FORMATION.F_4_4_2;
//        when(presenter.getFormation()).thenReturn(formation);
//        fragment = LineupFormationFragment.newInstance(formation);
//        SupportFragmentTestUtil.startFragment(fragment);
//        fragment.bindPlayers();
//        this.assertViewClick(fragment, formation_4_4_2_resourcesIds);
//    }
//
//    /**
//     * Test the behavior when on onClick is called on 3-2-3-2 formation.
//     */
//    @Test
//    public void testOnClickOn3_2_3_2Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startFragment(fragment);
//        fragment.bindPlayers();
//        this.assertViewClick(fragment, formation_3_2_3_2_resourcesIds);
//    }
//
//    /**
//     * Test the behavior when on onClick is called on 4-2-3-1 formation.
//     */
//    @Test
//    public void testOnClickOn4_2_3_1Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startFragment(fragment);
//        fragment.bindPlayers();
//
//    }
//
//    /**
//     * Test the behavior when onClick method is called on 4-3-3 formation.
//     */
//    @Test
//    public void testOnClickOn4_3_3Formation() {
//        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        fragment = LineupFormationFragment.newInstance(lineupPlayers);
//        SupportFragmentTestUtil.startFragment(fragment);
//        assertNotNull(fragment.getView());
//        fragment.bindPlayers();
//        for (int position : positions_4_4_3) {
//            View view = fragment.getView().findViewById(position);
//            assertNotNull(view);
//            view.performClick();
//            verify(presenter).onPlayerClick(position);
//        }
//    }

    /**
     * Test the behavior when showListPositionPlayersView is called.
     */
    @Test
    public void testShowListPositionPlayersFragment() {
        final POSITION_PLACE place = POSITION_PLACE.KEEPERS;
        final int[] playersToExclude = {1, 2, 3};
        final int startX = 0;
        final int startY = 0;
        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.showListPositionPlayersView(place, playersToExclude, startX, startY);
        verify(lffListener)
                .showListPositionPlayersFragment(place, playersToExclude, startX, startY);
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called.
     */
    @Test
    public void testShowPlayerDetailsDialog() {
        final int id = 123;
        final boolean editable = false;
        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
        fragment = LineupFormationFragment.newInstance(players, editable);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.showPlayerDetailsView(id, editable);
        verify(lffListener).showPlayerDetailsDialog(id, editable);
    }

    /**
     * Test the behavior when lineupInvalid is called.
     */
    @Test
    public void testFormationInvalid() {
        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_3_3);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.showInvalidLineup();
        verify(lffListener).showInvalidLineup();
    }

    /**
     * Test the behavior when lineupValid is called.
     */
    @Test
    public void testFormationValid() {
        when(presenter.getFormation()).thenReturn(FORMATION.F_4_3_3);
        fragment = LineupFormationFragment.newInstance(players, false);
        SupportFragmentTestUtil.startFragment(fragment, MockActivity.class);
        fragment.showValidLineup();
        verify(lffListener).showValidLineup();
    }

    /**
     * Mock activity class for the fragment.
     */
    public static class MockActivity extends AppCompatActivity
            implements BaseFragment.Listener,
            LineupFormationFragment.Listener {

        @Override
        public void onFragmentActive() {
            bfListener.onFragmentActive();
        }

        @Override
        public void changeTitle(String title) {
            bfListener.changeTitle(title);
        }

        @Override
        public void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                                    int[] playersToExclude, int startX,
                                                    int startY) {
            lffListener.showListPositionPlayersFragment(place, playersToExclude, startX, startY);
        }

        @Override
        public void showPlayerDetailsDialog(int id, boolean editable) {
            lffListener.showPlayerDetailsDialog(id, editable);
        }

        @Override
        public void showValidLineup() {
            lffListener.showValidLineup();
        }

        @Override
        public void showInvalidLineup() {
            lffListener.showInvalidLineup();
        }
    }
}