package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

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
        LineupPlayersView, ListPositionPlayersFragment.Listener, LineupFormationFragment.Listener,
        PlayerDetailsDialog.Listener {

    public static final String LINEUP_BUNDLE_KEY = "lineup";

    @Inject
    LineupPlayersViewPresenter presenter;

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

    @BindView(R.id.error_loading)
    RelativeLayout errorLoadingLayout;

    @BindView(R.id.lineupPlayersActivity_updateFailed)
    LinearLayout lineupUpdateErrorLayout;

    @BindView(R.id.lineupPlayersPlayers_mainContent)
    RelativeLayout mainContent;

    @BindView(R.id.lineupPlayersLayout_btnChangeFormation)
    ButtonAwesome btnChangeFormation;

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
        super.toggleVisibility(btnChangeFormation, visible);
    }

    /**
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.lineup_players_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getUserComponent()
                .plus(new LineupPlayersViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.registerForContextMenu(btnChangeFormation);
        presenter.loadPlayers(this.getIntent().getExtras());
    }

    /**
     * Called when the options menu is ready to be created or recreated.
     *
     * @param menu menu that will be created
     * @return whatever the menu should be showed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.save_lineup_menu, menu);
        MenuItem item = menu.findItem(R.id.lineupMenu_save);
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof LineupFormationFragment && presenter.isChanged() &&
                presenter.isLineupValid() && !presenter.isLineupUpdateFailed()) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    /**
     * Called when a options item has been selected.
     *
     * @param item selected menu item
     * @return whatever the action should be canceled or not.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lineupMenu_save:
                super.checkLineupFormationFragmentVisibility();
                presenter.update();
                return true;
            case android.R.id.home:
                super.onBackPressed();
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
     * Hide the "Change Formation" button.
     */
    @Override
    public void showBtnChangeFormation() {
        super.toggleVisibility(btnChangeFormation, true);
    }

    /**
     * Handle click on the button to change formation.
     */
    @OnClick(R.id.lineupPlayersLayout_btnChangeFormation)
    void onBtnChangeFormationClicked() {
        btnChangeFormation.showContextMenu();
    }

    /**
     * Called when a request to load players has been send has been send
     */
    @Override
    public void showLoading() {
        txtSpinner.setText(spinnerLoadingLineupText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
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
        super.toggleVisibility(mainContent, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
        super.showLineupFormationFragment(players, presenter.canEditLineup());
    }

    /**
     * Called when a error occurred while loading the players.
     */
    @Override
    public void showLoadingFailed() {
        super.toggleVisibility(errorLoadingLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(mainContent, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
    }

    /**
     * Called when a request has been send to update lineup data.
     */
    @Override
    public void showUpdating() {
        txtSpinner.setText(spinnerUpdatingLineupText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(mainContent, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
    }

    /**
     * Called when updating the lineup data is successful.
     */
    @Override
    public void showUpdatingSuccess() {
        super.toggleVisibility(mainContent, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
        super.toggleVisibility(lineupUpdateErrorLayout, false);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when updating lineup data failed.
     */
    public void showUpdatingFailed() {
        super.toggleVisibility(lineupUpdateErrorLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(errorLoadingLayout, false);
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
     */
    @Override
    public void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                                int[] playersToExclude) {
        super.showListPositionPlayersFragment(place, playersToExclude);
    }

    /**
     * Show the PlayerDetailsDialog.
     *
     * @param id       player
     * @param editable whatever the user can removeLike the player
     */
    @Override
    public void showPlayerDetailsDialog(int id, boolean editable) {
        super.showPlayerDetailsDialog(id, editable);
    }

    /**
     * Called when the lineup formation is valid.
     */
    @Override
    public void onValidLineup() {
        super.checkLineupFormationFragmentVisibility();
        presenter.setLineupValid(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Called when the lineup formation is invalid.
     */
    @Override
    public void onInvalidLineup() {
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
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).removeSelectedPlayer();
        presenter.setChanged(true);
        this.invalidateOptionsMenu();
    }

    /**
     * Handle click on the button "Try Again"
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void reload() {
        presenter.loadPlayers(this.getIntent().getExtras());
    }

    /**
     * Handle click on button to update the lineup again.
     */
    @OnClick(R.id.lineupPlayersActivity_btnTryUpdateAgain)
    void tryUpdateAgain() {
        presenter.update();
    }
}