package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.ListPositionPlayersFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 17.08.2016.
 */
@Module
public class ListPositionPlayersFragmentModule {

    private ListPositionPlayersFragment fragment;

    public ListPositionPlayersFragmentModule(ListPositionPlayersFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Provide instance of the ListPositionPlayersFragment presenter.
     *
     * @param playerDBService instance of the PlayerDBService
     * @return new instance of the ListPositionPlayersFragment presenter
     */
    @Provides
    @FragmentScope
    ListPositionPlayersFragmentPresenter provideListPositionPlayersFragmentPresenter(
            PlayerDBService playerDBService) {
        return new ListPositionPlayersFragmentPresenter(fragment, playerDBService);
    }
}
