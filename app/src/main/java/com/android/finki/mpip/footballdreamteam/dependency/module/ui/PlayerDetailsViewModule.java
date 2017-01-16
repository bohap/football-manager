package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.component.PlayerDetailsView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 17.08.2016.
 */
@Module
public class PlayerDetailsViewModule {

    private PlayerDetailsView view;

    public PlayerDetailsViewModule(PlayerDetailsView view) {
        this.view = view;
    }

    /**
     * Provides instance of the PlayerDetailsDialog presenter.
     *
     * @param playerDBService instance of the PlayerDBService
     * @return instance of the PlayerDetailsDialog presenter
     */
    @Provides
    @ViewScope
    PlayerDetailsViewPresenter providePlayerDetailsFragmentPresenter(
            PlayerDBService playerDBService) {
        return new PlayerDetailsViewPresenter(view, playerDBService);
    }
}
