package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.component.ListPositionPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 17.08.2016.
 */
@Module
public class ListPositionPlayersViewModule {

    private ListPositionPlayersView view;

    public ListPositionPlayersViewModule(ListPositionPlayersView view) {
        this.view = view;
    }

    /**
     * Provide instance of the ListPositionPlayersFragment presenter.
     *
     * @param playerDBService instance of the PlayerDBService
     * @return new instance of the ListPositionPlayersFragment presenter
     */
    @Provides
    @ViewScope
    ListPositionPlayersViewPresenter provideListPositionPlayersFragmentPresenter(
            PlayerDBService playerDBService) {
        return new ListPositionPlayersViewPresenter(view, playerDBService);
    }
}
