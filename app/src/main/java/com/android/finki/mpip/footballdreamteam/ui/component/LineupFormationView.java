package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

/**
 * Created by Borce on 09.09.2016.
 */
public interface LineupFormationView {

    void bindPlayers();

    void showListPositionPlayersView(PositionUtils.POSITION_PLACE place, int[] playersToExclude);

    void showPlayerDetailsView(int playerId, boolean editable);

    void showValidLineup();

    void showInvalidLineup();
}
