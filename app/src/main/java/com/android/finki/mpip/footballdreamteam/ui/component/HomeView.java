package com.android.finki.mpip.footballdreamteam.ui.component;

/**
 * Created by Borce on 08.09.2016.
 */
public interface HomeView extends BaseView {

    void showInitialDataLoading();

    void showInitialDataInfoDialog();

    void showTeamsLoading();

    void showTeamsLoadingFailed();

    void showTeamsStoring();

    void showTeamsStoringFailed();

    void showPositionsLoading();

    void showPositionsLoadingFailed();

    void showPositionsStoring();

    void showPositionsStoringFailed();

    void showPlayersLoading();

    void showPlayersLoadingFailed();

    void showPlayersStoring();

    void showPlayersStoringFailed();

    void showInitialDataLoadingSuccess();
}
