package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupDetailsActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupDetailsActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 15.08.2016.
 */
@ActivityScope
@Subcomponent(modules = LineupDetailsActivityModule.class)
public interface LineupDetailsActivityComponent {

    void inject(LineupDetailsActivity activity);
}
