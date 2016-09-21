package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.background.task.StorePlayersTask;
import com.android.finki.mpip.footballdreamteam.background.task.StorePositionsTask;
import com.android.finki.mpip.footballdreamteam.background.task.StoreTeamsTask;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.rest.web.LineupApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PlayerApi;
import com.android.finki.mpip.footballdreamteam.rest.web.PositionApi;
import com.android.finki.mpip.footballdreamteam.rest.web.TeamApi;
import com.android.finki.mpip.footballdreamteam.ui.activity.HomeActivity;

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

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
@Ignore
public class HomeViewPresenterTest {

    @Mock
    private HomeActivity activity;

    @Mock
    private SharedPreferences preferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private TeamApi teamApi;

    @Mock
    private PositionApi positionApi;

    @Mock
    private PlayerApi playerApi;

    @Mock
    private LineupApi lineupApi;

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

    @Mock
    private Call<List<Lineup>> lineupCall;

    @Captor
    private ArgumentCaptor<Callback<List<Lineup>>> lineupsCaptor;

    @Captor
    private ArgumentCaptor<Boolean> lineupShortResponseCaptor;

    @Captor
    private ArgumentCaptor<Boolean> lineupLatestCaptor;

    @Captor
    private ArgumentCaptor<Integer> lineupLimitCaptor;

    @Captor
    private ArgumentCaptor<Integer> lineupOffsetCaptor;

    @Captor
    private ArgumentCaptor<Player> playerCaptor;

    @Captor
    private ArgumentCaptor<Position> positionCaptor;

