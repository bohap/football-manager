package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.BaseFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Borce on 15.08.2016.
 */
public class LineupPlayersActivity extends LineupPlayersBaseActivity implements
        BaseFragment.Listener, LineupPlayersView, ListPositionPlayersFragment.Listener,
        LineupFormationFragment.Listener, PlayerDetailsDialog.Listener {

    private Logger logger = LoggerFactory.getLogger(LineupPlayersActivity.class);
    private LineupPlayersViewPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindString(R.string.lineupPlayersActivity_title)
    String title;

    @BindString(R.string.formationContextMenuTitle)
    String contextMenuTitle;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.lineupPlayersActivity_spinnerLoadPlayers_text)
    String spinnerLoadingLineupText;

    @BindString(R.string.lineupPlayersActivity_spinnerUpdatingLineup_text)
    String spinnerUpdatingLineupText;

    @BindView(R.id.error)
    RelativeLayout error;

    @BindView(R.id.txtError)
    TextView txtError;

    @BindString(R.string.lineupPlayersActivity_loadingLineupFailed_text)
    String loadingLineupFailedText;

    @BindView(R.id.lineupPlayersActivity_updateFailed)
    LinearLayout lineupUpdateErrorLayout;

    @BindView(R.id.lineupPlayersPlayers_mainContent)
    RelativeLayout mainContent;

    @BindView(R.id.lineupPlayersLayout_btnChangeFormation)
    ButtonAwesome btnChangeFormation;

    /**
     * Set the presenter for the activity.
     *
     * @param presenter activity presenter
     */
    @Inject
    public void setPresenter(LineupPlayersViewPresenter presenter) {
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
        this.setContentView(R.layout.lineup_players_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getAuthComponent()
                .plus(new LineupPlayersViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.registerForContextMenu(btnChangeFormation);
        presenter.onViewCreated(this.getIntent().getExtras());
        presenter.onViewLayoutCreated();
    }

    /**
     * Called when the options menu is ready to be created or recreated.
     *
     * @param menu menu that will be created
     * @return whatever the menu should be showed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logger.info("onCreateOptionsMenu");
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof LineupFormationFragment) {
            this.getMenuInflater().inflate(R.menu.lineup_players_menu, menu);
            MenuItem item = menu.findItem(R.id.lineupMenu_save);
            if (presenter.isChanged() && presenter.isLineupValid() &&
                    !presenter.isLineupUpdateFailed()) {
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    /**
     * Called when a options item has been selected.
     *
     * @param item selected menu item
     * @return whatever the action should be canceled or not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.info("onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.lineupMenu_save:
                super.checkLineupFormationFragmentVisibility();
                presenter.update();
                return true;
            case R.id.lineupMenu_likes:
                this.showLineupLikesFragment();
                return true;
            case R.id.lineupMenu_comments:
                this.showLineupCommentsFragment();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the context menu is ready to be created.
     *
     * @param menu     context menu that will be crated
     * @param v        view on which the menu is bind
     * @param menuInfo information about the menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        logger.info("onCreteContextMenu");
        menu.setHeaderTitle(contextMenuTitle);
        this.getMenuInflater().inflate(R.menu.formations_menu, menu);
    }

    /**
     * Handle select on the context menu.
     *
     * @param item menu item selected
     * @return whatever the action should be canceled or not
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        logger.info("onContextItemSelected");
        switch (item.getItemId()) {
            case R.id.formation_4_4_2:
                presenter.updateFormation(LineupUtils.FORMATION.F_4_4_2);
                return true;
            case R.id.formation_3_2_3_2:
                presenter.updateFormation(LineupUtils.FORMATION.F_3_2_3_2);
                return true;
            case R.id.formation_4_2_3_1:
                presenter.updateFormation(LineupUtils.FORMATION.F_4_2_3_1);
                return true;
            case R.id.formation_4_3_3:
                presenter.updateFormation(LineupUtils.FORMATION.F_4_3_3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewLayoutDestroyed();
        presenter.onViewDestroyed();
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
     * Checks if the formation has been changed.
     *
     * @return whatever the formation has been changed
     */
    @Override
    protected boolean isChanged() {
        return presenter.isChanged();
    }

    /**
     * Toggle the button "Change Formation" visibility.
     *
     * @param visible whatever the button is visible or not
     */
    @Override
    protected void toggleBtnChangeFormation(boolean visible) {
        if (presenter.canEditLineup()) {
            super.toggleVisibility(btnChangeFormation, visible);
        }
    }

    /**
     * Handle click on the button to change formation.
     */
    @OnClick(R.id.lineupPlayersLayout_btnChangeFormation)
    void onBtnChangeFormationClicked() {
        logger.info("btn 'Change Formation' clicked");
        btnChangeFormation.showContextMenu();
    }

    /**
     * Called when a request to load players has been send has been send
     */
    @Override
    public void showLoading() {
        logger.info("showLoading");
        txtSpinner.setText(spinnerLoadingLineupText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(error, false);
        super.toggleVisibility(mainContent, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
    }

    /**
     * Called when loading the players is successful.
     *
     * @param players List of players in the lineup
     */
    @Override
    public void showLoadingSuccess(List<Player> players) {
        logger.info("showLoadingSuccess");
        super.toggleVisibility(mainContent, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(error, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
        super.showLineupFormationFragment(players, presenter.canEditLineup());
        this.toggleBtnChangeFormation(true);
    }

    /**
     * Called when a error occurred while loading the players.
     */
    @Override
    public void showLoadingFailed() {
        logger.info("showLoadingFailed");
        txtError.setText(loadingLineupFailedText);
        super.toggleVisibility(error, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(mainContent, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
    }

    /**
     * Called when a request has been send to onUpdateSuccess lineup data.
     */
    @Override
    public void showUpdating() {
        logger.info("showUpdating");
        txtSpinner.setText(spinnerUpdatingLineupText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(error, false);
        super.toggleVisibility(mainContent, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
    }

    /**
     * Called when updating the lineup data is successful.
     */
    @Override
    public void showUpdatingSuccess() {
        logger.info("showUpdatingSuccess");
        super.toggleVisibility(mainContent, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(error, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when updating lineup data failed.
     */
    public void showUpdatingFailed() {
        logger.info("showUpdatingFailed");
        super.toggleVisibility(lineupUpdateErrorLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(error, false);
        super.toggleVisibility(mainContent, false);
        this.invalidateOptionsMenu();
    }

    /**
     * Get all players that are in the lineup.
     *
     * @return all players in the lineup
     */
    @Override
    public List<Player> getPlayersOrdered() {
        super.checkLineupFormationFragmentVisibility();
        return ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getPlayersOrdered();
    }

    /**
     * Get the lineup formation.
     *
     * @return lineup formation
     */
    @Override
    public LineupUtils.FORMATION getFormation() {
        super.checkLineupFormationFragmentVisibility();
        return ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getFormation();
    }

    /**
     * Get all the players that are in the lineup.
     *
     * @return lineup players
     */
    @Override
    public List<LineupPlayer> getLineupPlayers() {
        super.checkLineupFormationFragmentVisibility();
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        return ((LineupFormationFragment) fragment).getLineupPlayers();
    }

    /**
     * Change the formation for the lineup.
     *
     * @param formation new formation
     * @param players   players that are already in the lineup
     */
    @Override
    public void changeFormation(LineupUtils.FORMATION formation, List<Player> players) {
        logger.info("changeFormation");
        super.checkLineupFormationFragmentVisibility();
        super.showLineupFormationFragment(formation, players);
        presenter.setChanged(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Replace the LineupFormation fragment with a ListPositionPlayers fragment.
     *
     * @param place            position place
     * @param playersToExclude players to exclude from the list
     * @param startX           x position of the view (player) that was clicked
     * @param startY           y position of the view (player) that was clicked
     */
    @Override
    public void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                                int[] playersToExclude,
                                                int startX, int startY) {
        logger.info("showListPositionPlayersFragment");
        super.showListPositionPlayersFragment(place, playersToExclude, startX, startY);
    }

    /**
     * Show the PlayerDetailsDialog.
     *
     * @param id       player
     * @param editable whatever the user can removeLike the player
     */
    @Override
    public void showPlayerDetailsDialog(int id, boolean editable) {
        logger.info("showPlayersDetailsDialog");
        super.showPlayerDetailsDialog(id, editable);
    }

    /**
     * Called when the lineup formation is valid.
     */
    @Override
    public void showValidLineup() {
        logger.info("showValidLineup");
        super.checkLineupFormationFragmentVisibility();
        presenter.setLineupValid(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when the lineup formation is invalid.
     */
    @Override
    public void showInvalidLineup() {
        logger.info("showInvalidLineup");
        super.checkLineupFormationFragmentVisibility();
        presenter.setLineupValid(false);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when a item has been clicked from the list in ListPositionPlayersFragment.
     *
     * @param player selected Player
     */
    @Override
    public void onPlayerSelected(Player player) {
        logger.info("onPlayerSelected");
        super.removeListPositionPlayersFragment();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).updateLineupPosition(player);
        presenter.setChanged(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when the delete button is clicked on the PlayerDetailsDialog.
     */
    @Override
    public void removePlayer() {
        logger.info("removePlayer");
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).removeSelectedPlayer();
        presenter.setChanged(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Show the LineupLikesFragment.
     */
    private void showLineupLikesFragment() {
        logger.info("showLineupLikesFragment");
        super.checkLineupFormationFragmentVisibility();
        LikeFragment fragment = LikeFragment.newInstance(presenter.getLineup());
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(LineupFormationFragment.TAG);
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top,
                R.anim.enter_from_top, R.anim.exit_from_bottom);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        this.toggleBtnChangeFormation(false);
    }

    /**
     * Show the LineupCommentsFragment.
     */
    private void showLineupCommentsFragment() {
        logger.info("showLineupCommentsFragment");
        super.checkLineupFormationFragmentVisibility();
        CommentsFragment fragment = CommentsFragment.newInstance(presenter.getLineup().getId());
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(LineupFormationFragment.TAG);
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_from_top,
                R.anim.enter_from_top, R.anim.exit_from_bottom);
        transaction.replace(R.id.content, fragment);
        transaction.commit();
        this.toggleBtnChangeFormation(false);
    }

    /**
     * Handle click on the button "Try Again"
     */
    @OnClick(R.id.error_btnTryAgain)
    void reload() {
        logger.info("btn 'Try Again' clicked");
        presenter.loadPlayers();
    }

    /**
     * Handle click on button to onUpdateSuccess the lineup again.
     */
    @OnClick(R.id.lineupPlayersActivity_btnTryUpdateAgain)
    void tryUpdateAgain() {
        logger.info("btn 'Try Update Again' clicked");
        presenter.update();
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
            this.toggleBtnChangeFormation(true);
            this.invalidateOptionsMenu();
        }
    }
}