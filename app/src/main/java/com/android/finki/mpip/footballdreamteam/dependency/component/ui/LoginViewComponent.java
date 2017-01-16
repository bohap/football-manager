package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.LoginActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 06.08.2016.
 */
@ViewScope
@Subcomponent(modules = LoginViewModule.class)
public interface LoginViewComponent {

    void inject(LoginActivity activity);
}
