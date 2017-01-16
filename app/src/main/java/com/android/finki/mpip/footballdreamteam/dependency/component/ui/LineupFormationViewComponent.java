package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 13.08.2016.
 */
@ViewScope
@Subcomponent(modules = LineupFormationViewModule.class)
public interface LineupFormationViewComponent {

    void inject(LineupFormationFragment fragment);
}
