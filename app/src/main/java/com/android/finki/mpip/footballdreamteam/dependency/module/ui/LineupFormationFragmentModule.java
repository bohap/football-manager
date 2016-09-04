package com.android.finki.mpip.footballdreamteam.dependency.module.ui;

import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.ui.presenter.LineupFormationFragmentPresenter;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PlayerUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 13.08.2016.
 */
@Module
public class LineupFormationFragmentModule {

    private LineupFormationFragment fragment;

    public LineupFormationFragmentModule(LineupFormationFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Provides instance of the LineupFormationFragment presenter.
     *
     * @param positionDBService instance of the PositionDBService
     * @param lineupUtils       instance of the LineupUtils
     * @param playerUtils       instance of the PlayerUtils
     * @param positionUtils     instance of PositionUtils
     * @param validator         instance of LineupPlayerValidator
     * @return instance of the LineupFormationFragment presenter
     */
    @Provides
    @FragmentScope
    LineupFormationFragmentPresenter provideLineupFormationFragmentPresenter(
            PositionDBService positionDBService, LineupUtils lineupUtils,
            PlayerUtils playerUtils, PositionUtils positionUtils,
            LineupPlayerValidator validator) {
        return new LineupFormationFragmentPresenter(fragment, positionDBService,
                lineupUtils, playerUtils, positionUtils, validator);
    }
}
