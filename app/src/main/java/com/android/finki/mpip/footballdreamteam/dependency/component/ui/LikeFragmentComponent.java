package com.android.finki.mpip.footballdreamteam.dependency.component.ui;

import com.android.finki.mpip.footballdreamteam.dependency.module.ui.LikeFragmentModule;
import com.android.finki.mpip.footballdreamteam.dependency.scope.FragmentScope;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LikeFragment;

import dagger.Subcomponent;

/**
 * Created by Borce on 15.08.2016.
 */
@FragmentScope
@Subcomponent(modules = LikeFragmentModule.class)
public interface LikeFragmentComponent {

    void inject(LikeFragment fragment);
}
