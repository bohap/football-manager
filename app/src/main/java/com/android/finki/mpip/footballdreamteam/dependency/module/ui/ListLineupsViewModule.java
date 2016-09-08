package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListLineupsViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 05.09.2016.
 */
@Module
public class ListLineupsViewModule {

    private ListLineupsView view;

    public ListLineupsViewModule(ListLineupsView view) {
        this.view = view;
    }

    /**
     * Provides instance of ListLineupsView presenter.
     *
     * @param api instance of LineupApi
     * @return instance of ListLineupsView presenter
     */
    @Provides
    @ViewScope
    ListLineupsViewPresenter provideListLineupsViewPresenter(LineupApi api) {
        return new ListLineupsViewPresenter(view, api);
    }
}
