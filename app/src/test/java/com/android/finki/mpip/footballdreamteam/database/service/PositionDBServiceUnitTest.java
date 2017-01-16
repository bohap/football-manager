package com.android.finki.mpip.footballdreamteam.database.service;

import com.android.finki.mpip.footballdreamteam.database.repository.PositionRepository;
import com.android.finki.mpip.footballdreamteam.exception.PositionException;
import com.android.finki.mpip.footballdreamteam.exception.PrimaryKeyConstraintException;
import com.android.finki.mpip.footballdreamteam.exception.UniqueFieldConstraintException;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 01.08.2016.
 */
public class PositionDBServiceUnitTest {

    @Mock
    private PositionRepository repository;

    private PositionDBService service;

    private final int positionId = 1;
    private Position position = new Position(positionId, "Position 1");

    private Position keeper = new Position(1, "Keeper");
    private Position centreBack = new Position(2, "Centre Back");
    private Position leftBack = new Position(3, "Left Back");
    private Position rightBack = new Position(4, "Right Back");
    private Position defensiveMidfield = new Position(5, "Defensive Midfield");
    private Position centreMidfield = new Position(6, "Centre Midfield");
    private Position attackingMidfield = new Position(7, "Attacking Midfield");
    private Position rightWing = new Position(8, "Right Wing");
    private Position leftWing = new Position(9, "Left Wing");
    private Position centreForward = new Position(10, "Centre Forward");
    private Position secondaryForward = new Position(11, "Secondary Forward");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        service = new PositionDBService(repository);
    }

    /**
     * Init the mocks to return specific values on method calls.
     */
    private void initMocks() {
        when(repository.getByName("keeper")).thenReturn(keeper);
        when(repository.getByName("centre back")).thenReturn(centreBack);
        when(repository.getByName("left back")).thenReturn(leftBack);
        when(repository.getByName("right back")).thenReturn(rightBack);
        when(repository.getByName("defensive midfield")).thenReturn(defensiveMidfield);
        when(repository.getByName("centre midfield")).thenReturn(centreMidfield);
        when(repository.getByName("attacking midfield")).thenReturn(attackingMidfield);
        when(repository.getByName("right wing")).thenReturn(rightWing);
        when(repository.getByName("left wing")).thenReturn(leftWing);
        when(repository.getByName("centre forward")).thenReturn(centreForward);
        when(repository.getByName("secondary forward")).thenReturn(secondaryForward);
    }

    /**
     * Test that exists method returns true on existing id.
     */
    @Test
    public void testExists() {
        when(repository.get(positionId)).thenReturn(position);
        assertTrue(service.exists(positionId));
    }

    /**
     * Test that exists method returns false on un existing id.
     */
    @Test
    public void testNotExists() {
        when(repository.get(positionId)).thenReturn(null);
        assertFalse(service.exists(positionId));
    }

    /**
     * Test the behavior on exists method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExistsOnNull() {
        service.exists(null);
    }

    /**
     * Test the behavior on store method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNull() {
        service.store(null);
    }

    /**
     * Test the behavior on store method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnInvalidId() {
        service.store(new Position(0, ""));
    }

    /**
     * Test the behavior on store method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStoreOnNullName() {
        service.store(new Position(1, null));
    }

    /**
     * Test the behavior on store method called with existing id.
     */
    @Test(expected = PrimaryKeyConstraintException.class)
    public void testStoreOnExistingId() {
        when(repository.get(positionId)).thenReturn(position);
        service.store(position);
    }

    /**
     * Test the behavior on store method called with existing name.
     */
    @Test(expected = UniqueFieldConstraintException.class)
    public void testStoreOnExistingName() {
        when(repository.get(positionId)).thenReturn(null);
        when(repository.getByName(position.getName())).thenReturn(position);
        service.store(position);
    }

    /**
     * Test the behavior on store method when storing the record in the database failed.
     */
    @Test(expected = PositionException.class)
    public void testFailedStore() {
        when(repository.get(positionId)).thenReturn(null);
        when(repository.getByName(position.getName())).thenReturn(null);
        when(repository.store(position)).thenReturn(false);
        service.store(position);
    }

    /**
     * Test the behavior on store method when storing the record in the database is successful.
     */
    @Test
    public void testSuccessStore() {
        when(repository.get(positionId)).thenReturn(null);
        when(repository.getByName(position.getName())).thenReturn(null);
        when(repository.store(position)).thenReturn(true);
        Position stored = service.store(position);
        assertTrue(position.same(stored));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNull() {
        service.update(null);
    }

    /**
     * Test the behavior on onUpdateSuccess method called with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnInvalidId() {
        service.update(new Position(0, ""));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnNullName() {
        service.update(new Position(1, null));
    }

    /**
     * Test the behavior on onUpdateSuccess method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateOnUnExistingId() {
        when(repository.get(positionId)).thenReturn(null);
        service.update(position);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database failed.
     */
    @Test(expected = RuntimeException.class)
    public void testFailedUpdate() {
        when(repository.get(positionId)).thenReturn(position);
        when(repository.update(position)).thenReturn(false);
        service.update(position);
    }

    /**
     * Test the behavior on onUpdateSuccess method when updating the record in the database is successful.
     */
    @Test
    public void testSuccessUpdate() {
        when(repository.get(positionId)).thenReturn(position);
        when(repository.update(position)).thenReturn(true);
        Position updated = service.update(position);
        assertTrue(position.same(updated));
    }

    /**
     * Test the behavior on delete method called with un existing id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnUnExistingId() {
        when(repository.get(positionId)).thenReturn(null);
        service.delete(positionId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database failed.
     */
    @Test(expected = RuntimeException.class)
    public void testFailedDelete() {
        when(repository.get(positionId)).thenReturn(position);
        when(repository.delete(positionId)).thenReturn(false);
        service.delete(positionId);
    }

    /**
     * Test the behavior on delete method when deleting the record from the database is successful.
     */
    @Test
    public void testSuccessDelete() {
        when(repository.get(positionId)).thenReturn(position);
        when(repository.delete(positionId)).thenReturn(true);
        service.delete(positionId);
    }

    /**
     * Test the behavior on delete method called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteOnNull() {
        service.delete(null);
    }

    /**
     * Test that getGoalkeeperId will return the id of the position keeper.
     */
    @Test
    public void testGetGoalkeeperId() {
        int id = service.getGoalkeeperId();
        assertEquals(keeper.getId().intValue(), id);
    }

    /**
     * Test that getCentreBack will return the id of the position centre back.
     */
    @Test
    public void testGetCentreBackId() {
        int id = service.getCentreBackId();
        assertEquals(centreBack.getId().intValue(), id);
    }

    /**
     * Test that getLeftBack will return the id of the position left back.
     */
    @Test
    public void testGetLeftBackId() {
        int id = service.getLeftBackId();
        assertEquals(leftBack.getId().intValue(), id);
    }

    /**
     * Test that getRightBackID will return the id of the position right back.
     */
    @Test
    public void testGetRightBackId() {
        int id = service.getRightBackId();
        assertEquals(rightBack.getId().intValue(), id);
    }

    /**
     * Test that getDefensiveMidfield will return the id of the position defensive midfield.
     */
    @Test
    public void testGetDefensiveMidfieldId() {
        int id = service.getDefensiveMidfieldId();
        assertEquals(defensiveMidfield.getId().intValue(), id);
    }

    /**
     * Test that getCentreMidfieldId will return the id of the position centre midfield.
     */
    @Test
    public void testGetCentreMidfieldId() {
        int id = service.getCentreMidfieldId();
        assertEquals(centreMidfield.getId().intValue(), id);
    }

    /**
     * Test that getAttackingMidfieldId will return the id of the position attacking midfield.
     */
    @Test
    public void testGetAttackingMidfieldId() {
        int id = service.getAttackingMidfieldId();
        assertEquals(attackingMidfield.getId().intValue(), id);
    }

    /**
     * Test that getLeftWingId will return the id of the position left wing.
     */
    @Test
    public void testGetLeftWingId() {
        int id = service.getLeftWingId();
        assertEquals(leftWing.getId().intValue(), id);
    }

    /**
     * Test that getRightWingId will return the id of the position right wing.
     */
    @Test
    public void testGetRightWingId() {
        int id = service.getRightWingId();
        assertEquals(rightWing.getId().intValue(), id);
    }

    /**
     * Test that getCentreForwardId will return the id of the position centre forward.
     */
    @Test
    public void testGetCentreForwardId() {
        int id = service.getCentreForwardId();
        assertEquals(centreForward.getId().intValue(), id);
    }

    /**
     * Test that getSecondaryForwardId will return the id of the position secondary forward.
     */
    @Test
    public void testGetSecondaryForwardId() {
        int id = service.getSecondaryForwardId();
        assertEquals(secondaryForward.getId().intValue(), id);
    }

    /**
     * Assert that the positions ids are correctly mapped into a array of ids.
     *
     * @param positions List of positions
     * @param ids       array of positions ids
     */
    private void assertIds(List<Position> positions, int[] ids) {
        assertEquals(positions.size(), ids.length);
        for (int i = 0; i < ids.length; i++) {
            assertEquals(positions.get(i).getId().intValue(), ids[i]);
        }
    }

    /**
     * Test that getDefendersIds will return all position that are in the defense.
     */
    @Test
    public void testGetDefendersIds() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, "right back"));
        positions.add(new Position(2, "centre back"));
        positions.add(new Position(3, "left back"));
        when(repository.searchByName("back")).thenReturn(positions);
        this.assertIds(positions, service.getDefendersIds());
    }

    /**
     * Test that getMidfieldersIsd will return all position that are in the midfield.
     */
    @Test
    public void testGetMidfieldersIds() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, "centre midfield"));
        positions.add(new Position(2, "right wing"));
        positions.add(new Position(3, "left wing"));
        when(repository.searchByName("midfield", "wing")).thenReturn(positions);
        this.assertIds(positions, service.getMidfieldersIds());
    }

    /**
     * Test that getForwardIds will return all position that are in the forward.
     */
    @Test
    public void testGetForwardIds() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, "centre forward"));
        positions.add(new Position(2, "secondary forward"));
        when(repository.searchByName("forward")).thenReturn(positions);
        this.assertIds(positions, service.getAttackersIds());
    }

    /**
     * Test that mapPositions correctly maps the POSITION_TYPE with position id.
     */
    @Test
    public void testMapPositions() {
        Map<PositionUtils.POSITION_TYPE, Integer> map = service.mapPositions();
        assertNotNull(map);
        assertEquals(keeper.getId(), map.get(PositionUtils.POSITION_TYPE.KEEPER));
        assertEquals(centreBack.getId(), map.get(PositionUtils.POSITION_TYPE.CENTRE_BACK));
        assertEquals(leftBack.getId(), map.get(PositionUtils.POSITION_TYPE.LEFT_BACK));
        assertEquals(rightBack.getId(), map.get(PositionUtils.POSITION_TYPE.RIGHT_BACK));
        assertEquals(defensiveMidfield.getId(), map.get(PositionUtils.POSITION_TYPE.DEFENSIVE_MIDFIELD));
        assertEquals(centreMidfield.getId(), map.get(PositionUtils.POSITION_TYPE.CENTRE_MIDFIELD));
        assertEquals(attackingMidfield.getId(), map.get(PositionUtils.POSITION_TYPE.ATTACKING_MIDFIELD));
        assertEquals(leftWing.getId(), map.get(PositionUtils.POSITION_TYPE.LEFT_WING));
        assertEquals(rightWing.getId(), map.get(PositionUtils.POSITION_TYPE.RIGHT_WING));
        assertEquals(centreForward.getId(), map.get(PositionUtils.POSITION_TYPE.CENTRE_FORWARD));
        assertEquals(secondaryForward.getId(), map.get(PositionUtils.POSITION_TYPE.SECONDARY_FORWARD));
    }
}
