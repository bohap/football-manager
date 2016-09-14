package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LikeView;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 15.08.2016.
 */
@Module
public class LikeViewModule {

    private LikeView view;

    public LikeViewModule(LikeView view) {
        this.view = view;
    }

    /**
     * Provides instance of the LikeFragment presenter.
     *
     * @param api lineup api
     * @param user authenticated user
     * @return new instance of the LikeFragment presenter
     */
    @Provides
    @ViewScope
    LikeViewPresenter provideLikeFragmentPresenter(LineupApi api, User user) {
        return new LikeViewPresenter(view, api, user);
    }
}
