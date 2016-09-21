package com.android.finki.mpip.footballdreamteam;

import com.android.finki.mpip.footballdreamteam.dependency.component.AppComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.AuthComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListLineupsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LoginViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.RegisterViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.SplashViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CommentsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListLineupsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LoginViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.RegisterViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.SplashViewModule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 07.08.2016.
 */
public class MockApplication extends MainApplication {

    private SplashViewComponent splashViewComponent;
    private LoginViewComponent loginViewComponent;
    private RegisterViewComponent registerViewComponent;
    private HomeViewComponent homeViewComponent;
    private ListLineupsViewComponent listLineupsViewComponent;
    private LikeViewComponent likeViewComponent;
    private CommentsViewComponent commentsViewComponent;

    private LineupPlayersViewComponent lineupPlayerActivityComponent;
    private LineupFormationViewComponent lineupFormationFragmentComponent;
    private ListPositionPlayersViewComponent listPositionPlayersFragmentComponent;
    private PlayerDetailsViewComponent playerDetailsDialogComponent;
    private CreateLineupViewComponent createLineupViewComponent;

    /**
     * Mock the AppComponent.
     */
    @Override
    protected void initAppComponent() {
        appComponent = mock(AppComponent.class);
    }

    /**
     * Get the instance of AppComponent.
     *
     * @return instance of AppComponent
     */
    @Override
    public AppComponent getAppComponent() {
        when(appComponent.plus(any(SplashViewModule.class))).thenReturn(splashViewComponent);
        when(appComponent.plus(any(LoginViewModule.class))).thenReturn(loginViewComponent);
        when(appComponent.plus(any(RegisterViewModule.class))).thenReturn(registerViewComponent);
        return appComponent;
    }

    /**
     * Mock the AuthComponent.
     */
    @Override
    public void createAuthComponent() {
        authComponent = mock(AuthComponent.class);
        when(authComponent.plus(any(HomeViewModule.class)))
                .thenReturn(homeViewComponent);
        when(authComponent.plus(any(ListLineupsViewModule.class)))
                .thenReturn(listLineupsViewComponent);
        when(authComponent.plus(any(LikeViewModule.class)))
                .thenReturn(likeViewComponent);
        when(authComponent.plus(any(CommentsViewModule.class)))
                .thenReturn(commentsViewComponent);
        when(authComponent.plus(any(LineupPlayersViewModule.class)))
                .thenReturn(lineupPlayerActivityComponent);
        when(authComponent.plus(any(LineupFormationViewModule.class)))
                .thenReturn(lineupFormationFragmentComponent);
        when(authComponent.plus(any(ListPositionPlayersViewModule.class)))
                .thenReturn(listPositionPlayersFragmentComponent);
        when(authComponent.plus(any(PlayerDetailsViewModule.class)))
                .thenReturn(playerDetailsDialogComponent);
        when(authComponent.plus(any(CreateLineupViewModule.class)))
                .thenReturn(createLineupViewComponent);
    }

    /**
     * Release the AuthComponent.
     */
    @Override
    public void releaseAuthComponent() {
        authComponent = null;
    }

    /**
     * Set a mocked instance of SplashViewComponent.
     *
     * @param component mocked SplashViewComponent
     */
    public void setSplashViewComponent(SplashViewComponent component) {
        this.splashViewComponent = component;
    }

    /**
     * Set a mocked instance of LoginViewComponent.
     *
     * @param component mocked LoginViewComponent
     */
    public void setLoginViewComponent(LoginViewComponent component) {
        this.loginViewComponent = component;
    }

    /**
     * Set a mocked instance of RegisterViewComponent.
     *
     * @param registerViewComponent mocked RegisterViewComponent
     */
    public void setRegisterViewComponent(RegisterViewComponent registerViewComponent) {
        this.registerViewComponent = registerViewComponent;
    }

    /**
     * Set a mocked instance of HomeViewComponent.
     *
     * @param component mocked instance eof HomeViewComponent
     */
    public void setHomeViewComponent(HomeViewComponent component) {
        this.homeViewComponent = component;
    }

    /**
     * Set a mocked instance of ListLineupsViewComponent.
     *
     * @param component mocked instance of ListLineupsViewComponent
     */
    public void setListLineupsViewComponent(ListLineupsViewComponent component) {
        this.listLineupsViewComponent = component;
    }

    /**
     * Set a mocked instance of the LikeViewComponent.
     *
     * @param component mocked instance of the LikeViewComponent
     */
    public void setLikeViewComponent(LikeViewComponent component) {
        this.likeViewComponent = component;
    }

    /**
     * Set a mocked instance of CommentViewComponent.
     *
     * @param component mocked instance of CommentViewComponent
     */
    public void setCommentsViewComponent(CommentsViewComponent component) {
        this.commentsViewComponent = component;
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
