package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeActivityModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.ui.adapter.ListLineupsAdapter;
import com.android.finki.mpip.footballdreamteam.ui.dialog.InfoDialog;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeActivityPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Borce on 06.08.2016.
 */
public class HomeActivity extends BaseActivity {

    @Inject
    HomeActivityPresenter presenter;

    @BindString(R.string.homeActivity_title)
    String title;

    @BindString(R.string.homeActivity_spinnerInitialDataLoading_text)
    String spinnerInitialDataText;

    @BindString(R.string.homeActivity_spinnerLineupsLoading_text)
    String spinnerLineupText;

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

    @BindView(R.id.homeLayout_lineupsListView)
    ListView lineupsListView;

    @BindView(R.id.homeLayout_mainContent)
    RelativeLayout lineupsListContent;

    private ListLineupsAdapter lineupsAdapter;

    private LineupsListViewFooterHolder lineupsFooterHolder;

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
        /* Set the toolbar */
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }

        this.setupSidebar();
        this.setupLineupsListView();
        presenter.loadData();
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
     * Called wehn the options menu is ready to be creted or recreated.
     *
     * @param menu menu that will be created
     * @return whatever the menu should be created or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
                presenter.refresh();
                return true;
            case R.id.mainMenu_logout:
                this.logout();
                break;
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
     * Setup the list view for the lineups.
     */
    private void setupLineupsListView() {
        View footer = this.getLayoutInflater().inflate(R.layout.lineups_footer, null);
        lineupsFooterHolder = new LineupsListViewFooterHolder(footer);
        lineupsListView.addFooterView(footer);
        lineupsAdapter = new ListLineupsAdapter(this);
        lineupsListView.setAdapter(lineupsAdapter);
    }

    /**
     * Show the spinner for when the initial data is loading.
     */
    public void showInitialDataLoading() {
        txtSpinner.setText(spinnerInitialDataText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(lineupsListContent, false);
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
     * Show the spinner for when the lineups data is loading.
     */
    public void showLineupsLoading() {
        txtSpinner.setText(spinnerLineupText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(lineupsListContent, false);
    }

    /**
     * Show the error layout when a error occurred while loading the data.
     */
    public void showErrorLoading() {
        super.toggleVisibility(errorLoadingLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(lineupsListContent, false);
    }

    /**
     * Called when loading hte lineups is successful.
     *
     * @param lineups list of Lineups
     */
    public void successLoadingLineups(List<Lineup> lineups) {
        super.toggleVisibility(lineupsListContent, true);
        super.toggleVisibility(lineupsFooterHolder.lineupsListViewSpinner, false);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
        lineupsAdapter.update(lineups);
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
     * Handle click on the lineups list.
     *
     * @param position lineup position in the list
     */
    @OnItemClick(R.id.homeLayout_lineupsListView)
    void onLineupSelected(int position) {
        Lineup lineup = lineupsAdapter.getItem(position);
        Intent intent = new Intent(this, LineupDetailsActivity.class);
        intent.putExtra(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, lineup.getId());
        this.startActivity(intent);
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
     * Class holder for the lineups list view footer.
     */
    class LineupsListViewFooterHolder {

        /**
         * Is marked as nullable because the view is injected from the ButterKnife after is has been
         * added to the list view.
         */
        @Nullable
        @BindView(R.id.lineupsListVIew_spinnerLoadMore)
        ProgressBar lineupsListViewSpinner;

        LineupsListViewFooterHolder(View view) {
            ButterKnife.bind(this, view);
        }

        /**
         * Load more lineups when the button load more is clicked.
         */
        @OnClick(R.id.lineupsListView_btnLoadMore)
        void loadMore() {
            HomeActivity.super.toggleVisibility(lineupsListViewSpinner, true);
            HomeActivity.this.presenter.loadMoreLineups();
        }
    }
}