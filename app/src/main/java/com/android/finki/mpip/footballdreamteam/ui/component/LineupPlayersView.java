package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Borce on 28.08.2016.
 */
public interface LineupPlayersView {

    void showBtnChangeFormation();

    void showLoading();

    void showLoadingSuccess(List<Player> players);

    void showLoadingFailed();

    void showUpdating();

    void showUpdatingSuccess();

    void showUpdatingFailed();

    List<Player> getPlayers();

    LineupPlayers.FORMATION getFormation();

    void changeFormation(LineupPlayers.FORMATION formation, List<Player> players);
}
