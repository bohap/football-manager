package com.android.finki.mpip.footballdreamteam.ui.component;

/**
 * Created by Borce on 09.09.2016.
 */
public interface PlayerDetailsView {

    String BUNDLE_PLAYER_ID_KEY = "player_id";
    String BUNDLE_EDITABLE_KEY = "editable";

    void bindPlayer(String name, String team, String age, String position, boolean editable);
}