    @Captor
    private ArgumentCaptor<Team> teamCaptor;

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
    private Lineup lineup1 = new Lineup(1, 1);
    private Lineup lineup2 = new Lineup(1, 1);
    private List<Lineup> lineups = Arrays.asList(lineup1, lineup2);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
//        presenter = new HomeViewPresenter(activity, preferences, teamApi, positionApi,
//                playerApi, lineupApi, storeTeamsTask, storePositionsTask, storePlayersTask);
    }

    /**
     * Mock the object to return specific values on method calls.
     */
    @SuppressLint("CommitPrefEdits")
    private void initMocks() {
        when(activity.getString(R.string.preference_teams_loaded_key))
                .thenReturn(TEAMS_LOADED_KEY);
        when(activity.getString(R.string.preference_positions_loaded_key))
                .thenReturn(POSITIONS_LOADED_KEY);
        when(activity.getString(R.string.preference_players_loaded_key))
                .thenReturn(PLAYERS_LOADED_KEY);
        /* Mock the preferences */
        when(preferences.edit()).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);
        /* Mock the apps */
        when(teamApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(teamsCall);
        when(positionApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(positionCall);
        when(playerApi.index(anyBoolean(), anyInt(), anyInt())).thenReturn(playersCall);
        when(lineupApi.index(anyBoolean(), anyBoolean(), anyInt(), anyInt()))
                .thenReturn(lineupCall);
    }

    /**
     * Test the behavior when the teams data is not loaded and the request to load the failed.
     */
    @Test
    public void testLoadingTeamsFailed() {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onFailure(teamsCall, new Throwable());

        verify(activity).showInitialDataLoading();
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when the teams data is not loaded, the request to load then is successful
     * but a error occurred while saving the data.
     *
     * @throws InterruptedException
     */
    @Test
    public void testSavingTeamsDataFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingFailed();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());

        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));

        verify(activity).showInitialDataLoading();

        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when teams data is not loaded, the request to load them is successful,
     * the teams are successfully saved in the database, the positions data is not loaded and the
     * request to load positions data failed.
     *
     * @throws InterruptedException
     */
    @Test
    public void testTeamsLoadedSuccessAndPositionsLoadingFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onTeamsSavingSuccess();
                return null;
            }
        }).when(storeTeamsTask).execute(Matchers.<Team>anyVararg());

        presenter.loadData();
        verify(teamsCall).enqueue(teamsCaptor.capture());
        teamsCaptor.getValue().onResponse(teamsCall, Response.success(teams));

        verify(positionCall).enqueue(positionsCaptor.capture());
        positionsCaptor.getValue().onFailure(positionCall, new Throwable());
        verify(playersCall, never()).enqueue(playersCaptor.capture());

        verify(activity, times(2)).showInitialDataLoading();
    }

    /**
     * Test the behavior when the teams data is loaded, the position is not, the request
     * to load them is successful and saving the teams failed.
     *
     * @throws InterruptedException
     */
    @Test
    public void testPositionsStoringFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
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

        verify(activity).showInitialDataLoading();
        verify(playersCall, never()).enqueue(playersCaptor.capture());
    }

    /**
     * Test the behavior when only the teams data is loaded, the request to load the positions is
     * successful, saving the positions in the database is successful and the request to load
     * the teams failed.
     *
     * @throws InterruptedException
     */
    @Test
    public void testPositionsLoadingSuccessAndPlayersLoadingFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(false);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
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
        positionsCaptor.getValue().onResponse(positionCall, Response.success(positions));

        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onFailure(playersCall, new Throwable());

        verify(activity, times(2)).showInitialDataLoading();
    }

    /**
     * Test the behavior when the players data is not loaded, the players data is successfully
     * loaded from the server and saving the players in the local database failed.
     *
     * @throws InterruptedException
     */
    @Test
    public void testPlayersSavingFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
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
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));
    }

    /**
     * Test the behavior when the players is not loaded, the request to load them is
     * successful, the players are successfully saved in the database and loading the
     * lineup failed.
     *
     * @throws InterruptedException
     */
    @Test
    public void testPlayersLoadingSuccessAndLineupsLoadingFailed() throws InterruptedException {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(false);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.onPlayersSavingSuccess();
                return null;
            }
        }).when(storePlayersTask).execute(Matchers.<Player>anyVararg());

        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall).enqueue(playersCaptor.capture());
        playersCaptor.getValue().onResponse(playersCall, Response.success(players));

        verify(lineupCall).enqueue(lineupsCaptor.capture());
        lineupsCaptor.getValue().onFailure(lineupCall, new Throwable());

        verify(activity).showInitialDataLoading();
    }

    /**
     * Test the behavior when all teams, positions and players are already loaded and loading
     * the lineups is successful.
     */
    @Test
    public void testLineupsLoadingSuccess() {
        when(preferences.getBoolean(TEAMS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(POSITIONS_LOADED_KEY, false)).thenReturn(true);
        when(preferences.getBoolean(PLAYERS_LOADED_KEY, false)).thenReturn(true);

        presenter.loadData();
        verify(teamsCall, never()).enqueue(teamsCaptor.capture());
        verify(positionCall, never()).enqueue(positionsCaptor.capture());
        verify(playersCall, never()).enqueue(playersCaptor.capture());
        verify(lineupCall).enqueue(lineupsCaptor.capture());
        lineupsCaptor.getValue().onResponse(lineupCall, Response.success(lineups));
    }

    /**
     * Test the behavior when loadMoreLineups is called before a response from the last
     * request came.
     */
    @Test
    public void testLoadMoreLineupsCalledBeforeTHeLastRequestResponse() {
        verify(lineupCall).enqueue(lineupsCaptor.capture());
    }

    /**
     * Test the behavior when loading more lineups is successful.
     */
    @Test
    public void testLoadMoreLineupsSuccess() {
//        final int limit = HomeViewPresenter.LINEUPS_LIMIT;
//        presenter.loadMoreLineups();
//        verify(lineupCall).enqueue(lineupsCaptor.capture());
//        lineupsCaptor.getValue().onResponse(lineupCall, Response.success(lineups));
//
//        presenter.loadMoreLineups();
//        lineupsCaptor.getValue().onResponse(lineupCall, Response.success(lineups));
//
//        verify(lineupApi, times(2)).index(lineupShortResponseCaptor.capture(),
//                lineupLatestCaptor.capture(), lineupLimitCaptor.capture(),
//                lineupOffsetCaptor.capture());
//
//        assertNull(lineupShortResponseCaptor.getAllValues().get(0));
//        assertTrue(lineupLatestCaptor.getAllValues().get(0));
//        assertEquals(limit, lineupLimitCaptor.getAllValues().get(0).intValue());
//        assertEquals(limit, lineupOffsetCaptor.getAllValues().get(0).intValue());
//        assertNull(lineupShortResponseCaptor.getAllValues().get(1));
//        assertTrue(lineupLatestCaptor.getAllValues().get(1));
//        assertEquals(limit, lineupLimitCaptor.getAllValues().get(1).intValue());
//        assertEquals(2 * limit, lineupOffsetCaptor.getAllValues().get(1).intValue());
//
//        verify(activity, times(2)).successLoadingLineups(lineups);
    }

    /**
     * Test the behavior when loading more lineups failed.
     */
    @Test
    public void testLoadMoreLineupsFailed() {
//        final int limit = HomeViewPresenter.LINEUPS_LIMIT;
//        presenter.loadMoreLineups();
//        verify(lineupCall).enqueue(lineupsCaptor.capture());
//        lineupsCaptor.getValue().onFailure(lineupCall, new Throwable());
//
//        presenter.loadMoreLineups();
//        lineupsCaptor.getValue().onResponse(lineupCall, Response.success(lineups));
//
//        verify(lineupApi, times(2)).index(lineupShortResponseCaptor.capture(),
//                lineupLatestCaptor.capture(), lineupLimitCaptor.capture(),
//                lineupOffsetCaptor.capture());
//
//        assertNull(lineupShortResponseCaptor.getAllValues().get(0));
//        assertTrue(lineupLatestCaptor.getAllValues().get(0));
//        assertEquals(limit, lineupLimitCaptor.getAllValues().get(0).intValue());
//        assertEquals(limit, lineupOffsetCaptor.getAllValues().get(0).intValue());
//        assertNull(lineupShortResponseCaptor.getAllValues().get(1));
//        assertTrue(lineupLatestCaptor.getAllValues().get(1));
//        assertEquals(limit, lineupLimitCaptor.getAllValues().get(1).intValue());
//        assertEquals(limit, lineupOffsetCaptor.getAllValues().get(1).intValue());
//
//        verify(activity).showErrorLoading();
//        verify(activity).successLoadingLineups(lineups);
    }
}