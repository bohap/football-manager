package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CreateLineupViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.view.ButtonAwesome;

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

    @BindView(R.id.error_loading)
    RelativeLayout requestFailedLayout;

    @BindView(R.id.requestFailed_text)
    TextView txtRequestFailed;

    @BindString(R.string.createLineupView_requestFailedText)
    String requestFailedText;

    @BindView(R.id.createLineupLayout_mainContent)
    RelativeLayout mainContentLayout;

    @BindView(R.id.createLineupLayout_btnSave)
    Button btnSave;

    @BindView(R.id.createLineupLayout_btnChangeFormation)
    ButtonAwesome btnChangeFormation;

    @BindString(R.string.formationContextMenuTitle)
    String contextMenuTitle;

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
     * Called when the activity is ready to be created.
     *
     * @param savedInstanceState saved instance state when the activity is recreated
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.create_lineup_layout);
        ButterKnife.bind(this);
        ((MainApplication) this.getApplication()).getUserComponent()
                .plus(new CreateLineupViewModule(this)).inject(this);
        this.setSupportActionBar(toolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setTitle(title);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.registerForContextMenu(btnChangeFormation);
        super.showLineupFormationFragment(presenter.getFormation());
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
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getPlayersOrdered();
        int id = item.getItemId();
        switch (id) {
            case R.id.formation_4_4_2:
                presenter.updateFormation(LineupPlayers.FORMATION.F_4_4_2);
                return true;
            case R.id.formation_3_2_3_2:
                presenter.updateFormation(LineupPlayers.FORMATION.F_3_2_3_2);
                return true;
            case R.id.formation_4_2_3_1:
                presenter.updateFormation(LineupPlayers.FORMATION.F_4_2_3_1);
                return true;
            case R.id.formation_4_3_3:
                presenter.updateFormation(LineupPlayers.FORMATION.F_4_3_3);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Handle click on the button to change formation.
     */
    @OnClick(R.id.createLineupLayout_btnChangeFormation)
    void onBtnChangeFormationClick() {
        btnChangeFormation.showContextMenu();
    }

    /**
     * Make ListPositionPlayers fragment active.
     *
     * @param place            place on the field for which the players will be showed
     * @param playersToExclude what players to exclude from the list
     */
    @Override
    public void showListPositionPlayersFragment(Position.POSITION_PLACE place,
                                                int[] playersToExclude) {
        super.showListPositionPlayersFragment(place, playersToExclude);
    }

    /**
     * Show the PlayersDetailsDialog.
     *
     * @param id       player id
     * @param editable whatever the player can be removed from the lineup
     */
    @Override
    public void showPlayerDetailsDialog(int id, boolean editable) {
        super.showPlayerDetailsDialog(id, editable);
    }

    /**
     * Called when lineup formation is valid.
     */
    @Override
    public void onValidLineup() {
        super.toggleVisibility(btnSave, true);
    }

    /**
     * Called when lineup formation is not valid.
     */
    @Override
    public void onInvalidLineup() {
        super.toggleVisibility(btnSave, false);
    }

    /**
     * Called when the delete button is clicked from PlayerDetailsDialog.
     */
    @Override
    public void removePlayer() {
        super.checkLineupFormationFragmentVisibility();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).removeSelectedPlayer();
    }

    /**
     * Called when a item is selected from the list on ListPositionPlayersFragment.
     *
     * @param player selected player
     */
    @Override
    public void onPlayerSelected(Player player) {
        super.removeListPositionPlayersFragment();
        ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).updateLineupPosition(player);
    }

    /**
     * Get all the players that are currently in the lineup.
     *
     * @return lineup players
     */
    @Override
    public List<Player> getPlayers() {
        super.checkLineupFormationFragmentVisibility();
        return ((LineupFormationFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.content)).getPlayersOrdered();
    }

    /**
     * Change the lineup formation.
     *
     * @param formation new lineup formation
     * @param players   players that are already in the lineup
     */
    @Override
    public void changeFormation(LineupPlayers.FORMATION formation, List<Player> players) {
        super.checkLineupFormationFragmentVisibility();
        super.showLineupFormationFragment(formation, players);
    }

    /**
     * Handle click on the button "Save".
     */
    @OnClick(R.id.createLineupLayout_btnSave)
    void onBtnSaveClick() {
        super.checkLineupFormationFragmentVisibility();
        List<LineupPlayer> lineupPlayers = ((LineupFormationFragment)
                this.getSupportFragmentManager().findFragmentById(R.id.content)).getLineupPlayers();
        presenter.store(lineupPlayers);
    }

    /**
     * Called when a request has been send to store the lineup on the server.
     */
    @Override
    public void showStoring() {
        txtSpinner.setText(spinnerText);
        super.toggleVisibility(spinner, true);
        super.toggleVisibility(requestFailedLayout, false);
        super.toggleVisibility(mainContentLayout, false);
    }

    /**
     * Called when storing the lineup failed.
     */
    @Override
    public void showStoringFailed() {
        txtRequestFailed.setText(requestFailedText);
        super.toggleVisibility(requestFailedLayout, true);
        super.toggleVisibility(spinner, false);
        super.toggleVisibility(mainContentLayout, false);
    }

    /**
     * Handle click on the button "Try Again".
     */
    @OnClick(R.id.error_loading_btn_tryAgain)
    void onBtnTryAgainClick() {
        this.onBtnSaveClick();
    }

    /**
     * Called when storing the lineup is successful.
     *
     * @param lineup stored Lineup
     */
    @Override
    public void showStoringSuccessful(Lineup lineup) {
        Intent intent = new Intent(this, LineupDetailsActivity.class);
        intent.putExtra(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, lineup.getId());
        this.startActivity(intent);
        super.finish();
    }
}
