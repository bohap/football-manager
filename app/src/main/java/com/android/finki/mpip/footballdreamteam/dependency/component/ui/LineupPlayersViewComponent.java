package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupPlayersActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 16.08.2016.
 */
@ViewScope
@Subcomponent(modules = LineupPlayersViewModule.class)
public interface LineupPlayersViewComponent {

    void inject(LineupPlayersActivity activity);
}
