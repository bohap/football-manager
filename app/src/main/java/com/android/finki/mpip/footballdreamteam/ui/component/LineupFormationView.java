package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

/**
 * Created by Borce on 09.09.2016.
 */
public interface LineupFormationView {

    String LINEUP_PLAYERS_KEY = "LINEUP_PLAYERS";
    String FORMATION_KEY = "LINEUP_FORMATION";
    String LIST_PLAYERS_KEY = "LINEUP_LIST_PLAYERS";

    void bindPlayers();

    void showListPositionPlayersView(PositionUtils.POSITION_PLACE place, int[] playersToExclude);

    void showPlayerDetailsView(int playerId, boolean editable);

    void showValidLineup();

    void showInvalidLineup();
}
