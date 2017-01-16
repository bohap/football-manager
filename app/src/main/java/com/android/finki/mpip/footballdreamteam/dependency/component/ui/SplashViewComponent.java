package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 06.08.2016.
 */
@ViewScope
@Subcomponent(modules = SplashViewModule.class)
public interface SplashViewComponent {

    void inject(SplashActivity activity);
}
