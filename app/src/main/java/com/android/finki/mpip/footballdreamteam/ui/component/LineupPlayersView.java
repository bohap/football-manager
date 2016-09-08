package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;

import java.util.List;

/**
 * Created by Borce on 28.08.2016.
 */
public interface LineupPlayersView extends BaseView {

    void showBtnChangeFormation();

    void showLoading();

    void showLoadingSuccess(List<Player> players);

    void showLoadingFailed();

    void showUpdating();

    void showUpdatingSuccess();

    void showUpdatingFailed();

    List<Player> getPlayersOrdered();

    List<LineupPlayer> getLineupPlayers();

    LineupUtils.FORMATION getFormation();

    void changeFormation(LineupUtils.FORMATION formation, List<Player> players);
}
