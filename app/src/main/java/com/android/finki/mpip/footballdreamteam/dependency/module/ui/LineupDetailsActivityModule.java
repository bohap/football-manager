package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupDetailsActivity;
import com.android.finki.mpip.footballdreamteam.ui.adapter.LineupsDetailsViewPagerAdapter;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupDetailsActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 15.08.2016.
 */
@Module
public class LineupDetailsActivityModule {

    private LineupDetailsActivity activity;

    public LineupDetailsActivityModule(LineupDetailsActivity activity) {
        this.activity = activity;
    }

    /**
     * Provides instance of the LineupDetailsActivity presenter.
     *
     * @return instance of the LineupDetailsActivity presenter
     */
    @Provides
    @ActivityScope
    LineupDetailsActivityPresenter provideLineupDetailsPresenter(LineupApi api) {
        return new LineupDetailsActivityPresenter(activity, api);
    }

    /**
     * Provides instance of the LineupDetailsViewPagerAdapter.
     *
     * @return new instance of the LineupDetailsViewPagerAdapter
     */
    @Provides
    @ActivityScope
    LineupsDetailsViewPagerAdapter provideLineupDetailsViewPagerAdapter() {
        return new LineupsDetailsViewPagerAdapter(activity, activity.getSupportFragmentManager());
    }
}
