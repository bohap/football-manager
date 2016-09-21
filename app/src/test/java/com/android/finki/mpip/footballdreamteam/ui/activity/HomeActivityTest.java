package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListLineupsViewComponent;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListLineupsFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CommentsViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListLineupsViewPresenter;

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
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Borce on 09.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class HomeActivityTest {

    @Mock
    private HomeViewPresenter presenter;

    @Mock
    private HomeViewComponent component;

    @Mock
    private ListLineupsViewComponent listLineupsViewComponent;

    @Mock
    private ListLineupsViewPresenter listLineupsViewPresenter;

    @Mock
    private LikeViewComponent likeViewComponent;

    @Mock
    private LikeViewPresenter likeViewPresenter;

    @Mock
    private CommentsViewComponent commentsViewComponent;

    @Mock
    private CommentsViewPresenter commentsViewPresenter;

    private MockApplication application;
    private ActivityController<HomeActivity> controller;
    private HomeActivity activity;
    private RelativeLayout error;
    private TextView txtError;
    private RelativeLayout spinner;
    private TextView txtSpinner;
    private FrameLayout content;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        application = (MockApplication) RuntimeEnvironment.application;
        application.setHomeViewComponent(component);
        application.setListLineupsViewComponent(listLineupsViewComponent);
        application.setLikeViewComponent(likeViewComponent);
        application.setCommentsViewComponent(commentsViewComponent);
        application.createAuthComponent();
        this.mockDependencies();
        controller = Robolectric.buildActivity(HomeActivity.class);
        activity = controller.create().start().resume().visible().get();
        this.getViews();
    }

    @After
    public void shutdown() {
        controller.pause().stop().destroy();
    }

    /**
     * Mock the dependencies for the activity.
     */
    private void mockDependencies() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                HomeActivity activity = (HomeActivity) invocation.getArguments()[0];
                activity.setPresenter(presenter);
                return null;
            }
        }).when(component).inject(any(HomeActivity.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ListLineupsFragment fragment = (ListLineupsFragment) invocation.getArguments()[0];
                fragment.setPresenter(listLineupsViewPresenter);
                return null;
            }
        }).when(listLineupsViewComponent).inject(any(ListLineupsFragment.class));
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
     * Get the main views from the activity layout.
     */
    private void getViews() {
        error = (RelativeLayout) activity.findViewById(R.id.error);
        assertNotNull(error);
        txtError = (TextView) activity.findViewById(R.id.txtError);
        assertNotNull(txtError);
        spinner = (RelativeLayout) activity.findViewById(R.id.spinner);
        assertNotNull(spinner);
        txtSpinner = (TextView) activity.findViewById(R.id.spinner_text);
        assertNotNull(txtSpinner);
        content = (FrameLayout) activity.findViewById(R.id.content);
        assertNotNull(component);
    }

    /**
     * Test that the activity is successfully created.
     */
    @Test
    public void testActivityIsCreated() {
        String title = activity.getString(R.string.homeLayout_title);
        assertNotNull(activity.getSupportActionBar());
        assertEquals(title, activity.getSupportActionBar().getTitle());
        verify(presenter).onViewCreated();
        verify(presenter).onViewLayoutCreated();
    }

    /**
     * Test the behavior when onCreateOptionsMenu is called and the ListLineupFragment is active.
     */
    @Test
    public void testOnCreateOptionsMenuWhenListLineupsFragmentIsActive() {
        ShadowLooper.pauseMainLooper();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new ListLineupsFragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        Menu menu = new RoboMenu(application);
        assertTrue(activity.onCreateOptionsMenu(menu));
        assertNotNull(menu);
        assertNotNull(menu.findItem(R.id.mainMenu_refresh));
        assertNotNull(menu.findItem(R.id.mainMenu_createLineup));
        assertNotNull(menu.findItem(R.id.mainMenu_logout));
    }

    /**
     * Test the behavior when onCreateOptions menu is called and the ListLineupsFragment is
     * not active.
     */
    @Test
    public void testOnCreateOptionsMenuWhenListLineupsFragmentIsNotActive() {
        Menu menu = new RoboMenu(application);
        assertFalse(activity.onCreateOptionsMenu(menu));
        assertNotNull(menu);
        assertNull(menu.findItem(R.id.mainMenu_refresh));
        assertNull(menu.findItem(R.id.mainMenu_createLineup));
        assertNull(menu.findItem(R.id.mainMenu_logout));
    }

    /**
     * Test the behavior when the create lineup options menu item is selected.
     */
    @Test
    public void testCreateLineupMenuItemSelected() {
        MenuItem item = new RoboMenuItem(R.id.mainMenu_createLineup);
        assertTrue(activity.onOptionsItemSelected(item));
        Intent expectedIntent = new Intent(activity, CreateLineupActivity.class);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    /**
     * Test the behavior when refresh menu item is selected and the ListLineupsFragment is active.
     */
    @Test
    public void testRefreshMenuItemSelectedWhenListLineupsFragmentIsActive() {
        ShadowLooper.pauseMainLooper();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new ListLineupsFragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        MenuItem item = new RoboMenuItem(R.id.mainMenu_refresh);
        assertTrue(activity.onOptionsItemSelected(item));
        verify(listLineupsViewPresenter).refresh();
    }

    /**
     * Test the behavior when refresh menu item is selected and the ListLineupsFragment is
     * not active.
     */
    @Test
    public void testRefreshMenuItemSelectedWhenListLineupsFragmentIsNotActive() {
        MenuItem item = new RoboMenuItem(R.id.mainMenu_refresh);
        assertFalse(activity.onOptionsItemSelected(item));
        verify(listLineupsViewPresenter, never()).refresh();
    }

    /**
     * Test the behavior when logout options menu item is selected.
     */
    @Test
    public void testLogoutMenuItemSelected() {
        MenuItem item = new RoboMenuItem(R.id.mainMenu_logout);
        assertTrue(activity.onOptionsItemSelected(item));
        Intent expectedIntent = new Intent(activity, LoginActivity.class);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
        assertNull(application.getAuthComponent());
        assertTrue(activity.isFinishing());
    }

    /**
     * Test the behavior when home menu item is selected and there are fragment on the back stack.
     */
    @Test
    public void testHomeMenuItemSelectedWhenFragmentBackStackIsBiggerThenZero() {
        FragmentManager manager = activity.getSupportFragmentManager();
        ShadowLooper.pauseMainLooper();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new ListLineupsFragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        ShadowLooper.pauseMainLooper();
        transaction = manager.beginTransaction();
        transaction.addToBackStack("Tag");
        transaction.replace(R.id.content, new Fragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertEquals(1, manager.getBackStackEntryCount());
        MenuItem item = new RoboMenuItem(android.R.id.home);
        assertTrue(activity.onOptionsItemSelected(item));
        assertEquals(0, manager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when home menu item is selected and the back stack don't have fragments.
     */
    @Test
    public void testHomeMenuItemSelectedWhenFragmentBackStackIsZero() {
        FragmentManager manager = activity.getSupportFragmentManager();
        ShadowLooper.pauseMainLooper();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new ListLineupsFragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertEquals(0, manager.getBackStackEntryCount());
        activity.invalidateOptionsMenu();
        MenuItem item = new RoboMenuItem(android.R.id.home);
        assertTrue(activity.onOptionsItemSelected(item));
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertEquals(0, manager.getBackStackEntryCount());
        assertFalse(activity.isFinishing());
    }

    /**
     * Test the behavior when the back button is pressed and there are fragments on the
     * back stack.
     */
    @Test
    public void testOnBackPressedWhenThereAreFragmentsOnTheBackStack() {
        String newTitle = "Test Title";
        String originalTitle = activity.getString(R.string.homeLayout_title);
        FragmentManager manager = activity.getSupportFragmentManager();
        ShadowLooper.pauseMainLooper();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new ListLineupsFragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        ShadowLooper.pauseMainLooper();
        transaction = manager.beginTransaction();
        transaction.addToBackStack("Tag");
        transaction.replace(R.id.content, new Fragment());
        transaction.commit();
        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertEquals(1, manager.getBackStackEntryCount());
        assertNotNull(activity.getSupportActionBar());
        activity.getSupportActionBar().setTitle(newTitle);
        activity.onBackPressed();
        assertEquals(0, manager.getBackStackEntryCount());
        assertEquals(originalTitle, activity.getSupportActionBar().getTitle());
    }

    /**
     * Test the behavior when showInitialDataLoading is called.
     */
    @Test
    public void testShowInitialDataLoading() {
        content.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        activity.showInitialDataLoading();
        assertEquals(View.VISIBLE, activity.spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
    }

    /**
     * Test the behavior when showInitialDataInfoDialog is called.
     */
    @Test
    public void testShowInitialDataInfoDialog() {
        activity.showInitialDataInfoDialog();
        Dialog dialog = ShadowDialog.getLatestDialog();
        assertNotNull(dialog);
        assertTrue(dialog instanceof AlertDialog);
    }

    /**
     * Test the behavior when showTeamsLoading is called.
     */
    @Test
    public void testShowTeamsLoading() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerTeamsLoading_text);
        activity.showTeamsLoading();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showTeamsLoadingFailed is called.
     */
    @Test
    public void testShowTeamsLoadingFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_teamsLoadingFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showTeamsLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when showTeamsStoring is called.
     */
    @Test
    public void testShowTeamsStoring() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerTeamsStoring_text);
        activity.showTeamsStoring();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showTeamsStoringFailed is called.
     */
    @Test
    public void testShowTeamsStoringFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_teamsStoringFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showTeamsStoringFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when showPositionsLoading is called.
     */
    @Test
    public void testShowPositionsLoading() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerPositionsLoading_text);
        activity.showPositionsLoading();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showPositionsLoadingFailed is called.
     */
    @Test
    public void testShowPositionsLoadingFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_positionsLoadingFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showPositionsLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when showPositionsStoring is called.
     */
    @Test
    public void testShowPositionsStoring() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerPositionsStoring_text);
        activity.showPositionsStoring();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showPositionsStoringFailed is called.
     */
    @Test
    public void testShowPositionsStoringFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_positionsStoringFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showPositionsStoringFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when showTeamsLoading is called.
     */
    @Test
    public void testShowPlayersLoading() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerPlayersLoading_text);
        activity.showPlayersLoading();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showPlayersLoadingFailed is called.
     */
    @Test
    public void testShowPlayersLoadingFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_playersLoadingFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showPlayersLoadingFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when showPlayersStoring is called.
     */
    @Test
    public void testShowPlayersStoring() {
        txtSpinner.setText(null);
        String text = activity.getString(R.string.homeLayout_spinnerPlayersStoring_text);
        activity.showPlayersStoring();
        assertEquals(text, txtSpinner.getText());
    }

    /**
     * Test the behavior when showPlayersStoringFailed is called.
     */
    @Test
    public void testShowPlayersStoringFailed() {
        txtError.setText(null);
        String text = activity.getString(R.string.homeLayout_playersStoringFailed_text);
        spinner.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        activity.showPlayersStoringFailed();
        assertEquals(View.VISIBLE, error.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, content.getVisibility());
        assertEquals(text, txtError.getText());
    }

    /**
     * Test the behavior when button 'Try Again' is clicked.
     */
    @Test
    public void testBtnTryAgainClick() {
        Button btn = (Button) activity.findViewById(R.id.error_btnTryAgain);
        assertNotNull(btn);
        assertTrue(btn.performClick());
        verify(presenter).loadData();
    }

    /**
     * Test the behavior when showLoadingSuccess is called.
     */
    @Test
    public void showLoadingSuccess() {
        spinner.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        activity.showInitialDataLoadingSuccess();
        assertEquals(View.VISIBLE, content.getVisibility());
        assertEquals(View.GONE, spinner.getVisibility());
        assertEquals(View.GONE, error.getVisibility());
        Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.content);
        assertNotNull(fragment);
        assertTrue(fragment instanceof ListLineupsFragment);
    }

    /**
     * Test the behavior when showLineupPlayersView is called.
     */
    @Test
    public void testShowLineupPlayersView() {
        final Lineup lineup = new Lineup(1, 1);
        activity.showLineupPlayersView(lineup);
        Intent expectedIntent = new Intent(activity, LineupPlayersActivity.class);
        expectedIntent.putExtra(LineupPlayersView.LINEUP_BUNDLE_KEY, lineup);
        Intent actualIntent = shadowOf(activity).getNextStartedActivity();
        assertNotNull(actualIntent);
        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    /**
     * Test the behavior when showLineupLikes is called.
     */
    @Test
    public void testShowLineupLikesView() {
        final Lineup lineup = new Lineup(1, 1);
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new Fragment());
        transaction.commit();
        activity.showLineupLikesView(lineup);
        Fragment currentFragment = manager.findFragmentById(R.id.content);
        assertNotNull(currentFragment);
        assertTrue(currentFragment instanceof LikeFragment);
        assertEquals(1, manager.getBackStackEntryCount());
    }

    /**
     * Test the behavior when showLineupCommentsView is called.
     */
    @Test
    public void testShowLineupCommentsView() {
        final Lineup lineup = new Lineup(1, 1);
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new Fragment());
        transaction.commit();
        activity.showLineupCommentsView(lineup);
        Fragment currentFragment = manager.findFragmentById(R.id.content);
        assertNotNull(currentFragment);
        assertTrue(currentFragment instanceof CommentsFragment);
        assertEquals(1, manager.getBackStackEntryCount());
    }
}
