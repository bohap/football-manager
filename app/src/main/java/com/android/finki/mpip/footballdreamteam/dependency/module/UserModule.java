package com.android.finki.mpip.footballdreamteam.dependency.module;

import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.model.User;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Borce on 06.08.2016.
 */
@Module
public class UserModule {

    private User user;

    public UserModule(User user) {
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
