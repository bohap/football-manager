package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.ListLineupsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.ListLineupsFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 05.09.2016.
 */
@ViewScope
@Subcomponent(modules = ListLineupsViewModule.class)
public interface ListLineupsViewComponent {

    void inject(ListLineupsFragment fragment);
}
