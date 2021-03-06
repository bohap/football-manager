package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.utility.Base64Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 04.08.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LineupPlayerRepositoryTest {

    private UserRepository userRepository;
    private TeamRepository teamRepository;
    private PositionRepository positionRepository;
    private LineupRepository lineupRepository;
    private PlayerRepository playerRepository;
    private LineupPlayerRepository repository;

    private Calendar calendar = new GregorianCalendar(2016, 7, 4, 12, 7, 10);
    private Date date = calendar.getTime();
    private final int userId = 1;
    private User user = new User(userId, "User", "user@user.com", "pass", date, date);
    private final int teamId = 1;
    private Team team = new Team(teamId, "Team 1");
    private final int position1Id = 1;
    private final int position2Id = 2;
    private Position position1 = new Position(position1Id, "Position 1");
    private Position position2 = new Position(position2Id, "Position 2");
    private final int player1Id = 1;
    private final int player2Id = 2;
    private Player player1 = new Player(player1Id, team, position1, null, null, null, 0);
    private Player player2 = new Player(player2Id, team, position2, null, null, null, 0);
    private final int lineup1Id = 1;
    private final int lineup2Id = 2;
    private Lineup lineup1 = new Lineup(lineup1Id, userId);
    private Lineup lineup2 = new Lineup(lineup2Id, userId);
    private final int NUMBER_OF_LINEUPS_PLAYERS = 3;
    private LineupPlayer lineupPlayer1 = new LineupPlayer(lineup1, player1, position2, date, date);
    private LineupPlayer lineupPlayer2 = new LineupPlayer(lineup2, player2, position1, date, date);
    private LineupPlayer lineupPlayer3 = new LineupPlayer(lineup1, player2, position1, date, date);
    private LineupPlayer unExistingLineupPlayer =
            new LineupPlayer(lineup2, player1, position2, date, date);
    private List<LineupPlayer> lineup1Players = Arrays.asList(lineupPlayer1, lineupPlayer3);
    private List<LineupPlayer> player1Lineups = Collections.singletonList(lineupPlayer1);
    private List<LineupPlayer> positions1Lineups = Arrays.asList(lineupPlayer2, lineupPlayer3);

    /**
     * Create a new instances, open the connection and seed the tables.
     */
    @Before
    public void setup() {
        Base64Utils base64Utils = new Base64Utils();
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        userRepository = new UserRepository(context, dbHelper, base64Utils);
        teamRepository = new TeamRepository(context, dbHelper);
        positionRepository = new PositionRepository(context, dbHelper);
        playerRepository = new PlayerRepository(context, dbHelper);
        lineupRepository = new LineupRepository(context, dbHelper);
        repository = new LineupPlayerRepository(context, dbHelper);
        /*  Open the connection */
        userRepository.open();
        teamRepository.open();
        positionRepository.open();
        playerRepository.open();
        lineupRepository.open();
        repository.open();
        /* Seed the tables */
        userRepository.store(user);
        teamRepository.store(team);
        positionRepository.store(position1);
        positionRepository.store(position2);
        playerRepository.store(player1);
        playerRepository.store(player2);
        lineupRepository.store(lineup1);
        lineupRepository.store(lineup2);
        repository.store(lineupPlayer1);
        repository.store(lineupPlayer2);
        repository.store(lineupPlayer3);
    }

    /**
     * Close the database connections.
     */
    @After
    public void close() {
        repository.close();
        playerRepository.close();
        lineupRepository.close();
        positionRepository.close();
        teamRepository.close();
        userRepository.close();
    }

    /**
     * Assert that the list are the same.
     *
     * @param expectedList expected list of lineup players
     * @param actualList actual list of lineup players
     * @param checkPlayer whatever the player should be checked
     * @param checkPosition whatever the positions should be checked
     */
    private void assertList(List<LineupPlayer> expectedList, List<LineupPlayer> actualList,
                            boolean checkPlayer, boolean checkPosition) {
        assertEquals(expectedList.size(), actualList.size());
        int count = 0;
        for (LineupPlayer expected : expectedList) {
            for (LineupPlayer actual : actualList) {
                if (expected.equals(actual)) {
                    this.assertLineupPlayer(expected, actual, checkPlayer, checkPosition);
                    count++;
                }
            }
        }
        assertEquals(expectedList.size(), count);
    }

    /**
     * Assert that the LineupPlayer data is valid.
     *
     * @param compare       actual LineupPlayer
     * @param lineupPlayer  LineupPlayer ot be compared
     * @param checkPlayer   whatever the LineupPlayer player should be checked
     * @param checkPosition whatever the LineupPlayer position should be checked
     */
    private void assertLineupPlayer(LineupPlayer compare, LineupPlayer lineupPlayer,
                                    boolean checkPlayer, boolean checkPosition) {
        assertTrue(compare.same(lineupPlayer));
        if (checkPlayer) {
            Player player = lineupPlayer.getPlayer();
            assertNotNull(player);
            assertEquals(compare.getPlayerId(), player.getId().intValue());
        }
        if (checkPosition) {
            Position position = lineupPlayer.getPosition();
            assertNotNull(position);
            assertEquals(compare.getPositionId(), position.getId().intValue());
        }
    }

    /**
     * Test that the getAll method will return all players that are in a lineup.
     */
    @Test
    public void testGetAll() {
        List<LineupPlayer> lineupPlayers = repository.getAll();
        this.assertList(Arrays.asList(lineupPlayer1, lineupPlayer2, lineupPlayer3),
                lineupPlayers, true, true);
    }

    /**
     * Test that get method will return a existing player that is in the given lineup.
     */
    @Test
    public void testGet() {
        LineupPlayer lineupPlayer = repository.get(lineupPlayer1.getLineupId(),
                lineupPlayer1.getPlayerId());
        this.assertLineupPlayer(lineupPlayer1, lineupPlayer, true, true);
    }

    /**
     * Test that get method will return null when the player is not in the lineup.
     */
    @Test
    public void testGetOnUnExistingLike() {
        int lineupId = unExistingLineupPlayer.getLineupId();
        int playerId = unExistingLineupPlayer.getPlayerId();
        assertNull(repository.get(lineupId, playerId));
    }

    /**
     * Test that the count method will return the number of players in a lineup.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS, repository.count());
    }

    /**
     * Test that the store method will create a new player for the lineup.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingLineupPlayer);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS + 1, repository.count());
        LineupPlayer lineupPlayer = repository
                .get(unExistingLineupPlayer.getLineupId(), unExistingLineupPlayer.getPlayerId());
        this.assertLineupPlayer(unExistingLineupPlayer, lineupPlayer, true, true);
    }

    /**
     * Test that the store method will return false when storing the record
     * in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(lineupPlayer1));
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS, repository.count());
    }

    /**
     * Test that the onUpdateSuccess method will onUpdateSuccess the existing player in the lineup.
     */
    @Test
    public void testUpdate() {
        calendar.add(Calendar.HOUR, 1);
        lineupPlayer1.setUpdatedAt(calendar.getTime());
        boolean result = repository.update(lineupPlayer1);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS, repository.count());
        LineupPlayer lineupPlayer = repository.get(lineupPlayer1.getLineupId(),
                lineupPlayer1.getPlayerId());
        this.assertLineupPlayer(lineupPlayer1, lineupPlayer, true, true);
    }

    /**
     * Test that the onUpdateSuccess method will return false when updating the record
     * in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingLineupPlayer));
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS, repository.count());
    }

    /**
     * Test that the delete method will delete the existing player from the lineup.
     */
    @Test
    public void testDelete() {
        int lineupId = lineupPlayer1.getLineupId();
        int playerId = lineupPlayer1.getPlayerId();
        boolean result = repository.delete(lineupId, playerId);
        assertTrue(result);
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS - 1, repository.count());
        assertNull(repository.get(lineupId, playerId));
    }

    /**
     * Test tha delete method will return false when deleting the record from the database failed.
     */
    @Test
    public void testFailedDelete() {
        int lineupId = unExistingLineupPlayer.getLineupId();
        int playerId = unExistingLineupPlayer.getPlayerId();
        assertFalse(repository.delete(lineupId, playerId));
        assertEquals(NUMBER_OF_LINEUPS_PLAYERS, repository.count());
    }

    /**
     * Get that getLineupPlayers method will return all players that were added on the lineup.
     */
    @Test
    public void testGetLineupPlayers() {
        List<LineupPlayer> lineupPlayers = repository.getLineupPlayers(lineup1Id);
        this.assertList(lineup1Players, lineupPlayers, true, true);
    }

    /**
     * Test that getPlayerLineups will return all lineups that contains the given player.
     */
    @Test
    public void testGetPlayerLineup() {
        List<LineupPlayer> lineupPlayers = repository.getPlayerLineups(player1Id);
        this.assertList(player1Lineups, lineupPlayers, false, true);
    }

    /**
     * Test that getPositionLineups will return all player that in a lineup
     * have the given position.
     */
    @Test
    public void testGetPositionLineups() {
        List<LineupPlayer> lineupPlayers = repository.getPositionLineups(position1Id);
        this.assertList(positions1Lineups, lineupPlayers, true, false);
    }
}
