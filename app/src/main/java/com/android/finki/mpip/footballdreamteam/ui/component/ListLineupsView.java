package com.android.finki.mpip.footballdreamteam.ui.component;

import com.android.finki.mpip.footballdreamteam.model.Lineup;

import java.util.List;

/**
 * Created by Borce on 05.09.2016.
 */
public interface ListLineupsView extends BaseView {

    void showLoading();

    void showLoadingSuccess(List<Lineup> lineups);

    void showLoadingFailed();

    void showRefreshing();

    void showRefreshingFailed();

    void showNoMoreLineups();

    void showLineupDeletingSuccess(Lineup lineup);

    void showLineupDeletingFailed(Lineup lineup);
}
