package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CreateLineupViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_PLACE;

import org.junit.After;
import org.junit.Before;
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
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 24.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class CreateLineupActivityTest {

    @Mock
    private CreateLineupViewPresenter presenter;

    @Mock
    private CreateLineupViewComponent component;

    @Mock
    private LineupFormationViewComponent lineupFormationFragmentComponent;

    @Mock
    private LineupFormationViewPresenter lineupFormationFragmentPresenter;

    @Mock
    private ListPositionPlayersViewComponent listPositionPlayersFragmentComponent;

    @Mock
    private ListPositionPlayersViewPresenter listPositionPlayersFragmentPresenter;

    @Mock
    private PlayerDetailsViewComponent playerDetailsDialogComponent;

    @Mock
    private PlayerDetailsViewPresenter playerDetailsDialogPresenter;

    private ActivityController<CreateLineupActivity> controller;
    private CreateLineupActivity activity;
    private FragmentManager fragmentManager;

    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout error;
    private TextView errorText;
    private Button btnTryAgain;
    private RelativeLayout mainContent;
    private Button btnChangeFormation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        controller = Robolectric.buildActivity(CreateLineupActivity.class);
        activity = controller.create().start().resume().visible().get();
        this.getViews();
        fragmentManager = activity.getSupportFragmentManager();
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setCreateLineupViewComponent(component);
        application.setLineupFormationFragmentComponent(lineupFormationFragmentComponent);
        application.setListPositionPlayersViewComponent(listPositionPlayersFragmentComponent);
        application.setPlayerDetailsViewComponent(playerDetailsDialogComponent);

        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CreateLineupActivity activity =
                        (CreateLineupActivity) invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(CreateLineupActivity.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupFormationFragment fragment =
                        (LineupFormationFragment) invocation.getArguments()[0];
                fragment.setPresenter(lineupFormationFragmentPresenter);
                return null;
            }
        }).when(lineupFormationFragmentComponent).inject(any(LineupFormationFragment.class));
        when(lineupFormationFragmentPresenter.getFormation())
                .thenReturn(LineupUtils.FORMATION.F_4_4_2);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListPositionPlayersFragment fragment =
                        (ListPositionPlayersFragment) invocation.getArguments()[0];
                fragment.setPresenter(listPositionPlayersFragmentPresenter);
                return null;
            }
        }).when(listPositionPlayersFragmentComponent)
                .inject(any(ListPositionPlayersFragment.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                PlayerDetailsDialog dialog = (PlayerDetailsDialog) invocation.getArguments()[0];
                dialog.setPresenter(playerDetailsDialogPresenter);
                return null;
            }
        }).when(playerDetailsDialogComponent).inject(any(PlayerDetailsDialog.class));
    }

    /**
     * Get the views for the activity.
     */
    private void getViews() {
        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
        error = (RelativeLayout) activity.findViewById(R.id.error);
        errorText = (TextView) activity.findViewById(R.id.txtError);
        btnTryAgain = (Button) activity.findViewById(R.id.error_btnTryAgain);
        mainContent = (RelativeLayout) activity.findViewById(R.id.createLineupLayout_mainContent);
        btnChangeFormation =
                (Button) activity.findViewById(R.id.createLineupLayout_btnChangeFormation);
        assertNotNull(spinner);
        assertNotNull(txtSpinner);
        assertNotNull(error);
        assertNotNull(errorText);
        assertNotNull(btnTryAgain);
        assertNotNull(mainContent);
        assertNotNull(btnChangeFormation);
    }

    /**
     * Get the active fragment in the activity.
     *
     * @return the active fragment in the activity
     */
    private Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(R.id.content);
    }

    /**
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.createLineupView_title);
        ActionBar actionBar = activity.getSupportActionBar();
        assertNotNull(actionBar);
        assertEquals(title, actionBar.getTitle());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertSame(activity, shadowOf(btnChangeFormation).getOnCreateContextMenuListener());
    }

    /**
     * Test the behavior when context item is selected with id formation_4_4_2.
     */
    @Test
    public void testContextItemSelectedWithId4_4_2() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_4_2);
        assertTrue(activity.onContextItemSelected(item));
        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_4_2);
    }

    /**
     * Test the behavior when context item is selected with id formation_3_2_3_2.
     */
    @Test
    public void testContextItemSelectedWith3_2_3_2() {
        MenuItem item = new RoboMenuItem(R.id.formation_3_2_3_2);
        assertTrue(activity.onContextItemSelected(item));
        verify(presenter).updateFormation(LineupUtils.FORMATION.F_3_2_3_2);
    }

    /**
     * Test the behavior when context item is selected with id formation_4_2_3_1.
     */
    @Test
    public void testContextItemSelectedWith4_2_3_1() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_2_3_1);
        assertTrue(activity.onContextItemSelected(item));
        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_2_3_1);
    }

    /**
     * Test the behavior when context item is selected with id formation_4_3_3.
     */
    @Test
    public void testContextItemSelectedWith4_3_3() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_3_3);
        assertTrue(activity.onContextItemSelected(item));
        verify(presenter).updateFormation(LineupUtils.FORMATION.F_4_3_3);
    }

    /**
     * Test the behavior when contextItemSelected is called with un existing formation id.
     */
    @Test
    public void testContextItemSelectedOnUnExistingFormationId() {
        MenuItem item = new RoboMenuItem(R.id.mainMenu_refresh);
        activity.onContextItemSelected(item);
        verify(presenter, never()).updateFormation(any(LineupUtils.FORMATION.class));
    }

    /**
     * Test the behavior when menu item R.id.lineupMenu_save is clicked.
     */
    @Test
    public void testMenuItemSaveClick() {
        assertTrue(shadowOf(activity).clickMenuItem(R.id.lineupMenu_save));
        verify(presenter).store();
    }

    /**
     * Test the behavior when showListPositionPlayers is called.
     */
    @Test
    public void testShowListPositionPlayersFragment() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        assertTrue(this.getCurrentFragment() instanceof ListPositionPlayersFragment);
        assertEquals(1, fragmentManager.getBackStackEntryCount());
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when showListPositionPlayers is called and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowListPositionPlayersIsCalledWithLineupFormationFragmentNoVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called.
     */
    @Test
    public void testShowPlayerDetailsDialog() {
        activity.showPlayerDetailsDialog(1, false);
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
        Fragment dialog = fragmentManager.findFragmentByTag(PlayerDetailsDialog.TAG);
        assertTrue(dialog instanceof PlayerDetailsDialog);
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called andLineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowPlayerDetailsDialogWhenLineupFormationFragmentIsNotVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.showPlayerDetailsDialog(1, false);
    }

    /**
     * Test the behavior when showValidLineup is called.
     */
    @Test
    public void testShowValidFormation() {
        Menu optionsMenu = shadowOf(activity).getOptionsMenu();
        assertNotNull(optionsMenu);
        MenuItem menuItem = optionsMenu.findItem(R.id.lineupMenu_save);
        assertNotNull(menuItem);
        assertFalse(menuItem.isVisible());

        when(presenter.isFormationValid()).thenReturn(true);
        activity.showValidLineup();
        menuItem = optionsMenu.findItem(R.id.lineupMenu_save);
        assertTrue(menuItem.isVisible());
    }

    /**
     * Test the behavior when on invalid formation is called.
     */
    @Test
    public void testShowInvalidFormation() {
        Menu optionsMenu = shadowOf(activity).getOptionsMenu();
        assertNotNull(optionsMenu);
        MenuItem menuItem = optionsMenu.findItem(R.id.lineupMenu_save);
        assertNotNull(menuItem);
        assertFalse(menuItem.isVisible());

        when(presenter.isFormationValid()).thenReturn(true);
        activity.showValidLineup();
        menuItem = optionsMenu.findItem(R.id.lineupMenu_save);
        assertTrue(menuItem.isVisible());

        when(presenter.isFormationValid()).thenReturn(false);
        activity.showValidLineup();
        menuItem = optionsMenu.findItem(R.id.lineupMenu_save);
        assertFalse(menuItem.isVisible());
    }

    /**
     * Test the behavior when remove player is called and LineupFormationFragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemovePlayerOnLineupFormationFragmentNoVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.removePlayer();
    }

    /**
     * Test the behavior when removePlayer is called and LineupFormationFragment is visible.
     */
    @Test
    public void testRemovePlayer() {
        activity.removePlayer();
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        verify(lineupFormationFragmentPresenter).removeSelectedPlayer();
        verify(presenter).setChanged(true);
    }

    /**
     * Test the behavior when onPlayerSelected is called and ListPositionPlayersFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerSelectedWhenListPositionPlayersIsNotActive() {
        activity.onPlayerSelected(new Player());
    }

    /**
     * Test the behavior when onPlayerSelected is called.
     */
    @Test
    public void testOnPlayerSelected() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
        activity.onPlayerSelected(new Player());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, activity.getSupportFragmentManager().getBackStackEntryCount());
        assertEquals(View.VISIBLE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when getPlayersOrdered is called and LineupFormationFragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayersOrderedPlayersWhenLineupFormationFragmnetIsNotVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.getPlayersOrdered();
    }

    /**
     * Test that get players ordered works.
     */
    @Test
    public void testGetPlayersOrdered() {
        List<Player> mockList = Arrays.asList(new Player(), new Player());
        when(lineupFormationFragmentPresenter.getPlayersOrdered()).thenReturn(mockList);
        List<Player> result = activity.getPlayersOrdered();
        assertSame(mockList, result);
    }

    /**
     * Test the behavior when getLineupPLayers is called and LineupFormationFragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLineupPlayersPlayersWhenLineupFormationFragmnetIsNotVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.getLineupPlayers();
    }

    /**
     * Test that getLineupPlayers method works.
     */
    @Test
    public void testGetLineupPlayers() {
        List<LineupPlayer> mockList = Arrays.asList(new LineupPlayer(), new LineupPlayer());
        when(lineupFormationFragmentPresenter.getLineupPlayers()).thenReturn(mockList);
        List<LineupPlayer> result = activity.getLineupPlayers();
        assertSame(mockList, result);
    }

    /**
     * Test the behavior when updateFormation is called and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testChangeFormationWhenLineupFormationIsNotVisible() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
        activity.changeFormation(LineupUtils.FORMATION.F_4_3_3, Collections.<Player>emptyList());
    }

    /**
     * Test that updateFormation method replaces the current LineupFormationFragment
     * with a new one.
     */
    @Test
    public void testChangeFormation() {
        final LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_3_3;
        final List<Player> mockList = Arrays.asList(new Player(), new Player());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        LineupFormationFragment currFragment = (LineupFormationFragment) this.getCurrentFragment();
        activity.changeFormation(formation, mockList);
        Fragment newFragment = this.getCurrentFragment();
        assertTrue(newFragment instanceof LineupFormationFragment);
        assertNotSame(currFragment, newFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when showStoring is called.
     */
    @Test
    public void testShowStoring() {
        String text = activity.getString(R.string.createLineupView_spinnerCreateLineup_text);
        error.setVisibility(View.VISIBLE);
        activity.showStoring();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when showStoringFailed is called.
     */
    @Test
    public void testShowStoringFailed() {
        String text = activity.getString(R.string.createLineupView_requestFailedText);
        spinner.setVisibility(View.VISIBLE);
        activity.showStoringFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(text, errorText.getText());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when button "Try Again" is clicked and LineupFormationFragment
     * is not active.
     */
    @Test
    public void testBtnTryAgainWhenLineupFormationIsNotActive() {
        assertTrue(btnTryAgain.performClick());
        verify(presenter).store();
    }

    @Test
    public void testShowStoringSuccessful() {
        String text = activity.getString(R.string.createLineupView_lineupCreateSuccessful_text);
        activity.showStoringSuccessful(new Lineup());
        assertEquals(text, ShadowToast.getTextOfLatestToast());
        assertTrue(shadowOf(activity).isFinishing());
    }

    /**
     * Test the behavior when back button is pressed and PlayerDetailsDialog is active.
     */
    @Test
    public void testOnBackBtnPressedWhenPlayerDetailsDialogIsActive() {
        activity.showPlayerDetailsDialog(1, false);
        DialogFragment dialog =
                (DialogFragment) fragmentManager.findFragmentByTag(PlayerDetailsDialog.TAG);
        assertTrue(dialog.isVisible());
        dialog.dismiss();
        assertFalse(dialog.isVisible());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, activity.getSupportFragmentManager().getBackStackEntryCount());
        assertFalse(shadowOf(activity).isFinishing());
    }

    /**
     * Test the behavior when back button is pressed and the ListPositionPlayersFragment
     * is active.
     */
    @Test
    public void testOnBackBtnPressedWhenListPositionPlayersFragmentIsActive() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        assertTrue(this.getCurrentFragment() instanceof ListPositionPlayersFragment);
        activity.onBackPressed();
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
        assertFalse(shadowOf(activity).isFinishing());
    }

    /**
     * Test the behavior when back btn is pressed and LineupFormationFragment is active.
     */
    @Test
    public void testOnBackBtnPressedWhenLineupFormationFragmentIsActive() {
        activity.onBackPressed();
        assertTrue(shadowOf(activity).isFinishing());
    }
}