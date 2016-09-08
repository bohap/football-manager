package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;

import dagger.Subcomponent;

/**
 * Created by Borce on 17.08.2016.
 */
@FragmentScope
@Subcomponent(modules = PlayerDetailsViewModule.class)
public interface PlayerDetailsViewComponent {

    void inject(PlayerDetailsDialog dialog);
}
