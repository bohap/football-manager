package com.android.finki.mpip.footballdreamteam.dependency.component.service;

import com.android.finki.mpip.footballdreamteam.dependency.scope.ServiceScope;
import com.android.finki.mpip.footballdreamteam.service.UserStatisticService;

import dagger.Subcomponent;

/**
 * Created by Borce on 13.09.2016.
 */
@ServiceScope
@Subcomponent
public interface BackgroundServiceComponent {

    void inject(UserStatisticService service);
}
