package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CreateLineupViewPresenter;
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
 * Created by Borce on 22.08.2016.
 */
public class CreateLineupActivity extends LineupPlayersBaseActivity implements CreatedLineupView,
        LineupFormationFragment.Listener, ListPositionPlayersFragment.Listener,
        PlayerDetailsDialog.Listener {

    private Logger logger = LoggerFactory.getLogger(CreateLineupActivity.class);
    private CreateLineupViewPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindString(R.string.createLineupView_title)
    String title;

    @BindView(R.id.spinner)
    RelativeLayout spinner;

    @BindView(R.id.spinner_text)
    TextView txtSpinner;

    @BindString(R.string.createLineupView_spinnerCreateLineup_text)
    String spinnerText;

    @BindView(R.id.error)
    RelativeLayout error;

    @BindView(R.id.txtError)
    TextView txtRequestFailed;

    @BindString(R.string.createLineupView_requestFailedText)
    String requestFailedText;

    @BindView(R.id.createLineupLayout_mainContent)
    RelativeLayout mainContentLayout;

    @BindView(R.id.createLineupLayout_btnChangeFormation)
    ButtonAwesome btnChangeFormation;

    @BindString(R.string.formationContextMenuTitle)
    String contextMenuTitle;

    @BindString(R.string.createLineupView_lineupCreateSuccessful_text)
    String lineupCreateSuccessfullyText;

    /**
     * Set the presenter for the activity.
     *
     * @param presenter instance of activity presenter
     */
    @Inject
    void setPresenter(CreateLineupViewPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Checks whatever the formation has been changed.
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
     * @param savedInstanceState saved instance state when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        logger.info("onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_lineup_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getAuthComponent()
                .plus(new CreateLineupViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        presenter.onViewLayoutCreated();
        this.registerForContextMenu(btnChangeFormation);
        super.showLineupFormationFragment(presenter.getFormation());
    }

    /**
     * Called when the options menu is ready to be creted or recreated.
     *
     * @param menu menu to be created
     * @return whatever the action should be canceled or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        logger.info("onCreateOptionsMenu");
        this.getMenuInflater().inflate(R.menu.create_lineup_menu, menu);
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        MenuItem item = menu.findItem(R.id.lineupMenu_save);
        if (fragment instanceof LineupFormationFragment && presenter.isFormationValid()) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }
        return true;
    }

    /**
     * Called when the options item is selected.
     *
     * @param item selected item
     * @return whatever the action should be canceled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logger.info("onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.lineupMenu_save:
                presenter.store();
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when the context menu is ready to be  created.
     *
     * @param menu     context menu that will be created
     * @param v        view on which the menu is bind
     * @param menuInfo information about the menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        logger.info("onCreateContextMenu");
        menu.setHeaderTitle(contextMenuTitle);
        this.getMenuInflater().inflate(R.menu.formations_menu, menu);
    }

    /**
     * Called when a options from the context menu is selected.
     *
     * @param item menu item that has been selected
     * @return whatever the action should be canceled or not
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        logger.info("onContextItemSelected");
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getPlayersOrdered();
        int id = item.getItemId();
        switch (id) {
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
                return super.onContextItemSelected(item);
        }
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
    }

    /**
     * Handle click on the button to change formation.
     */
    @OnClick(R.id.createLineupLayout_btnChangeFormation)
    void onBtnChangeFormationClick() {
        logger.info("btn 'Change Formation' clicked");
        btnChangeFormation.showContextMenu();
    }

    /**
     * Make ListPositionPlayers fragment active.
     *
     * @param place            place on the field for which the players will be showed
     * @param playersToExclude what players to exclude from the list
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
     * Show the PlayersDetailsDialog.
     *
     * @param id       player id
     * @param editable whatever the player can be removed from the lineup
     */
    @Override
    public void showPlayerDetailsDialog(int id, boolean editable) {
        logger.info("showPlayerDetailsDialog");
        super.showPlayerDetailsDialog(id, editable);
    }

    /**
     * Called when lineup formation is valid.
     */
    @Override
    public void showValidLineup() {
        logger.info("showValidLineup");
        presenter.setFormationValid(true);
        invalidateOptionsMenu();
    }

    /**
     * Called when lineup formation is not valid.
     */
    @Override
    public void showInvalidLineup() {
        logger.info("showInvalidLineup");
        presenter.setFormationValid(false);
        invalidateOptionsMenu();
    }

    /**
     * Called when the delete button is clicked from PlayerDetailsDialog.
     */
    @Override
    public void removePlayer() {
        logger.info("removePlayer");
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).removeSelectedPlayer();
        presenter.setChanged(true);
    }

    /**
     * Called when a item is selected from the list on ListPositionPlayersFragment.
     *
     * @param player selected player
     */
    @Override
    public void onPlayerSelected(Player player) {
        logger.info("onPlayerSelected");
        super.removeListPositionPlayersFragment();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).updateLineupPosition(player);
        presenter.setChanged(true);
    }

    /**
     * Get all the players that are currently in the lineup ordered by their positions.
     *
     * @return lineup players
     */
    @Override
    public List<Player> getPlayersOrdered() {
        super.checkLineupFormationFragmentVisibility();
        return ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getPlayersOrdered();
    }

    /**
     * Get all the players that are in the lienup.
     *
     * @return lineup players
     */
    @Override
    public List<LineupPlayer> getLineupPlayers() {
        super.checkLineupFormationFragmentVisibility();
        return ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getLineupPlayers();
    }

    /**
     * Change the lineup formation.
     *
     * @param formation new lineup formation
     * @param players   players that are already in the lineup
     */
    @Override
    public void changeFormation(LineupUtils.FORMATION formation, List<Player> players) {
        logger.info("change formation");
        super.checkLineupFormationFragmentVisibility();
        super.showLineupFormationFragment(formation, players);
    }

    /**
     * Called when a request has been send to store the lineup on the server.
     */
    @Override
    public void showStoring() {
        logger.info("showStoring");
        txtSpinner.setText(spinnerText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(error, false);
        super.toggleVisibility(mainContentLayout, false);
    }

    /**
     * Called when storing the lineup failed.
     */
    @Override
    public void showStoringFailed() {
        logger.info("showStoringFailed");
        txtRequestFailed.setText(requestFailedText);
        super.toggleVisibility(error, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(mainContentLayout, false);
    }

    /**
     * Handle click on the button "Try Again". v
     */
    @OnClick(R.id.error_btnTryAgain)
    void onBtnTryAgainClick() {
        logger.info("btn 'Try Again' clicked");
        presenter.store();
    }

    /**
     * Called when storing the lineup is successful.
     *
     * @param lineup stored Lineup
     */
    @Override
    public void showStoringSuccessful(Lineup lineup) {
        logger.info("showStoringSuccessful");
        Toast.makeText(this, lineupCreateSuccessfullyText, Toast.LENGTH_LONG).show();
        super.finish();
    }
}
