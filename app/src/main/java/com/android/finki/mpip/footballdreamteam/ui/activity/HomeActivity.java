package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.component.HomeView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.BaseFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListLineupsFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeViewPresenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 06.08.2016.
 */
public class HomeActivity extends BaseActivity implements HomeView,
                                                          ListLineupsFragment.Listener,
        BaseFragment.Listener {

    private Logger logger = LoggerFactory.getLogger(HomeActivity.class);

    private HomeViewPresenter presenter;

    @BindString(R.string.homeLayout_title)
    String title;

    @BindString(R.string.sidebarOpen_title)
    String sidebarOpenTitle;

    @BindString(R.string.initialDataLoading_title)
    String infoDialogTitle;

    @BindString(R.string.initialDataLoading_message)
    String infoDialogMessage;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.homeLayout_drawerLayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.sidebar)
    NavigationView sidebar;

    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindString(R.string.homeLayout_spinnerTeamsLoading_text)
    String teamsLoadingSpinnerText;

    @BindString(R.string.homeLayout_teamsLoadingFailed_text)
    String teamsLoadingFailedText;

    @BindString(R.string.homeLayout_spinnerTeamsStoring_text)
    String teamsStoringSpinnerText;

    @BindString(R.string.homeLayout_teamsStoringFailed_text)
    String teamsStoringFailedText;

    @BindString(R.string.homeLayout_spinnerPositionsLoading_text)
    String positionsLoadingSpinnerText;

    @BindString(R.string.homeLayout_positionsLoadingFailed_text)
    String positionsLoadingFailedText;

    @BindString(R.string.homeLayout_spinnerPositionsStoring_text)
    String positionsStoringSpinnerText;

    @BindString(R.string.homeLayout_positionsStoringFailed_text)
    String positionsStoringFailedText;

    @BindString(R.string.homeLayout_spinnerPlayersLoading_text)
    String playersLoadingSpinnerText;

    @BindString(R.string.homeLayout_playersLoadingFailed_text)
    String playersLoadingFailedText;

    @BindString(R.string.homeLayout_spinnerPlayersStoring_text)
    String playersStoringSpinnerText;

    @BindString(R.string.homeLayout_playersStoringFailed_text)
    String playersStoringFailedText;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindView(R.id.error)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.content)
    FrameLayout content;

    /**
     * Set the presenter for the actiivty.
     *
     * @param presenter activity presenter
     */
    @Inject
    public void setPresenter(HomeViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home_layout);

        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getUserComponent()
                .plus(new HomeViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        this.changeTitle(title);
        this.setupSidebar();
        presenter.onViewCreated();
        presenter.onViewLayoutCreated();
    }

    /**
     * Setup the sidebar drawer.
     */
    private void setupSidebar() {
        this.setupSidebarToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
        sidebar.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        onSidebarItemSelected(item);
                        return true;
                    }
                });
    }

    /**
     * Setup the drawer toggle for the sidebar.
     */
    private void setupSidebarToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.sidebar_drawerOpen_text, R.string.sidebar_drawerClose_text) {

            /* Called when the drawer is open */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(sidebarOpenTitle);
                }
                invalidateOptionsMenu();
            }

            /* Called when the drawer is closed */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(title);
                }
                invalidateOptionsMenu();
            }
        };
    }

    /**
     * Called after activity start-up is complete, after onStart.
     *
     * @param savedInstanceState saved state for when the activity is recreated
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onPostCreate");
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * Called when the options menu is ready to be creted or recreated.
     *
     * @param menu menu that will be created
     * @return whatever the menu should be created or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logger.info("onCreateOptionsMenu");
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof ListLineupsFragment) {
            this.getMenuInflater().inflate(R.menu.main_menu, menu);
            if (this.getSupportActionBar() != null) {
                this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            drawerToggle.setDrawerIndicatorEnabled(true);
            return true;
        } else {
            drawerToggle.setDrawerIndicatorEnabled(false);
            return false;
        }
    }

    /**
     * Handle click on the options menu item.
     *
     * @param item menu item that has been selected
     * @return whatever the select should be ignored or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.info("onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.mainMenu_createLineup:
                this.startCreteLineupActivity();
                return true;
            case R.id.mainMenu_refresh:
                Fragment fragment = this.getSupportFragmentManager()
                        .findFragmentById(R.id.content);
                if (fragment instanceof ListLineupsFragment) {
                    ((ListLineupsFragment) fragment).refresh();
                    return true;
                }
                return false;
            case R.id.mainMenu_logout:
                this.logout();
                return true;
            case android.R.id.home:
                if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    this.onBackPressed();
                    return true;
                }
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Handle click on the sidebar menu item.
     *
     * @param item menu item that has been clicked
     */
    private void onSidebarItemSelected(MenuItem item) {
        this.onOptionsItemSelected(item);
    }

    /**
     * Start the CreteLineupActivity.
     */
    private void startCreteLineupActivity() {
        this.startActivity(new Intent(this, CreateLineupActivity.class));
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        logger.info("onDestroy");
        super.onDestroy();
        presenter.onViewLayoutDestroyed();
        presenter.onViewDestroyed();
        if (this.isFinishing()) {
            ((MainApplication) this.getApplication()).releaseUserComponent();
        }
    }

    /**
     * Called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        logger.info("onBackPressed");
        super.onBackPressed();
        if (this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.changeTitle(title);
            this.invalidateOptionsMenu();
        }
    }

    /**
     * Called when some of the sub fragments is active.
     */
    @Override
    public void onFragmentActive() {
        this.invalidateOptionsMenu();
    }

    /**
     * Change the action bar title.
     *
     * @param title new action bar title
     */
    @Override
    public void changeTitle(String title) {
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Show the spinner for when the initial data is loading.
     */
    @Override
    public void showInitialDataLoading() {
        logger.info("showInitialDataLoading");
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(content, false);
    }

    /**
     * Show the info dialog for when the initial data is laoding.
     */
    @Override
    public void showInitialDataInfoDialog() {
        logger.info("showInitialDataInfoDialog");
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        InfoDialog dialog = InfoDialog.newInstance(infoDialogTitle, infoDialogMessage);
        dialog.show(transaction, InfoDialog.TAG);
    }

    /**
     * Called when loading the initial data is successful.
     */
    @Override
    public void showInitialDataLoadingSuccess() {
        logger.info("showInitialDataLoadingSuccess");
        super.toggleVisibility(content, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new ListLineupsFragment()).commit();
    }

    /**
     * Show the request failed layout content.
     */
    private void showInitialDataLoadingFailed() {
        super.toggleVisibility(errorLoadingLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(content, false);
    }

    /**
     * Called when a request has been send to load the teams
     */
    @Override
    public void showTeamsLoading() {
        txtSpinner.setText(teamsLoadingSpinnerText);
    }

    /**
     * Called when loading the teams failed.
     */
    @Override
    public void showTeamsLoadingFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(teamsLoadingFailedText);
    }

    /**
     * Called when the teams has started storing in the database.
     */
    @Override
    public void showTeamsStoring() {
        txtSpinner.setText(teamsStoringSpinnerText);
    }

    /**
     * Called when storing the teams failed.
     */
    @Override
    public void showTeamsStoringFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(teamsStoringFailedText);
    }

    /**
     * Called when a request has been send to load the positions.
     */
    @Override
    public void showPositionsLoading() {
        txtSpinner.setText(positionsLoadingSpinnerText);
    }

    /**
     * Called when loading the positions failed.
     */
    @Override
    public void showPositionsLoadingFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(positionsLoadingFailedText);
    }

    /**
     * Called when the positions has started storing in the database.
     */
    @Override
    public void showPositionsStoring() {
        txtSpinner.setText(positionsStoringSpinnerText);
    }

    /**
     * Called when storing the positions failed.
     */
    @Override
    public void showPositionsStoringFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(positionsStoringFailedText);
    }

    /**
     * Called when the a request has been send to load the players.
     */
    @Override
    public void showPlayersLoading() {
        txtSpinner.setText(playersLoadingSpinnerText);
    }

    /**
     * Called when loading the players failed.
     */
    @Override
    public void showPlayersLoadingFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(playersLoadingFailedText);
    }

    /**
     * Called when the players has started storing in the database.
     */
    @Override
    public void showPlayersStoring() {
        txtSpinner.setText(playersStoringSpinnerText);
    }

    /**
     * Called when storing the players failed.
     */
    @Override
    public void showPlayersStoringFailed() {
        this.showInitialDataLoadingFailed();
        txtError.setText(playersStoringFailedText);
    }

    /**
     * Bind the button click to call the presenter method to reload the
     * data when a error happened.
     */
    @OnClick(R.id.error_btnTryAgain)
    void reload() {
        logger.info("btn 'Try Again' clicked");
        presenter.loadData();
    }

    /**
     * Called when the user has selected the options to be logged out.
     */
    private void logout() {
        logger.info("logout options item selected");
        presenter.logout();
        this.startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Show the activity for displaying lineup players.
     *
     * @param lineup lineup for which the players will be showed
     */
    @Override
    public void showLineupPlayersView(Lineup lineup) {
        logger.info("showLineupPlayersView");
        Intent intent = new Intent(this, LineupPlayersActivity.class);
        intent.putExtra(LineupPlayersActivity.LINEUP_BUNDLE_KEY, lineup);
        this.startActivity(intent);
    }

    /**
     * Show the fragment for displaying lineup likes.
     *
     * @param lineup lineup for which the likes will be displayed
     */
    @Override
    public void showLineupLikesView(Lineup lineup) {
        logger.info("showLineupLikesView");
        LikeFragment fragment = LikeFragment.newInstance(lineup);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(ListLineupsFragment.TAG);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    /**
     * Show the fragment for displaying lineup comments.
     *
     * @param lineup lineup for which the comments will be displayed
     */
    @Override
    public void showLineupCommentsView(Lineup lineup) {
        logger.info("showLineupCommentsView");
        CommentsFragment fragment = CommentsFragment.newInstance(lineup.getId());
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(ListLineupsFragment.TAG);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}