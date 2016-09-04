package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.LineupDetailsActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 15.08.2016.
 */
public class LineupDetailsActivityPresenterTest {

    @Mock
    private LineupDetailsActivity activity;

    @Mock
    private LineupApi api;

    @Mock
    private Call<Lineup> call;

    @Captor
    private ArgumentCaptor<Callback<Lineup>> callbackCaptor;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    @Mock
    private Bundle args;

    private LineupDetailsActivityPresenter presenter;

    private Lineup lineup = new Lineup(1, 1);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(api.get(anyInt())).thenReturn(call);
        presenter = new LineupDetailsActivityPresenter(activity, api);
    }

    /**
     * Test the behavior on the presenter when the bundle is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLineupDaaWithNullParam() {
        presenter.loadLineupData(null);
    }

    /**
     * Test the behavior on the presenter when the bundle lineup id data is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadLineupDataWithUnExistingIntentExtraKey() {
        when(args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1)).thenReturn(0);
        presenter.loadLineupData(args);
    }

    /**
     * Test that loadLineupData method works.
     */
    @Test
    public void testLoadLineupData() {
        when(args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1))
                .thenReturn(lineup.getId());
        presenter.loadLineupData(args);
        verify(activity).showLoading();
        verify(api).get(intCaptor.capture());
        assertEquals(lineup.getId(), intCaptor.getValue());
        verify(call).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior on the presenter when loading the lineup data is successful.
     */
    @Test
    public void testSuccessLoadingLineupData() {
        when(args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1))
                .thenReturn(lineup.getId());
        presenter.loadLineupData(args);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineup));
        verify(activity).successLoading(lineup);
        verify(activity, never()).errorLoading();
    }

    /**
     * Test the behavior on the presenter when loading the lineup failed with
     * SocketTimeout error.
     */
    @Test
    public void testErrorLoadingLineupDataWithSocketTimeout() {
        when(args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1))
                .thenReturn(lineup.getId());
        presenter.loadLineupData(args);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new SocketTimeoutException());
        verify(activity).errorLoading();
        verify(activity).showConnectionTimeoutMessage();
        verify(activity, never()).successLoading(any(Lineup.class));
        verify(activity, never()).showServerErrorMessage();
    }

    /**
     * Test the behavior on the presenter when loading the lineup failed with unknown error.
     */
    @Test
    public void testErrorLoadingLineupDataWithUnknownError() {
        when(args.getInt(LineupDetailsActivity.LINEUP_ID_BUNDLE_KEY, -1))
                .thenReturn(lineup.getId());
        presenter.loadLineupData(args);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        verify(activity).errorLoading();
        verify(activity).showServerErrorMessage();
        verify(activity, never()).successLoading(any(Lineup.class));
        verify(activity, never()).showConnectionTimeoutMessage();
    }
}
