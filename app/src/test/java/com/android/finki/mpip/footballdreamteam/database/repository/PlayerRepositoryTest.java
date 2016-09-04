package com.android.finki.mpip.footballdreamteam.database.repository;

import android.content.Context;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Borce on 31.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class PlayerRepositoryTest {

    private PlayerRepository repository;
    private TeamRepository teamRepository;
    private PositionRepository positionRepository;

    private final int year = 2016, month = 6, day = 31;
    private Calendar calendar = new GregorianCalendar(year, month, day);

    private final int NUMBER_TEAM_1_PLAYERS = 2;
    private final int team1Id = 1;
    private Team team1 = new Team(team1Id, "Team 1", "TM1", "20000$");
    private final int NUMBER_TEAM_2_PLAYERS = 1;
    private final int team2Id = 2;
    private Team team2 = new Team(team2Id, "Team 2", "TM2", "30000$");

    private final int NUMBER_POSITION_1_PLAYERS = 1;
    private final int position1Id = 1;
    private Position position1 = new Position(position1Id, "Position 1");
    private final int NUMBER_POSITION_2_PLAYERS = 2;
    private final int position2Id = 2;
    private Position position2 = new Position(position2Id, "Position 2");

    private final int NUMBER_OF_PLAYERS = 3;
    private final int player1Id = 1;
    private Player player1 = new Player(player1Id, team1Id,
            position1Id, "Player 1", "Nationality 1", calendar.getTime());
    private final int player2Id = 2;
    private Player player2 = new Player(player2Id, team1Id,
            position2Id, "Player 2", "Nationality 2", calendar.getTime());
    private final int player3Id = 3;
    private Player player3 = new Player(player3Id, team2Id,
            position2Id, "Player 2", "Nationality 2", calendar.getTime());
    private final int unExistingPlayerId = 4;
    private Player unExistingPlayer = new Player(unExistingPlayerId, team1Id,
            position1Id, "Player 4", "Nationality 4", calendar.getTime());

    /**
     * Create a new instances of the repositories, open the connections and seed the tables.
     */
    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(context);
        repository = new PlayerRepository(context, dbHelper);
        teamRepository = new TeamRepository(context, dbHelper);
        positionRepository = new PositionRepository(context, dbHelper);
        teamRepository.open();
        positionRepository.open();
        repository.open();
        /* Seed the teams table */
        teamRepository.store(team1);
        teamRepository.store(team2);
        /* Seed the positions table */
        positionRepository.store(position1);
        positionRepository.store(position2);
        /* Seed the players table */
        repository.store(player1);
        repository.store(player2);
        repository.store(player3);
    }

    /**
     * Close the database connections.
     */
    @After
    public void shutdown() {
        teamRepository.close();
        positionRepository.close();
        repository.close();
    }

    /**
     * Assert that the given array of players is valid.
     *
     * @param number expected number of players
     * @param players List of players
     * @param checkTeam whatever that player team should be asserted
     * @param checkPosition whatever the player position should be asserted
     */
    private void assertPlayers(int number, List<Player> players,
                               boolean checkTeam, boolean checkPosition) {
        assertNotNull(players);
        assertEquals(number, players.size());
        int count = 0;
        for (Player player : players) {
            if (player.getId().equals(player1Id)) {
                this.assertPlayer(player1, player, checkTeam, checkPosition);
                count++;
            } else if (player.getId().equals(player2Id)) {
                this.assertPlayer(player2, player, checkTeam, checkPosition);
                count++;
            } else if (player.getId().equals(player3Id)) {
                this.assertPlayer(player3, player, checkTeam, checkPosition);
                count++;
            }
        }
        assertEquals(number, count);
    }

    /**
     * Assert that the given player data is valid.
     *
     * @param compare actual player
     * @param player player to bo compared
     * @param checkTeam whatever the player team should be asserted
     * @param checkPosition whatever the player position should be asserted
     */
    private void assertPlayer(Player compare, Player player,
                              boolean checkTeam, boolean checkPosition) {
        assertNotNull(player);
        assertEquals(compare.getId(), player.getId());
        assertEquals(compare.getTeamId(), player.getTeamId());
        assertEquals(compare.getPositionId(), player.getPositionId());
        if (checkTeam) {
            Team team = player.getTeam();
            assertNotNull(team);
            assertEquals(compare.getTeamId(), team.getId().intValue());
        }
        if (checkPosition) {
            Position position = player.getPosition();
            assertNotNull(position);
            assertEquals(compare.getPositionId(), position.getId().intValue());
        }
    }

    /**
     * Test that get all method will return all players.
     */
    @Test
    public void testGetAll() {
        List<Player> players = repository.getAll();
        this.assertPlayers(NUMBER_OF_PLAYERS, players, true, true);
    }

    /**
     * Test that the get method will return a existing player.
     */
    @Test
    public void testGet() {
        Player player = repository.get(player1Id);
        this.assertPlayer(player1, player, true, true);
    }

    /**
     * Test that get method will return null when the player id don't exists.
     */
    @Test
    public void testGetOnUnExistingId() {
        assertNull(repository.get(unExistingPlayerId));
    }

    /**
     * Test that getByName method will return a player when the name exists.
     */
    @Test
    public void testGetByName() {
        String name = player1.getName();
        Player player = repository.getByName(name);
        this.assertPlayer(player, player, true, true);
        assertEquals(name, player.getName());
    }

    /**
     * Test that getByName will return null when the name don't exists.
     */
    @Test
    public void testGetByNameOnUnExistingName() {
        String name = unExistingPlayer.getName();
        assertNull(repository.getByName(name));
    }

    /**
     * Test that the count method will return the number of players.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
    }

    /**
     * Test that the store method will create a new player in the database.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingPlayer);
        assertTrue(result);
        assertEquals(NUMBER_OF_PLAYERS + 1, repository.count());
        Player player = repository.get(unExistingPlayerId);
        this.assertPlayer(unExistingPlayer, player, false, false);
    }

    /**
     * Test that the store method will return false when storing the record in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(player1));
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
    }

    /**
     * Test that the update method will update the player data.
     */
    @Test
    public void testUpdate() {
        String name = "Player Updated";
        player1.setName(name);
        boolean result = repository.update(player1);
        assertTrue(result);
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
        Player player = repository.get(player1Id);
        this.assertPlayer(player1, player, false, false);
        assertEquals(name, player.getName());
    }

    /**
     * Test that update method will return false when updating the record in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingPlayer));
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
    }

    /**
     * Test that the delete method will delete the player from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(player1Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_PLAYERS - 1, repository.count());
    }

    /**
     * Test that delete method will return false when deleting the record from the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingPlayerId));
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
    }

    /**
     * Test that getTeamPlayers method will return all players that plays for the given team.
     */
    @Test
    public void testGetTeamPlayers() {
        List<Player> players = repository.getTeamPlayers(team1Id);
        this.assertPlayers(NUMBER_TEAM_1_PLAYERS, players, false, true);
    }

    /**
     * Test that getPositionPlayer will return all players that plays on the given position.
     */
    @Test
    public void testGetPositionPlayers() {
        List<Player> players = repository.getPositionPlayers(position1Id);
        this.assertPlayers(NUMBER_POSITION_1_PLAYERS, players, true, false);
    }

    /**
     * Test getPositionPlayers called with multiple positions.
     */
    @Test
    public void testGetPositionPlayersWithMultiplePositions() {
        List<Player> players = repository.getPositionPlayers(position1Id, position2Id);
        this.assertPlayers(NUMBER_POSITION_1_PLAYERS + NUMBER_POSITION_2_PLAYERS,
                players, true, false);
    }

    /**
     * Test getPositionsPlayers called with a exclude players.
     */
    @Test
    public void testGetPositionsPlayersWithExcludedPlayers() {
        int[] playersToExclude = {player1Id, player2Id};
        List<Player> players = repository
                .getPositionPlayers(playersToExclude, position1Id, position2Id);
        this.assertPlayers(NUMBER_POSITION_1_PLAYERS
                + NUMBER_POSITION_2_PLAYERS - playersToExclude.length, players, true, false);
    }
}