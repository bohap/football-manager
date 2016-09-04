package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.database.service.TeamDBService;
import com.android.finki.mpip.footballdreamteam.exception.TeamException;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 30.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class StoreTeamsTaskTest {

    @Mock
    private TeamDBService teamDBService;

    @Mock
    private StoreTeamsTask.Listener listener;

    private StoreTeamsTask task;

    private Team[] teams = {new Team(1, "Team 1"), new Team(2, "Team 2")};

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = new StoreTeamsTask(teamDBService);
        task.setListener(listener);
    }

    /**
     * Test the behavior when saving the teams failed.
     */
    @Test
    public void testStoreFailed() {
        when(teamDBService.exists(anyInt())).thenReturn(false);
        doThrow(new TeamException("")).when(teamDBService).store(any(Team.class));

        task.execute(teams);
        Robolectric.flushBackgroundThreadScheduler();

        verify(teamDBService).open();
        verify(teamDBService).exists(teams[0].getId());
        verify(teamDBService).store(teams[0]);
        verify(teamDBService).close();
        verify(listener).onTeamsSavingFailed();
    }

    /**
     * Test the behavior when storing the teams is successful.
     */
    @Test
    public void testStoreSuccessful() {
        when(teamDBService.exists(teams[0].getId())).thenReturn(true);
        when(teamDBService.exists(teams[1].getId())).thenReturn(false);

        task.execute(teams);
        Robolectric.flushBackgroundThreadScheduler();

        verify(teamDBService).open();
        verify(teamDBService).exists(teams[0].getId());
        verify(teamDBService, never()).store(teams[0]);
        verify(teamDBService).exists(teams[1].getId());
        verify(teamDBService).store(teams[1]);
        verify(teamDBService).close();
        verify(listener).onTeamsSavingSuccess();
    }
}
