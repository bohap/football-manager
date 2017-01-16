package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 17.08.2016.
 */
@ViewScope
@Subcomponent(modules = ListPositionPlayersViewModule.class)
public interface ListPositionPlayersViewComponent {

    void inject(ListPositionPlayersFragment fragment);
}
