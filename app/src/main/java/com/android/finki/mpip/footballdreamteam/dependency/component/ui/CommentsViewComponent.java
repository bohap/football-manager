package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.CommentsViewModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.ViewScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.CommentsFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 05.09.2016.
 */
@ViewScope
@Subcomponent(modules = CommentsViewModule.class)
public interface CommentsViewComponent {

    void inject(CommentsFragment fragment);
}
