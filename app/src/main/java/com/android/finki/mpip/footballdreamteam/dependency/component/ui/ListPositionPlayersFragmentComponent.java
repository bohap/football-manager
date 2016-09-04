package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListPositionPlayersFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListPositionPlayersFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 17.08.2016.
 */
@FragmentScope
@Subcomponent(modules = ListPositionPlayersFragmentModule.class)
public interface ListPositionPlayersFragmentComponent {

    void inject(ListPositionPlayersFragment fragment);
}
