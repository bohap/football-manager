package com.android.finki.mpip.footballdreamteam.background.task;

import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.MockApplication;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.exception.PositionException;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 30.08.2016.
 */
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
        InOrder inOrder = inOrder(positionDBService);
        inOrder.verify(positionDBService).open();
        inOrder.verify(positionDBService).exists(positions[0].getId());
        inOrder.verify(positionDBService).store(positions[0]);
        inOrder.verify(positionDBService).close();
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
        InOrder inOrder = inOrder(positionDBService);
        inOrder.verify(positionDBService).open();
        inOrder.verify(positionDBService).exists(positions[0].getId());
        inOrder.verify(positionDBService, never()).store(positions[0]);
        inOrder.verify(positionDBService).exists(positions[1].getId());
        inOrder.verify(positionDBService).store(positions[1]);
        inOrder.verify(positionDBService).close();
        verify(listener).onPositionsSavingSuccess();
    }
}
