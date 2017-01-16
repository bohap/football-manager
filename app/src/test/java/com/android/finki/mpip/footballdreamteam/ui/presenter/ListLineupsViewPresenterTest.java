package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.ListLineupsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 23.09.2016.
 */
public class ListLineupsViewPresenterTest {

    @Mock
    private ListLineupsView view;

    @Mock
    private LineupApi api;

    @Mock
    private Call<List<Lineup>> call;

    @Mock
    private Call<Void> deleteCall;

    @Captor
    private ArgumentCaptor<Callback<List<Lineup>>> callbackCaptor;

    @Captor
    private ArgumentCaptor<Callback<Void>> deleteCallbackCaptor;

    @Captor
    private ArgumentCaptor<Integer> limitCaptor;

    @Captor
    private ArgumentCaptor<Integer> offsetCaptor;

    private ListLineupsViewPresenter presenter;
    private final User user = new User(1, "User");
    private List<Lineup> lineups = Arrays.asList(new Lineup(1, 1),
            new Lineup(2, 2), new Lineup(3, 3));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(api.index(anyBoolean(), anyBoolean(), anyInt(), anyInt())).thenReturn(call);
        when(api.delete(anyInt())).thenReturn(deleteCall);
        presenter = new ListLineupsViewPresenter(view, api, user);
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request for loading the lineups
     * is sending.
     */
    @Test
    public void onViewLayoutCreatedWhenRequestIsSending() {
        presenter.loadLineups(true);
        verify(view, never()).showLoading();
        presenter.onViewLayoutCreated();
        verify(view).showLoading();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request for loading the lineups
     * is not sending.
     */
    @Test
    public void onViewLayoutCreatedWhenRequestIsNotSending() {
        presenter.onViewLayoutCreated();
        verify(view, never()).showLoading();
    }

    /**
     * Test the behavior when loadLineups is called and a request to load them is already sending.
     */
    @Test
    public void testLoadLineupsWhenRequestIsAlreadySending() {
        presenter.loadLineups(false);
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loading the lineups data failed and the view layout
     * is created before the request is send.
     */
    @Test
    public void testLoadLineupsFailedAndViewLayoutCreated() {
        presenter.onViewCreated();
        presenter.onViewLayoutCreated();
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        verify(view).showLoading();
        callbackCaptor.getValue().onFailure(call, new InternalServerErrorException());
        verify(view).showLoadingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
    }

    /**
     * Test the behavior when loading the lineups failed, the view layout is destroyed before
     * the response is received created and method is called with false value for param callView.
     */
    @Test
    public void testLoadLineupsFailedAndViewLayoutDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.loadLineups(false);
        verify(call).enqueue(callbackCaptor.capture());
        verify(view, never()).showLoading();
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onFailure(call, new InternalServerErrorException());
        verify(view, never()).showLoadingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
    }

    /**
     * Test the behavior when loading the lineups is successful and the view layout is destroyed
     * before the response is received.
     */
    @Test
    public void testLoadLineupsSuccessAndViewLayoutDestroyedBeforeResponse() {
        presenter.onViewCreated();
        presenter.onViewLayoutCreated();
        verify(call).enqueue(callbackCaptor.capture());
        verify(view).showLoading();
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
        verify(view, never()).showLoadingFailed();
        verify(view, never()).showNoMoreLineups();
        List<Lineup> lineups = presenter.getLineups();
        assertEquals(this.lineups.size(), lineups.size());
        for (int i = 0; i < lineups.size(); i++) {
            assertSame(this.lineups.get(i), lineups.get(i));
        }
    }

    /**
     * Test the behavior when loading the lineups is successful and the view layout is not
     * destroyed before the response is received and the response body lineups size is lesser
     * then the Presenter LIMIT lineups value.
     */
    @Test
    public void testLoadLineupsSuccessAndResponseLineupsSizeLesserThenLimit() {
        presenter.onViewCreated();
        presenter.onViewLayoutCreated();
        verify(call).enqueue(callbackCaptor.capture());
        verify(view).showLoading();
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        verify(view).showLoadingSuccess(lineups);
        verify(view).showNoMoreLineups();
        verify(view, never()).showLoadingFailed();
        List<Lineup> lineups = presenter.getLineups();
        assertEquals(this.lineups.size(), lineups.size());
        for (int i = 0; i < lineups.size(); i++) {
            assertSame(this.lineups.get(i), lineups.get(i));
        }
    }

    /**
     * Test the behavior when loading the lineups is successful and the view layout is not
     * destroyed before the response is received and the response body lineups size is not lesser
     * then the Presenter LIMIT lineups value.
     */
    @Test
    public void testLoadLineupsSuccessAndResponseLineupsSizeIsNotLesserThenLimit() {
        List<Lineup> body = new ArrayList<>();
        for (int i = 0; i < ListLineupsViewPresenter.LINEUPS_LIMIT; i++) {
            body.add(new Lineup(i, i));
        }
        presenter.onViewCreated();
        presenter.onViewLayoutCreated();
        verify(call).enqueue(callbackCaptor.capture());
        verify(view).showLoading();
        callbackCaptor.getValue().onResponse(call, Response.success(body));
        verify(view).showLoadingSuccess(body);
        verify(view, never()).showNoMoreLineups();
        verify(view, never()).showLoadingFailed();
        List<Lineup> lineups = presenter.getLineups();
        assertEquals(body.size(), lineups.size());
        for (int i = 0; i < body.size(); i++) {
            assertSame(body.get(i), lineups.get(i));
        }
    }

    /**
     * Test that the request offset param for loading the lineups will increase after the previous
     * request for loading the lineups is successful.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRequestOffsetParamWillIncreaseAfterTheLineupsAreLoadedSuccessfully() {
        int id = this.lineups.get(this.lineups.size() - 1).getId();
        List<Lineup> newItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newItems.add(new Lineup(id, id++));
        }
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        reset(call);
        presenter.loadLineups(false);
        verify(api, times(2)).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        final int limit = limitCaptor.getAllValues().get(2);
        final int offset = offsetCaptor.getAllValues().get(2);
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limit);
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, offset);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(newItems));
        List<Lineup> lineups = presenter.getLineups();
        assertEquals(this.lineups.size() + newItems.size(), lineups.size());
        int j = 0;
        for (int i = 0; i < this.lineups.size(); i++,j++) {
            assertSame(this.lineups.get(i), lineups.get(j));
        }
        for (int i = 0; i < newItems.size(); i++,j++) {
            assertSame(newItems.get(i), lineups.get(j));
        }
    }

    /**
     * Test that the request offset param for loading the lineups will increase after the previous
     * request for loading the lineups is successful.
     */
    @Test
    public void testRequestOffsetParamWillNotIncreaseAfterLoadingTheLineupsFailed() {
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.loadLineups(false);
        verify(api, times(2)).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        final int limit = limitCaptor.getAllValues().get(2);
        final int offset = offsetCaptor.getAllValues().get(2);
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limit);
        assertEquals(0, offset);
    }

