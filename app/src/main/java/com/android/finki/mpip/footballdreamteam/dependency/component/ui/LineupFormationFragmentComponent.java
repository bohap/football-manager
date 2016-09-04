package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 13.08.2016.
 */
@FragmentScope
@Subcomponent(modules = LineupFormationFragmentModule.class)
public interface LineupFormationFragmentComponent {

    void inject(LineupFormationFragment fragment);
}
