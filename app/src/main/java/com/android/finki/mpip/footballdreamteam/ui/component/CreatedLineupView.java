package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;

import java.util.List;

/**
 * Created by Borce on 22.08.2016.
 */
public interface CreatedLineupView {

    List<Player> getPlayers();

    void changeFormation(LineupPlayers.FORMATION formation, List<Player> players);

    void showStoring();

    void showStoringFailed();

    void showStoringSuccessful(Lineup lineup);
}
