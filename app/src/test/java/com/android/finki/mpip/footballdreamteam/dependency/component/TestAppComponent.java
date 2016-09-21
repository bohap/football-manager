package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.ui.activity.BaseActivityTest;
//import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginViewPresenterTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Borce on 06.08.2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class, UserModule.class})
public interface TestAppComponent {

    void inject(BaseActivityTest test);
}
