package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Player;

import java.util.List;

/**
 * Created by Borce on 09.09.2016.
 */
public interface ListPositionPlayersView {

    String PLACE_KEY = "place";
    String EXCLUDE_LAYERS_KEY = "exclude_players";
    String START_X_KEY = "start_x";
    String START_Y_KEY = "start_y";

    void onPlayersLoaded(List<Player> players, String place);
}
