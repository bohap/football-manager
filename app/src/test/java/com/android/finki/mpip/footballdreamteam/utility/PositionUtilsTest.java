package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_PLACE;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_TYPE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Borce on 17.08.2016.
 */
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
    private Position unExistingPosition = new Position(12, "Un Existing Position");
    private List<Position> positions = Arrays.asList(keeper, centreBack, leftBack, rightBack,
            defensiveMidfield, centreMidfield, attackingMidfield, rightWing, leftWing,
            centreForward, secondaryForward);
    private PositionUtils utils;

    @Before
    public void setup() {
        utils = new PositionUtils();
    }

    /**
     * Test the behavior when setPositions is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionsWithNullParam() {
        utils.setPositions(null);
    }

    /**
     * Test the behavior when getPositionType is called with null position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWithNullPosition() {
        utils.getPositionType(null);
    }

    /**
     * Test the behavior when getPositionType is called with null position name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWithNullPositionName() {
        keeper.setName(null);
        utils.getPositionType(keeper);
    }

    /**
     * Test that getPositionType return the correct position type when is called with
     * positions that can be mapped with position type.
     */
    @Test
    public void testGetPositionTypeWithPositionsThatCanBeMappedWithType() {
        assertEquals(POSITION_TYPE.KEEPER, utils.getPositionType(keeper));
        assertEquals(POSITION_TYPE.CENTRE_BACK, utils.getPositionType(centreBack));
        assertEquals(POSITION_TYPE.LEFT_BACK, utils.getPositionType(leftBack));
        assertEquals(POSITION_TYPE.RIGHT_BACK, utils.getPositionType(rightBack));
        assertEquals(POSITION_TYPE.DEFENSIVE_MIDFIELD, utils.getPositionType(defensiveMidfield));
        assertEquals(POSITION_TYPE.CENTRE_MIDFIELD, utils.getPositionType(centreMidfield));
        assertEquals(POSITION_TYPE.ATTACKING_MIDFIELD, utils.getPositionType(attackingMidfield));
        assertEquals(POSITION_TYPE.LEFT_WING, utils.getPositionType(leftWing));
        assertEquals(POSITION_TYPE.RIGHT_WING, utils.getPositionType(rightWing));
        assertEquals(POSITION_TYPE.CENTRE_FORWARD, utils.getPositionType(centreForward));
        assertEquals(POSITION_TYPE.SECONDARY_FORWARD, utils.getPositionType(secondaryForward));
    }

    /**
     * Test the behavior when  getPositionType is called with position that can't be mapped
     * with position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWithPositionThatCanNotBeMappedWithType() {
        utils.getPositionType(unExistingPosition);
    }

    /**
     * Test the behavior when getPositionType is called with id param and the positions list is
     * not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWithIdParamWhenPositionsListIsNoSet() {
        utils.getPositionType(keeper.getId());
    }

    /**
     * Test the behavior when getPositionType is called with position id then is not in the
     * List of position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWithUnExistingId() {
        utils.setPositions(positions);
        utils.getPositionId(unExistingPosition.getId());
    }

    /**
     * Test that getPositionType return the correct position type when is called with position id
     * that is in the List of positions and the position can be mapped with a position type.
     */
    @Test
    public void testGetPositionTypeWIthExistingPositionIds() {
        utils.setPositions(positions);
        assertEquals(POSITION_TYPE.KEEPER, utils.getPositionType(keeper.getId()));
        assertEquals(POSITION_TYPE.CENTRE_BACK, utils.getPositionType(centreBack.getId()));
        assertEquals(POSITION_TYPE.LEFT_BACK, utils.getPositionType(leftBack.getId()));
        assertEquals(POSITION_TYPE.RIGHT_BACK, utils.getPositionType(rightBack.getId()));
        assertEquals(POSITION_TYPE.DEFENSIVE_MIDFIELD,
                utils.getPositionType(defensiveMidfield.getId()));
        assertEquals(POSITION_TYPE.CENTRE_MIDFIELD, utils.getPositionType(centreMidfield.getId()));
        assertEquals(POSITION_TYPE.ATTACKING_MIDFIELD,
                utils.getPositionType(attackingMidfield.getId()));
        assertEquals(POSITION_TYPE.LEFT_WING, utils.getPositionType(leftWing.getId()));
        assertEquals(POSITION_TYPE.RIGHT_WING, utils.getPositionType(rightWing.getId()));
        assertEquals(POSITION_TYPE.CENTRE_FORWARD, utils.getPositionType(centreForward.getId()));
        assertEquals(POSITION_TYPE.SECONDARY_FORWARD,
                utils.getPositionType(secondaryForward.getId()));
    }

    /**
     * Test the behavior when getPositionType is called with position id that can be found in the
     * List of position but that position can't be mapped with position type
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionTypeWIthExistingPositionIdsThatCanNotBeMappedWIthPositionType() {
        List<Position> positions = new ArrayList<>(this.positions);
        positions.add(unExistingPosition);
        utils.setPositions(positions);
        utils.getPositionType(unExistingPosition.getId());
    }

    /**
     * Test that getPositionResourceIdType returns the correct position type when is called with
     * resource ids that can be mapped with position type.
     */
    @Test
    public void testPositionResourceIdTypeWithResourcesIdsThatCanBeMappedWithPositionType() {
        assertEquals(POSITION_TYPE.KEEPER, utils.getPositionResourceIdType(R.id.keeper));
        assertEquals(POSITION_TYPE.CENTRE_BACK,
                utils.getPositionResourceIdType(R.id.leftCentreBack));
        assertEquals(POSITION_TYPE.CENTRE_BACK,
                utils.getPositionResourceIdType(R.id.rightCentreBack));
        assertEquals(POSITION_TYPE.CENTRE_BACK,
                utils.getPositionResourceIdType(R.id.centreCentreBack));
        assertEquals(POSITION_TYPE.LEFT_BACK, utils.getPositionResourceIdType(R.id.leftBack));
        assertEquals(POSITION_TYPE.RIGHT_BACK, utils.getPositionResourceIdType(R.id.rightBack));
        assertEquals(POSITION_TYPE.CENTRE_MIDFIELD,
                utils.getPositionResourceIdType(R.id.leftCentreMidfield));
        assertEquals(POSITION_TYPE.CENTRE_MIDFIELD,
                utils.getPositionResourceIdType(R.id.rightCentreMidfield));
        assertEquals(POSITION_TYPE.CENTRE_MIDFIELD,
                utils.getPositionResourceIdType(R.id.centreCentreMidfield));
        assertEquals(POSITION_TYPE.ATTACKING_MIDFIELD,
                utils.getPositionResourceIdType(R.id.attackingMidfield));
        assertEquals(POSITION_TYPE.LEFT_WING, utils.getPositionResourceIdType(R.id.leftWing));
        assertEquals(POSITION_TYPE.RIGHT_WING, utils.getPositionResourceIdType(R.id.rightWing));
        assertEquals(POSITION_TYPE.CENTRE_FORWARD,
                utils.getPositionResourceIdType(R.id.leftCentreForward));
        assertEquals(POSITION_TYPE.CENTRE_FORWARD,
                utils.getPositionResourceIdType(R.id.rightCentreForward));
        assertEquals(POSITION_TYPE.CENTRE_FORWARD,
                utils.getPositionResourceIdType(R.id.centreCentreForward));
    }

    /**
     * Test the behavior when getPositionResourceIdType is called with position resource id that
     * can't be mapped with position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPositionResourceIdTypeWithResourcesIdThatCanNotBeMappedWithPositionType() {
        utils.getPositionResourceIdType(R.id.content);
    }

    /**
     * Test that getPositionResourceIdPlace returns the correct position place when is called with
     * resource ids that can be mapped with position place.
     */
    @Test
    public void testGetPositionResourceIdPlaceWithResourcesIdsThatCanBeMappedWithPositionPlace() {
        assertEquals(POSITION_PLACE.KEEPERS, utils.getPositionResourceIdPlace(R.id.keeper));
        assertEquals(POSITION_PLACE.DEFENDERS,
                utils.getPositionResourceIdPlace(R.id.leftCentreBack));
        assertEquals(POSITION_PLACE.DEFENDERS,
                utils.getPositionResourceIdPlace(R.id.rightCentreBack));
        assertEquals(POSITION_PLACE.DEFENDERS,
                utils.getPositionResourceIdPlace(R.id.centreCentreBack));
        assertEquals(POSITION_PLACE.DEFENDERS, utils.getPositionResourceIdPlace(R.id.leftBack));
        assertEquals(POSITION_PLACE.DEFENDERS, utils.getPositionResourceIdPlace(R.id.rightBack));
        assertEquals(POSITION_PLACE.MIDFIELDERS,
                utils.getPositionResourceIdPlace(R.id.leftCentreMidfield));
        assertEquals(POSITION_PLACE.MIDFIELDERS,
                utils.getPositionResourceIdPlace(R.id.rightCentreMidfield));
        assertEquals(POSITION_PLACE.MIDFIELDERS,
                utils.getPositionResourceIdPlace(R.id.centreCentreMidfield));
        assertEquals(POSITION_PLACE.MIDFIELDERS,
                utils.getPositionResourceIdPlace(R.id.attackingMidfield));
        assertEquals(POSITION_PLACE.MIDFIELDERS, utils.getPositionResourceIdPlace(R.id.leftWing));
        assertEquals(POSITION_PLACE.MIDFIELDERS, utils.getPositionResourceIdPlace(R.id.rightWing));
        assertEquals(POSITION_PLACE.ATTACKERS,
                utils.getPositionResourceIdPlace(R.id.leftCentreForward));
        assertEquals(POSITION_PLACE.ATTACKERS,
                utils.getPositionResourceIdPlace(R.id.rightCentreForward));
        assertEquals(POSITION_PLACE.ATTACKERS,
                utils.getPositionResourceIdPlace(R.id.centreCentreForward));
    }

    /**
     * Test the behavior when getPositionsResourceIdPlace is called with resource id that can't be
     * mapped with position place.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionResourceIdPlaceWithResourcesIdsThatCanNotBeMappedWithPlace() {
        utils.getPositionResourceIdPlace(R.id.content);
    }

    /**
     * Test the behavior when getShortName is called with position that can't be mapped with
     * position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getGetShortNameWithPositionThatCanNotBeMappedWithType() {
        utils.getShortName(unExistingPosition);
    }

    /**
     * Test the behavior when getShortName is called with position that can be mapped with
     * position type.
     */
    @Test
    public void getGetShortNameWithPositionThatCanBeMappedWithType() {
        assertEquals(POSITION_TYPE.SECONDARY_FORWARD.getShortName(),
                utils.getShortName(secondaryForward));
    }

    /**
     * Test the behavior when getPositionId is called and the utils positions are not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionIdWhenPositionsIsNotSet() {
        utils.getPositionId(R.id.keeper);
    }

    /**
     * Test the getPositionId return the correct position id when is called with a resource id that
     * can be mapped with the position type.
     */
    @Test
    public void testGetPositionIdWithResourcesIdsThatCanBeMappedWithPositionType() {
        utils.setPositions(positions);
        assertEquals(keeper.getId().intValue(), utils.getPositionId(R.id.keeper));
        assertEquals(centreBack.getId().intValue(), utils.getPositionId(R.id.leftCentreBack));
        assertEquals(centreBack.getId().intValue(), utils.getPositionId(R.id.rightCentreBack));
        assertEquals(centreBack.getId().intValue(), utils.getPositionId(R.id.centreCentreBack));
        assertEquals(leftBack.getId().intValue(), utils.getPositionId(R.id.leftBack));
        assertEquals(rightBack.getId().intValue(), utils.getPositionId(R.id.rightBack));
        assertEquals(centreMidfield.getId().intValue(),
                utils.getPositionId(R.id.leftCentreMidfield));
        assertEquals(centreMidfield.getId().intValue(),
                utils.getPositionId(R.id.rightCentreMidfield));
        assertEquals(centreMidfield.getId().intValue(),
                utils.getPositionId(R.id.centreCentreMidfield));
        assertEquals(leftWing.getId().intValue(), utils.getPositionId(R.id.leftWing));
        assertEquals(rightWing.getId().intValue(), utils.getPositionId(R.id.rightWing));
        assertEquals(attackingMidfield.getId().intValue(),
                utils.getPositionId(R.id.attackingMidfield));
        assertEquals(centreForward.getId().intValue(),
                utils.getPositionId(R.id.leftCentreForward));
        assertEquals(centreForward.getId().intValue(),
                utils.getPositionId(R.id.rightCentreForward));
        assertEquals(centreForward.getId().intValue(),
                utils.getPositionId(R.id.centreCentreForward));
    }

    /**
     * Test the behavior when getPositionId is called with a position resource id that can't be
     * mapped with position type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPositionIdWithResourceIdThatCanNotBeMappedWithType() {
        utils.setPositions(positions);
        utils.getPositionId(R.id.content);
    }

    /**
     * Test that samePositions returns true when the positions types params are same.
     */
    @Test
    public void testSamePositionsWithSamePositionsTypes() {
        assertTrue(utils.samePositions(POSITION_TYPE.LEFT_WING, POSITION_TYPE.LEFT_WING));
    }

    /**
     * Test that samePositions returns false when the positions types params are not same.
     */
    @Test
    public void testSamePositionsWithNotSamePositionsTypes() {
        assertFalse(utils.samePositions(POSITION_TYPE.CENTRE_FORWARD, POSITION_TYPE.LEFT_BACK));
    }

    /**
     * Test that samePositions returns true when one of the positions is a DEFENSIVE_MIDFIELD type
     * and the other is CENTRE_MIDFIELD.
     */
    @Test
    public void testSamePositionsWithDefensiveAndCentreMidfieldPositionsTypes() {
        assertTrue(utils.samePositions(POSITION_TYPE.DEFENSIVE_MIDFIELD,
                POSITION_TYPE.CENTRE_MIDFIELD));
        assertTrue(utils.samePositions(POSITION_TYPE.CENTRE_MIDFIELD,
                POSITION_TYPE.DEFENSIVE_MIDFIELD));
    }

    /**
     * Test that samePositions returns true when one of the positions is a CENTRE_FORWARD type
     * and the other is SECONDARY_FORWARD.
     */
    @Test
    public void testSamePositionsWithCentreAndSecondaryForwardPositionsTypes() {
        assertTrue(utils.samePositions(POSITION_TYPE.CENTRE_FORWARD,
                POSITION_TYPE.SECONDARY_FORWARD));
        assertTrue(utils.samePositions(POSITION_TYPE.SECONDARY_FORWARD,
                POSITION_TYPE.CENTRE_FORWARD));
    }

    /**
     * Create a mock players with the given position.
     *
     * @param position players lineup position
     * @return player
     */
    private Player createLineupPlayer(Position position) {
        Player player = new Player();
        LineupPlayer lineupPlayer = new LineupPlayer(new Lineup(), new Player(), position);
        player.setLineupPlayer(lineupPlayer);
        return player;
    }

    /**
     * Generate a List of players with the given params.
     *
     * @param cbs number of centre backs
     * @param rbs number of right backs
     * @param lbs number of left backs
     * @param dms number of defensive midfielders
     * @param cms number of centre midfielders
     * @param ams number of attacking midfielders
     * @param lws number of left wings
     * @param rws number of right wings
     * @param cfs number of centre forwards
     * @param sfs number of secondary forwards
     * @return List of players
     */
    private List<Player> generatePlayers(int cbs, int rbs, int lbs, int dms, int cms, int ams,
                                         int lws, int rws, int cfs, int sfs) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < cbs; i++) {
            players.add(this.createLineupPlayer(this.centreBack));
        }
        for (int i = 0; i < rbs; i++) {
            players.add(this.createLineupPlayer(this.rightBack));
        }
        for (int i = 0; i < lbs; i++) {
            players.add(this.createLineupPlayer(this.leftBack));
        }
        for (int i = 0; i < dms; i++) {
            players.add(this.createLineupPlayer(this.defensiveMidfield));
        }
        for (int i = 0; i < cms; i++) {
            players.add(this.createLineupPlayer(this.centreMidfield));
        }
        for (int i = 0; i < ams; i++) {
            players.add(this.createLineupPlayer(this.attackingMidfield));
        }
        for (int i = 0; i < lws; i++) {
            players.add(this.createLineupPlayer(this.leftWing));
        }
        for (int i = 0; i < rws; i++) {
            players.add(this.createLineupPlayer(this.rightWing));
        }
        for (int i = 0; i < cfs; i++) {
            players.add(this.createLineupPlayer(this.centreForward));
        }
        for (int i = 0; i < sfs; i++) {
            players.add(this.createLineupPlayer(this.secondaryForward));
        }
        return players;
    }

    /**
     * Assert that the values in the map return from the countLineupPlayers method are correct.
     *
     * @param cbs number of centre backs
     * @param rbs number of right backs
     * @param lbs number of left backs
     * @param dms number of defensive midfielders
     * @param cms number of centre midfielders
     * @param ams number of attacking midfielders
     * @param lws number of left wings
     * @param rws number of right wings
     * @param cfs number of centre forwards
     * @param sfs number of secondary forwards
     * @param map map containing the values
     */
    private void assertCountLineupPlayersMap(int cbs, int rbs, int lbs, int dms, int cms, int ams,
                                             int lws, int rws, int cfs, int sfs,
                                             Map<POSITION_TYPE, Integer> map) {
        assertNotNull(map);
        assertEquals(cbs, map.get(POSITION_TYPE.CENTRE_BACK).intValue());
        assertEquals(lbs, map.get(POSITION_TYPE.LEFT_BACK).intValue());
        assertEquals(rbs, map.get(POSITION_TYPE.RIGHT_BACK).intValue());
        assertEquals(dms + cms, map.get(POSITION_TYPE.CENTRE_MIDFIELD).intValue());
        assertEquals(lws, map.get(POSITION_TYPE.LEFT_WING).intValue());
        assertEquals(rws, map.get(POSITION_TYPE.RIGHT_WING).intValue());
        assertEquals(ams, map.get(POSITION_TYPE.ATTACKING_MIDFIELD).intValue());
        assertEquals(cfs + sfs, map.get(POSITION_TYPE.CENTRE_FORWARD).intValue());
    }

    /**
     * Test the behavior when countLineupPlayersPositions is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountLineupPlayersPositionsWithNullParam() {
        utils.countLineupPlayersPositions(null);
    }

    /**
     * Test the behavior when countLineupPlayersPositions is called with a List of players that
     * contains a players on which the lineup player field is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountLineupPlayersPositionWithNullLineupPlayer() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        players.get(2).setLineupPlayer(null);
        utils.countLineupPlayersPositions(players);
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players.
     */
    @Test
    public void testCountLineupPlayersCorrectlyCountsThePositions() {
        final int cbs = 2, rbs = 3, lbs = 1, dms = 0, cms = 3, ams = 3, lws = 2, rws = 5,
                cfs = 4, sfs = 0;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players when the List contains a players on which the lineup position is null.
     */
    @Test
    public void testCountLineupPlayersCorrectlyCountsThePositionsWithNullLineupPosition() {
        utils.setPositions(positions);
        final int cbs = 2, rbs = 3, lbs = 1, dms = 0, cms = 3, ams = 3, lws = 2, rws = 5,
                cfs = 4, sfs = 0;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        LineupPlayer player = players.get(4).getLineupPlayer();
        player.setPositionId(player.getPosition().getId());
        player.setPosition(null);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players when the List contains a player on which the lineup position
     * name is null.
     */
    @Test
    public void testCountLineupPlayersCorrectlyCountsThePositionsWithNullLineupPositionName() {
        utils.setPositions(positions);
        final int cbs = 2, rbs = 3, lbs = 1, dms = 0, cms = 3, ams = 3, lws = 2, rws = 5,
                cfs = 4, sfs = 0;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        LineupPlayer player = players.get(4).getLineupPlayer();
        Position position = new Position(player.getPositionId(), null);
        player.setPosition(position);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players when there are not players for some positions.
     */
    @Test
    public void testCountLineupPlayersWhenThereIsNoPlayersForSomePositions() {
        utils.setPositions(positions);
        final int cbs = 0, rbs = 3, lbs = 1, dms = 0, cms = 3, ams = 0, lws = 2, rws = 5,
                cfs = 0, sfs = 0;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        LineupPlayer player = players.get(4).getLineupPlayer();
        Position position = new Position(player.getPositionId(), null);
        player.setPosition(position);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players when there is player that plays on the defensive midfield position.
     */
    @Test
    public void testCountLineupPlayersWhenThereIsADefensiveMidfieldPlayer() {
        utils.setPositions(positions);
        final int cbs = 1, rbs = 3, lbs = 1, dms = 3, cms = 3, ams = 4, lws = 2, rws = 5,
                cfs = 1, sfs = 0;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        LineupPlayer player = players.get(4).getLineupPlayer();
        Position position = new Position(player.getPositionId(), null);
        player.setPosition(position);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }

    /**
     * Test that countLineupPlayersPositions correctly counts that positions for the
     * given List of players when there is player that plays on the secondary forward position.
     */
    @Test
    public void testCountLineupPlayersWhenThereIsASecondaryForwardPlayer() {
        utils.setPositions(positions);
        final int cbs = 1, rbs = 3, lbs = 1, dms = 0, cms = 3, ams = 4, lws = 2, rws = 5,
                cfs = 1, sfs = 2;
        List<Player> players = this.generatePlayers(cbs, rbs, lbs, dms, cms, ams, lws, rws,
                cfs, sfs);
        LineupPlayer player = players.get(4).getLineupPlayer();
        Position position = new Position(player.getPositionId(), null);
        player.setPosition(position);
        this.assertCountLineupPlayersMap(cbs, rbs, lbs, dms, cms, ams, lws, rws, cfs, sfs,
                utils.countLineupPlayersPositions(players));
    }
}