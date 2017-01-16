package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;

import java.util.List;

/**
 * Created by Borce on 22.08.2016.
 */
public interface CreatedLineupView extends BaseView {

    List<Player> getPlayersOrdered();

    List<LineupPlayer> getLineupPlayers();

    void changeFormation(LineupUtils.FORMATION formation, List<Player> players);

    void showStoring();

    void showStoringFailed();

    void showStoringSuccessful(Lineup lineup);
}
