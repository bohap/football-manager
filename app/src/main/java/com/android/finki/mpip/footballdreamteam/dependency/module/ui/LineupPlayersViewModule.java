package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupPlayersViewPresenter;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 16.08.2016.
 */
@Module
public class LineupPlayersViewModule {

    private LineupPlayersView view;

    public LineupPlayersViewModule(LineupPlayersView view) {
        this.view = view;
    }

    /**
     * Provides instance of the LineupPlayersView presenter.
     *
     * @param user                  authenticated user
     * @param api                   instance o LineupApi
     * @param lineupDBService       instance of LineupDBService
     * @param lineupPlayerDBService instance of LineupPlayerDBService
     * @param validator             instance of LineupPlayerValidator
     * @return instance of the LineupPlayersView presenter
     */
    @Provides
    @ViewScope
    LineupPlayersViewPresenter provideLineupPlayerViewPresenter(User user, LineupApi api,
                                                    LineupDBService lineupDBService,
                                                    LineupPlayerDBService lineupPlayerDBService,
                                                    LineupPlayerValidator validator) {
        return new LineupPlayersViewPresenter(view, user, api,
                lineupDBService, lineupPlayerDBService, validator);
    }
}
