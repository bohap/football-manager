package com.android.finki.mpip.footballdreamteam.ui.component;

/**
 * Created by Borce on 08.09.2016.
 */
public interface HomeView extends BaseView {

    void showInitialDataLoading();

    void showInitialDataInfoDialog();

    void showInitialDataLoadingSuccess();

    void showErrorLoadingInitialData();
}
