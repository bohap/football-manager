package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.activity.BaseActivity;

import dagger.Subcomponent;

/**
 * Created by Borce on 08.09.2016.
 */
@ViewScope
@Subcomponent
public interface BaseViewComponent {

    void inject(BaseActivity activity);
}
