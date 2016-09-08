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
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeActivityModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListLineupsFragment;
import com.android.finki.mpip.footballdreamteam.ui.listener.ActivityTitleSetterListener;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeActivityPresenter;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 06.08.2016.
 */
public class HomeActivity extends BaseActivity implements ListLineupsAdapter.Listener,
        ActivityTitleSetterListener {

    @Inject
    HomeActivityPresenter presenter;

    @BindString(R.string.homeActivity_title)
    String title;

    @BindString(R.string.homeActivity_spinnerInitialDataLoading_text)
    String spinnerText;

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

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindView(R.id.error_loading)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.content)
    FrameLayout content;

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home_layout);

        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getUserComponent()
                .plus(new HomeActivityModule(this)).inject(this);

        this.setSupportActionBar(toolbar);
        this.setTitle(title);
        this.setupSidebar();
        presenter.loadData();
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
     * Set the activity title.
     *
     * @param title activity title
     */
    @Override
    public void setTitle(String title) {
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Called after activity start-up is complete, after onStart.
     *
     * @param savedInstanceState saved state for when the activity is recreated
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
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
        if (presenter.isMainViewVisible()) {
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
        super.onDestroy();
        ((MainApplication) this.getApplication()).releaseUserComponent();
    }

    /**
     * Show the spinner for when the initial data is loading.
     */
    public void showInitialDataLoading() {
        txtSpinner.setText(spinnerText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(content, false);
    }

    /**
     * Show the info dialog for when the initial data is laoding.
     */
    public void showInfoDialog() {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        InfoDialog dialog = InfoDialog.newInstance(infoDialogTitle, infoDialogMessage);
        dialog.show(transaction, InfoDialog.TAG);
    }

    /**
     * Show the error layout when a error occurred while loading the data.
     */
    public void showErrorLoading() {
        super.toggleVisibility(errorLoadingLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(content, false);
    }

    /**
     * Bind the button click to call the presenter method to reload the
     * data when a error happened.
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadData();
    }

    /**
     * Called when loading the initial data is successful.
     */
    public void showSuccessLoading() {
        super.toggleVisibility(content, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new ListLineupsFragment()).commit();
        presenter.setMainViewVisible(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when the user has selected the options to be logged out.
     */
    private void logout() {
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
    public void showLineupPlayers(Lineup lineup) {
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
    public void showLineupLikes(Lineup lineup) {
        LikeFragment fragment = LikeFragment.newInstance(lineup);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(ListLineupsFragment.TAG);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        presenter.setMainViewVisible(false);
        this.invalidateOptionsMenu();
    }

    /**
     * Show the fragment for displaying lineup comments.
     *
     * @param lineup lineup for which the comments will be displayed
     */
    @Override
    public void showLineupComments(Lineup lineup) {
        CommentsFragment fragment = CommentsFragment.newInstance(lineup.getId());
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(ListLineupsFragment.TAG);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        presenter.setMainViewVisible(false);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            this.setTitle(title);
            presenter.setMainViewVisible(true);
            this.invalidateOptionsMenu();
        }
        super.onBackPressed();
    }
}