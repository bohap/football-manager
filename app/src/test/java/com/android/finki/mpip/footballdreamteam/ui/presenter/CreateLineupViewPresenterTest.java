package com.android.finki.mpip.footballdreamteam.ui.presenter;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.exception.LineupPlayerException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.CreatedLineupView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    private List<LineupPlayer> lineupPlayers = Arrays.asList(new LineupPlayer(1, 1, 1),
            new LineupPlayer(2, 2, 2), new LineupPlayer(3, 3, 3));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(api.store(any(LineupRequest.class))).thenReturn(call);
        presenter = new CreateLineupViewPresenter(view, api,
                lineupDBService, lineupPlayerDBService);
    }

    /**
     * Test the behavior when updateFormation is called and the new formation is same
     * as the current.
     */
    @Test
    public void testUpdateFormationOnSameFormation() {
        presenter.updateFormation(LineupPlayers.FORMATION.F_4_4_2);
        verify(view, never())
                .changeFormation(any(LineupPlayers.FORMATION.class), anyListOf(Player.class));
    }

    /**
     * Test the behavior when updateFormation is called and the new formation is
     * not the same as the current.
     */
    @Test
    public void testUpdateFormationOnNewFormation() {
        List<Player> players = Arrays.asList(new Player(), new Player());
        when(view.getPlayers()).thenReturn(players);
        presenter.updateFormation(LineupPlayers.FORMATION.F_4_2_3_1);
        verify(view).changeFormation(LineupPlayers.FORMATION.F_4_2_3_1, players);
    }

    /**
     * Test the behavior when store is called with invalid player id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreWithInvalidPlayerId() {
        lineupPlayers.get(0).setPlayerId(-1);
        presenter.store(lineupPlayers);
    }

    /**
     * Test the behavior when store is called with invalid position id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreWithInvalidPositionId() {
        lineupPlayers.get(1).setPositionId(-2);
        presenter.store(lineupPlayers);
    }

    /**
     * Test the behavior when store is called.
     */
    @Test
    public void testStore() {
        presenter.store(lineupPlayers);
        verify(api).store(lineupRequestCaptor.capture());
        List<LineupRequest.Content> lineupPlayers = lineupRequestCaptor.getValue().getPlayers();
        assertEquals(this.lineupPlayers.size(), lineupPlayers.size());
        for (int i = 0; i < this.lineupPlayers.size(); i++) {
            assertEquals(this.lineupPlayers.get(i).getPlayerId(),
                    lineupPlayers.get(i).getPlayerId());
            assertEquals(this.lineupPlayers.get(i).getPositionId(),
                    lineupPlayers.get(i).getPositionId());
        }
        verify(call).enqueue(callbackCaptor.capture());
    }

    /**
     * Test the behavior when storing the lineup is successful and the server response
     * for the lineup is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoringSuccessWhenServerRespondedWithNullLineup() {
        presenter.store(lineupPlayers);
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
        presenter.store(lineupPlayers);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(lineup);
        verify(lineupDBService).close();
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
        presenter.store(lineupPlayers);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService).store(lineup);
        verify(lineupDBService).close();
        verify(lineupPlayerDBService).open();
        verify(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService).close();
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
        presenter.store(lineupPlayers);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, Response.success(new LineupResponse(lineup)));

        verify(lineupDBService).open();
        verify(lineupDBService).exists(lineup.getId());
        verify(lineupDBService, never()).store(lineup);
        verify(lineupDBService).close();
        verify(lineupPlayerDBService).open();
        verify(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService).close();
        verify(view).showStoringSuccessful(lineup);
    }

    /**
     * Test the behavior when storing the lineup failed.
     */
    @Test
    public void testStoringFailed() {
        presenter.store(lineupPlayers);
        verify(call).enqueue(callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, new SocketTimeoutException());
        verify(view).showStoringFailed();
    }
}
