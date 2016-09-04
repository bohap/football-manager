package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ActivityScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 06.08.2016.
 */
@ActivityScope
@Subcomponent(modules = SplashActivityModule.class)
public interface SplashActivityComponent {

    void inject(SplashActivity activity);
}
