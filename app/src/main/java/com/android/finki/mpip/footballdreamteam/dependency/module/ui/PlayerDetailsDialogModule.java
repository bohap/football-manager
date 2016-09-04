package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;
import com.android.finki.mpip.footballdreamteam.ui.presenter.PlayerDetailsDialogPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 17.08.2016.
 */
@Module
public class PlayerDetailsDialogModule {

    private PlayerDetailsDialog dialog;

    public PlayerDetailsDialogModule(PlayerDetailsDialog dialog) {
        this.dialog = dialog;
    }

    /**
     * Provides instance of the PlayerDetailsDialog presenter.
     *
     * @param playerDBService instance of the PlayerDBService
     * @return instance of the PlayerDetailsDialog presenter
     */
    @Provides
    @FragmentScope
    PlayerDetailsDialogPresenter providePlayerDetailsFragmentPresenter(
            PlayerDBService playerDBService) {
        return new PlayerDetailsDialogPresenter(dialog, playerDBService);
    }
}
