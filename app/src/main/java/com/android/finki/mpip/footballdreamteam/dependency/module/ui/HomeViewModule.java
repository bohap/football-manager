package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.background.task.StorePlayersTask;
import com.android.finki.mpip.footballdreamteam.background.task.StorePositionsTask;
import com.android.finki.mpip.footballdreamteam.background.task.StoreTeamsTask;
import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.database.service.TeamDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;
import com.android.finki.mpip.footballdreamteam.ui.component.HomeView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class HomeViewModule {

    private HomeView view;

    public HomeViewModule(HomeView view) {
        this.view = view;
    }

    /**
     * Provides instance of HomeActivity presenter.
     *
     * @param context instance of application context
     * @param preferences application shared preferences
     * @param teamApi instance of TeamApi
     * @param positionApi instance of PositionApi
     * @param playerApi instance of PlayerApi
     * @param teamDBService instance of TeamDbService
     * @param positionDBService instance of PositionDBService
     * @param playerDBService instance of PlayerDBService
     * @return instance of HomeActivity presenter
     */
    @Provides
    @ViewScope
    HomeViewPresenter provideHomeActivityPresenter(Context context, SharedPreferences preferences,
                                                   TeamApi teamApi, PositionApi positionApi,
                                                   PlayerApi playerApi,
                                                   TeamDBService teamDBService,
                                                   PositionDBService positionDBService,
                                                   PlayerDBService playerDBService) {
        StoreTeamsTask storeTeamsTask = new StoreTeamsTask(teamDBService);
        StorePositionsTask storePositionsTask = new StorePositionsTask(positionDBService);
        StorePlayersTask storePlayersTask = new StorePlayersTask(playerDBService);
        return new HomeViewPresenter(view, preferences, context, teamApi,
                positionApi, playerApi, storeTeamsTask,storePositionsTask, storePlayersTask);
    }
}
