package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.background.task.StorePlayersTask;
import com.android.finki.mpip.footballdreamteam.background.task.StorePositionsTask;
import com.android.finki.mpip.footballdreamteam.background.task.StoreTeamsTask;
import com.android.finki.mpip.footballdreamteam.database.service.PlayerDBService;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.database.service.TeamDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;
import com.android.finki.mpip.footballdreamteam.ui.presenter.HomeActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class HomeActivityModule {

    private HomeActivity activity;

    public HomeActivityModule(HomeActivity activity) {
        this.activity = activity;
    }

    /**
     * Provides instance of HomeActivity presenter.
     *
     * @param preferences application shared preferences
     * @param teamDBService instance of TeamDbService
     * @param positionDBService instance of PositionDBService
     * @param playerDBService instance of PlayerDBService
     * @return instance of HomeActivity presenter
     */
    @Provides
    @ActivityScope
    HomeActivityPresenter provideHomeActivityPresenter(SharedPreferences preferences,
                            TeamApi teamApi, PositionApi positionApi, PlayerApi playerApi,
                            LineupApi lineupApi, TeamDBService teamDBService,
                            PositionDBService positionDBService, PlayerDBService playerDBService) {
        StoreTeamsTask storeTeamsTask = new StoreTeamsTask(teamDBService);
        StorePositionsTask storePositionsTask = new StorePositionsTask(positionDBService);
        StorePlayersTask storePlayersTask = new StorePlayersTask(playerDBService);
        return new HomeActivityPresenter(activity, preferences, teamApi, positionApi, playerApi,
                lineupApi, storeTeamsTask,storePositionsTask, storePlayersTask);
    }
}
