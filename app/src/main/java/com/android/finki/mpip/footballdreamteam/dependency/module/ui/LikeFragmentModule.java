package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.LikeDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LikeFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 15.08.2016.
 */
@Module
public class LikeFragmentModule {

    private LikeFragment fragment;

    public LikeFragmentModule(LikeFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Provides instance of the LikeFragment presenter.
     *
     * @param api lineup api
     * @param user authenticated user
     * @param lineupDBService instance of the LineupDBService
     * @param likeDBService instance of the LikeDBService
     * @return new instance of the LikeFragment presenter
     */
    @Provides
    @FragmentScope
    LikeFragmentPresenter provideLikeFragmentPresenter(LineupApi api, User user,
                                                       LineupDBService lineupDBService,
                                                       LikeDBService likeDBService) {
        return new LikeFragmentPresenter(fragment, api, user, lineupDBService, likeDBService);
    }
}
