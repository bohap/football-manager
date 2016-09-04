package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestAppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestAuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.TestNetModule;
//import com.android.finki.mpip.footballdreamteam.ui.presenter.LoginActivityPresenterTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Borce on 06.08.2016.
 */
@Singleton
@Component(modules = {TestAppModule.class, TestNetModule.class, TestAuthModule.class})
public interface TestAppComponent {

//    void inject(LoginActivityPresenterTest test);
}
