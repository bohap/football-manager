package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AppModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.NetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashActivityModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by Borce on 25.07.2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class, AuthModule.class})
public interface AppComponent {

    SplashActivityComponent plus(SplashActivityModule module);

    LoginActivityComponent plus(LoginActivityModule module);

    UserComponent plus(UserModule module);
}
