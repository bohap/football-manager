package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 09.08.2016.
 */
@ViewScope
@Subcomponent(modules = HomeViewModule.class)
public interface HomeViewComponent {

    void inject(HomeActivity activity);
}
