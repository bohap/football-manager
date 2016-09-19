package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.exception.PositionException;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

/**
 * Created by Borce on 30.08.2016.
 */
@Ignore
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        application = MockApplication.class)
public class StorePositionsTaskTest {

    @Mock
    private PositionDBService positionDBService;

    @Mock
    private StorePositionsTask.Listener listener;

    private StorePositionsTask task;

    private Position[] positions = {new Position(1, "Position 1"), new Position(2, "Position 2")};

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        task = new StorePositionsTask(positionDBService);
        task.setListener(listener);
    }

    /**
     * Test the behavior when storing the positions failed.
     */
    @Test
    public void testStoreFailed() {
        when(positionDBService.exists(anyInt())).thenReturn(false);
        doThrow(new PositionException("")).when(positionDBService).store(any(Position.class));

        task.execute(positions);
        Robolectric.flushBackgroundThreadScheduler();

        verify(positionDBService).open();
        verify(positionDBService).exists(positions[0].getId());
        verify(positionDBService).store(positions[0]);
        verify(positionDBService).close();
        verify(listener).onPositionsSavingFailed();
    }

    /**
     * Test the behavior when storing the positions is successful.
     */
    @Test
    public void testStoreSuccessful() {
        when(positionDBService.exists(positions[0].getId())).thenReturn(true);
        when(positionDBService.exists(positions[1].getId())).thenReturn(false);

        task.execute(positions);
        Robolectric.flushBackgroundThreadScheduler();

        verify(positionDBService).open();
        verify(positionDBService).exists(positions[0].getId());
        verify(positionDBService, never()).store(positions[0]);
        verify(positionDBService).exists(positions[1].getId());
        verify(positionDBService).store(positions[1]);
        verify(positionDBService).close();
        verify(listener).onPositionsSavingSuccess();
    }
}
