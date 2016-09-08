package com.android.finki.mpip.footballdreamteam.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.dialog.ConfirmDialog;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Borce on 23.08.2016.
 */
public abstract class LineupPlayersBaseActivity extends BaseActivity implements
                                                                        ConfirmDialog.Listener {

    private String confirmDialogTitle;
    private String confirmDialogMessage;

    private static final Logger logger = LoggerFactory.getLogger(LineupPlayersBaseActivity.class);

    protected abstract boolean isChanged();

    protected abstract void toggleBtnChangeFormation(boolean visible);

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.confirmDialogTitle = this.getString(R.string.lineupActivity_confirmDialog_title);
        this.confirmDialogMessage = this.getString(R.string.lineupActivity_confirmDialog_message);
    }

    /**
     * Make the LineupFormation fragment visible.
     *
     * @param players  players in the lineup
     * @param editable whatever the lineup is editable
     */
    void showLineupFormationFragment(List<Player> players, boolean editable) {
        LineupPlayers playersList = new LineupPlayers(players, editable);
        LineupFormationFragment fragment = LineupFormationFragment.newInstance(playersList);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Make the LineupFormation fragment visible.
     *
     * @param formation formation for the lineup
     */
    void showLineupFormationFragment(LineupUtils.FORMATION formation) {
        LineupFormationFragment fragment = LineupFormationFragment.newInstance(formation);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Make the LineupFormation fragment visible.
     *
     * @param formation formation for the lineup
     * @param players   players that will be in the lineup
     */
    void showLineupFormationFragment(LineupUtils.FORMATION formation, List<Player> players) {
        LineupFormationFragment fragment = LineupFormationFragment.newInstance(formation, players);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Replace the LineupFormation fragment with a ListPositionPlayers fragment.
     *
     * @param place            position place
     * @param playersToExclude players to exclude from the list
     */
    void showListPositionPlayersFragment(PositionUtils.POSITION_PLACE place,
                                         int[] playersToExclude) {
        this.checkLineupFormationFragmentVisibility();
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ListPositionPlayersFragment fragment = ListPositionPlayersFragment
                .newInstance(place, playersToExclude);
        transaction.addToBackStack(LineupFormationFragment.TAG);
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
        this.toggleBtnChangeFormation(false);
    }

    /**
     * Remove the ListPositionPlayers fragment.
     */
    void removeListPositionPlayersFragment() {
        Fragment currentFragment = this.getSupportFragmentManager()
                .findFragmentById(R.id.content);
        if (!(currentFragment instanceof ListPositionPlayersFragment)) {
            String message = "ListPositionPlayers fragment is not active";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        boolean result = this.getSupportFragmentManager().popBackStackImmediate();
        if (!result) {
            String message = "error occurred while removing ListPositionPlayers fragment";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment == null || !(currentFragment instanceof LineupFormationFragment)) {
            String message = "error occurred, the current fragment is not LineupFormation";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        this.toggleBtnChangeFormation(true);
    }

    /**
     * Show the PlayerDetails dialog.
     *
     * @param id       player id
     * @param editable whatever the player can be removed from the lineup
     */
    void showPlayerDetailsDialog(int id, boolean editable) {
        this.checkLineupFormationFragmentVisibility();
        PlayerDetailsDialog dialog = PlayerDetailsDialog.newInstance(id, editable);
        dialog.show(this.getSupportFragmentManager(), PlayerDetailsDialog.TAG);
    }

    /**
     * Check the visibility of the lineup formation fragment and throws a error
     */
    void checkLineupFormationFragmentVisibility() {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment == null || !(currentFragment instanceof LineupFormationFragment)) {
            String message = "LineupFormation fragment is not visible";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Handle click the the back button.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackPressed() {
        boolean playerSelectCanceled = false;
        FragmentManager manager = this.getSupportFragmentManager();
        Fragment currentFragment = manager.findFragmentById(R.id.content);
        if (currentFragment != null && currentFragment instanceof ListPositionPlayersFragment) {
            playerSelectCanceled = true;
        } else if (manager.getBackStackEntryCount() == 0 && this.isChanged()) {
            this.showConfirmDialog();
        }
        super.onBackPressed();
        if (playerSelectCanceled) {
            this.checkLineupFormationFragmentVisibility();
            ((LineupFormationFragment) this.getSupportFragmentManager()
                    .findFragmentById(R.id.content)).onPlayerSelectedCanceled();
            this.toggleBtnChangeFormation(true);
        }
    }

    /**
     * Show the confirm dialog for when the user wants to exit but the changes is not yet saved.
     */
    private void showConfirmDialog() {
        ConfirmDialog dialog = ConfirmDialog.newInstance(confirmDialogTitle, confirmDialogMessage);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, ConfirmDialog.TAG);
    }

    /**
     * Called when the ConfirmDialog is closed click the yes button.
     */
    @Override
    public void onDialogConfirm() {
        super.onBackPressed();
    }
}
