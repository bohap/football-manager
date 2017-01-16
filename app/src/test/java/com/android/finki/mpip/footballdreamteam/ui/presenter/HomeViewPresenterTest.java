package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.background.task.StorePlayersTask;
import com.android.finki.mpip.footballdreamteam.background.task.StorePositionsTask;
import com.android.finki.mpip.footballdreamteam.background.task.StoreTeamsTask;
import com.android.finki.mpip.footballdreamteam.exception.InternalServerErrorException;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.ui.component.HomeView;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 09.08.2016.
 */
public class HomeViewPresenterTest {

    @Mock
    private HomeView view;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private Context context;

    @Mock
    private TeamApi teamApi;

    @Mock
    private PositionApi positionApi;

    @Mock
    private PlayerApi playerApi;

    @Mock
    private StoreTeamsTask storeTeamsTask;

    @Mock
    private StorePositionsTask storePositionsTask;

    @Mock
    private StorePlayersTask storePlayersTask;

    @Mock
    private Call<List<Team>> teamsCall;

    @Captor
    private ArgumentCaptor<Callback<List<Team>>> teamsCaptor;

    @Mock
    private Call<List<Position>> positionCall;

    @Captor
    private ArgumentCaptor<Callback<List<Position>>> positionsCaptor;

    @Mock
    private Call<List<Player>> playersCall;

    @Captor
    private ArgumentCaptor<Callback<List<Player>>> playersCaptor;

    private HomeViewPresenter presenter;

    private String TEAMS_LOADED_KEY = "teams_loaded";
    private String POSITIONS_LOADED_KEY = "positions_loaded";
    private String PLAYERS_LOADED_KEY = "players_loaded";

