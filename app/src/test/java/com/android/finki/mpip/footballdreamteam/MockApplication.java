package com.android.finki.mpip.footballdreamteam;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.UserComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashViewModule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 07.08.2016.
 */
public class MockApplication extends MainApplication {

    private AppComponent appComponent;
    private UserComponent userComponent;
    private SplashViewComponent splashActivityComponent;
    private LoginViewComponent loginActivityComponent;
    private HomeViewComponent homeActivityComponent;
    private LineupPlayersViewComponent lineupPlayerActivityComponent;
    private LineupFormationViewComponent lineupFormationFragmentComponent;
    private ListPositionPlayersViewComponent listPositionPlayersFragmentComponent;
    private PlayerDetailsViewComponent playerDetailsDialogComponent;
    private CreateLineupViewComponent createLineupViewComponent;
    private LikeViewComponent likeFragmentComponent;

    /**
     * Get a instance of mocked AppComponent.
     *
     * @return mocked AppComponent
     */
    @Override
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = mock(AppComponent.class);
            when(appComponent.plus(any(SplashViewModule.class)))
                    .thenReturn(splashActivityComponent);
            when(appComponent.plus(any(LoginViewModule.class)))
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
            when(userComponent.plus(any(HomeViewModule.class)))
                    .thenReturn(homeActivityComponent);
            when(userComponent.plus(any(LikeViewModule.class)))
                    .thenReturn(likeFragmentComponent);
            when(userComponent.plus(any(LineupPlayersViewModule.class)))
                    .thenReturn(lineupPlayerActivityComponent);
            when(userComponent.plus(any(LineupFormationViewModule.class)))
                    .thenReturn(lineupFormationFragmentComponent);
            when(userComponent.plus(any(ListPositionPlayersViewModule.class)))
                    .thenReturn(listPositionPlayersFragmentComponent);
            when(userComponent.plus(any(PlayerDetailsViewModule.class)))
                    .thenReturn(playerDetailsDialogComponent);
            when(userComponent.plus(any(CreateLineupViewModule.class)))
                    .thenReturn(createLineupViewComponent);
        }
        return userComponent;
    }

    /**
     * Set a mocked instance of SplashViewComponent.
     *
     * @param component mocked SplashViewComponent
     */
    public void setSplashActivityComponent(SplashViewComponent component) {
        this.splashActivityComponent = component;
    }

    /**
     * Set a mocked instance of LoginViewComponent.
     *
     * @param component mocked LoginViewComponent
     */
    public void setLoginActivityComponent(LoginViewComponent component) {
        this.loginActivityComponent = component;
    }

    /**
     * Set a mocked instance of HomeViewComponent.
     *
     * @param component mocked instance eof HomeViewComponent
     */
    public void setHomeActivityComponent(HomeViewComponent component) {
        this.homeActivityComponent = component;
    }

    /**
     * Set a mocked instance of the LikeViewComponent.
     *
     * @param component mocked instance of the LikeViewComponent
     */
    public void setLikeFragmentComponent(LikeViewComponent component) {
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
     * Set a mocked instance of the LineupFormationViewComponent.
     *
     * @param component mocked instance of the LineupFormationViewComponent
     */
    public void setLineupFormationFragmentComponent(LineupFormationViewComponent component) {
        this.lineupFormationFragmentComponent = component;
    }

    /**
     * Set a mocked instance of the ListPositionPlayersComponent.
     *
     * @param component mocked instance of the ListPositionPlayersComponent
     */
    public void setListPositionPlayersFragmentComponent(
            ListPositionPlayersViewComponent component) {
        this.listPositionPlayersFragmentComponent = component;
    }

    /**
     * Set a mocked instance of the PlayerDetailsViewComponent.
     *
     * @param component mocked instance of the PlayerDetailsViewComponent
     */
    public void setPlayerDetailsDialogComponent(PlayerDetailsViewComponent component) {
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
