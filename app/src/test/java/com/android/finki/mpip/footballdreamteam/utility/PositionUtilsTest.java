package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 17.08.2016.
 */
@Ignore
public class PositionUtilsTest {

    private Position keeper = new Position(1, "Keeper");
    private Position centreBack = new Position(2, "Centre Back");
    private Position rightBack = new Position(3, "Right Back");
    private Position leftBack = new Position(4, "Left Back");
    private Position defensiveMidfield = new Position(5, "Defensive Midfield");
    private Position centreMidfield = new Position(6, "Centre Midfield");
    private Position attackingMidfield = new Position(7, "Attacking Midfield");
    private Position rightWing = new Position(8, "Right Wing");
    private Position leftWing = new Position(9, "Left Wing");
    private Position centreForward = new Position(10, "Centre Forward");
    private Position secondaryForward = new Position(11, "Secondary Forward");
    private Map<PositionUtils.POSITION, Integer> mapped;

    private PositionUtils utils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        utils = new PositionUtils();
        this.initMap();
    }

    /**
     * Put all position in the map.
     */
    private void initMap() {
        mapped = new HashMap<>();
        mapped.put(PositionUtils.POSITION.KEEPER, keeper.getId());
        mapped.put(PositionUtils.POSITION.CENTRE_BACK, centreBack.getId());
        mapped.put(PositionUtils.POSITION.LEFT_BACK, leftBack.getId());
        mapped.put(PositionUtils.POSITION.RIGHT_BACK, rightBack.getId());
        mapped.put(PositionUtils.POSITION.DEFENSIVE_MIDFIELD, defensiveMidfield.getId());
        mapped.put(PositionUtils.POSITION.CENTRE_MIDFIELD, centreMidfield.getId());
        mapped.put(PositionUtils.POSITION.ATTACKING_MIDFIELD, attackingMidfield.getId());
        mapped.put(PositionUtils.POSITION.LEFT_WING, leftWing.getId());
        mapped.put(PositionUtils.POSITION.RIGHT_WING, rightWing.getId());
        mapped.put(PositionUtils.POSITION.CENTRE_FORWARD, centreForward.getId());
        mapped.put(PositionUtils.POSITION.SECONDARY_FORWARD, secondaryForward.getId());
    }

    /**
     * Test that isKeeper returns true when the position is keeper.
     */
    @Test
    public void testIsKeeper() {
        assertTrue(utils.isKeeper(keeper));
    }

    /**
     * Test that isKeeper method return false when called with null param.
     */
    @Test
    public void testIsKeeperOnNull() {
        assertFalse(utils.isKeeper(null));
    }

    /**
     * Test that isKeeper method returns false when the position is not a keeper.
     */
    @Test
    public void testIsNotKeeper() {
        assertFalse(utils.isKeeper(centreBack));
    }

    /**
     * Test that isKeeper returns true when the position is centre back.
     */
    @Test
    public void testIsCentreBack() {
        assertTrue(utils.isCentreBack(centreBack));
    }

    /**
     * Test that isCentreBack returns false when called with null param.
     */
    @Test
    public void testIsCentreBackOnUnExistingId() {
        assertFalse(utils.isCentreBack(null));
    }

    /**
     * Test that isCentreBack method returns false when the position is not a centre back.
     */
    @Test
    public void testIsNotCentreBack() {
        assertFalse(utils.isCentreBack(keeper));
    }

    /**
     * Test that isRightBack returns true when the position is right back.
     */
    @Test
    public void testIsRightBack() {
        assertTrue(utils.isRightBack(rightBack));
    }

    /**
     * Test that isRightBack returns false when called with null param.
     */
    @Test
    public void testIsRightBackOnUnExistingId() {
        assertFalse(utils.isRightBack(null));
    }

    /**
     * Test that isRightBack method returns false when the position is not a right back.
     */
    @Test
    public void testIsNotRightBack() {
        assertFalse(utils.isRightBack(secondaryForward));
    }

    /**
     * Test that isLefBack returns true when the position is left back.
     */
    @Test
    public void testIsLeftBack() {
        assertTrue(utils.isLeftBack(leftBack));
    }

    /**
     * Test that isLeftBack returns false when called with null param.
     */
    @Test
    public void testIsLeftBackOnUnExistingId() {
        assertFalse(utils.isLeftBack(null));
    }

    /**
     * Test that isLeftBack method returns false when the position is not a left back.
     */
    @Test
    public void testIsNotLeftBack() {
        assertFalse(utils.isLeftBack(attackingMidfield));
    }

    /**
     * Test that isDefensiveMidfield returns true when the position is defensive midfield.
     */
    @Test
    public void testIsDefensiveMidfield() {
        assertTrue(utils.isDefensiveMidfield(defensiveMidfield));
    }

    /**
     * Test that isDefensiveMidfield returns false when called with null param.
     */
    @Test
    public void testIsDefensiveMidfieldOnUnExistingId() {
        assertFalse(utils.isDefensiveMidfield(null));
    }

    /**
     * Test that isDefensiveMidfield method returns false when the position is
     * not a defensive midfield.
     */
    @Test
    public void testIsNotDefensiveMidfield() {
        assertFalse(utils.isDefensiveMidfield(centreMidfield));
    }

    /**
     * Test that isCentreMidfield returns true when the position is centre midfield.
     */
    @Test
    public void testIsCentreMidfield() {
        assertTrue(utils.isCentreMidfield(centreMidfield));
    }

    /**
     * Test that isCentreMidfield returns false when called with null param.
     */
    @Test
    public void testIsCentreMidfieldOnUnExistingId() {
        assertFalse(utils.isCentreMidfield(null));
    }

    /**
     * Test that isCentreMidfield method returns false when the position is
     * not a centre midfield.
     */
    @Test
    public void testIsNotCentreMidfield() {
        assertFalse(utils.isCentreMidfield(leftWing));
    }

    /**
     * Test that isAttackingMidfield returns true when the position is attacking midfield.
     */
    @Test
    public void testIsAttackingMidfield() {
        assertTrue(utils.isAttackingMidfield(attackingMidfield));
    }

    /**
     * Test that isAttackingMidfield returns false when called with null param.
     */
    @Test
    public void testIsAttackingMidfieldOnUnExistingId() {
        assertFalse(utils.isAttackingMidfield(null));
    }

    /**
     * Test that isAttackingMidfield method returns false when the position is
     * not a attacking midfield.
     */
    @Test
    public void testIsNotAttackingMidfield() {
        assertFalse(utils.isAttackingMidfield(centreBack));
    }

    /**
     * Test that isRightWing returns true when the position is right wing.
     */
    @Test
    public void testIsRightWing() {
        assertTrue(utils.isRightWing(rightWing));
    }

    /**
     * Test that isRightWing returns false when called with null param.
     */
    @Test
    public void testIsRightWingOnUnExistingId() {
        assertFalse(utils.isRightWing(null));
    }

    /**
     * Test that isRightWing method returns false when the position is
     * not a right wing.
     */
    @Test
    public void testIsNotRightWing() {
        assertFalse(utils.isRightWing(leftWing));
    }

    /**
     * Test that isLeftWing returns true when the position is left wing.
     */
    @Test
    public void testIsLeftWing() {
        assertTrue(utils.isLeftWing(leftWing));
    }

    /**
     * Test that isLeftWing returns false when called with null param.
     */
    @Test
    public void testIsLeftWingOnUnExistingId() {
        assertFalse(utils.isLeftWing(null));
    }

    /**
     * Test that isLeftWing method returns false when the position is not a left wing.
     */
    @Test
    public void testIsNotLeftWing() {
        assertFalse(utils.isLeftWing(leftBack));
    }

    /**
     * Test that isCentreForward returns true when the position is centre forward.
     */
    @Test
    public void testIsCentreForward() {
        assertTrue(utils.isCentreForward(centreForward));
    }

    /**
     * Test that isCentreForward returns false when called with null param.
     */
    @Test
    public void testIsCentreForwardOnUnExistingId() {
        assertFalse(utils.isCentreForward(null));
    }

    /**
     * Test that isCentreForward method returns false when the position is
     * not a centre forward.
     */
    @Test
    public void testIsNotCentreForward() {
        assertFalse(utils.isCentreForward(rightBack));
    }

    /**
     * Test that isSecondaryForward returns true when the position is secondary forward.
     */
    @Test
    public void testIsSecondaryForward() {
        assertTrue(utils.isSecondaryForward(secondaryForward));
    }

    /**
     * Test that isSecondaryForward returns false when called with null param.
     */
    @Test
    public void testIsSecondaryForwardOnUnExistingId() {
        assertFalse(utils.isSecondaryForward(null));
    }

    /**
     * Test that isSecondaryForward method returns false when the position is
     * not a secondary forward.
     */
    @Test
    public void testIsNotSecondaryForward() {
        assertFalse(utils.isSecondaryForward(defensiveMidfield));
    }

    /**
     * Test that isInDefence will return true when the position is centre back.
     */
    @Test
    public void testIsInDefenceForCentreBack() {
        assertTrue(utils.isInDefence(centreBack));
    }

    /**
     * Test that isInDefence will return true when the position is left back.
     */
    @Test
    public void testIsInDefenceForLeftBack() {
        assertTrue(utils.isInDefence(leftBack));
    }

    /**
     * Test that isInDefence will return true when the position is right back.
     */
    @Test
    public void testIsInDefenceForRightBack() {
        assertTrue(utils.isInDefence(rightBack));
    }

    /**
     * Test that isInMidfield will return true when the position is defensive midfield.
     */
    @Test
    public void testIsInMidfieldForDefensiveMidfield() {
        assertTrue(utils.isInMidfield(defensiveMidfield));
    }

    /**
     * Test that isInMidfield will return true when the position is centre midfield.
     */
    @Test
    public void testIsInMidfieldForCentreMidfield() {
        assertTrue(utils.isInMidfield(centreMidfield));
    }

    /**
     * Test that isInMidfield will return true when the position is attacking midfield.
     */
    @Test
    public void testIsInMidfieldForAttackingMidfield() {
        assertTrue(utils.isInMidfield(attackingMidfield));
    }

    /**
     * Test that isInMidfield will return true when the position is right wing.
     */
    @Test
    public void testIsInMidfieldForRightWing() {
        assertTrue(utils.isInMidfield(rightWing));
    }

    /**
     * Test that isInMidfield will return true when the position is left wing.
     */
    @Test
    public void testIsInMidfieldForLeftWing() {
        assertTrue(utils.isInMidfield(leftWing));
    }

    /**
     * Test that isInAttack will return true when the position is centre forward.
     */
    @Test
    public void testisInAttackForCentreForward() {
        assertTrue(utils.isInAttack(centreForward));
    }

    /**
     * Test that isInAttack will return true when the position is secondary forward.
     */
    @Test
    public void testisInAttackForSecondaryForward() {
        assertTrue(utils.isInAttack(secondaryForward));
    }

    /**
     * Test the behavior when getPosition is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionWithNullPosition() {
        utils.getPosition(null);
    }

    /**
     * Test the behavior when getPosition is called with null position name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionWithNullPositionName() {
        utils.getPosition(new Position());
    }

    /**
     * Test that getPosition will return KEEPER when the given positions is keeper.
     */
    @Test
    public void testGetPositionForKeeper() {
        assertEquals(PositionUtils.POSITION.KEEPER, utils.getPosition(keeper));
    }

    /**
     * Test that getPosition will return CENTRE_BACK when the given positions is centre back.
     */
    @Test
    public void testGetPositionForCentreBack() {
        assertEquals(PositionUtils.POSITION.CENTRE_BACK, utils.getPosition(centreBack));
    }

    /**
     * Test that getPosition will return RIGHT_BACK when the given positions is right back.
     */
    @Test
    public void testGetPositionForRightBack() {
        assertEquals(PositionUtils.POSITION.RIGHT_BACK, utils.getPosition(rightBack));
    }

    /**
     * Test that getPosition will return LEFT_BACK when the given positions is left back.
     */
    @Test
    public void testGetPositionForLeftBack() {
        assertEquals(PositionUtils.POSITION.LEFT_BACK, utils.getPosition(leftBack));
    }

    /**
     * Test that getPosition will return DEFENSIVE_MIDFIELD when the given positions
     * is defensive midfield.
     */
    @Test
    public void testGetPositionForDefensiveMidfield() {
        assertEquals(PositionUtils.POSITION.DEFENSIVE_MIDFIELD, utils.getPosition(defensiveMidfield));
    }

    /**
     * Test that getPosition will return CENTRE_MIDFIELD when the given positions
     * is defensive midfield.
     */
    @Test
    public void testGetPositionForCentreMidfield() {
        assertEquals(PositionUtils.POSITION.CENTRE_MIDFIELD, utils.getPosition(centreMidfield));
    }

    /**
     * Test that getPosition will return ATTACKING_MIDFIELD when the given positions
     * is attacking midfield.
     */
    @Test
    public void testGetPositionForAttackingMidfield() {
        assertEquals(PositionUtils.POSITION.ATTACKING_MIDFIELD, utils.getPosition(attackingMidfield));
    }

    /**
     * Test that getPosition will return RIGHT_WING when the given positions is rigth wing.
     */
    @Test
    public void testGetPositionForRightWing() {
        assertEquals(PositionUtils.POSITION.RIGHT_WING, utils.getPosition(rightWing));
    }

    /**
     * Test that getPosition will return LEFT_WING when the given positions is left wing.
     */
    @Test
    public void testGetPositionForLeftWing() {
        assertEquals(PositionUtils.POSITION.LEFT_WING, utils.getPosition(leftWing));
    }

    /**
     * Test that getPosition will return CENTRE_FORWARD when the given positions
     * is centre forward.
     */
    @Test
    public void testGetPositionForCentreForward() {
        assertEquals(PositionUtils.POSITION.CENTRE_FORWARD, utils.getPosition(centreForward));
    }

    /**
     * Test that getPosition will return SECONDARY_FORWARD when the given positions
     * is secondary forward.
     */
    @Test
    public void testGetPositionForSecondaryForward() {
        assertEquals(PositionUtils.POSITION.SECONDARY_FORWARD, utils.getPosition(secondaryForward));
    }

    /**
     * Test the behavior when getPosition is called with unknown position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionForUnknownPosition() {
        utils.getPosition(new Position(1, "Position"));
    }

    /**
     * Test the behavior when getPositionPlace is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionNameWithNullPosition() {
        utils.getPositionPlace(null);
    }

    /**
     * Test the behavior when getPositionPlace is called with null position name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionPlaceWithNullPositionName() {
        utils.getPositionPlace(new Position());
    }

    /**
     * Test that getPositionPlace method will return KEEPERS when the id is keeper.
     */
    @Test
    public void testGetPositionPlaceForKeeper() {
        assertEquals(PositionUtils.POSITION_PLACE.KEEPERS, utils.getPositionPlace(keeper));
    }

    /**
     * Test that getPositionPlace method will return DEFENDERS when the id is defender.
     */
    @Test
    public void testGetPositionPlaceForDefender() {
        assertEquals(PositionUtils.POSITION_PLACE.DEFENDERS, utils.getPositionPlace(rightBack));
    }

    /**
     * Test that getPositionPlace method will return MIDFIELDERS when the id is midfielder.
     */
    @Test
    public void testGetPositionPlaceForMidfielder() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS, utils.getPositionPlace(centreMidfield));
    }

    /**
     * Test that getPositionPlace method will return ATTACKERS when the id is attacker.
     */
    @Test
    public void testGetPositionPlaceForAttacker() {
        assertEquals(PositionUtils.POSITION_PLACE.ATTACKERS, utils.getPositionPlace(secondaryForward));
    }

    /**
     * Test the behavior on getPositionPlace called with unknown position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionPlaceForUnknownPosition() {
        utils.getPositionPlace(new Position(1, "Test"));
    }

    /**
     * Test that getPositionPlace will return KEEPERS when the position resource id is keeper.
     */
    @Test
    public void testGetPositionPlaceForKeeperResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.KEEPERS, utils.getPositionPlace(R.id.keeper));
    }

    /**
     * Test that getPositionPlace will return DEFENDERS when the position resource id
     * is left centre back.
     */
    @Test
    public void testGetPositionPlaceForLeftCentreBackResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.DEFENDERS,
                utils.getPositionPlace(R.id.leftCentreBack));
    }

    /**
     * Test that getPositionPlace will return DEFENDERS when the position resource id
     * is right centre back.
     */
    @Test
    public void testGetPositionPlaceForRightCentreBackResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.DEFENDERS,
                utils.getPositionPlace(R.id.rightCentreBack));
    }

    /**
     * Test that getPositionPlace will return DEFENDERS when the position resource id
     * is centre centre back.
     */
    @Test
    public void testGetPositionPlaceForCentreCentreBackResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.DEFENDERS,
                utils.getPositionPlace(R.id.centreCentreBack));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is left centre midfield.
     */
    @Test
    public void testGetPositionPlaceForLeftCentreMidfieldResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.leftCentreMidfield));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is right centre midfield.
     */
    @Test
    public void testGetPositionPlaceForRightCentreMidfieldResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.rightCentreMidfield));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is centre centre midfield.
     */
    @Test
    public void testGetPositionPlaceForCentreCentreMidfieldResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.centreCentreMidfield));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is left wing.
     */
    @Test
    public void testGetPositionPlaceForLeftWingResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.leftWing));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is right wing.
     */
    @Test
    public void testGetPositionPlaceForRightWingResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.rightWing));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is attacking midfield.
     */
    @Test
    public void testGetPositionPlaceForAttackingMidfieldResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.MIDFIELDERS,
                utils.getPositionPlace(R.id.attackingMidfield));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is left centre forward.
     */
    @Test
    public void testGetPositionPlaceForLeftCentreForwardResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.ATTACKERS,
                utils.getPositionPlace(R.id.leftCentreForward));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is right centre forward.
     */
    @Test
    public void testGetPositionPlaceForRightCentreForwardResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.ATTACKERS,
                utils.getPositionPlace(R.id.rightCentreForward));
    }

    /**
     * Test that getPositionPlace will return MIDFIELDERS when the position resource id
     * is centre centre forward.
     */
    @Test
    public void testGetPositionPlaceForCentreCentreForwardResourceId() {
        assertEquals(PositionUtils.POSITION_PLACE.ATTACKERS,
                utils.getPositionPlace(R.id.centreCentreForward));
    }

    /**
     * Test the behavior when getPositionPlace is called with unknown position resource id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionPlaceForUnknownPositionResourceId() {
        utils.getPositionPlace(R.id.content);
    }

    /**
     * Test the behavior on countPositions called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountPositionsWithNullParam() {
        utils.countPositions(null);
    }

    /**
     * Test that countPositions returns a map where the key is a positions enum and the value is
     * the number of ids for the position in the array.
     */
    @Test
    public void testCountPositions() {
        List<Position> positions = Arrays.asList(keeper, centreBack, centreBack, rightBack,
                leftBack, centreMidfield, centreMidfield, rightWing, leftWing,
                attackingMidfield, centreForward);
        Map<PositionUtils.POSITION, Integer> map = utils.countPositions(positions);
        assertNotNull(map);
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_BACK).intValue());
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_MIDFIELD).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.ATTACKING_MIDFIELD).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_WING).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_WING).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.CENTRE_FORWARD).intValue());
    }

    /**
     * Test that countPositions returns a map where the key is a positions enum and the value is
     * the number of ids for the position in the array when there is defensive midfield position.
     */
    @Test
    public void testCountPositionsWithDefensiveMidfield() {
        List<Position> positions = Arrays.asList(keeper, centreBack, centreBack, rightBack, leftBack, defensiveMidfield,
                centreMidfield, rightWing, leftWing, attackingMidfield, centreForward);
        Map<PositionUtils.POSITION, Integer> map = utils.countPositions(positions);
        assertNotNull(map);
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_BACK).intValue());
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_MIDFIELD).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.ATTACKING_MIDFIELD).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_WING).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_WING).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.CENTRE_FORWARD).intValue());
    }

    /**
     * Test that countPositions returns a map where the key is a positions enum and the value is
     * the number of ids for the position in the array when there is secondary forward position.
     */
    @Test
    public void testCountPositionsWithSecondaryForward() {
        List<Position> positions = Arrays.asList(keeper, centreBack, centreBack, rightBack,
                leftBack, centreMidfield, centreMidfield, rightWing, leftWing,
                centreForward, secondaryForward);
        Map<PositionUtils.POSITION, Integer> map = utils.countPositions(positions);
        assertNotNull(map);
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_BACK).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_BACK).intValue());
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_MIDFIELD).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.LEFT_WING).intValue());
        assertEquals(1, map.get(PositionUtils.POSITION.RIGHT_WING).intValue());
        assertEquals(2, map.get(PositionUtils.POSITION.CENTRE_FORWARD).intValue());
    }

    /**
     * Test the behavior when getShortName is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetShortNameWithNullPosition() {
        utils.getShortName(null);
    }

    /**
     * Test the behavior when getShortName is called with null position name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSHortNameWithNullPositionName() {
        utils.getShortName(new Position());
    }

    /**
     * Test that getShortName return the correct result when the position is keeper.
     */
    @Test
    public void testShortNameOnKeeper() {
        assertEquals("GK", utils.getShortName(keeper));
    }

    /**
     * Test that getShortName return the correct result when the position is centre back.
     */
    @Test
    public void testShortNameOnCentreBack() {
        assertEquals("CB", utils.getShortName(centreBack));
    }

    /**
     * Test that getShortName return the correct result when the position is left back.
     */
    @Test
    public void testShortNameOnLeftBack() {
        assertEquals("LB", utils.getShortName(leftBack));
    }

    /**
     * Test that getShortName return the correct result when the position is right back.
     */
    @Test
    public void testShortNameOnRightBack() {
        assertEquals("RB", utils.getShortName(rightBack));
    }

    /**
     * Test that getShortName return the correct result when the position is defensive midfield.
     */
    @Test
    public void testShortNameOnDefensiveMidfield() {
        assertEquals("DM", utils.getShortName(defensiveMidfield));
    }

    /**
     * Test that getShortName return the correct result when the position is centre midfield.
     */
    @Test
    public void testShortNameOnCentreMidfield() {
        assertEquals("CM", utils.getShortName(centreMidfield));
    }

    /**
     * Test that getShortName return the correct result when the position is left wing.
     */
    @Test
    public void testShortNameOnLeftWing() {
        assertEquals("LW", utils.getShortName(leftWing));
    }

    /**
     * Test that getShortName return the correct result when the position is right wing.
     */
    @Test
    public void testShortNameOnRightWing() {
        assertEquals("RW", utils.getShortName(rightWing));
    }

    /**
     * Test that getShortName return the correct result when the position is attacking midfield.
     */
    @Test
    public void testShortNameOnAttackingMidfield() {
        assertEquals("AM", utils.getShortName(attackingMidfield));
    }

    /**
     * Test that getShortName return the correct result when the position is centre forward.
     */
    @Test
    public void testShortNameOnCentreForward() {
        assertEquals("CF", utils.getShortName(centreForward));
    }

    /**
     * Test that getShortName return the correct result when the position is secondary forward.
     */
    @Test
    public void testShortNameOnSecondaryForward() {
        assertEquals("SF", utils.getShortName(secondaryForward));
    }

    /**
     * Test getShortName return null when the position name can't be recognized.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShortNameOnUnExistingPosition() {
        utils.getShortName(new Position(214, "Position"));
    }

    /**
     * Test the behavior when getPositionId is caled with null map.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionIdWithNullMap() {
        utils.getPositionId(1, null);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id is keeper.
     */
    @Test
    public void testGetPositionIdForKeeperResourceId() {
        int id = utils.getPositionId(R.id.keeper, mapped);
        assertEquals(keeper.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is left centre back.
     */
    @Test
    public void testGetPositionIdForLeftCentreBackResourceId() {
        int id = utils.getPositionId(R.id.leftCentreBack, mapped);
        assertEquals(centreBack.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is right centre back.
     */
    @Test
    public void testGetPositionIdForRightCentreBackResourceId() {
        int id = utils.getPositionId(R.id.rightCentreBack, mapped);
        assertEquals(centreBack.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is centre centre back.
     */
    @Test
    public void testGetPositionIdForCentreCentreBackResourceId() {
        int id = utils.getPositionId(R.id.centreCentreBack, mapped);
        assertEquals(centreBack.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is left back.
     */
    @Test
    public void testGetPositionIdForLeftBackResourceId() {
        int id = utils.getPositionId(R.id.leftBack, mapped);
        assertEquals(leftBack.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is right back.
     */
    @Test
    public void testGetPositionIdForRightBackResourceId() {
        int id = utils.getPositionId(R.id.rightBack, mapped);
        assertEquals(rightBack.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is left centre midfield.
     */
    @Test
    public void testGetPositionIdForLeftCentreMidfieldResourceId() {
        int id = utils.getPositionId(R.id.leftCentreMidfield, mapped);
        assertEquals(centreMidfield.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is right centre midfield.
     */
    @Test
    public void testGetPositionIdForRightCentreMidfieldResourceId() {
        int id = utils.getPositionId(R.id.rightCentreMidfield, mapped);
        assertEquals(centreMidfield.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is centre centre midfield.
     */
    @Test
    public void testGetPositionIdForCentreCentreMidfieldResourceId() {
        int id = utils.getPositionId(R.id.centreCentreMidfield, mapped);
        assertEquals(centreMidfield.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is attacking midfield.
     */
    @Test
    public void testGetPositionIdForAttickingMidfieldResourceId() {
        int id = utils.getPositionId(R.id.attackingMidfield, mapped);
        assertEquals(attackingMidfield.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is left wing.
     */
    @Test
    public void testGetPositionIdForLeftWingResourceId() {
        int id = utils.getPositionId(R.id.leftWing, mapped);
        assertEquals(leftWing.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is right wing.
     */
    @Test
    public void testGetPositionIdForRightWingResourceId() {
        int id = utils.getPositionId(R.id.rightWing, mapped);
        assertEquals(rightWing.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is left centre forward.
     */
    @Test
    public void testGetPositionIdForLeftCentreForwardResourceId() {
        int id = utils.getPositionId(R.id.leftCentreForward, mapped);
        assertEquals(centreForward.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is right centre forward.
     */
    @Test
    public void testGetPositionIdForRightCentreForwardResourceId() {
        int id = utils.getPositionId(R.id.rightCentreForward, mapped);
        assertEquals(centreForward.getId().intValue(), id);
    }

    /**
     * Test the getPositionId will return the keeper id when the position resource id
     * is centre centre forward.
     */
    @Test
    public void testGetPositionIdForCentreCentreForwardResourceId() {
        int id = utils.getPositionId(R.id.centreCentreForward, mapped);
        assertEquals(centreForward.getId().intValue(), id);
    }

    /**
     * Test the behavior on getPositionId called with un existing position resource id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionIdOnUnknownPositionId() {
        utils.getPositionId(R.id.content, mapped);
    }
}