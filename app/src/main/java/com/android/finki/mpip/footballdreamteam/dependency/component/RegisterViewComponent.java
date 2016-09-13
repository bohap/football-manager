package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.RegisterViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.RegisterActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 13.09.2016.
 */
@ViewScope
@Subcomponent(modules = RegisterViewModule.class)
public interface RegisterViewComponent {

    void inject(RegisterActivity activity);
}
