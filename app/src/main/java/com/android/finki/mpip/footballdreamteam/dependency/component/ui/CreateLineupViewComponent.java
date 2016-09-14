package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.CreateLineupActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 22.08.2016.
 */
@ViewScope
@Subcomponent(modules = CreateLineupViewModule.class)
public interface CreateLineupViewComponent {

    void inject(CreateLineupActivity activity);
}
