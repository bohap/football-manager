package com.android.finki.mpip.footballdreamteam.dependency.component;

import com.android.finki.mpip.footballdreamteam.dependency.component.ui.CreateLineupViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.HomeActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LikeFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupDetailsActivityComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupFormationFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.LineupPlayersViewComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.ListPositionPlayersFragmentComponent;
import com.android.finki.mpip.footballdreamteam.dependency.component.ui.PlayerDetailsDialogComponent;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserApiModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserNetModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.UserPersistenceModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CreateLineupViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.HomeActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupDetailsActivityModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupFormationFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LineupPlayersViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsDialogModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.UserScope;

import dagger.Subcomponent;

/**
 * Created by Borce on 06.08.2016.
 */
@UserScope
@Subcomponent(modules = {UserModule.class, UserNetModule.class, UserApiModule.class,
        UserPersistenceModule.class})
public interface UserComponent {

    HomeActivityComponent plus(HomeActivityModule module);

    LineupDetailsActivityComponent plus(LineupDetailsActivityModule module);

    LineupPlayersViewComponent plus(LineupPlayersViewModule module);

    LikeFragmentComponent plus(LikeFragmentModule module);

    LineupFormationFragmentComponent plus(LineupFormationFragmentModule module);

    ListPositionPlayersFragmentComponent plus(ListPositionPlayersFragmentModule module);

    PlayerDetailsDialogComponent plus(PlayerDetailsDialogModule module);

    CreateLineupViewComponent plus(CreateLineupViewModule module);
}
