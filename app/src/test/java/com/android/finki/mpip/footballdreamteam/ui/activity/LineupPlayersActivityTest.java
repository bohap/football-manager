package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;
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
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.util.ActivityController;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
public class LineupPlayersActivityTest {

    @Mock
    private LineupPlayersViewComponent component;

    @Mock
    private LineupPlayersViewPresenter presenter;

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

    @Mock
    private LikeViewComponent likeViewComponent;

    @Mock
    private LikeViewPresenter likeViewPresenter;

    @Mock
    private CommentsViewComponent commentsViewComponent;

    @Mock
    private CommentsViewPresenter commentsViewPresenter;

    private ActivityController<LineupPlayersActivity> controller;
    private LineupPlayersActivity activity;
    private FragmentManager fragmentManager;

    private RelativeLayout spinner;
    private TextView txtSpinner;
    private RelativeLayout error;
    private TextView txtError;
    private RelativeLayout mainContent;
    private LinearLayout updateLineupErrorLayout;
    private Button btnChangeFormation;

    private List<Player> players = Arrays.asList(
            new Player(), new Player(), new Player(), new Player(), new Player(), new Player(),
            new Player(), new Player(), new Player(), new Player(), new Player());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockDependencies();
        controller = Robolectric.buildActivity(LineupPlayersActivity.class);
        activity = controller.create().start().resume().visible().get();
        fragmentManager = activity.getSupportFragmentManager();
        this.getViews();
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity and fragment that the activity is starting.
     */
    private void mockDependencies() {
        MockApplication application = (MockApplication) RuntimeEnvironment.application;
        application.setLineupPlayerActivityComponent(component);
        application.setLineupFormationFragmentComponent(lineupFormationFragmentComponent);
        application.setListPositionPlayersViewComponent(listPositionPlayersFragmentComponent);
        application.setPlayerDetailsViewComponent(playerDetailsDialogComponent);
        application.setLikeViewComponent(likeViewComponent);
        application.setCommentsViewComponent(commentsViewComponent);

        when(lineupFormationFragmentPresenter.getFormation()).thenReturn(FORMATION.F_3_2_3_2);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupPlayersActivity activity =
                        (LineupPlayersActivity) invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(LineupPlayersActivity.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LineupFormationFragment fragment = (LineupFormationFragment)
                        invocation.getArguments()[0];
                fragment.setPresenter(lineupFormationFragmentPresenter);
                return null;
            }
        }).when(lineupFormationFragmentComponent).inject(any(LineupFormationFragment.class));

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

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LikeFragment fragment = (LikeFragment) invocation.getArguments()[0];
                fragment.setPresenter(likeViewPresenter);
                return null;
            }
        }).when(likeViewComponent).inject(any(LikeFragment.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CommentsFragment fragment = (CommentsFragment) invocation.getArguments()[0];
                fragment.setPresenter(commentsViewPresenter);
                return null;
            }
        }).when(commentsViewComponent).inject(any(CommentsFragment.class));
    }

    /**
     * Get the views for the activity.
     */
    private void getViews() {
        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
        error = (RelativeLayout) activity.findViewById(R.id.error);
        txtError = (TextView) activity.findViewById(R.id.txtError);
        mainContent =
                (RelativeLayout) activity.findViewById(R.id.lineupPlayersActivity_mainContent);
        updateLineupErrorLayout =
                (LinearLayout) activity.findViewById(R.id.lineupPlayersActivity_updateFailed);
        btnChangeFormation =
                (Button) activity.findViewById(R.id.lineupPlayersLayout_btnChangeFormation);
        assertNotNull(spinner);
        assertNotNull(txtSpinner);
        assertNotNull(error);
        assertNotNull(txtError);
        assertNotNull(mainContent);
        assertNotNull(updateLineupErrorLayout);
        assertNotNull(btnChangeFormation);
    }

    /**
     * Get the fragment who is active in the activity.
     *
     * @return the active fragment in the activity
     */
    private Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(R.id.content);
    }

    /**
     * Assert the menu icon R.id.lineupMenu_save visibility.
     *
     * @param visible whatever the icon should be visible or not
     */
    private void assertMenuIconSaveVisibility(boolean visible) {
        Menu optionsMenu = shadowOf(activity).getOptionsMenu();
        assertNotNull(optionsMenu);
        MenuItem item = optionsMenu.findItem(R.id.lineupMenu_save);
        assertNotNull(item);
        assertEquals(visible, item.isVisible());
    }

    /**
     * Test that the activity is correctly created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.lineupPlayersActivity_title);
        ActionBar actionBar = activity.getSupportActionBar();
        assertNotNull(actionBar);
        assertEquals(title, actionBar.getTitle());
        verify(presenter).onViewCreated(activity.getIntent().getExtras());
        assertSame(activity, shadowOf(btnChangeFormation).getOnCreateContextMenuListener());
    }

    /**
     * Test the behavior when onCrateOptionsMenu is called and the LineupFormationFragmentFragment
     * is not visible.
     */
    @Test
    public void testOnCreateOptionsMenuWhenLineupFormationFragmentIsNotVisible() {
        Menu optionsMenu = shadowOf(activity).getOptionsMenu();
        assertNotNull(optionsMenu);
        MenuItem item = optionsMenu.findItem(R.id.lineupMenu_save);
        assertNull(item);
    }

    /**
     * Test the behavior when onCrateOptionsMenu is called and the lineup is not changed.
     */
    @Test
    public void testOnCreateOptionsMenuWhenLineupIsNotChanged() {
        when(presenter.isChanged()).thenReturn(false);
        activity.showLineupFormationFragment(players, true);
        activity.invalidateOptionsMenu();
        this.assertMenuIconSaveVisibility(false);
    }

    /**
     * Test the behavior when onCrateOptionsMenu is called and the lineup is not valid.
     */
    @Test
    public void testOnCreateOptionsMenuWhenLineupIsNotValid() {
        when(presenter.isChanged()).thenReturn(true);
        when(presenter.isLineupValid()).thenReturn(false);
        activity.showLineupFormationFragment(players, true);
        activity.invalidateOptionsMenu();
        this.assertMenuIconSaveVisibility(false);
    }

    /**
     * Test the behavior when onCreateOptionsMenu is called and the lineup
     * is changed and is valid.
     */
    @Test
    public void testOnCreateOptionsMenuWHenLineupIsChangedAndValid() {
        when(presenter.isChanged()).thenReturn(true);
        when(presenter.isLineupValid()).thenReturn(true);
        activity.showLineupFormationFragment(players, true);
        activity.invalidateOptionsMenu();
        this.assertMenuIconSaveVisibility(true);
    }

    /**
     * Test that behavior when context item with id 4_4_2 is selected.
     */
    @Test
    public void testContextItemSelectedWithId4_4_2() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_4_2);
        activity.onContextItemSelected(item);
        verify(presenter).updateFormation(FORMATION.F_4_4_2);
    }

    /**
     * Test the behavior when context item with id 3_2_3_2 is selected.
     */
    @Test
    public void testContextItemSelectedWithId3_2_3_2() {
        MenuItem item = new RoboMenuItem(R.id.formation_3_2_3_2);
        activity.onContextItemSelected(item);
        verify(presenter).updateFormation(FORMATION.F_3_2_3_2);
    }

    /**
     * Test the behavior when context item with id 4_2_3_1 is selected.
     */
    @Test
    public void testContextItemSelectedWithId4_2_3_1() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_2_3_1);
        activity.onContextItemSelected(item);
        verify(presenter).updateFormation(FORMATION.F_4_2_3_1);
    }

    /**
     * Test the behavior when context item with id 4_3_3 is selected.
     */
    @Test
    public void testContextItemSelectedWithId4_3_3() {
        MenuItem item = new RoboMenuItem(R.id.formation_4_3_3);
        activity.onContextItemSelected(item);
        verify(presenter).updateFormation(FORMATION.F_4_3_3);
    }

    /**
     * Test the behavior when toggleBtnChangeFormation is called, the user can edit the lineup
     * and the btn is not showed.
     */
    @Test
    public void testShowBtnChangeFormation() {
        when(presenter.canEditLineup()).thenReturn(true);
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
        activity.toggleBtnChangeFormation(true);
        assertEquals(View.VISIBLE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when toggleBtnChangeFormation is called and the user can't edit the lineup.
     */
    @Test
    public void testShowBtnChangeFormationWhenUserCanNotEditTheLineup() {
        when(presenter.canEditLineup()).thenReturn(false);
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
        activity.toggleBtnChangeFormation(true);
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior on the activity when showLoading method is called.
     */
    @Test
    public void testShowLoading() {
        error.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        updateLineupErrorLayout.setVisibility(View.VISIBLE);
        activity.showLoading();
        String text = activity.getString(R.string.lineupPlayersActivity_spinnerLoadPlayers_text);
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
    }

    /**
     * Test the behavior on the activity when showLoadingFailed method is called.
     */
    @Test
    public void testShowLoadingFailed() {
        String text = activity.getString(R.string.lineupPlayersActivity_loadingLineupFailed_text);
        spinner.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        updateLineupErrorLayout.setVisibility(View.VISIBLE);
        activity.showLoadingFailed();
        assertEquals(text, txtError.getText());
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
    }

    /**
     * Test the behavior on the activity when btn "Try Again" is clicked.
     */
    @Test
    public void testLoadBtnTryAgainClick() {
        Button btn = (Button) activity.findViewById(R.id.error_btnTryAgain);
        assertNotNull(btn);
        btn.performClick();
        verify(presenter).loadPlayers();
    }

    /**
     * Test the behavior on the activity when testShowLoadingSuccess method is called.
     */
    @Test
    public void testShowLoadingSuccess() {
        when(presenter.canEditLineup()).thenReturn(true);
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        updateLineupErrorLayout.setVisibility(View.VISIBLE);
        activity.showLoadingSuccess(players);
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(View.VISIBLE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when showUpdating is called.
     */
    @Test
    public void testShowUpdating() {
        error.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        updateLineupErrorLayout.setVisibility(View.VISIBLE);
        String text = activity.getString(R.string.lineupPlayersActivity_spinnerUpdatingLineup_text);
        activity.showUpdating();
        assertEquals(View.VISIBLE, spinner.getVisibility());
        assertEquals(text, txtSpinner.getText());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
        assertEquals(View.GONE, updateLineupErrorLayout.getVisibility());
    }

    /**
     * Test the behavior when showUpdatingFailed is called.
     */
    @Test
    public void testShowUpdatingFailed() {
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.VISIBLE);
        activity.showUpdatingFailed();
        assertEquals(View.VISIBLE, updateLineupErrorLayout.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, mainContent.getVisibility());
    }

    /**
     * Test the behavior when btn "Try Update Again" is clicked.
     */
    @Test
    public void testBtnTryUpdateAgainClicked() {
        Button button =
                (Button) activity.findViewById(R.id.lineupPlayersActivity_btnTryUpdateAgain);
        assertNotNull(button);
        assertTrue(button.performClick());
        verify(presenter).update();
    }

    /**
     * Test the behavior when showUpdateSuccessful is called.
     */
    @Test
    public void testShowUpdateSuccessful() {
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        updateLineupErrorLayout.setVisibility(View.VISIBLE);
        activity.showUpdatingSuccess();
        assertEquals(View.VISIBLE, mainContent.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
    }

    /**
     * Test the behavior when getPlayersOrdered is called and the lineup formation fragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayersOrderedWhenLineupFormationFragmentIsNotVisible() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.ATTACKERS, new int[]{}, 0, 0);
        activity.getPlayersOrdered();
    }

    /**
     * Test that getPlayersOrdered ordered works.
     */
    @Test
    public void testGetPlayersOrdered() {
        activity.showLoadingSuccess(players);
        when(lineupFormationFragmentPresenter.getPlayersOrdered()).thenReturn(players);
        List<Player> result = activity.getPlayersOrdered();
        assertSame(players, result);
    }

    /**
     * Test the behavior when getFormation is called and LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormationWhenLineupFormationFragmentIsNotVisible() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.ATTACKERS, new int[]{}, 0, 0);
        activity.getFormation();
    }

    /**
     * Test that getFormation method works.
     */
    @Test
    public void testGetFormation() {
        activity.showLoadingSuccess(players);
        final FORMATION formation = FORMATION.F_3_2_3_2;
        when(lineupFormationFragmentPresenter.getFormation()).thenReturn(formation);
        FORMATION result = activity.getFormation();
        assertSame(formation, result);
    }

    /**
     * Test the behavior when getLineupPlayers is called and the lineup formation fragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLineupPlayersWhenLineupFormationFragmentIsNotVisible() {
        activity.showListPositionPlayersFragment(POSITION_PLACE.ATTACKERS, new int[]{}, 0, 0);
        activity.getLineupPlayers();
    }

    /**
     * Test that getLineupPlayers ordered works.
     */
    @Test
    public void testGetLineupPlayers() {
        activity.showLoadingSuccess(players);
        final List<LineupPlayer> mockList = Arrays.asList(new LineupPlayer(), new LineupPlayer());
        when(lineupFormationFragmentPresenter.getLineupPlayers()).thenReturn(mockList);
        List<LineupPlayer> result = activity.getLineupPlayers();
        assertSame(mockList, result);
    }

    /**
     * Test the behavior when chageFormation is called.
     */
    @Test
    public void testChangeFormation() {
        when(presenter.isLineupValid()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(presenter.isChanged()).thenReturn(true);
                return null;
            }
        }).when(presenter).setChanged(true);

        activity.showLoadingSuccess(players);
        this.assertMenuIconSaveVisibility(false);
        Fragment currFragment = this.getCurrentFragment();
        assertTrue(currFragment instanceof LineupFormationFragment);
        activity.changeFormation(FORMATION.F_3_2_3_2, players);
        Fragment newFragment = this.getCurrentFragment();
        assertTrue(newFragment instanceof LineupFormationFragment);
        assertNotSame(currFragment, newFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
        verify(presenter).setChanged(true);
        this.assertMenuIconSaveVisibility(true);
    }

    /**
     * Test the behavior on the activity when showListPositionPlayersView method is called and
     * LineupFormationFragment is not yet displayed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowListPositionPlayerFragmentWhenLineupFormationFragmentNotSet() {
        activity.showListPositionPlayersFragment(null, new int[]{}, 0, 0);
    }

    /**
     * Test the behavior on the activity when showListPositionPlayersView method is celled.
     */
    @Test
    public void testShowListPositionPlayersFragment() {
        activity.showLoadingSuccess(players);
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        assertTrue(this.getCurrentFragment() instanceof ListPositionPlayersFragment);
        assertEquals(1, fragmentManager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called and
     * LineupFormationFragment is not yet shown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowPlayerDetailsDialogWhenLineupFormationFragmentNoSet() {
        activity.showPlayerDetailsDialog(-1, false);
    }

    /**
     * Test the behavior when showPlayerDetailsDialog is called.
     */
    @Test
    public void testShowPlayerDetailsDialog() {
        activity.showLoadingSuccess(players);
        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
        Fragment dialog = fragmentManager.findFragmentByTag(PlayerDetailsDialog.TAG);
        assertTrue(dialog instanceof PlayerDetailsDialog);
        assertTrue(dialog.isVisible());
        assertNotNull(ShadowDialog.getLatestDialog());
    }

    /**
     * Test the behavior when showValid lineup is called and the lineup formation fragment is not
     * visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowValidLineupWhenLineupFormationFragmentIsNotVisible() {
        activity.showValidLineup();
    }

    /**
     * Test the behavior when showValid lineup is called.
     */
    @Test
    public void testShowValidLineup() {
        when(presenter.isChanged()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(presenter.isLineupValid()).thenReturn(true);
                return null;
            }
        }).when(presenter).setLineupValid(true);

        activity.showLoadingSuccess(players);
        this.assertMenuIconSaveVisibility(false);
        activity.showValidLineup();
        verify(presenter).setLineupValid(true);
        this.assertMenuIconSaveVisibility(true);
    }

    /**
     * Test the behavior when showInvalidValid lineup is called and the lineup formation
     * fragment is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowInvalidValidLineupWhenLineupFormationFragmentIsNotVisible() {
        activity.showInvalidLineup();
    }

    /**
     * Test the behavior when showInvalidValid lineup is called.
     */
    @Test
    public void testShowInvalidValidLineup() {
        when(presenter.isChanged()).thenReturn(true);
        when(presenter.isLineupValid()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(presenter.isLineupValid()).thenReturn(false);
                return null;
            }
        }).when(presenter).setLineupValid(false);

        activity.showLoadingSuccess(players);
        this.assertMenuIconSaveVisibility(true);
        activity.showInvalidLineup();
        verify(presenter).setLineupValid(false);
        this.assertMenuIconSaveVisibility(false);
    }

    /**
     * Test the behavior on the activity when onPlayerSelected method is called and
     * the ListPositionPlayers fragment is not displayed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerSelectedWhenListPositionPlayersFragmentNotSet() {
        activity.onPlayerSelected(players.get(0));
    }

    /**
     * Test the behavior on the activity when onPlayerSelected is called.
     */
    @Test
    public void testOnPlayerSelected() {
        when(presenter.isLineupValid()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(presenter.isChanged()).thenReturn(true);
                return null;
            }
        }).when(presenter).setChanged(true);

        final Player player = players.get(0);
        activity.showLoadingSuccess(players);
        this.assertMenuIconSaveVisibility(false);
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        activity.onPlayerSelected(player);
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
        verify(presenter).setChanged(true);
        this.assertMenuIconSaveVisibility(true);
        verify(lineupFormationFragmentPresenter).updateLineupPosition(player);
    }


    /**
     * Test the behavior when remove player is called and PlayerDetailsFragment is not yet shown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemovePlayerWhenPlayerDetailsDialogIsNotShown() {
        activity.removePlayer();
    }

    /**
     * Test the behavior when removePlayer is called.
     */
    @Test
    public void testRemovePlayer() {
        when(presenter.isLineupValid()).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                when(presenter.isChanged()).thenReturn(true);
                return null;
            }
        }).when(presenter).setChanged(true);

        activity.showLoadingSuccess(players);
        this.assertMenuIconSaveVisibility(false);
        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
        activity.removePlayer();
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        verify(presenter).setChanged(true);
        verify(lineupFormationFragmentPresenter).removeSelectedPlayer();
        this.assertMenuIconSaveVisibility(true);
    }

    /**
     * Test the behavior when R.id.lineupMenu_likes is clicked and the LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowLineupLikesFragmentWhenLineupFormationFragmentIsNotVisible() {
        assertTrue(shadowOf(activity).clickMenuItem(R.id.lineupMenu_likes));
    }

    /**
     * Test the behavior when R.id.lineupMenu_likes is clicked.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowLineupLikesFragment() {
        when(presenter.canEditLineup()).thenReturn(true);
        assertTrue(shadowOf(activity).clickMenuItem(R.id.lineupMenu_likes));
        activity.showLoadingSuccess(players);
        assertEquals(View.VISIBLE, btnChangeFormation.getVisibility());
        shadowOf(activity).clickMenuItem(R.id.lineupMenu_likes);
        assertTrue(this.getCurrentFragment() instanceof LikeFragment);
        assertEquals(1, fragmentManager.getBackStackEntryCount());
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when R.id.lineupMenu_comments is clicked and the LineupFormationFragment
     * is not visible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowLineupCommentsFragmentWhenLineupFormationFragmentIsNotVisible() {
        assertTrue(shadowOf(activity).clickMenuItem(R.id.lineupMenu_comments));
    }

    /**
     * Test the behavior when R.id.lineupMenu_comments is clicked.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShowLineupCommentsFragment() {
        when(presenter.canEditLineup()).thenReturn(true);
        assertTrue(shadowOf(activity).clickMenuItem(R.id.lineupMenu_comments));
        activity.showLoadingSuccess(players);
        assertEquals(View.VISIBLE, btnChangeFormation.getVisibility());
        shadowOf(activity).clickMenuItem(R.id.lineupMenu_likes);
        assertTrue(this.getCurrentFragment() instanceof CommentsFragment);
        assertEquals(1, fragmentManager.getBackStackEntryCount());
        assertEquals(View.GONE, btnChangeFormation.getVisibility());
    }

    /**
     * Test the behavior when onBackPressed is called and the current fragment
     * is ListPositionPlayersFragment.
     */
    @Test
    public void testOnBackButtonPressedWhenListPositionPlayersFragmentIsActive() {
        activity.showLoadingSuccess(players);
        activity.showListPositionPlayersFragment(POSITION_PLACE.KEEPERS, new int[]{}, 0, 0);
        assertTrue(this.getCurrentFragment() instanceof ListPositionPlayersFragment);
        activity.onBackPressed();
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when onBackPressed is called and the current fragment
     * is LineupFormationFragment and the PlayerDetailsDialog is showed.
     */
    @Test
    public void testDialogDismissedWhenPlayerDetailsDialogIsActive() {
        activity.showLoadingSuccess(players);
        activity.showPlayerDetailsDialog(players.get(0).getId(), false);
        /**
         * When a dialog fragment is active and back button is pressed, onBackPressed method on
         * the activity will not be called, but the onDismissed method
         * on the DialogFragment will be.
         */
        PlayerDetailsDialog dialog =
                (PlayerDetailsDialog) fragmentManager.findFragmentByTag(PlayerDetailsDialog.TAG);
        assertTrue(dialog.isVisible());
        dialog.dismiss();
        assertFalse(dialog.isVisible());
        assertTrue(this.getCurrentFragment() instanceof LineupFormationFragment);
        assertEquals(0, fragmentManager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when onBackPressed is called and the LineupFormationFragment is active.
     */
    @Test
    public void testOnBackButtonPressedWhenLineupFormationIsActive() {
        activity.showLoadingSuccess(players);
        activity.onBackPressed();
        assertTrue(shadowOf(activity).isFinishing());
    }
}
