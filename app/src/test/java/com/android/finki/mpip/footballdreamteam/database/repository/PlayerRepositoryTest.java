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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    private Date date = calendar.getTime();

    private final int team1Id = 1;
    private Team team1 = new Team(team1Id, "Team 1", "TM1", "20000$");
    private final int team2Id = 2;
    private Team team2 = new Team(team2Id, "Team 2", "TM2", "30000$");

    private final int position1Id = 1;
    private Position position1 = new Position(position1Id, "Position 1");
    private final int position2Id = 2;
    private Position position2 = new Position(position2Id, "Position 2");

    private final int NUMBER_OF_PLAYERS = 5;
    private final int player1Id = 1;
    private Player player1 = new Player(player1Id, team1Id, position1Id,
            "Player 1", "Nationality", date);
    private final int player2Id = 2;
    private Player player2 = new Player(player2Id, team1Id, position2Id,
            "Player 2", "Nationality", date);
    private final int player3Id = 3;
    private Player player3 = new Player(player3Id, team2Id, position2Id,
            "Player 3", "Nationality", date);
    private final int player4Id = 4;
    private Player player4 = new Player(player4Id, team2Id, position1Id,
            "Player 4", "Nationality", date);
    private final int player5Id = 5;
    private Player player5 = new Player(player5Id, team1Id, position2Id,
            "Player 5", "Nationality", date);
    private final int unExistingPlayerId = 6;
    private Player unExistingPlayer = new Player(unExistingPlayerId, team1Id, position1Id,
            "Un Existing Player", "Nationality 4", date);

    private List<Player> team1Players = Arrays.asList(player1, player2, player5);
    private List<Player> position1Players = Arrays.asList(player1, player4);
    private List<Player> positions2Players = Arrays.asList(player2, player3, player5);

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
        repository.store(player4);
        repository.store(player5);
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
     * Assert that the given lists are equal.
     *
     * @param expectedList   expected List of players
     * @param actualList     actual List of players
     * @param checkTeam      whatever the player team should be checked
     * @param checkPositions whatever the player positions should be checked
     */
    private void assertList(List<Player> expectedList, List<Player> actualList,
                            boolean checkTeam, boolean checkPositions) {
        assertEquals(expectedList.size(), actualList.size());
        int count = 0;
        for (Player expected : expectedList) {
            for (Player actual : actualList) {
                if (expected.getId().equals(actual.getId())) {
                    this.assertPlayer(expected, actual, checkTeam, checkPositions);
                    count++;
                }
            }
        }
        assertEquals(expectedList.size(), count);
    }

    /**
     * Assert that the given player data is valid.
     *
     * @param compare       actual player
     * @param player        player to bo compared
     * @param checkTeam     whatever the player team should be asserted
     * @param checkPosition whatever the player position should be asserted
     */
    private void assertPlayer(Player compare, Player player,
                              boolean checkTeam, boolean checkPosition) {
        assertTrue(compare.same(player));
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
        this.assertList(Arrays.asList(player1, player2, player3, player4, player5),
                players, true, true);
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
     * Test that the onUpdateSuccess method will onUpdateSuccess the player data.
     */
    @Test
    public void testUpdate() {
        String name = "Player Updated";
        player2.setName(name);
        boolean result = repository.update(player2);
        assertTrue(result);
        assertEquals(NUMBER_OF_PLAYERS, repository.count());
        Player player = repository.get(player2Id);
        this.assertPlayer(player2, player, false, false);
    }

    /**
     * Test that onUpdateSuccess method will return false when updating the record in the database failed.
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
        boolean result = repository.delete(player3Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_PLAYERS - 1, repository.count());
        assertNull(repository.get(player3Id));
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
        this.assertList(team1Players, players, false, true);
    }

    /**
     * Test that getPositionPlayers will return all players that plays on the given position.
     */
    @Test
    public void testGetPositionPlayers() {
        List<Player> players = repository.getPositionsPlayers(position1Id);
        this.assertList(position1Players, players, true, false);
    }

    /**
     * Test the behavior when getPositionsPlayers is called with empty arrays of positions ids.
     */
    @Test
    public void testGetPositionsPlayersCalledWithEmptyArraysOfPositions() {
        List<Player> players = repository.getPositionsPlayers(new int[0]);
        assertEquals(0, players.size());
    }

    /**
     * Test the behavior when getPositionsPlayers is called with one player to exclude
     * and one position id.
     */
    @Test
    public void testGetPositionsPlayersWithOnePlayerToExcludeAndOnePositionId() {
        List<Player> positionPlayers = new ArrayList<>(position1Players);
        final int index = positionPlayers.size() - 1;
        int[] playersToExclude = {positionPlayers.get(index).getId()};
        positionPlayers.remove(index);
        List<Player> players = repository.getPositionsPlayers(playersToExclude, position1Id);
        this.assertList(positionPlayers, players, true, true);
    }

    /**
     * Test the behavior when getPositionsPlayers is called with multiple players to exclude and
     * one position id.
     */
    @Test
    public void testGetPositionsPlayersCalledWithMultiplePlayersToExcludeAndOnePosition() {
        List<Player> positionsPlayers = new ArrayList<>(positions2Players);
        final Player player1 = positionsPlayers.get(0);
        final Player player2 = positionsPlayers.get(1);
        int[] playersToExclude = {player1.getId(), player2.getId()};
        positionsPlayers.remove(player1);
        positionsPlayers.remove(player2);
        List<Player> players = repository.getPositionsPlayers(playersToExclude, position2Id);
        this.assertList(positionsPlayers, players, true, true);
    }

    /**
     * Test the behavior when getPositionsPlayers is called with zero players to exclude
     * and multiple positions ids.
     */
    @Test
    public void testGetPositionsPlayersCalledWithZeroPlayersToExcludeAndMultiplePositions() {
        List<Player> positionsPlayers = new ArrayList<>(position1Players);
        positionsPlayers.addAll(positions2Players);
        List<Player> players = repository.getPositionsPlayers(new int[0], position1Id, position2Id);
        this.assertList(positionsPlayers, players, true, true);
    }
}