    private Team team1 = new Team(1, "Team 1");
    private Team team2 = new Team(2, "Team 2");
    private List<Team> teams = Arrays.asList(team1, team2);
    private Position position1 = new Position(1, "Position 1");
    private Position position2 = new Position(2, "Position 2");
    private List<Position> positions = Arrays.asList(position1, position2);
    private Player player1 = new Player(1, "Player 1");
    private Player player2 = new Player(2, "Player 2");
    private List<Player> players = Arrays.asList(player1, player2);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        presenter = new HomeViewPresenter(view, preferences, context, teamApi, positionApi,
                playerApi, storeTeamsTask, storePositionsTask, storePlayersTask);
    }

    /**
     * Mock the object to return specific values on method calls.
     */
    @SuppressLint("CommitPrefEdits")
    private void initMocks() {
        when(context.getString(R.string.preference_teams_loaded_key))
                .thenReturn(TEAMS_LOADED_KEY);
        when(context.getString(R.string.preference_positions_loaded_key))
                .thenReturn(POSITIONS_LOADED_KEY);
        when(context.getString(R.string.preference_players_loaded_key))
                .thenReturn(PLAYERS_LOADED_KEY);
        /* Mock the preferences */
        when(preferences.edit()).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);
        /* Mock the apps */
        when(teamApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(teamsCall);
        when(positionApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(positionCall);
        when(playerApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(playersCall);
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request is not sending.
     */
    @Test
    public void testOnViewLayoutCreatedWhenRequestIsNotSending() {
        presenter.onViewLayoutCreated();
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showInitialDataInfoDialog();
    }

    /**
     * Test the behavior when onViewLayoutCreated is called and a request is sending.
     */
    @Test
    public void testOnViewLayoutCreatedWhenARequestIsSending() {
        presenter.loadData();
        verify(view, never()).showInitialDataInfoDialog();
        verify(view, never()).showInitialDataInfoDialog();
        presenter.onViewLayoutCreated();
        verify(view).showInitialDataLoading();
        verify(view).showInitialDataInfoDialog();
    }

    /**
     * Test the behavior when loadTeams is called and the teams are already loading.
     */
    @Test
    public void testLoadTeamsWhenTeamsAreCurrentlyLoading() {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(false);
        presenter.loadData();
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadPositions is called and the teams are currently loading.
     */
    @Test
    public void testLoadPositionsWhenTeamsAreCurrentlyLoading() {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(false);
        presenter.loadData();
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionApi, never()).index(anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Test the behavior when loadPlayers is called and the positions are currently loading.
     */
    @Test
    public void testLoadPlayersWhenPositionsAreCurrentlyLoading() {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        presenter.loadData();
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playerApi, never()).index(anyBoolean(), anyInt(), anyInt());
    }

    /**
     * Mock the shared preferences.
     *
     * @param teamsLoaded     whatever the teams is be loaded
     * @param positionsLoaded whatever the positions is loaded
     * @param playersLoaded   whatever the players is loaded
     */
    private void initPreferences(boolean teamsLoaded, boolean positionsLoaded,
                                 boolean playersLoaded) {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(teamsLoaded);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(positionsLoaded);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(playersLoaded);
    }

    /**
     * Test the behavior when the teams data is not loaded, the request to load them failed and
     * the view layout is created before the request is send and not destroyed before the request
     * response is received.
     */
    @Test
    public void testLoadingTeamsFailedAndViewLayoutNotDestroyed() {
        this.initPreferences(false, false, false);
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        verify(view).showInitialDataLoading();
        verify(view).showInitialDataInfoDialog();
        teamsCaptor.getValue().onFailure(teamsCall, new InternalServerErrorException());
        verify(view).showTeamsLoadingFailed();
        verify(view).showInternalServerError();
        verify(view, never()).showTeamsStoring();
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is not loaded and the request to load them failed and
     * the view layout is created before the request is send and destroyed before the request
     * is received.
     */
    @Test
    public void testLoadingTeamsFailedAndViewLayoutDestroyed() {
        this.initPreferences(false, false, false);
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        verify(view).showInitialDataLoading();
        verify(view).showInitialDataInfoDialog();
        presenter.onViewLayoutDestroyed();
        teamsCaptor.getValue().onFailure(teamsCall, new InternalServerErrorException());
        verify(view, never()).showTeamsLoadingFailed();
        verify(view, never()).showInternalServerError();
        verify(view, never()).showTeamsStoring();
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is not loaded, the request to load then is successful
     * but a error occurred while saving the data, and the view layout is not created before the
     * request is send and is created before the request response is received.
     */
    @Test
    public void testTeamsStoringFailedAndViewLayoutCreatedAfterTheRequest() {
        this.initPreferences(false, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingFailed();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showInitialDataInfoDialog();
        presenter.onViewLayoutCreated();
        verify(view).showInitialDataLoading();
        verify(view).showInitialDataInfoDialog();
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        verify(view).showTeamsStoring();
        verify(view).showTeamsStoringFailed();
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is not loaded, the request to load then is successful
     * but a error occurred while saving the data, and the view layout is not created neither
     * before the request is send or the request response is received.
     */
    @Test
    public void testTeamsStoringFailedAndViewLayoutNotCreatedAfterTheRequest() {
        this.initPreferences(false, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingFailed();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showInitialDataInfoDialog();
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        verify(view, never()).showTeamsStoring();
        verify(view, never()).showTeamsStoringFailed();
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when teams data is not loaded, the request to load them is successful,
     * the teams are successfully saved in the database, the positions data is not loaded, the
     * request to load positions data failed and the view layout created before the positions
     * request is send.
     */
    @Test
    public void testTeamsStoringSuccessAndPositionsLoadingFailed() {
        this.initPreferences(false, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingSuccess();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        presenter.onViewLayoutCreated();
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        verify(positionCall).enqueue(positionsCaptor.capture());
        verify(view, times(2)).showInitialDataLoading();
        verify(view).showInitialDataInfoDialog();
        verify(editor).putBoolean(TEAMS_LOADED_KEY, true);
        verify(editor).apply();
        positionsCaptor.getValue().onFailure(positionCall, new UnknownHostException());
        verify(view).showPositionsLoadingFailed();
        verify(view).showNoInternetConnection();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when teams data is not loaded, the view layout is created before the
     * request is send, the request to load the teams is successful, the teams are successfully
     * saved in the database, the positions data is not loaded, the request to load positions data
     * failed and the view layout is destroyed before the positions request is received.
     */
    @Test
    public void testTeamsStoringSuccessAndPositionsLoadingFailedAndViewLayoutDestroyed() {
        this.initPreferences(false, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingSuccess();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        verify(positionCall).enqueue(positionsCaptor.capture());
        verify(view, times(2)).showInitialDataLoading();
        verify(view, times(1)).showInitialDataInfoDialog();
        verify(view).showPositionsLoading();
        presenter.onViewLayoutDestroyed();
        positionsCaptor.getValue().onFailure(positionCall, new UnknownHostException());
        verify(view, never()).showPositionsLoadingFailed();
        verify(view, never()).showNoInternetConnection();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is loaded, the position is not, the request
     * to load them is successful, saving the positions failed and the view layout is created before
     * the request response is received.
     */
    @Test
    public void testPositionsStoringFailedAndViewLayoutCreatedBeforeTheResponseIsReceived() {
        this.initPreferences(true, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingFailed();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall).enqueue(positionsCaptor.capture());
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showInitialDataInfoDialog();
        verify(view, never()).showPositionsLoading();
        presenter.onViewLayoutCreated();
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        verify(view).showPositionsStoring();
        verify(view).showPositionsStoringFailed();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is loaded, the position is not, the request
     * to load them is successful, saving the positions failed and the view layout is
     * never created.
     */
    @Test
    public void testPositionsStoringFailedAndViewLayoutNeverCreated() {
        this.initPreferences(true, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingFailed();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showInitialDataInfoDialog();
        verify(view, never()).showPositionsLoading();
        verify(view, never()).showPositionsStoring();
        verify(view, never()).showPositionsStoringFailed();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is loaded, the position is not, the request
     * to load them is successful, saving the positions is successful and the players data
     * is loaded.
     */
    @Test
    public void testPositionsStoringSuccessAndPlayersDataLoaded() {
        this.initPreferences(true, false, true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingSuccess();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall).enqueue(positionsCaptor.capture());
        presenter.onViewLayoutCreated();
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        verify(view, never()).showPositionsStoringFailed();
        verify(editor).putBoolean(POSITIONS_LOADED_KEY, true);
        verify(editor).apply();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when only the teams and positions data is loaded, the request to load
     * the teams failed and the view layout is created before the players is send.
     */
    @Test
    public void testPlayersLoadingFailedAndViewLayoutCreatedBeforeTheRequestIsSend() {
        this.initPreferences(true, true, false);
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall).enqueue(playersCaptor.capture());
        verify(view).showInitialDataLoading();
        verify(view).showPlayersLoading();
        verify(view).showInitialDataInfoDialog();
        playersCaptor.getValue().onFailure(playersCall, new SocketTimeoutException());
        verify(view).showPlayersLoadingFailed();
        verify(view).showSocketTimeout();
    }

    /**
     * Test the behavior when teams data is loaded, the positions data is not, the request to load
     * positions data is successful, storing the positions is successful, the request to load
     * players failed and the view layout is created before the positions request is send and
     * destroyed before the players request response is received send.
     */
    @Test
    public void testPlayersLoadingFailedAndViewLayoutDestroyedBeforePlayersResponse() {
        this.initPreferences(true, false, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingSuccess();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(view).showInitialDataLoading();
        verify(view).showPositionsLoading();
        verify(view).showInitialDataInfoDialog();
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        verify(playersCall).enqueue(playersCaptor.capture());
        verify(view, times(2)).showInitialDataLoading();
        verify(view).showPlayersLoading();
        verify(view, times(1)).showInitialDataInfoDialog();
        presenter.onViewLayoutDestroyed();
        playersCaptor.getValue().onFailure(playersCall, new SocketTimeoutException());
        verify(view, never()).showPlayersLoadingFailed();
        verify(view, never()).showSocketTimeout();
    }

    /**
     * Test the behavior when the teams and positions data is loaded, the request to load players
     * data is successful, storing the players data failed and the view layout is created before
     * the request response is received.
     */
    @Test
    public void testPlayersStoringFailedAndViewLayoutCreatedAfterPlayersResponse() {
        this.initPreferences(true, true, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingFailed();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showPlayersLoading();
        verify(view, never()).showInitialDataInfoDialog();
        verify(playersCall).enqueue(playersCaptor.capture());
        presenter.onViewLayoutCreated();
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view).showPlayersStoring();
        verify(view).showPlayersStoringFailed();
    }

    /**
     * Test the behavior when the teams and positions data is loaded, loading the players data is
     * successful, storing the players data failed and the view layout is never created.
     */
    @Test
    public void testPlayersStoringFailedAndViewLayoutNeverCreated() {
        this.initPreferences(true, true, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingFailed();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view, never()).showInitialDataLoading();
        verify(view, never()).showPlayersLoading();
        verify(view, never()).showInitialDataInfoDialog();
        verify(view, never()).showPlayersStoring();
        verify(view, never()).showPlayersStoringFailed();
        verify(view, never()).showInitialDataLoadingSuccess();
    }

    /**
     * Test the behavior when the teams and positions data is loaded, loading the players data is
     * successful and storing the players data is successful.
     */
    @Test
    public void testPlayersStoringSuccess() {
        this.initPreferences(true, true, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingSuccess();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(view).showInitialDataLoading();
        verify(view).showPlayersLoading();
        verify(view).showInitialDataInfoDialog();
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view).showPlayersStoring();
        verify(view, never()).showPlayersStoringFailed();
        verify(view).showInitialDataLoadingSuccess();
        verify(editor).putBoolean(PLAYERS_LOADED_KEY, true);
        verify(editor).apply();
    }

    /**
     * Test the behavior when the teams and positions data is loaded, loading the players data is
     * successful, storing the players data is successful and the view layout is destroyed when
     * request response is received.
     */
    @Test
    public void testPlayersStoringSuccessAndViewLayoutDestroyed() {
        this.initPreferences(true, true, false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingSuccess();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        presenter.onViewLayoutCreated();
        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(view).showInitialDataLoading();
        verify(view).showPlayersLoading();
        verify(view).showInitialDataInfoDialog();
        verify(playersCall).enqueue(playersCaptor.capture());
        presenter.onViewLayoutDestroyed();
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        verify(view, never()).showPlayersStoring();
        verify(view, never()).showPlayersStoringFailed();
        verify(view, never()).showInitialDataLoadingSuccess();
        verify(editor).putBoolean(PLAYERS_LOADED_KEY, true);
        verify(editor).apply();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the teams is not
     * yet finished.
     */
    @Test
    public void testOnViewDestroyedWhenTeamsAreLoading() {
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(teamsCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the teams that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterTeamsLoadingSucceeded() {
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        presenter.onViewDestroyed();
        verify(teamsCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the teams that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterTeamsLoadingFailed() {
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onFailure(teamsCall, new Throwable());
        presenter.onViewDestroyed();
        verify(teamsCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the positions is
     * not yet finished.
     */
    @Test
    public void testOnViewDestroyedWhenPositionsAreLoading() {
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(positionCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the positions that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterPositionsLoadingSucceeded() {
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        presenter.onViewDestroyed();
        verify(positionCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the positions that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterPositionsLoadingFailed() {
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onFailure(positionCall, new Throwable());
        presenter.onViewDestroyed();
        verify(positionCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the players is
     * not yet finished.
     */
    @Test
    public void testOnViewDestroyedWhenPlayersAreLoading() {
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        presenter.onViewDestroyed();
        verify(playersCall).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the players that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterPlayersLoadingSucceeded() {
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        presenter.onViewDestroyed();
        verify(playersCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called after request to load the players that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterPlayersLoadingFailed() {
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onFailure(playersCall, new Throwable());
        presenter.onViewDestroyed();
        verify(playersCall, never()).cancel();
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the teams is
     * successful and the task to store the teams is not yer finished.
     */
    @Test
    public void testOnViewDestroyedWhenTaskForStoringTheTeamsIsNotFinished() {
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        presenter.onViewDestroyed();
        verify(teamsCall, never()).cancel();
        verify(storeTeamsTask).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the teams that
     * succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterStoreTeamsTaskSucceeded() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingSuccess();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        presenter.onViewDestroyed();
        verify(teamsCall, never()).cancel();
        verify(storeTeamsTask, never()).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the teams that
     * failed.
     */
    @Test
    public void testOnViewDestroyedAfterStoreTeamsTaskFailed() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingFailed();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());
        this.initPreferences(false, true, true);
        presenter.loadData();
        verify(teamApi).index(anyBoolean(), anyInt(), anyInt());
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));
        presenter.onViewDestroyed();
        verify(teamsCall, never()).cancel();
        verify(storeTeamsTask, never()).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the positions is
     * successful and the task to store the positions is not yer finished.
     */
    @Test
    public void testOnViewDestroyedWhenTaskForStoringThePositionsIsNotFinished() {
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        presenter.onViewDestroyed();
        verify(positionCall, never()).cancel();
        verify(storePositionsTask).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the positions
     * that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterStorePositionsTaskSucceeded() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingSuccess();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        presenter.onViewDestroyed();
        verify(positionCall, never()).cancel();
        verify(storePositionsTask, never()).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the positions
     * that failed.
     */
    @Test
    public void testOnViewDestroyedAfterStorePositionsTaskFailed() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPositionsSavingFailed();
                return null;
            }
        }).when(storePositionsTask).execute(Matchers.<Position>anyVararg());
        this.initPreferences(true, false, true);
        presenter.loadData();
        verify(positionApi).index(anyBoolean(), anyInt(), anyInt());
        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));
        presenter.onViewDestroyed();
        verify(positionCall, never()).cancel();
        verify(storePositionsTask, never()).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called and the request to load the players is
     * successful and the task to store the players is not yer finished.
     */
    @Test
    public void testOnViewDestroyedWhenTaskForStoringThePlayersIsNotFinished() {
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        presenter.onViewDestroyed();
        verify(playersCall, never()).cancel();
        verify(storePlayersTask).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the players
     * that succeeded.
     */
    @Test
    public void testOnViewDestroyedAfterStorePlayersTaskSucceeded() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingSuccess();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        presenter.onViewDestroyed();
        verify(playersCall, never()).cancel();
        verify(storePlayersTask, never()).cancel(true);
    }

    /**
     * Test the behavior when onViewDestroyed is called after the task for storing the players
     * that failed.
     */
    @Test
    public void testOnViewDestroyedAfterStorePlayersTaskFailed() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingSuccess();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());
        this.initPreferences(true, true, false);
        presenter.loadData();
        verify(playerApi).index(anyBoolean(), anyInt(), anyInt());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
        presenter.onViewDestroyed();
        verify(playersCall, never()).cancel();
        verify(storePlayersTask, never()).cancel(true);
    }
}