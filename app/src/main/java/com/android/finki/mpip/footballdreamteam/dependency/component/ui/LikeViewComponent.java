package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 15.08.2016.
 */
@ViewScope
@Subcomponent(modules = LikeViewModule.class)
public interface LikeViewComponent {

    void inject(LikeFragment fragment);
}
