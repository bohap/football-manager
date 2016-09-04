package com.android.finki.mpip.footballdreamteam;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.UserComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupDetailsActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsDialogComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupDetailsActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsDialogModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashActivityModule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 07.08.2016.
 */
public class MockApplication extends MainApplication {

    private AppComponent appComponent;
    private UserComponent userComponent;
    private SplashActivityComponent splashActivityComponent;
    private LoginActivityComponent loginActivityComponent;
    private HomeActivityComponent homeActivityComponent;
    private LineupDetailsActivityComponent lineupDetailsActivityComponent;
    private LineupPlayersViewComponent lineupPlayerActivityComponent;
    private LineupFormationFragmentComponent lineupFormationFragmentComponent;
    private ListPositionPlayersFragmentComponent listPositionPlayersFragmentComponent;
    private PlayerDetailsDialogComponent playerDetailsDialogComponent;
    private CreateLineupViewComponent createLineupViewComponent;
    private LikeFragmentComponent likeFragmentComponent;

    /**
     * Get a instance of mocked AppComponent.
     *
     * @return mocked AppComponent
     */
    @Override
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = mock(AppComponent.class);
            when(appComponent.plus(any(SplashActivityModule.class)))
                    .thenReturn(splashActivityComponent);
            when(appComponent.plus(any(LoginActivityModule.class)))
                    .thenReturn(loginActivityComponent);
        }
        return appComponent;
    }

    /**
     * Get a instance of mocked UserComponent.
     *
     * @return mocked instance of the UserComponent
     */
    @Override
    public UserComponent getUserComponent() {
        if (userComponent == null) {
            userComponent = mock(UserComponent.class);
            when(userComponent.plus(any(HomeActivityModule.class)))
                    .thenReturn(homeActivityComponent);
            when(userComponent.plus(any(LineupDetailsActivityModule.class)))
                    .thenReturn(lineupDetailsActivityComponent);
            when(userComponent.plus(any(LikeFragmentModule.class)))
                    .thenReturn(likeFragmentComponent);
            when(userComponent.plus(any(LineupPlayersViewModule.class)))
                    .thenReturn(lineupPlayerActivityComponent);
            when(userComponent.plus(any(LineupFormationFragmentModule.class)))
                    .thenReturn(lineupFormationFragmentComponent);
            when(userComponent.plus(any(ListPositionPlayersFragmentModule.class)))
                    .thenReturn(listPositionPlayersFragmentComponent);
            when(userComponent.plus(any(PlayerDetailsDialogModule.class)))
                    .thenReturn(playerDetailsDialogComponent);
            when(userComponent.plus(any(CreateLineupViewModule.class)))
                    .thenReturn(createLineupViewComponent);
        }
        return userComponent;
    }

    /**
     * Set a mocked instance of SplashActivityComponent.
     *
     * @param component mocked SplashActivityComponent
     */
    public void setSplashActivityComponent(SplashActivityComponent component) {
        this.splashActivityComponent = component;
    }

    /**
     * Set a mocked instance of LoginActivityComponent.
     *
     * @param component mocked LoginActivityComponent
     */
    public void setLoginActivityComponent(LoginActivityComponent component) {
        this.loginActivityComponent = component;
    }

    /**
     * Set a mocked instance of HomeActivityComponent.
     *
     * @param component mocked instance eof HomeActivityComponent
     */
    public void setHomeActivityComponent(HomeActivityComponent component) {
        this.homeActivityComponent = component;
    }

    /**
     * Set a mocked instance of the LineupDetailsActivityComponent.
     *
     * @param component mocked instance of the LineupDetailsActivityComponent
     */
    public void setLineupDetailsActivityComponent(LineupDetailsActivityComponent component) {
        this.lineupDetailsActivityComponent = component;
    }

    /**
     * Set a mocked instance of the LikeFragmentComponent.
     *
     * @param component mocked instance of the LikeFragmentComponent
     */
    public void setLikeFragmentComponent(LikeFragmentComponent component) {
        this.likeFragmentComponent = component;
    }

    /**
     * Set a mocked instance of the LineupPlayersViewComponent.
     *
     * @param component mocked instance of the LineuoPlayerActivityComponent
     */
    public void setLineupPlayerActivityComponent(LineupPlayersViewComponent component) {
        this.lineupPlayerActivityComponent = component;
    }

    /**
     * Set a mocked instance of the LineupFormationFragmentComponent.
     *
     * @param component mocked instance of the LineupFormationFragmentComponent
     */
    public void setLineupFormationFragmentComponent(LineupFormationFragmentComponent component) {
        this.lineupFormationFragmentComponent = component;
    }

    /**
     * Set a mocked instance of the ListPositionPlayersComponent.
     *
     * @param component mocked instance of the ListPositionPlayersComponent
     */
    public void setListPositionPlayersFragmentComponent(
            ListPositionPlayersFragmentComponent component) {
        this.listPositionPlayersFragmentComponent = component;
    }

    /**
     * Set a mocked instance of the PlayerDetailsDialogComponent.
     *
     * @param component mocked instance of the PlayerDetailsDialogComponent
     */
    public void setPlayerDetailsDialogComponent(PlayerDetailsDialogComponent component) {
        this.playerDetailsDialogComponent = component;
    }

    /**
     * Set a mocked instance of the CreateLineupViewComponent.
     *
     * @param component mocked instance of the CreateLineupViewComponent
     */
    public void setCreateLineupViewComponent(CreateLineupViewComponent component) {
        this.createLineupViewComponent = component;
    }
}
