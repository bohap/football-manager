package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 09.08.2016.
 */
@ActivityScope
@Subcomponent(modules = HomeActivityModule.class)
public interface HomeActivityComponent {

    void inject(HomeActivity activity);
}
