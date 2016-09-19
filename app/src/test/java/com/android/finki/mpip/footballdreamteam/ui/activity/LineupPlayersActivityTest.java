package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowDialog;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
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
public class LineupPlayersActivityTest {

//    @Mock
//    private LineupPlayersViewComponent component;
//
//    @Mock
//    private LineupPlayersViewPresenter presenter;
//
//    @Mock
//    private LineupFormationViewComponent lineupFormationFragmentComponent;
//
//    @Mock
//    private LineupFormationViewPresenter lineupFormationFragmentPresenter;
//
//    @Mock
//    private ListPositionPlayersViewComponent listPositionPlayersFragmentComponent;
//
//    @Mock
//    private ListPositionPlayersViewPresenter listPositionPlayersFragmentPresenter;
//
//    @Mock
//    private PlayerDetailsViewComponent playerDetailsDialogComponent;
//
//    @Mock
//    private PlayerDetailsViewPresenter playerDetailsDialogPresenter;
//
//    private LineupPlayersActivity activity;
//    private FragmentManager manager;
//
//    private RelativeLayout spinner;
//    private TextView txtSpinner;
//    private RelativeLayout errorLayout;
//    private RelativeLayout mainContent;
//    private LinearLayout updateLineupErrorLayout;
//
//    private List<Player> players = Arrays.asList(new Player(), new Player(), new Player(),
//            new Player(), new Player(), new Player(), new Player(), new Player(), new Player(),
//            new Player(), new Player());
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        this.mockDependencies();
//        MockApplication application = (MockApplication) RuntimeEnvironment.application;
//        application.setLineupPlayerActivityComponent(component);
//        application.setLineupFormationFragmentComponent(lineupFormationFragmentComponent);
//        application.setListPositionPlayersFragmentComponent(listPositionPlayersFragmentComponent);
//        application.setPlayerDetailsDialogComponent(playerDetailsDialogComponent);
//        activity = Robolectric.setupActivity(LineupPlayersActivity.class);
//        manager = activity.getSupportFragmentManager();
//
//        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
//        assertNotNull(spinner);
//        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
//        assertNotNull(txtSpinner);
//        errorLayout = (RelativeLayout) activity.findViewById(R.id.error);
//        assertNotNull(errorLayout);
//        mainContent = (RelativeLayout) activity
//                .findViewById(R.id.lineupPlayersPlayers_mainContent);
//        assertNotNull(mainContent);
//        updateLineupErrorLayout = (LinearLayout) activity
//                .findViewById(R.id.lineupPlayersActivity_updateFailed);
//        assertNotNull(updateLineupErrorLayout);
//    }
//
//    /**
//     * Mock the dependencies for the activity and fragment that the activity is starting.
//     */
//    private void mockDependencies() {
////        when(lineupFormationFragmentPresenter.getFormation())
////                .thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
////        doAnswer(new Answer() {
////            @Override
////            public Object answer(InvocationOnMock invocation) throws Throwable {
////                LineupPlayersActivity activity = (LineupPlayersActivity)
////                        invocation.getArguments()[0];
////                activity.presenter = presenter;
////                return null;
////            }
////        }).when(component).inject(any(LineupPlayersActivity.class));
//
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                LineupFormationFragment fragment = (LineupFormationFragment)
//                        invocation.getArguments()[0];
//                fragment.setPresenter(lineupFormationFragmentPresenter);
//                return null;
//            }
//        }).when(lineupFormationFragmentComponent).inject(any(LineupFormationFragment.class));
//
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                ListPositionPlayersFragment fragment = (ListPositionPlayersFragment)
//                        invocation.getArguments()[0];
//                fragment.setPresenter(listPositionPlayersFragmentPresenter);
//                return null;
//            }
//        }).when(listPositionPlayersFragmentComponent)
//                .inject(any(ListPositionPlayersFragment.class));
//
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                PlayerDetailsDialog dialog = (PlayerDetailsDialog) invocation.getArguments()[0];
//                dialog.setPresenter(playerDetailsDialogPresenter);
//                return null;
//            }
//        }).when(playerDetailsDialogComponent).inject(any(PlayerDetailsDialog.class));
//    }
//
//    /**
//     * Test that the activity is correctly created.
//     */
//    @Test
//    public void testActivityIsCreated() {
//        String title = activity.getString(R.string.lineupPlayersActivity_title);
//        assertNotNull(activity.getSupportActionBar());
//        assertEquals(title, activity.getSupportActionBar().getTitle());
////        verify(presenter).loadPlayers(activity.getIntent().getExtras());
//        ButtonAwesome btn = (ButtonAwesome) activity
//                .findViewById(R.id.lineupPlayersLayout_btnChangeFormation);
//        assertNotNull(btn);
//        assertSame(activity, shadowOf(btn).getOnCreateContextMenuListener());
//    }
//
//    /**
//     * Test the behavior when onCrateOptionsMenu is called and the LineupFormationFragmentFragment
//     * is not visible.
//     */
//    @Test
//    public void testOnCreateOptionsMenuWhenLineupFormationFragmentIsNotVisible() {
//        activity.invalidateOptionsMenu();
//        MenuItem item = shadowOf(activity).getOptionsMenu().findItem(R.id.lineupMenu_save);
//        assertNotNull(item);
//        assertFalse(item.isVisible());
//    }
//
//    /**
//     * Test the behavior when onCrateOptionsMenu is called and the lineup is not valid.
//     */
//    @Test
//    public void testOnCreateOptionsMenuWhenLineupIsNotValid() {
//        activity.showLoadingSuccess(players);
//        when(presenter.isChanged()).thenReturn(true);
//        when(presenter.isLineupValid()).thenReturn(false);
//        activity.invalidateOptionsMenu();
//        MenuItem item = shadowOf(activity).getOptionsMenu().findItem(R.id.lineupMenu_save);
//        assertNotNull(item);
//        assertFalse(item.isVisible());
//    }
//
//    /**
//     * Test that behavior when context item with id 4_4_2 is selected.
//     */
//    @Test
//    public void testContextItemSelectedWithId4_4_2() {
//        MenuItem item = new RoboMenuItem(R.id.formation_4_4_2);
//        activity.onContextItemSelected(item);
//        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_4_2);
//    }
//
//    /**
//     * Test the behavior when context item with id 3_2_3_2 is selected.
//     */
//    @Test
//    public void testContextItemSelectedWithId3_2_3_2() {
//        MenuItem item = new RoboMenuItem(R.id.formation_3_2_3_2);
//        activity.onContextItemSelected(item);
//        verify(presenter).updateFormation(LineupUtils.FORMATION.F_3_2_3_2);
//    }
//
//    /**
//     * Test the behavior when context item with id 4_2_3_1 is selected.
//     */
//    @Test
//    public void testContextItemSelectedWithId4_2_3_1() {
//        MenuItem item = new RoboMenuItem(R.id.formation_4_2_3_1);
//        activity.onContextItemSelected(item);
//        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_2_3_1);
//    }
//
//    /**
//     * Test the behavior when context item with id 4_3_3 is selected.
//     */
//    @Test
//    public void testContextItemSelectedWithId4_3_3() {
//        MenuItem item = new RoboMenuItem(R.id.formation_4_3_3);
//        activity.onContextItemSelected(item);
//        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_3_3);
//    }
//
//    /**
//     * Test the behavior when showBtnChangeFormation is called.
//     */
//    @Test
//    public void testShowBtnChangeFormation() {
//        Button button = (Button) activity
//                .findViewById(R.id.lineupPlayersLayout_btnChangeFormation);
//        assertNotNull(button);
//        assertEquals(View.GONE, button.getVisibility());
////        activity.showBtnChangeFormation();
//        assertEquals(View.VISIBLE, button.getVisibility());
//    }
//
//    /**
//     * Test the behavior on the activity when showLoading method is called.
//     */
//    @Test
//    public void testShowLoading() {
//        errorLayout.setVisibility(View.VISIBLE);
//        mainContent.setVisibility(View.VISIBLE);
//        updateLineupErrorLayout.setVisibility(View.VISIBLE);
//        activity.showLoading();
//        String text = activity.getString(R.string.lineupPlayersActivity_spinnerLoadPlayers_text);
//        assertEquals(text, txtSpinner.getText());
//        assertEquals(View.VISIBLE, spinner.getVisibility());
//        assertEquals(View.GONE, errorLayout.getVisibility());
//        assertEquals(View.GONE, mainContent.getVisibility());
//        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
//    }
//
//    /**
//     * Test the behavior on the activity when showLoadingFailed method is called.
//     */
//    @Test
//    public void testShowLoadingFailed() {
//        spinner.setVisibility(View.VISIBLE);
//        mainContent.setVisibility(View.VISIBLE);
//        updateLineupErrorLayout.setVisibility(View.VISIBLE);
//        activity.showLoadingFailed();
//        assertEquals(View.VISIBLE, errorLayout.getVisibility());
//        assertEquals(View.GONE, spinner.getVisibility());
//        assertEquals(View.GONE, mainContent.getVisibility());
//        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
//    }
//
//    /**
//     * Test the behavior on the activity when btn "Try Again" is clicked.
//     */
//    @Test
//    public void testBtnTryAgainClick() {
//        Button btn = (Button) activity.findViewById(R.id.error_btnTryAgain);
//        assertNotNull(btn);
//        btn.performClick();
////        verify(presenter, times(2)).loadPlayers(activity.getIntent().getExtras());
//    }
//
//    /**
//     * Test the behavior on the activity when showLoadingSuccess method is called.
//     */
//    @Test
//    public void testShowLoadingSuccess() {
//        spinner.setVisibility(View.VISIBLE);
//        errorLayout.setVisibility(View.VISIBLE);
//        updateLineupErrorLayout.setVisibility(View.VISIBLE);
//        activity.showLoadingSuccess(players);
//        assertEquals(View.VISIBLE, mainContent.getVisibility());
//        assertEquals(View.GONE, spinner.getVisibility());
//        assertEquals(View.GONE, errorLayout.getVisibility());
//        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.content);
//        assertNotNull(fragment);
//        assertTrue(fragment instanceof LineupFormationFragment);
//    }
//
//    /**
//     * Test the behavior when onCrateOptionsMenu is called and the lineup is not changed.
//     */
//    @Test
//    public void testOnCreateOptionsMenuWhenLineupIsNotChanged() {
//        activity.showLoadingSuccess(players);
//        when(presenter.isChanged()).thenReturn(false);
//        activity.invalidateOptionsMenu();
//        MenuItem item = shadowOf(activity).getOptionsMenu().findItem(R.id.lineupMenu_save);
//        assertNotNull(item);
//        assertFalse(item.isVisible());
//    }
//
//    /**
//     * Test the behavior on the activity when showListPositionPlayersView method is called and
//     * LineupFormationFragment is not yet displayed.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testShowListPositionPlayerFragmentWhenLineupFormationFragmentNotSet() {
//        activity.showListPositionPlayersFragment(null, new int[]{});
//    }
//
//    /**
//     * Test the behavior on the activity when showListPositionPlayersView method is celled.
//     */
//    @Test
//    public void testShowListPositionPlayersFragment() {
//        /* A LineupFormationFragment need to be addLike in order to be replaced
//         * with ListPositionPlayersFragment */
//        activity.showLoadingSuccess(players);
//        activity.showListPositionPlayersFragment(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        Fragment fragment = manager.findFragmentById(R.id.content);
//        assertNotNull(fragment);
//        assertTrue(fragment instanceof ListPositionPlayersFragment);
//        assertEquals(1, manager.getBackStackEntryCount());
//    }
//
//    /**
//     * Test the behavior on the activity when onPlayerSelected method is called and
//     * the ListPositionPlayers fragment is not displayed.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnPlayerSelectedWhenListPositionPlayersFragmentNotSet() {
//        activity.onPlayerSelected(players.get(0));
//    }
//
//    /**
//     * Test the behavior on the activity when onPlayerSelected is called.
//     */
//    @Test
//    public void testOnPlayerSelected() {
//        //TODO
//        activity.showLoadingSuccess(players);
//        activity.showListPositionPlayersFragment(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        activity.onPlayerSelected(players.get(0));
//        Fragment fragment = manager.findFragmentById(R.id.content);
//        assertNotNull(fragment);
//        assertTrue(fragment instanceof LineupFormationFragment);
//        assertEquals(0, manager.getBackStackEntryCount());
////        verify(presenter).setChanged();
//    }
//
//    /**
//     * Test the behavior when showPlayerDetailsDialog is called and
//     * LineupFormationFragment is not yet shown.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testShowPlayerDetailsDialogWhenLineupFormationFragmentNoSet() {
//        activity.showPlayerDetailsDialog(-1, false);
//    }
//
//    /**
//     * Test the behavior when showPlayerDetailsDialog is called.
//     */
//    @Test
//    public void testShowPlayerDetailsDialog() {
//        activity.showLoadingSuccess(players);
//        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
//        FragmentManager manager = activity.getSupportFragmentManager();
//        assertTrue(manager.findFragmentById(R.id.content) instanceof LineupFormationFragment);
//        assertEquals(0, manager.getBackStackEntryCount());
//        Fragment dialog = manager.findFragmentByTag(PlayerDetailsDialog.TAG);
//        assertTrue(dialog instanceof PlayerDetailsDialog);
//        assertTrue(dialog.isVisible());
//        assertNotNull(ShadowDialog.getLatestDialog());
//    }
//
//    /**
//     * Test the behavior when removeLike player is called and PlayerDetailsFragment is not yet shown.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testRemovePlayerWhenPlayerDetailsDialogIsNotShown() {
//        activity.removePlayer();
//    }
//
//    @Test
//    public void testRemovePlayer() {
//        activity.showLoadingSuccess(players);
//        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
//        activity.removePlayer();
//        assertTrue(activity.getSupportFragmentManager().findFragmentById(R.id.content)
//                instanceof LineupFormationFragment);
////        verify(presenter).setChanged();
//        //TODO
//    }
//
//    /**
//     * Test the behavior when onBackPressed is called and the current fragment
//     * is ListPositionPlayersFragment.
//     */
//    @Test
//    public void testOnBackButtonPressedWhenListPositionPlayersFragmentIsActive() {
//        FragmentManager manager = activity.getSupportFragmentManager();
//        activity.showLoadingSuccess(players);
//        activity.showListPositionPlayersFragment(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        assertTrue(manager.findFragmentById(R.id.content) instanceof ListPositionPlayersFragment);
//        activity.onBackPressed();
//        assertTrue(manager.findFragmentById(R.id.content) instanceof LineupFormationFragment);
//        assertEquals(0, manager.getBackStackEntryCount());
//    }
//
//    /**
//     * Test the behavior when onBackPressed is called and the current fragment
//     * is LineupFormationFragment and the PlayerDetailsDialog is showed.
//     */
//    @Test
//    public void testDialogDismissedWhenPlayerDetailsDialogIsActive() {
//        FragmentManager manager = activity.getSupportFragmentManager();
//        activity.showLoadingSuccess(players);
//        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
//        /**
//         * When a dialog fragment is active and back button is pressed, onBackPressed method on
//         * the activity will not be called, but the onDismissed method
//         * on the DialogFragment will be.
//         */
//        PlayerDetailsDialog dialog = (PlayerDetailsDialog) manager
//                .findFragmentByTag(PlayerDetailsDialog.TAG);
//        assertTrue(dialog.isVisible());
//        dialog.dismiss();
//        assertFalse(dialog.isVisible());
//        assertTrue(manager.findFragmentById(R.id.content) instanceof LineupFormationFragment);
//        assertEquals(0, manager.getBackStackEntryCount());
//    }
//
//    /**
//     * Test the behavior when onBackPressed is called and the current fragment is active.
//     */
//    @Test
//    public void testOnBackButtonPressedWhenLineupFormationIsActive() {
//        activity.showLoadingSuccess(players);
//        activity.onBackPressed();
//        assertTrue(shadowOf(activity).isFinishing());
//    }
//
//    /**
//     * Test the behavior when button "Update" is clicked and the
//     * LineupFormationFragment is not yet shown.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testBtnUpdateClickedWhenLineupFormationIsNotShowed() {
//        //TODO
////        Button button = (Button) activity.findViewById(R.id.lineupPlayersLayout_btnUpdate);
////        assertNotNull(button);
////        button.performClick();
//    }
//
//    /**
//     * Test the behavior when btn "Update" is clicked.
//     */
//    @Test
//    public void testBtnUpdate() {
//        //TODO
////        activity.showLoadingSuccess(players);
////        Button button = (Button) activity.findViewById(R.id.lineupPlayersLayout_btnUpdate);
////        assertNotNull(button);
////        button.performClick();
////        verify(presenter).onUpdateSuccess(anyListOf(LineupPlayer.class));
//    }
//
//    /**
//     * Test the behavior when showUpdating is called.
//     */
//    @Test
//    public void testShowUpdating() {
//        errorLayout.setVisibility(View.VISIBLE);
//        mainContent.setVisibility(View.VISIBLE);
//        updateLineupErrorLayout.setVisibility(View.VISIBLE);
//        String text = activity.getString(R.string.lineupPlayersActivity_spinnerUpdatingLineup_text);
//        activity.showUpdating();
//        assertEquals(View.VISIBLE, spinner.getVisibility());
//        assertEquals(text, txtSpinner.getText());
//        assertEquals(View.GONE, errorLayout.getVisibility());
//        assertEquals(View.GONE, mainContent.getVisibility());
//        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
//    }
//
//    /**
//     * Test the behavior when showUpdatingFailed is called.
//     */
//    @Test
//    public void testShowUpdatingFailed() {
//        spinner.setVisibility(View.VISIBLE);
//        errorLayout.setVisibility(View.VISIBLE);
//        mainContent.setVisibility(View.VISIBLE);
//        activity.showUpdatingFailed();
//        assertEquals(View.VISIBLE, updateLineupErrorLayout.getVisibility());
//        assertEquals(View.GONE, spinner.getVisibility());
//        assertEquals(View.GONE, errorLayout.getVisibility());
//        assertEquals(View.GONE, mainContent.getVisibility());
//    }
//
//    /**
//     * Test the behavior when btn "Try Update Again" is clicked.
//     */
//    @Test
//    public void testBtnTryUpdateAgainClicked() {
//        //TODO
//        activity.showLoadingSuccess(players);
//        Button button = (Button) activity
//                .findViewById(R.id.lineupPlayersActivity_btnTryUpdateAgain);
//        assertNotNull(button);
//        button.performClick();
////        verify(presenter).onUpdateSuccess(anyListOf(LineupPlayer.class));
//    }
//
//    /**
//     * Test the behavior when showUpdateSuccessful is called.
//     */
//    @Test
//    public void testShowUpdateSuccessful() {
//        //TODO
////        Button btnUpdate = (Button) activity.findViewById(R.id.lineupPlayersLayout_btnUpdate);
////        assertNotNull(btnUpdate);
////        spinner.setVisibility(View.VISIBLE);
////        errorLayout.setVisibility(View.VISIBLE);
////        updateLineupErrorLayout.setVisibility(View.VISIBLE);
////        btnUpdate.setVisibility(View.VISIBLE);
////        activity.showUpdatingSuccess();
////        assertEquals(View.VISIBLE, mainContent.getVisibility());
////        assertEquals(View.GONE, spinner.getVisibility());
////        assertEquals(View.GONE, errorLayout.getVisibility());
////        assertEquals(View.GONE, btnUpdate.getVisibility());
//    }
//
//    /**
//     * Test the behavior when getPlayersOrdered is called and the lineup formation fragment
//     * is not visible.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetPlayersWhenLineupFormationFragmentIsNotVisible() {
//        activity.showLoadingSuccess(players);
//        activity.showListPositionPlayersFragment(PositionUtils.POSITION_PLACE.ATTACKERS, new int[]{});
//        activity.getPlayersOrdered();
//    }
//
//    /**
//     * Test the behavior when getFormation is called and LineupFormationFragment
//     * is not visible.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetFormationWhenLineupFormationFragmentIsNotVisible() {
//        activity.showLoadingSuccess(players);
//        activity.showListPositionPlayersFragment(PositionUtils.POSITION_PLACE.ATTACKERS, new int[]{});
//        activity.getFormation();
//    }
//
//    /**
//     * Test the behavior when chageFormation is called.
//     */
//    @Test
//    public void testChangeFormation() {
//        activity.showLoadingSuccess(players);
//        FragmentManager manager = activity.getSupportFragmentManager();
//        Fragment currentFragment = manager.findFragmentById(R.id.content);
//        activity.changeFormation(LineupUtils.FORMATION.F_3_2_3_2, players);
//        Fragment fragment = manager.findFragmentById(R.id.content);
//        assertTrue(fragment instanceof LineupFormationFragment);
//        assertNotSame(currentFragment, fragment);
//        assertEquals(0, manager.getBackStackEntryCount());
//    }
}
