package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.PlayerDetailsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.dialog.PlayerDetailsDialog;

import dagger.Subcomponent;

/**
 * Created by Borce on 17.08.2016.
 */
@ViewScope
@Subcomponent(modules = PlayerDetailsViewModule.class)
public interface PlayerDetailsViewComponent {

    void inject(PlayerDetailsDialog dialog);
}
