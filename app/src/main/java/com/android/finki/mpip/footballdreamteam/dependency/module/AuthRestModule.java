package com.android.finki.mpip.footballdreamteam.dependency.module;

import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.rest.web.UsersApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Borce on 09.08.2016.
 */
@Module
public class AuthRestModule {

    /**
     * Provides instance of the UserApi.
     *
     * @param retrofit instance of authenticated Retrofit
     * @return instance of UserApi
     */
    @Provides
    @UserScope
    UsersApi provideUserApi(@Named("authenticated") Retrofit retrofit) {
        return retrofit.create(UsersApi.class);
    }

    /**
     * Provides instance of the PositionApi.
     *
     * @param retrofit instance of authenticated Retrofit
     * @return instance of PositionApi
     */
    @Provides
    @UserScope
    TeamApi provideTeamApi(@Named("authenticated") Retrofit retrofit) {
        return retrofit.create(TeamApi.class);
    }

    /**
     * Provides instance of PositionApi.
     *
     * @param retrofit instance of authenticated Retrofit
     * @return instance of PositionApi
     */
    @Provides
    @UserScope
    PositionApi providePositionAPi(@Named("authenticated") Retrofit retrofit) {
        return retrofit.create(PositionApi.class);
    }

    /**
     * Provides instance of PlayerApi.
     *
     * @param retrofit instance of authenticated Retrofit
     * @return instance of PlayerApi
     */
    @Provides
    @UserScope
    PlayerApi providePlayerApi(@Named("authenticated") Retrofit retrofit) {
        return retrofit.create(PlayerApi.class);
    }

    /**
     * Provides instance of LineupApi.
     *
     * @param retrofit instance of authenticated Retrofit
     * @return instance of LineupApi
     */
    @Provides
    @UserScope
    LineupApi provideLineupApi(@Named("authenticated") Retrofit retrofit) {
        return retrofit.create(LineupApi.class);
    }
}