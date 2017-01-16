package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.component.service.BackgroundServiceComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CommentsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListLineupsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthNetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthPersistenceModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthRestModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.AuthModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CommentsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListLineupsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;

import dagger.Subcomponent;

/**
 * Created by Borce on 06.08.2016.
 */
@UserScope
@Subcomponent(modules = {AuthModule.class, AuthNetModule.class,
        AuthRestModule.class, AuthPersistenceModule.class})
public interface AuthComponent {

    HomeViewComponent plus(HomeViewModule module);

    ListLineupsViewComponent plus(ListLineupsViewModule module);

    LikeViewComponent plus(LikeViewModule module);

    CommentsViewComponent plus(CommentsViewModule module);

    LineupPlayersViewComponent plus(LineupPlayersViewModule module);

    LineupFormationViewComponent plus(LineupFormationViewModule module);

    ListPositionPlayersViewComponent plus(ListPositionPlayersViewModule module);

    PlayerDetailsViewComponent plus(PlayerDetailsViewModule module);

    CreateLineupViewComponent plus(CreateLineupViewModule module);

    BackgroundServiceComponent plus();
}
