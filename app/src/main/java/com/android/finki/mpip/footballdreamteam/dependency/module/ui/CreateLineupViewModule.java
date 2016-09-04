package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.presenter.CreateLineupViewPresenter;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 22.08.2016.
 */
@Module
public class CreateLineupViewModule {

    private CreatedLineupView view;

    public CreateLineupViewModule(CreatedLineupView view) {
        this.view = view;
    }

    /**
     * Provides instance of the CreateLineupView presenter.
     *
     * @param api                   instance of LineupApi
     * @param lineupDBService       instance of LineupDBService
     * @param lineupPlayerDBService instance of LineupPlayerDBService
     * @return instance of CreateLineupView presenter
     */
    @Provides
    @ViewScope
    CreateLineupViewPresenter providesCreteLineupViewPresenter(LineupApi api,
                                                               LineupDBService lineupDBService,
                                                               LineupPlayerDBService lineupPlayerDBService) {
        return new CreateLineupViewPresenter(view, api, lineupDBService, lineupPlayerDBService);
    }
}
