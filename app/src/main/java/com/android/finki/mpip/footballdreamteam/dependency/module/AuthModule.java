package com.android.finki.mpip.footballdreamteam.dependency.module;

import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class AuthModule {

    private User user;

    public AuthModule(User user) {
        this.user = user;
    }

    /**
     * Provides instance of the authenticated user.
     *
     * @return instance of the authenticated user
     */
    @Provides
    @UserScope
    User user() {
        return user;
    }
}
