package com.android.finki.mpip.footballdreamteam.database.repository;

import android.app.Application;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Team;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Borce on 31.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TeamRepositoryTest {

    private TeamRepository repository;

    private final int NUMBER_OF_TEAMS = 3;
    private final int team1Id = 1;
    private final int team2Id = 2;
    private final int team3Id = 3;
    private final int unExistingTeamId = 4;
    private Team team1 = new Team(team1Id, "Team 1", "TM1", "20000$");
    private Team team2 = new Team(team2Id, "Team 2", "TM2", "30000$");
    private Team team3 = new Team(team3Id, "Team 3", "TM3", "40000$");
    private Team unExistingTeam = new Team(unExistingTeamId, "Team 4", "TM4", "40000$");

    @Before
    public void setup() {
        Application application = RuntimeEnvironment.application;
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(application.getBaseContext());
        repository = new TeamRepository(application.getBaseContext(), dbHelper);
        repository.open();
        repository.store(team1);
        repository.store(team2);
        repository.store(team3);
    }

    @After
    public void close() {
        repository.close();
    }

    /**
     * Test that the getAll method return all teams int he database.
     */
    @Test
    public void testGetAll() {
        List<Team> teams = repository.getAll();
        assertNotNull(teams);
        assertEquals(NUMBER_OF_TEAMS, teams.size());
        int count = 0;
        for (Team expected : Arrays.asList(team1, team2, team3)) {
            for (Team actual : teams) {
                if (expected.equals(actual)) {
                    assertTrue(expected.same(actual));
                    count++;
                }
            }
        }
        assertEquals(NUMBER_OF_TEAMS, count);
    }

    /**
     * Test that the get method returns the existing team.
     */
    @Test
    public void testGet() {
        Team team = repository.get(team1Id);
        assertTrue(team1.same(team));
    }

    /**
     * Test that the get method return null on un existing team id.
     */
    @Test
    public void testGetOnUnExistingId() {
        Team team = repository.get(unExistingTeamId);
        assertNull(team);
    }

    /**
     * Test that hte getByName method returns the existing user.
     */
    @Test
    public void testGetByName() {
        String name = team2.getName();
        Team team = repository.getByName(name);
        assertTrue(team2.same(team));
    }

    /**
     * Test that the getByName returns null whe the name don't exists.
     */
    @Test
    public void testGetByNameOnUnExistingName() {
        Team team = repository.getByName(unExistingTeam.getName());
        assertNull(team);
    }

    /**
     * Test that the count method returns the number of user in the database.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_TEAMS, repository.count());
    }

    /**
     * Test the store method will save a new team in the databse.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingTeam);
        assertTrue(result);
        assertEquals(NUMBER_OF_TEAMS + 1, repository.count());
        Team team = repository.get(unExistingTeamId);
        assertTrue(unExistingTeam.same(team));
    }

    /**
     * Test that store method will return false when storing the record in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(team1));
        assertEquals(NUMBER_OF_TEAMS, repository.count());
    }

    /**
     * Test that the onUpdateSuccess method will onUpdateSuccess the team data.
     */
    @Test
    public void testUpdate() {
        String name = "Team updated";
        team3.setName(name);
        boolean result = repository.update(team3);
        assertTrue(result);
        assertEquals(NUMBER_OF_TEAMS, repository.count());
        Team team = repository.get(team3Id);
        assertTrue(team3.same(team));
    }

    /**
     * Test that the onUpdateSuccess method will return false when updating the record
     * in the database failed.
     */
    @Test
    public void testFailedUpdate() {
        assertFalse(repository.update(unExistingTeam));
        assertEquals(NUMBER_OF_TEAMS, repository.count());
    }

    /**
     * Test that the delete method will removeLike the team from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(team1Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_TEAMS - 1, repository.count());
        assertNull(repository.get(team1Id));
    }

    /**
     * Test that delete method will return false when deleting the record
     * in the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingTeamId));
        assertEquals(NUMBER_OF_TEAMS, repository.count());
    }
}
