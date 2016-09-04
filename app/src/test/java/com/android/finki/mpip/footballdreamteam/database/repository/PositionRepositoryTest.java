package com.android.finki.mpip.footballdreamteam.database.repository;

import android.app.Application;
import android.os.Build;

import com.android.finki.mpip.footballdreamteam.BuildConfig;
import com.android.finki.mpip.footballdreamteam.database.MainSQLiteOpenHelper;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Borce on 31.07.2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class PositionRepositoryTest {

    private PositionRepository repository;

    private final int NUMBER_OF_POSITIONS = 3;
    private final int position1Id = 1;
    private final int position2Id = 2;
    private final int position3Id = 3;
    private final int unExistingPositionId = 4;
    private Position position1 = new Position(position1Id, "Position 1");
    private Position position2 = new Position(position2Id, "Position 2");
    private Position position3 = new Position(position3Id, "Position 3");
    private Position unExistingPosition = new Position(unExistingPositionId, "Position 4");

    /**
     * Create a new instance of PositionRepository, open the connection and seed the table.
     */
    @Before
    public void setup() {
        Application application = RuntimeEnvironment.application;
        MainSQLiteOpenHelper dbHelper = new MainSQLiteOpenHelper(application.getBaseContext());
        repository = new PositionRepository(application.getBaseContext(), dbHelper);
        repository.open();
        repository.store(position1);
        repository.store(position2);
        repository.store(position3);
    }

    /**
     * Close the database connection.
     */
    @After
    public void shutdown() {
        repository.close();
    }

    /**
     * Assert that the given position of players is valid.
     *
     * @param number expected number of positions
     * @param positions List of Position
     */
    private void assertPositions(int number, List<Position> positions) {
        assertEquals(number, positions.size());
        assertEquals(number, positions.size());
        int count = 0;
        for (Position position : positions) {
            if (position.getId().equals(position1Id) || position.getId().equals(position2Id)
                    || position.getId().equals(position3Id)) {
                count++;
            }
        }
        assertEquals(number, count);
    }

    /**
     * Assert that the given position data is valid.
     *
     * @param compare actual position
     * @param position Position to be compared
     */
    private void assertPosition(Position compare, Position position) {
        assertNotNull(position);
        assertEquals(compare.getId(), position.getId());
    }

    /**
     * Test that getAll method will return all positions sin the database.
     */
    @Test
    public void testGetAll() {
        List<Position> positions = repository.getAll();
        assertNotNull(positions);
        this.assertPositions(NUMBER_OF_POSITIONS, positions);
    }

    /**
     * Test that get method will return existing position.
     */
    @Test
    public void testGet() {
        Position position = repository.get(position1Id);
        this.assertPosition(position1, position);
    }

    /**
     * Test that get method will return null on un exiting id.
     */
    @Test
    public void testGetOnUnExistingId() {
        assertNull(repository.get(unExistingPositionId));
    }

    /**
     * Test that getByName will return a position when the name exists.
     */
    @Test
    public void testGetByName() {
        String name = position1.getName();
        Position position = repository.getByName(name);
        this.assertPosition(position1, position);
        assertEquals(name, position.getName());
    }

    /**
     * Test that getByName will not make difference between small and big letters.
     */
    @Test
    public void testGetByNameWIllNotMakeDifferenceOnLetterType() {
        String name = position1.getName();
        String testName = "";
        for (char c : name.toCharArray()) {
            if ((int) c % 2 == 0) {
                testName += Character.toUpperCase(c);
            } else {
                testName += Character.toLowerCase(c);
            }
        }
        Position position = repository.getByName(testName);
        this.assertPosition(position1, position);
        assertEquals(name, position.getName());
    }

    /**
     * Test that getByName will return null when the name don't exists.
     */
    @Test
    public void testGetByNameOnUnExistingName() {
        assertNull(repository.getByName(unExistingPosition.getName()));
    }

    /**
     * Test that count method will return then number of records in the database.
     */
    @Test
    public void testCount() {
        assertEquals(NUMBER_OF_POSITIONS, repository.count());
    }

    /**
     * Test that the store method will create a new position.
     */
    @Test
    public void testStore() {
        boolean result = repository.store(unExistingPosition);
        assertTrue(result);
        assertEquals(NUMBER_OF_POSITIONS + 1, repository.count());
        Position position = repository.get(unExistingPositionId);
        this.assertPosition(unExistingPosition, position);
    }

    /**
     * Test that store method will return false when storing the record in the database failed.
     */
    @Test
    public void testFailedStore() {
        assertFalse(repository.store(position1));
        assertEquals(NUMBER_OF_POSITIONS, repository.count());
    }

    /**
     * Test that the update method will update the position date
     */
    @Test
    public void testUpdate() {
        String name = "Position Updated";
        position1.setName(name);
        boolean result = repository.update(position1);
        assertTrue(result);
        assertEquals(NUMBER_OF_POSITIONS, repository.count());
        Position position = repository.get(position1Id);
        this.assertPosition(position1, position);
        assertEquals(name, position.getName());
    }

    /**
     * Test that update method will return false when updating the record in the database failed.
     */
    @Test
    public void testFailedSUpdate() {
        assertFalse(repository.update(unExistingPosition));
        assertEquals(NUMBER_OF_POSITIONS, repository.count());
    }

    /**
     * Test that the delete method will removeLike the position from the database.
     */
    @Test
    public void testDelete() {
        boolean result = repository.delete(position1Id);
        assertTrue(result);
        assertEquals(NUMBER_OF_POSITIONS - 1, repository.count());
        assertNull(repository.get(position1Id));
    }

    /**
     * Test that delete method will return false when deleting the record
     * from the database failed.
     */
    @Test
    public void testFailedDelete() {
        assertFalse(repository.delete(unExistingPositionId));
        assertEquals(NUMBER_OF_POSITIONS, repository.count());
    }

    /**
     * Test that searchByName method will search for position name, when the input param only
     * contains a fracture of the name.
     */
    @Test
    public void testSearchByName() {
        String name = "Position";
        List<Position> positions = repository.searchByName(name);
        this.assertPositions(NUMBER_OF_POSITIONS, positions);
    }

    /**
     * Test the searchByName method will not make difference between small and big latest.
     */
    @Test
    public void testSearchByNameWillNotMakeDifferenceOnLetterType() {
        String name = "positioN";
        List<Position> positions = repository.searchByName(name);
        this.assertPositions(NUMBER_OF_POSITIONS, positions);
    }

    /**
     * Test the searchByName method will work when we pass his multiple names.
     */
    @Test
    public void testSearchByNameWithMultipleNames() {
        String name = "test";
        Position position = new Position(unExistingPositionId, name);
        boolean store = repository.store(position);
        assertTrue(store);
        assertEquals(NUMBER_OF_POSITIONS + 1, repository.count());
        List<Position> positions = repository.searchByName("position", "test");
        assertEquals(NUMBER_OF_POSITIONS + 1, positions.size());
    }

    /**
     * Test that searchByName will not return all positions in the database.
     */
    @Test
    public void testSearchByNameWillNotReturnAllPositions() {
        String name = "test";
        Position position = new Position(unExistingPositionId, name);
        boolean store = repository.store(position);
        assertTrue(store);
        assertEquals(NUMBER_OF_POSITIONS + 1, repository.count());
        List<Position> positions = repository.searchByName("position");
        this.assertPositions(NUMBER_OF_POSITIONS, positions);
    }

    /**
     * Test the searchByName method will return empty list when a position can;t be find.
     */
    @Test
    public void testSearchByNameOnUnExistingName() {
        String name = "simple name";
        List<Position> positions = repository.searchByName(name);
        assertEquals(0, positions.size());
    }
}
