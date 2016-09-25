package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CreateLineupViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.After;
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
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

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
@Ignore
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

    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout failedRequestLayout;
    private TextView failedRequestText;
    private RelativeLayout mainContent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setCreateLineupViewComponent(component);
        application.setLineupFormationFragmentComponent(lineupFormationFragmentComponent);
        application.setListPositionPlayersFragmentComponent(listPositionPlayersFragmentComponent);
        application.setPlayerDetailsDialogComponent(playerDetailsDialogComponent);
        this.mockDependencies();
        controller = Robolectric.buildActivity(CreateLineupActivity.class);
        activity = controller.create().start().resume().visible().get();

        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        assertNotNull(spinner);
        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
        assertNotNull(txtSpinner);
//        failedRequestLayout = (RelativeLayout) activity.findViewById(R.id.error_loading);
//        assertNotNull(failedRequestLayout);
//        failedRequestText = (TextView) activity.findViewById(R.id.requestFailed_text);
        mainContent = (RelativeLayout) activity.findViewById(R.id.createLineupLayout_mainContent);
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        when(presenter.getFormation()).thenReturn(LineupUtils.FORMATION.F_4_4_2);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CreateLineupActivity activity = (CreateLineupActivity)
                        invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(CreateLineupActivity.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupFormationFragment fragment = (LineupFormationFragment)
                        invocation.getArguments()[0];
                fragment.setPresenter(lineupFormationFragmentPresenter);
                return null;
            }
        }).when(lineupFormationFragmentComponent).inject(any(LineupFormationFragment.class));
        when(lineupFormationFragmentPresenter.getFormation())
                .thenReturn(LineupUtils.FORMATION.F_4_4_2);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListPositionPlayersFragment fragment = (ListPositionPlayersFragment)
                        invocation.getArguments()[0];
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
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.createLineupView_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        assertTrue(activity.getSupportFragmentManager().findFragmentById(R.id.content)
                instanceof LineupFormationFragment);
        ButtonAwesome btn = (ButtonAwesome) activity
                .findViewById(R.id.createLineupLayout_btnChangeFormation);
        assertNotNull(btn);
        assertSame(activity, shadowOf(btn).getOnCreateContextMenuListener());
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
     * Test the behavior when showListPositionPlayers is called.
     */
    @Test
    public void testShowListPositionPlayersFragment() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        FragmentManager manager = activity.getSupportFragmentManager();
        assertTrue(manager.findFragmentById(R.id.content) instanceof ListPositionPlayersFragment);
        assertEquals(1, manager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when showListPositionPlayers is called and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowListPositionPlayersIsCalledWithLineupFormationFragmentNoVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called.
     */
    @Test
    public void testShowPlayerDetailsDialog() {
        activity.showPlayerDetailsDialog(1, false);
        FragmentManager manager = activity.getSupportFragmentManager();
        assertTrue(manager.findFragmentById(R.id.content) instanceof LineupFormationFragment);
        assertEquals(0, manager.getBackStackEntryCount());
        assertTrue(manager.findFragmentByTag(PlayerDetailsDialog.TAG)
                instanceof PlayerDetailsDialog);
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called andLineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowPlayerDetailsDialogWhenLineupFormationFragmentIsNotVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        activity.showPlayerDetailsDialog(1, false);
    }

    /**
     * Test the behavior when showValidLineup is called.
     */
    @Test
    public void testOnValidFormation() {
        activity.showValidLineup();
//        Button btn = (Button) activity.findViewById(R.id.createLineupLayout_btnSave);
//        assertNotNull(btn);
//        assertEquals(View.VISIBLE, btn.getVisibility());
    }

    /**
     * Test the behavior when on invalid formation is called.
     */
    @Test
    public void testOnInvalidFormation() {
        activity.showInvalidLineup();
//        Button btn = (Button) activity.findViewById(R.id.createLineupLayout_btnSave);
//        assertNotNull(btn);
//        assertEquals(View.GONE, btn.getVisibility());
    }

    /**
     * Test the behavior when remove player is called and LineupFormationFragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemovePlayerOnLineupFormationFragmentNoVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        activity.removePlayer();
    }

    /**
     * Test the behavior when removePlayer is called and LineupFormationFragment is visible.
     */
    @Test
    public void testRemovePlayer() {
        activity.removePlayer();
        assertTrue(activity.getSupportFragmentManager().findFragmentById(R.id.content)
                instanceof LineupFormationFragment);
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
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        activity.onPlayerSelected(new Player());
        assertTrue(activity.getSupportFragmentManager().findFragmentById(R.id.content)
                instanceof LineupFormationFragment);
        assertEquals(0, activity.getSupportFragmentManager().getBackStackEntryCount());
    }

    /**
     * Test the behavior when getPlayersOrdered is called and LineupFormationFragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayersWhenLineupFormationFragmnetIsNotVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        activity.getPlayersOrdered();
    }

    /**
     * Test that getPlayersOrdered method works.
     */
    @Test
    public void testGetPlayers() {
        activity.getPlayersOrdered();
    }

    /**
     * Test the behavior when updateFormation is called and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFormationWhenLineupFormationIsNotVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        activity.changeFormation(LineupUtils.FORMATION.F_4_3_3, new ArrayList<Player>());
    }

    /**
     * Test that updateFormation method replaces the current LineupFormationFragment
     * with a new one.
     */
    @Test
    public void testUpdateFormation() {
        final LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_3_3;
        FragmentManager manager = activity.getSupportFragmentManager();
        LineupFormationFragment currentFragment = (LineupFormationFragment)
                manager.findFragmentById(R.id.content);
        activity.changeFormation(formation, new ArrayList<Player>());
        Fragment fragment = manager.findFragmentById(R.id.content);
        assertTrue(fragment instanceof LineupFormationFragment);
        assertNotSame(currentFragment, fragment);
        assertEquals(0, manager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when button "Save" is clicked and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBtnSaveClickWhenLineupFormationFragmentIsNotVisible() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        Button btn = (Button) activity.findViewById(R.id.createLineupLayout_btnSave);
//        assertNotNull(btn);
//        btn.performClick();
    }

    /**
     * Test the behavior when button "Save" is clicked.
     */
    @Test
    public void testBtnSaveClick() {
//        Button btn = (Button) activity.findViewById(R.id.createLineupLayout_btnSave);
//        assertNotNull(btn);
//        btn.performClick();
//        verify(presenter).store(anyListOf(LineupPlayer.class));
    }

    /**
     * Test the behavior when showStoring is called.
     */
    @Test
    public void testShowStoring() {
        String text = activity.getString(R.string.createLineupView_spinnerCreateLineup_text);
        failedRequestLayout.setVisibility(View.VISIBLE);
        activity.showStoring();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, failedRequestLayout.getVisibility());
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
        assertEquals(View.VISIBLE, failedRequestLayout.getVisibility());
        assertEquals(text, failedRequestText.getText());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when button "Try Again" is clicked and LineupFormationFragment
     * is not active.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBtnTryAgainWhenLineupFormationIsNotActive() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
//        Button btn = (Button) activity.findViewById(R.id.error_loading_btn_tryAgain);
//        assertNotNull(btn);
//        btn.performClick();
    }

    /**
     * Test the behavior when button "Try Again" is clicked.
     */
    @Test
    public void testBtnTryAgain() {
//        Button btn = (Button) activity.findViewById(R.id.error_loading_btn_tryAgain);
//        assertNotNull(btn);
//        btn.performClick();
//        verify(presenter).store(anyListOf(LineupPlayer.class));
    }

    /**
     * Test the behavior when back button is pressed and PlayerDetailsDialog is active.
     */
    @Test
    public void testOnBackBtnPressedWhenPlayerDetailsDialogIsActive() {
        activity.showPlayerDetailsDialog(1, false);
        DialogFragment dialog = (DialogFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(PlayerDetailsDialog.TAG);
        assertTrue(dialog.isVisible());
        dialog.dismiss();
        assertFalse(dialog.isVisible());
        assertTrue(activity.getSupportFragmentManager().findFragmentById(R.id.content)
                instanceof LineupFormationFragment);
        assertEquals(0, activity.getSupportFragmentManager().getBackStackEntryCount());
        assertFalse(shadowOf(activity).isFinishing());
    }

    /**
     * Test the behavior when back button is pressed and the ListPositionPlayersFragment
     * is active.
     */
    @Test
    public void testOnBackBtnPressedWhenListPositionPlayersFragmentIsActive() {
//        activity.showListPositionPlayersView(PositionUtils.POSITION_PLACE.KEEPERS, new int[]{});
        FragmentManager manager = activity.getSupportFragmentManager();
        assertTrue(manager.findFragmentById(R.id.content) instanceof ListPositionPlayersFragment);
        activity.onBackPressed();
        assertTrue(manager.findFragmentById(R.id.content) instanceof LineupFormationFragment);
        assertEquals(0, manager.getBackStackEntryCount());
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