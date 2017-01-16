package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.MainApplication;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.RegisterViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.RegisterViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashViewModule;
import com.android.finki.mpip.footballdreamteam.receiver.ApplicationBootReceiver;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Borce on 25.07.2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class, UserModule.class})
public interface AppComponent {

    void inject(MainApplication application);

    void inject(ApplicationBootReceiver receiver);

    SplashViewComponent plus(SplashViewModule module);

    LoginViewComponent plus(LoginViewModule module);

    RegisterViewComponent plus(RegisterViewModule module);

    AuthComponent plus(AuthModule module);
}
