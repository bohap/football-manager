package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.LineupDBService;
import com.android.finki.mpip.footballdreamteam.database.service.LineupPlayerDBService;
import com.android.finki.mpip.footballdreamteam.exception.LineupException;
import com.android.finki.mpip.footballdreamteam.exception.LineupPlayerException;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.rest.request.LineupRequest;
import com.android.finki.mpip.footballdreamteam.rest.response.LineupResponse;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupPlayersView;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 17.08.2016.
 */
public class LineupPlayersViewPresenterTest {

    @Mock
    LineupPlayersView view;

    @Mock
    private User user;

    @Mock
    private LineupApi api;

    @Mock
    private Bundle args;

    @Mock
    private LineupDBService lineupDBService;

    @Mock
    private LineupPlayerDBService lineupPlayerDBService;

    @Mock
    private LineupPlayerValidator validator;

    @Mock
    private Call<List<Player>> playersCall;

    @Captor
    private ArgumentCaptor<Callback<List<Player>>> playersCaptor;

    @Mock
    private Call<LineupResponse> updateCall;

    @Captor
    private ArgumentCaptor<Callback<LineupResponse>> updateCaptor;

    private LineupPlayersViewPresenter presenter;

    private Lineup lineup = new Lineup(1, new User(1, "User 1"));
    private List<Player> players = Arrays.asList(new Player(), new Player(), new Player());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        presenter = new LineupPlayersViewPresenter(view, user, api, lineupDBService,
                lineupPlayerDBService, validator);
    }

    /**
     * Init the mocks to return specific values on method calls.
     */
    private void initMocks() {
        when(api.players(anyInt(), anyInt(), anyInt())).thenReturn(playersCall);
        when(api.update(anyInt(), any(LineupRequest.class))).thenReturn(updateCall);
        when(args.getSerializable(LineupPlayersView.LINEUP_BUNDLE_KEY)).thenReturn(lineup);
    }

    /**
     * Test the behavior when onViewCreated is called with null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithNullArgument() {
        presenter.onViewCreated(null);
    }

    /**
     * Test the behavior when onViewCreated is called with data in the bundle
     * that is not a Lineup.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithInvalidBundleData() {
        when(args.getSerializable(LineupPlayersView.LINEUP_BUNDLE_KEY)).thenReturn(new User());
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onVIewCreated is called with a lineup id in the bundle that is invalid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadPlayersOnInvalidLineupId() {
        when(args.getSerializable(LineupPlayersView.LINEUP_BUNDLE_KEY))
                .thenReturn(new Lineup(-1, 1));
        presenter.onViewCreated(args);
    }

    /**
     * Test that onViewCreated method will send a request to load the lineup players data
     * from the server, when the view layout is not yet created.
     */
    @Test
    public void testLoadPlayersWhenViewLayoutIsNotCreated() {
        presenter.onViewCreated(args);
        verify(api).players(lineup.getId(), null, null);
        verify(playersCall).enqueue(playersCaptor.capture());
        verify(view, never()).showLoading();
    }

    /**
     * Test that onViewCreated method will send a request to load the lineup players data
     * from the server, when the view layout is created before the request is send.
     */
    @Test
    public void testLoadPlayersWhenViewLayoutIsCreated() {
        presenter.onViewLayoutCreated();
        presenter.onViewCreated(args);
        verify(api).players(lineup.getId(), null, null);
        verify(playersCall).enqueue(playersCaptor.capture());
        verify(view).showLoading();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request to load lineup data
     * is already sending.
     */
    @Test
    public void testOnViewLayoutCreatedWhenARequestIsSending() {
        presenter.onViewCreated(args);
        verify(api).players(lineup.getId(), null, null);
        verify(view, never()).showLoading();
        presenter.onViewLayoutCreated();
        verify(view).showLoading();
    }

    /**
     * Test the behavior on the presenter when data from the server is loaded successfully
     * and the view layout is created before the response id received.
     */
    @Test
    public void testLoadPlayersSuccessWhenViewLayoutIsCreated() {
        lineup.getUser().setId(user.getId());
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view).showLoadingSuccess(players);
        verify(view, never()).showLoadingFailed();
    }

    /**
     * Test the behavior on the presenter when data from the server is loaded successfully
     * and the view layout is not created before the response id received.
     */
    @Test
    public void testLoadPlayersSuccessWhenViewLayoutIsNotCreated() {
        lineup.getUser().setId(user.getId());
        presenter.onViewCreated(args);
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view, never()).showLoadingSuccess(players);
        verify(view, never()).showLoadingFailed();
    }

    /**
     * Test the behavior on the presenter when loading the data failed with socket timeout error
     * and the view layout is crated before the response is received.
     */
    @Test
    public void testLoadPlayersFailedWithSocketTimeoutErrorAndViewLayoutIsCreated() {
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onFailure(playersCall, new SocketTimeoutException());
        verify(view).showLoadingFailed();
        verify(view, never()).showLoadingSuccess(players);
        verify(view).showSocketTimeout();
    }

    /**
     * Test the behavior on the presenter when loading the data failed with socket timeout error
     * and the view layout is crated before the response is received.
     */
    @Test
    public void testLoadPlayersFailedWithSocketTimeoutErrorAndViewLayoutIsNotCreated() {
        presenter.onViewCreated(args);
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onFailure(playersCall, new SocketTimeoutException());
        verify(view, never()).showLoadingFailed();
        verify(view, never()).showLoadingSuccess(players);
        verify(view, never()).showSocketTimeout();
    }

    /**
     * Test the behavior when loadPlayers is called and a request to load players
     * is already sending.
     */
    @Test
    public void testLoadPlayersWhenPlayersAreAlreadyLoading() {
        presenter.onViewCreated(args);
        verify(api).players(lineup.getId(), null, null);
        reset(api);
        presenter.loadPlayers();
        verify(api, never()).players(anyInt(), any(Integer.class), any(Integer.class));
    }

    /**
     * TEst the behavior when loadPlayers is called and the view is destroyed before the response
     * from the server is received.
     */
    @Test
    public void testLoadPlayersWhenViewIsDestroyed() {
        presenter.onViewCreated(args);
        verify(api).players(lineup.getId(), null, null);
        verify(playersCall).enqueue(playersCaptor.capture());
        presenter.onViewDestroyed();
        verify(playersCall).cancel();
    }

    /**
     * Test that the canEditLineup method returns true when the authenticated user
     * is the author of the lineup.
     */
    @Test
    public void testCanEditLineup() {
        when(user.getId()).thenReturn(lineup.getUserId());
        presenter.extractLineup(args);
        assertTrue(presenter.canEditLineup());
    }

    /**
     * Test that the canEditLineup method returns false when the authenticated user
     * is not the author of the lineup.
     */
    @Test
    public void testCanNotEditLineup() {
        when(user.getId()).thenReturn(lineup.getUserId() + 1);
        presenter.extractLineup(args);
        assertFalse(presenter.canEditLineup());
    }

    /**
     * Test the behavior on updated called before the lineup data is set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnSetLineup() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.update();
    }

    /**
     * Test the behavior when onUpdateSuccess is called and the data is not yet changed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnChangedData() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewCreated(args);
        presenter.update();
    }

    /**
     * Test the behavior when update is called and the lineup players are not valid.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWIthInvalidLineupPlayers() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(false);
        presenter.onViewCreated(args);
        presenter.update();
    }

    /**
     * Test the behavior when update is called and the view layout is not created.
     */
    @Test
    public void testUpdateWhenViewLayoutIsNotCreated() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewCreated(args);
        presenter.setChanged(true);
        presenter.update();
        verify(api).update(anyInt(), any(LineupRequest.class));
        verify(updateCall).enqueue(updateCaptor.capture());
        verify(view, never()).showUpdating();
    }

    /**
     * Test the behavior when update is called and the view layout is created.
     */
    @Test
    public void testUpdateWhenViewLayoutIsCreated() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(api).update(anyInt(), any(LineupRequest.class));
        verify(updateCall).enqueue(updateCaptor.capture());
        verify(view).showUpdating();
    }

    /**
     * Test the behavior when updating the lineup is successful, the lineup is not saved
     * in the database and saving the lineup failed.
     */
    @Test
    public void testUpdateSuccessOnUnSavedLineupWithLineupSavingFailed() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        when(lineupDBService.exists(any(Lineup.class))).thenReturn(false);
        doThrow(LineupException.class).when(lineupDBService).store(any(Lineup.class));
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onResponse(updateCall, Response.success(new LineupResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).store(any(Lineup.class));
        verify(lineupDBService).close();
        verify(lineupPlayerDBService, never()).open();
        verify(lineupPlayerDBService, never()).storePlayers(anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService, never()).close();
        verify(view).showUpdatingSuccess();
    }

    /**
     * Test the behavior when onUpdateSuccess is successful, the lineup is not saved in the database and
     * saving the players failed.
     */
    @Test
    public void testUpdateSuccessOnUnSavedLineupWIthPlayerSavingFailed() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        when(lineupDBService.exists(any(Lineup.class))).thenReturn(false);
        doThrow(LineupPlayerException.class).when(lineupPlayerDBService)
                .storePlayers(anyListOf(LineupPlayer.class));
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onResponse(updateCall, Response.success(new LineupResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).store(any(Lineup.class));
        verify(lineupDBService).close();
        verify(lineupPlayerDBService).open();
        verify(lineupPlayerDBService).storePlayers(anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService).close();
        verify(view).showUpdatingSuccess();
    }

    /**
     * Test the behavior when onUpdateSuccess is successful, the lineup is saved in the
     * database and updating the lineup failed.
     */
    @Test
    public void testUpdateSuccessOnSavedLineupWithUpdateLineupFailed() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        when(lineupDBService.exists(any(Lineup.class))).thenReturn(true);
        doThrow(LineupException.class).when(lineupDBService).update(any(Lineup.class));
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onResponse(updateCall, Response.success(new LineupResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).update(any(Lineup.class));
        verify(lineupDBService).close();
        verify(lineupPlayerDBService, never()).open();
        verify(lineupPlayerDBService, never())
                .syncPlayers(anyInt(), anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService, never()).close();
        verify(view).showUpdatingSuccess();
    }

    /**
     * Test the behavior when the onUpdateSuccess is successful, the lineup is saved in the database
     * and updating the players failed.
     */
    @Test
    public void testUpdateSuccessOnSavedLineupWithUpdatePlayersFailed() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        when(lineupDBService.exists(any(Lineup.class))).thenReturn(true);
        doThrow(LineupPlayerException.class).when(lineupPlayerDBService)
                .syncPlayers(anyInt(), anyListOf(LineupPlayer.class));
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onResponse(updateCall, Response.success(new LineupResponse()));

        verify(lineupDBService).open();
        verify(lineupDBService).update(any(Lineup.class));
        verify(lineupDBService).close();
        verify(lineupPlayerDBService).open();
        verify(lineupPlayerDBService).syncPlayers(anyInt(), anyListOf(LineupPlayer.class));
        verify(lineupPlayerDBService).close();
        verify(view).showUpdatingSuccess();
    }

    /**
     * Test the behavior when updating the lineup failed with SocketTimeout error.
     */
    @Test
    public void testFailedUpdateOnSocketTimeoutMessage() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onFailure(updateCall, new SocketTimeoutException());
        verify(view).showUpdatingFailed();
        verify(view).showSocketTimeout();
    }

    /**
     * Test the behavior when updating the lineup failed with unknown error.
     */
    @Test
    public void testFailedUpdateOnUnknownError() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        presenter.setChanged(true);
        presenter.update();
        verify(updateCall).enqueue(updateCaptor.capture());
        updateCaptor.getValue().onFailure(updateCall, new Throwable());
        verify(view).showUpdatingFailed();
    }

    /**
     * Test the behavior when updateFormation is called and the view getFormation returns null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateFormationOnNullViewFormation() {
        presenter.updateFormation(LineupUtils.FORMATION.F_3_2_3_2);
    }

    /**
     * Test that updateFormation call the changeFormation on the view when the new formation is
     * different that the current.
     */
    @Test
    public void testUpdateFormationWhenFormationIsDifferent() {
        final List<Player> players = new ArrayList<>();
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        when(view.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
        when(view.getPlayersOrdered()).thenReturn(players);
        presenter.updateFormation(LineupUtils.FORMATION.F_4_4_2);
        verify(view).changeFormation(LineupUtils.FORMATION.F_4_4_2, players);
    }

    /**
     * Test that updateFormation call the changeFormation on the view when the new formation is
     * same as the current.
     */
    @Test
    public void testUpdateFormationWhenFormationIsSame() {
        final List<Player> players = new ArrayList<>();
        when(view.getFormation()).thenReturn(LineupUtils.FORMATION.F_3_2_3_2);
        when(view.getPlayersOrdered()).thenReturn(players);
        presenter.updateFormation(LineupUtils.FORMATION.F_3_2_3_2);
        verify(view, never())
                .changeFormation(any(LineupUtils.FORMATION.class), anyListOf(Player.class));
    }
}
