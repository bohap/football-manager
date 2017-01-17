package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.exception.LineupPlayerException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 25.08.2016.
 */
public class CreateLineupViewPresenterTest {

    @Mock
    private CreatedLineupView view;

    @Mock
    private LineupApi api;

    @Mock
    private LineupDBService lineupDBService;

    @Mock
    private LineupPlayerDBService lineupPlayerDBService;

    @Captor
    private ArgumentCaptor<LineupRequest> lineupRequestCaptor;

    @Mock
    private Call<LineupResponse> call;

    @Captor
    private ArgumentCaptor<Callback<LineupResponse>> callbackCaptor;

    private CreateLineupViewPresenter presenter;

    private List<LineupPlayer> lineupPlayers = Arrays.asList(
            new LineupPlayer(1, 1, 1), new LineupPlayer(2, 2, 2), new LineupPlayer(3, 3, 3));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(api.store(any(LineupRequest.class))).thenReturn(call);
        presenter = new CreateLineupViewPresenter(view, api, lineupDBService,
                lineupPlayerDBService);
    }

    /**
     * Test the behavior when updateFormation is called and the new formation is same
     * as the current.
     */
    @Test
    public void testUpdateFormationOnSameFormation() {
        presenter.onViewLayoutCreated();
        presenter.updateFormation(FORMATION.F_4_4_2);
        verify(view, never()).changeFormation(any(FORMATION.class), anyListOf(Player.class));
    }

    /**
     * Test the behavior when updateFormation is called and the new formation is
     * not the same as the current.
     */
    @Test
    public void testUpdateFormationOnNewFormation() {
        List<Player> mockList = Arrays.asList(new Player(), new Player());
        when(view.getPlayersOrdered()).thenReturn(mockList);
        presenter.onViewLayoutCreated();
        presenter.updateFormation(FORMATION.F_4_2_3_1);
        verify(view).changeFormation(FORMATION.F_4_2_3_1, mockList);
    }

    /**
     * Test the behavior when store is called and the view layout is not created.
     */
    @Test
    public void testStoreWhenViewLayoutIsNotCreated() {
        presenter.store();
        verify(view, never()).showStoring();
        verify(api, never()).store(any(LineupRequest.class));
    }

    /**
     * Test the behavior when store is called with invalid player id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreWithInvalidPlayerId() {
        lineupPlayers.get(0).setPlayerId(-1);
        when(view.getLineupPlayers()).thenReturn(lineupPlayers);
        presenter.onViewLayoutCreated();
        presenter.store();
    }

    /**
     * Test the behavior when store is called with invalid position id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreWithInvalidPositionId() {
        lineupPlayers.get(1).setPositionId(-2);
        when(view.getLineupPlayers()).thenReturn(lineupPlayers);
        presenter.onViewLayoutCreated();
        presenter.store();
    }

    /**
     * Test the behavior when store is called.
     */
    @Test
    public void testStore() {
        when(view.getLineupPlayers()).thenReturn(lineupPlayers);
        presenter.onViewLayoutCreated();
        presenter.store();
        verify(api).store(lineupRequestCaptor.capture());
        List<LineupRequest.Content> requestPlayers = lineupRequestCaptor.getValue().getPlayers();
        assertEquals(lineupPlayers.size(), requestPlayers.size());
        for (int i = 0; i < lineupPlayers.size(); i++) {
            LineupPlayer expected = this.lineupPlayers.get(i);
            LineupRequest.Content actual = requestPlayers.get(i);
            assertEquals(expected.getPlayerId(), actual.getPlayerId());
            assertEquals(expected.getPositionId(), actual.getPositionId());
        }
        verify(call).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when store is called and a store request is already sending.
     */
    @Test
    public void testStoreWhenARequestIsAlreadySending() {
        presenter.onViewLayoutCreated();
        presenter.store();
        verify(api).store(any(LineupRequest.class));
        reset(api);
        presenter.store();
        verify(api, never()).store(any(LineupRequest.class));
    }

    /**
     * Test the behavior when storing the lineup is successful and the server response
     * for the lineup is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoringSuccessWhenServerRespondedWithNullLineup() {
        presenter.onViewLayoutCreated();
        presenter.store();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(null)));
        verify(view, never()).showStoringSuccessful(any(Lineup.class));
    }

    /**
     * Test the behavior when storing the lineup is successful, the lineup is not saved in the
     * database and saving the lineup failed.
     */
    @Test
    public void testStoringSuccessWhenLineupNotSavedAndLineupSavingFailed() {
        final Lineup lineup = new Lineup(1, 1);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        doThrow(new LineupException("")).when(lineupDBService).store(any(Lineup.class));

        presenter.onViewLayoutCreated();
        presenter.store();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        InOrder inOrder = inOrder(lineupDBService);
        inOrder.verify(lineupDBService).open();
        inOrder.verify(lineupDBService).exists(lineup.getId());
        inOrder.verify(lineupDBService).store(lineup);
        inOrder.verify(lineupDBService).close();
        verify(lineupPlayerDBService, never()).open();
        verify(lineupPlayerDBService, never()).storePlayers(anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService, never()).close();
        verify(view).showStoringSuccessful(lineup);
    }

    /**
     * Test the behavior when storing the lineup is successful, the lineup is not saved in the
     * database and saving the lineup players failed.
     */
    @Test
    public void testStoreSuccessWhenLineupNotSavedAndSavingThePlayersFailed() {
        final Lineup lineup = new Lineup(1, 1);
        when(lineupDBService.exists(anyInt())).thenReturn(false);
        doThrow(new LineupPlayerException(""))
                .when(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));

        presenter.onViewLayoutCreated();
        presenter.store();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        InOrder inOrder = inOrder(lineupDBService);
        inOrder.verify(lineupDBService).open();
        inOrder.verify(lineupDBService).exists(lineup.getId());
        inOrder.verify(lineupDBService).store(lineup);
        inOrder.verify(lineupDBService).close();

        inOrder = inOrder(lineupPlayerDBService);
        inOrder.verify(lineupPlayerDBService).open();
        inOrder.verify(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));
        inOrder.verify(lineupPlayerDBService).close();
        verify(view).showStoringSuccessful(lineup);
    }

    /**
     * Test the behavior when storing the lineup is successful, the lineup is saved in the
     * database and saving the lineup players is successful.
     */
    @Test
    public void testStoreSuccessWhenStoringThePlayersIsSuccessful() {
        final Lineup lineup = new Lineup(1, 1);
        when(lineupDBService.exists(anyInt())).thenReturn(true);

        presenter.onViewLayoutCreated();
        presenter.store();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        InOrder inOrder = inOrder(lineupDBService);
        inOrder.verify(lineupDBService).open();
        inOrder.verify(lineupDBService).exists(lineup.getId());
        inOrder.verify(lineupDBService, never()).store(lineup);
        inOrder.verify(lineupDBService).close();

        inOrder = inOrder(lineupPlayerDBService);
        inOrder.verify(lineupPlayerDBService).open();
        inOrder.verify(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));
        inOrder.verify(lineupPlayerDBService).close();
        verify(view).showStoringSuccessful(lineup);
    }

    /**
     * Test the behavior when storing the lineup failed.
     */
    @Test
    public void testStoringFailed() {
        presenter.onViewLayoutCreated();
        presenter.store();
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new SocketTimeoutException());
        verify(view).showStoringFailed();
        verify(view).showSocketTimeout();
    }
}