    /**
     * Test the behavior when refresh is called and a refresh reqeust is already sending.
     */
    @Test
    public void testRefreshCalledWhenRefreshingRequestIsAlreadySending() {
        presenter.refresh();
        presenter.refresh();
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when refresh reqeust failed and the view layout is created before the
     * request is send.
     */
    @Test
    public void testRefreshingFailedAndViewLayoutCreated() {
        presenter.onViewLayoutCreated();
        presenter.refresh();
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(view).showRefreshing();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new UnknownHostException());
        verify(view).showRefreshingFailed();
        verify(view).showNoInternetConnection();
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
    }

    /**
     * Test the behavior when refresh reqeust failed and the view layout is never created.
     */
    @Test
    public void testRefreshingFailedAndViewLayoutNotCreated() {
        presenter.refresh();
        verify(view, never()).showRefreshing();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new UnknownHostException());
        verify(view, never()).showRefreshingFailed();
        verify(view, never()).showNoInternetConnection();
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
    }

    /**
     * Test the behavior when refreshing the lineups is successful and the view layout is not
     * destroyed when the response is received.
     */
    @Test
    public void testRefreshSuccessAndViewLayoutNotDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.refresh();
        verify(view).showRefreshing();
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        verify(view).showLoadingSuccess(lineups);
        verify(view, never()).showRefreshingFailed();
        assertSame(lineups, presenter.getLineups());
    }

    /**
     * Test the behavior when refreshing the lineups is successful and the view layout is not
     * destroyed when the response is received.
     */
    @Test
    public void testRefreshSuccessAndViewLayoutDestroyed() {
        presenter.onViewLayoutCreated();
        presenter.refresh();
        verify(view).showRefreshing();
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        presenter.onViewLayoutDestroyed();
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        verify(view, never()).showLoadingSuccess(anyListOf(Lineup.class));
        verify(view, never()).showRefreshingFailed();
        assertSame(lineups, presenter.getLineups());
    }

    /**
     * Test the behavior when refresh is called after previous request for loading a lineups is
     * successful.
     */
    @Test
    public void testRefreshCalledAfterLineupsLoadingSucceeded() {
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        assertEquals(ListLineupsViewPresenter.LINEUPS_LIMIT, limitCaptor.getValue().intValue());
        assertEquals(0, offsetCaptor.getValue().intValue());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        presenter.refresh();
        verify(api, times(2)).index(anyBoolean(), anyBoolean(),
                limitCaptor.capture(), offsetCaptor.capture());
        final int limit = limitCaptor.getAllValues().get(2);
        final int offset = offsetCaptor.getAllValues().get(2);
        assertEquals(2 * ListLineupsViewPresenter.LINEUPS_LIMIT, limit);
        assertEquals(0, offset);
    }

    /**
     * Test the behavior when delete is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithNullParam() {
        presenter.deleteLineup(null);
    }

    /**
     * Test the deleting the lineup failed and the view layout is created before the request
     * is send.
     */
    @Test
    public void testDeleteLineupFailedAndViewLayoutCreated() {
        final Lineup lineup = lineups.get(1);
        presenter.onViewLayoutCreated();
        presenter.deleteLineup(lineup);
        verify(api).delete(lineup.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        deleteCallbackCaptor.getValue().onFailure(deleteCall, new SocketTimeoutException());
        verify(view).showLineupDeletingFailed(lineup);
        verify(view).showSocketTimeout();
        verify(view, never()).showLineupDeletingSuccess(any(Lineup.class));
    }

    /**
     * Test the deleting the lineup failed and the view layout is never created.
     */
    @Test
    public void testDeleteLineupFailedAndViewLayoutNotCreated() {
        final Lineup lineup = lineups.get(1);
        presenter.deleteLineup(lineup);
        verify(api).delete(lineup.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        deleteCallbackCaptor.getValue().onFailure(deleteCall, new SocketTimeoutException());
        verify(view, never()).showLineupDeletingFailed(any(Lineup.class));
        verify(view, never()).showSocketTimeout();
        verify(view, never()).showLineupDeletingSuccess(any(Lineup.class));
    }

    /**
     * Test the behavior when deleting the lineup is successful and the view layout is created
     * before the request is send.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testDeleteLineupSuccessAndViewLayoutCreated() {
        final Lineup lineup = lineups.get(0);
        presenter.onViewLayoutCreated();
        presenter.deleteLineup(lineup);
        verify(api).delete(lineup.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        Void response = null;
        deleteCallbackCaptor.getValue().onResponse(deleteCall, Response.success(response));
        verify(view).showLineupDeletingSuccess(lineup);
        verify(view, never()).showLineupDeletingFailed(any(Lineup.class));
    }

    /**
     * Test the behavior when deleting the lineup is successful and the view layout is never
     * created.
     */
    @SuppressWarnings("ConstantConditions")
    @Test
    public void testDeleteLineupSuccessAndVIewLayoutNotCreated() {
        final Lineup lineup = lineups.get(0);
        presenter.deleteLineup(lineup);
        verify(api).delete(lineup.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        Void response = null;
        deleteCallbackCaptor.getValue().onResponse(deleteCall, Response.success(response));
        verify(view, never()).showLineupDeletingSuccess(any(Lineup.class));
        verify(view, never()).showLineupDeletingFailed(any(Lineup.class));
    }

    /**
     * Test the behavior when deleteLineup is called and at the moment another request is sending
     * for deleting the lineup and that request succeeded.
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Test
    public void testDeleteLineupCalledBeforeThePreviousRequestFinishedAndPreviousSucceeded() {
        final Lineup lineup1 = lineups.get(1);
        final Lineup lineup2 = lineups.get(0);
        presenter.deleteLineup(lineup1);
        verify(api).delete(lineup1.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        presenter.deleteLineup(lineup2);
        verify(api, times(1)).delete(anyInt());
        verify(api, never()).delete(lineup2.getId());
        Void response = null;
        reset(deleteCall);
        deleteCallbackCaptor.getValue().onResponse(deleteCall, Response.success(response));
        verify(api).delete(lineup2.getId());
        verify(api, times(2)).delete(anyInt());
    }

    /**
     * Test the behavior when deleteLineup is called and at the moment another request is sending
     * for deleting the lineup and that request failed.
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Test
    public void testDeleteLineupCalledBeforeThePreviousRequestFinishedAndPreviousFailed() {
        final Lineup lineup1 = lineups.get(2);
        final Lineup lineup2 = lineups.get(0);
        presenter.deleteLineup(lineup1);
        verify(api).delete(lineup1.getId());
        verify(deleteCall).enqueue(deleteCallbackCaptor.capture());
        presenter.deleteLineup(lineup2);
        verify(api, times(1)).delete(anyInt());
        verify(api, never()).delete(lineup2.getId());
        reset(deleteCall);
        deleteCallbackCaptor.getValue().onFailure(deleteCall, new Throwable());
        verify(api).delete(lineup2.getId());
        verify(api, times(2)).delete(anyInt());
    }

    /**
     * Test the behavior when loadLineups is called after the previous request for loading them
     * succeeded.
     */
    @Test
    public void testLoadLineupsCalledAfterThePreviousRequestSucceeded() {
        presenter.loadLineups(false);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        presenter.loadLineups(false);
        verify(api, times(2)).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadLineups is called after the previous request for loading them
     * failed.
     */
    @Test
    public void testLoadLineupsCalledAfterThePreviousRequestFailed() {
        presenter.loadLineups(false);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.loadLineups(false);
        verify(api, times(2)).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when refresh is called after the previous request for refreshing them
     * succeeded.
     */
    @Test
    public void testRefreshLineupsCalledAfterThePreviousRequestSucceeded() {
        presenter.refresh();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        presenter.refresh();
        verify(api, times(2)).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when refresh is called after the previous request for refreshing them
     * failed.
     */
    @Test
    public void testRefreshLineupsCalledAfterThePreviousRequestFailed() {
        presenter.refresh();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.refresh();
        verify(api, times(2)).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when onViewDestroyed is called and a request for the lineups
     * is sending.
     */
    @Test
    public void testOnViewDestroyedWhenLoadLineupsCallIsSending() {
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(call).cancel();
    }

    /**
     * Test the behavior when onVIewDestroyed is called after a request to load
     * lineups that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterLoadLineupsRequestSucceeded() {
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        presenter.onViewDestroyed();
        verify(call, never()).cancel();
    }

    /**
     * Test the behavior when onVIewDestroyed is called after a request to load
     * lineups that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterLoadLineupsRequestFailed() {
        presenter.loadLineups(false);
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.onViewDestroyed();
        verify(call, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and a request for the lineups
     * is sending.
     */
    @Test
    public void testOnViewDestroyedWhenRefreshLineupsCallIsSending() {
        presenter.refresh();
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(call).cancel();
    }

    /**
     * Test the behavior when onVIewDestroyed is called after a request to refresh
     * lineups that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterRefreshLineupsRequestSucceeded() {
        presenter.refresh();
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(lineups));
        presenter.onViewDestroyed();
        verify(call, never()).cancel();
    }

    /**
     * Test the behavior when onVIewDestroyed is called after a request to refresh
     * lineups that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterRefreshLineupsRequestFailed() {
        presenter.refresh();
        verify(api).index(anyBoolean(), anyBoolean(), anyInt(), anyInt());
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new Throwable());
        presenter.onViewDestroyed();
        verify(call, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and a delete lineup request is sending.
     */
    @Test
    public void testOnViewDestroyedWhenDeleteCallIsSending() {
        presenter.deleteLineup(lineups.get(0));
        verify(api).delete(lineups.get(0).getId());
        presenter.onViewDestroyed();
        verify(deleteCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and there are multiple delete calls in the
     * queue waiting to be executed.
     */
    @Test
    public void testOnViewDestroyedWhenThereAreMultipleDeleteCallInTheQueue() {
        presenter.deleteLineup(lineups.get(0));
        presenter.deleteLineup(lineups.get(1));
        presenter.deleteLineup(lineups.get(2));
        verify(api).delete(anyInt());
        presenter.onViewDestroyed();
        verify(deleteCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and a delete lineup request is not sending.
     */
    @Test
    public void testOnViewDestroyedWhenDeleteCallIsNotSending() {
        presenter.onViewDestroyed();
        verify(deleteCall, never()).cancel();
    }
}
