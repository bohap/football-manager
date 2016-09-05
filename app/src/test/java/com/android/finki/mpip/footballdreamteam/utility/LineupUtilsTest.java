package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 10.08.2016.
 */
public class LineupUtilsTest {

    @Mock
    protected PositionUtils positionUtils;

    private LineupUtils utils;

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

    private final int[] positions_F_4_4_2 = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward};
    private final int[] positions_F_3_2_3_2_V1 = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.leftCentreForward, R.id.rightCentreForward};
    private final int[] positions_F_3_2_3_2_V2 = {R.id.keeper, R.id.centreCentreBack,
            R.id.leftCentreBack, R.id.rightCentreBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.leftCentreForward, R.id.rightCentreForward};
    private final int[] positions_F_4_2_3_1 = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.centreCentreForward};
    private final int[] positions_F_4_3_3 = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.centreCentreMidfield, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};
    private Map<PositionUtils.POSITION, Integer> mappedPositions = new HashMap<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mappedPositions.put(PositionUtils.POSITION.KEEPER, keeper.getId());
        mappedPositions.put(PositionUtils.POSITION.CENTRE_BACK, centreBack.getId());
        mappedPositions.put(PositionUtils.POSITION.RIGHT_BACK, rightBack.getId());
        mappedPositions.put(PositionUtils.POSITION.LEFT_BACK, leftBack.getId());
        mappedPositions.put(PositionUtils.POSITION.DEFENSIVE_MIDFIELD, defensiveMidfield.getId());
        mappedPositions.put(PositionUtils.POSITION.CENTRE_MIDFIELD, centreMidfield.getId());
        mappedPositions.put(PositionUtils.POSITION.ATTACKING_MIDFIELD, attackingMidfield.getId());
        mappedPositions.put(PositionUtils.POSITION.RIGHT_WING, rightWing.getId());
        mappedPositions.put(PositionUtils.POSITION.LEFT_WING, leftWing.getId());
        mappedPositions.put(PositionUtils.POSITION.CENTRE_FORWARD, centreForward.getId());
        mappedPositions.put(PositionUtils.POSITION.SECONDARY_FORWARD, secondaryForward.getId());
        this.mockPositionUtils();
        utils = new LineupUtils(positionUtils);
    }

    /**
     * Mock the PositionUtils to return specific values in method calls.
     */
    private void mockPositionUtils() {
        when(positionUtils.getPosition(keeper)).thenReturn(PositionUtils.POSITION.KEEPER);
        when(positionUtils.getPosition(centreBack)).thenReturn(PositionUtils.POSITION.CENTRE_BACK);
        when(positionUtils.getPosition(rightBack)).thenReturn(PositionUtils.POSITION.RIGHT_BACK);
        when(positionUtils.getPosition(leftBack)).thenReturn(PositionUtils.POSITION.LEFT_BACK);
        when(positionUtils.getPosition(defensiveMidfield))
                .thenReturn(PositionUtils.POSITION.DEFENSIVE_MIDFIELD);
        when(positionUtils.getPosition(centreMidfield))
                .thenReturn(PositionUtils.POSITION.CENTRE_MIDFIELD);
        when(positionUtils.getPosition(rightWing)).thenReturn(PositionUtils.POSITION.RIGHT_WING);
        when(positionUtils.getPosition(leftWing)).thenReturn(PositionUtils.POSITION.LEFT_WING);
        when(positionUtils.getPosition(attackingMidfield))
                .thenReturn(PositionUtils.POSITION.ATTACKING_MIDFIELD);
        when(positionUtils.getPosition(centreForward))
                .thenReturn(PositionUtils.POSITION.CENTRE_FORWARD);
        when(positionUtils.getPosition(secondaryForward))
                .thenReturn(PositionUtils.POSITION.SECONDARY_FORWARD);
        when(positionUtils.getPositionId(R.id.keeper, mappedPositions)).thenReturn(keeper.getId());
        when(positionUtils.getPositionId(R.id.leftCentreBack, mappedPositions))
                .thenReturn(centreBack.getId());
        when(positionUtils.getPositionId(R.id.rightCentreBack, mappedPositions))
                .thenReturn(centreBack.getId());
        when(positionUtils.getPositionId(R.id.centreCentreBack, mappedPositions))
                .thenReturn(centreBack.getId());
        when(positionUtils.getPositionId(R.id.leftBack, mappedPositions))
                .thenReturn(leftBack.getId());
        when(positionUtils.getPositionId(R.id.rightBack, mappedPositions))
                .thenReturn(rightBack.getId());
        when(positionUtils.getPositionId(R.id.leftCentreMidfield, mappedPositions))
                .thenReturn(centreMidfield.getId());
        when(positionUtils.getPositionId(R.id.rightCentreMidfield, mappedPositions))
                .thenReturn(centreMidfield.getId());
        when(positionUtils.getPositionId(R.id.centreCentreMidfield, mappedPositions))
                .thenReturn(centreMidfield.getId());
        when(positionUtils.getPositionId(R.id.leftWing, mappedPositions))
                .thenReturn(leftWing.getId());
        when(positionUtils.getPositionId(R.id.rightWing, mappedPositions))
                .thenReturn(rightWing.getId());
        when(positionUtils.getPositionId(R.id.attackingMidfield, mappedPositions))
                .thenReturn(attackingMidfield.getId());
        when(positionUtils.getPositionId(R.id.leftCentreForward, mappedPositions))
                .thenReturn(centreForward.getId());
        when(positionUtils.getPositionId(R.id.rightCentreForward, mappedPositions))
                .thenReturn(centreForward.getId());
        when(positionUtils.getPositionId(R.id.centreCentreForward, mappedPositions))
                .thenReturn(centreForward.getId());
    }

    /**
     * Generate a list of Lineup players.
     *
     * @param centreBacks          number of player tha plays centre back in the lineup
     * @param rightBacks           number of players that plays right back in the lineup
     * @param leftBacks            number of players that plays left back in the lineup
     * @param defensiveMidfielders number of players that plays defensive midfield in the lineup
     * @param centreMidfielders    number of players that plays defensive midfield in the lineup
     * @param attackingMidfielders number of players that plays attacking midfield in the lineup
     * @param rightWings           number of players that plays right wing in the lineup
     * @param leftWings            number of players that plays left wing in the lineup
     * @param centreForwards       number of players that plays centre forward in the lineup
     * @param secondaryForwards    number of players that plays secondary forward in the lineup
     * @return List of lineup players
     */
    private List<Player> generatePlayers(int centreBacks, int leftBacks, int rightBacks,
                                         int defensiveMidfielders, int centreMidfielders,
                                         int attackingMidfielders,
                                         int leftWings, int rightWings, int centreForwards,
                                         int secondaryForwards) {
        List<Player> players = new ArrayList<>();
        Lineup lineup = new Lineup();
        Player player = new Player();
        int playerId = 1;
        players.add(new Player(playerId++, 0, 0, new LineupPlayer(lineup, player, keeper)));
        for (int i = 0; i < centreBacks; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, centreBack)));
        }
        for (int i = 0; i < leftBacks; i++) {
            players.add(new Player(playerId++, 0, 0, new LineupPlayer(lineup, player, leftBack)));
        }
        for (int i = 0; i < rightBacks; i++) {
            players.add(new Player(playerId++, 0, 0, new LineupPlayer(lineup, player, rightBack)));
        }
        for (int i = 0; i < defensiveMidfielders; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, defensiveMidfield)));
        }
        for (int i = 0; i < centreMidfielders; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, centreMidfield)));
        }
        for (int i = 0; i < attackingMidfielders; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, attackingMidfield)));
        }
        for (int i = 0; i < leftWings; i++) {
            players.add(new Player(playerId++, 0, 0, new LineupPlayer(lineup, player, leftWing)));
        }
        for (int i = 0; i < rightWings; i++) {
            players.add(new Player(playerId++, 0, 0, new LineupPlayer(lineup, player, rightWing)));
        }
        for (int i = 0; i < centreForwards; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, centreForward)));
        }
        for (int i = 0; i < secondaryForwards; i++) {
            players.add(new Player(playerId++, 0, 0,
                    new LineupPlayer(lineup, player, secondaryForward)));
        }
        return players;
    }

    private List<Position> getLineupPositions(List<Player> players) {
        List<Position> positions = new ArrayList<>();
        for (Player player : players) {
            positions.add(new Position(player.getLineupPositionId(), ""));
        }
        return positions;
    }

    /**
     * Generate a map where the key is a POSITIONS and is mapped with
     * the number of ids on that position.
     *
     * @return generated map
     */
    private Map<PositionUtils.POSITION, Integer> generateCountMap(int centreBacks, int rightBacks,
                                                                  int leftBacks, int centreMidfielders,
                                                                  int attackingMidfielders,
                                                                  int rightWings, int leftWings,
                                                                  int centreForwards) {
        Map<PositionUtils.POSITION, Integer> result = new HashMap<>();
        result.put(PositionUtils.POSITION.CENTRE_BACK, centreBacks);
        result.put(PositionUtils.POSITION.RIGHT_BACK, rightBacks);
        result.put(PositionUtils.POSITION.LEFT_BACK, leftBacks);
        result.put(PositionUtils.POSITION.CENTRE_MIDFIELD, centreMidfielders);
        result.put(PositionUtils.POSITION.ATTACKING_MIDFIELD, attackingMidfielders);
        result.put(PositionUtils.POSITION.RIGHT_WING, rightWings);
        result.put(PositionUtils.POSITION.LEFT_WING, leftWings);
        result.put(PositionUtils.POSITION.CENTRE_FORWARD, centreForwards);
        return result;
    }

    /**
     * Test the behavior on the getFormation method called with incorrect players size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormationOnInvalidPlayersSize() {
        List<Player> players = this.generatePlayers(2, 0, 1, 2, 0, 0, 0, 0, 0, 0);
        utils.getFormation(this.getLineupPositions(players));
    }

    /**
     * Test that getFormation works for 4-2-2 formation.
     */
    @Test
    public void testGetFormationOnF_4_2_2() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_4_4_2, formation);
    }

    /**
     * Test that getFormation method returns a 3-2-3-2 formation when there is three center backs.
     */
    @Test
    public void testGetFormationOn3_2_2_2() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 2, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(3, 0, 0, 2, 1, 1, 1, 2));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, formation);
    }

    /**
     * Test that getFormation method returns a 3-2-3-2 formation when there is o
     * nly one centre, left and right back.
     */
    @Test
    public void testGetFormationOn3_2_3_2WithLeftAndRightBack() {
        List<Player> players = this.generatePlayers(1, 1, 1, 0, 2, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(1, 1, 1, 2, 1, 1, 1, 2));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, formation);
    }

    /**
     * Test that getFormation works for 4-2-3-1.
     */
    @Test
    public void testGetFormationOn4_2_3_1() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 1, 1, 1, 1, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 1, 1, 1, 1));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_4_2_3_1, formation);
    }

    /**
     * Test that getFormation works for 4-3-3 lineup.
     */
    @Test
    public void testGetFormationOn4_3_3() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 3, 0, 0, 0, 3, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 3, 0, 0, 0, 3));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_4_3_3, formation);
    }

    /**
     * Test that getFormation method returns 3-2-2-2 formation when all positions ids in the
     * array are different.
     */
    @Test
    public void testGetFormationWithAllDifferentPositionsIds() {
        List<Player> players = this.generatePlayers(1, 1, 1, 0, 2, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(1, 1, 1, 2, 1, 1, 1, 2));
        LineupUtils.FORMATION formation = utils.getFormation(positions);
        assertNotNull(formation);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, formation);
    }

    /**
     * Test the behavior on the mapPlayer method called with invalid players size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnInvalidSize() {
        List<Player> players = this.generatePlayers(1, 1, 1, 0, 0, 1, 1, 1, 1, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        utils.mapPlayers(lineupPlayers);
    }

    /**
     * Assert that the players in the list are correctly mapped into a map
     * for the given formation.
     *
     * @param players           List of Player
     * @param mapPlayers        Mapped players
     * @param formation         lineup formation
     * @param checkLineupPlayer whateve the lineup player should be checked
     */
    private void assertMapPlayers(List<Player> players, Map<Integer, Player> mapPlayers,
                                  LineupUtils.FORMATION formation, boolean checkLineupPlayer) {
        int i = 0;
        int[] positions = positions_F_4_4_2;
        switch (formation) {
            case F_3_2_3_2:
                /* Check if the lineup 3-2-3-2 is with left and right back. */
                if (players.get(1).getId().equals(mapPlayers
                        .get(R.id.centreCentreBack).getId())) {
                    positions = positions_F_3_2_3_2_V2;
                } else {
                    positions = positions_F_3_2_3_2_V1;
                }
                break;
            case F_4_2_3_1:
                positions = positions_F_4_2_3_1;
                break;
            case F_4_3_3:
                positions = positions_F_4_3_3;
                break;
        }
        for (int position : positions) {
            if (i > players.size() - 1 || players.get(i).getId() == 0) {
                Player mapPlayer = mapPlayers.get(position);
                assertEquals(0, mapPlayer.getId().intValue());
            } else {
                if (!checkLineupPlayer) {
                    assertSame(players.get(i), mapPlayers.get(position));
                } else {
                    Player player = players.get(i);
                    Player mapPlayer = mapPlayers.get(position);
                    assertEquals(player.getId(), mapPlayer.getId());
                    assertEquals(player.getName(), mapPlayer.getName());
                    assertNotNull(mapPlayer.getLineupPlayer());
                    assertEquals(positionUtils.getPositionId(position, mappedPositions),
                            mapPlayer.getLineupPositionId());
                }
            }
            i++;
        }
    }

    /**
     * Test the behavior on mapPlayers called with a player with null lineupPlayer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnPlayerWithNullLineupPlayer() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        players.get(7).setLineupPlayer(null);
        LineupPlayers lineupPlayers = new LineupPlayers(players, positions, null, false);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
    }

    /**
     * Test the behavior on mapPlayers called with a player with null lienup player position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnPlayerWIthNullLineupPlayerPosition() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        players.get(7).getLineupPlayer().setPosition(null);
        LineupPlayers lineupPlayers = new LineupPlayers(players, positions, null, false);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
    }

    /**
     * Test the behavior when mapPlayers is called with players which formation
     * can't be determined.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnInvalidFormation() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 2, 0, 1, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 2, 0, 2));
        utils.mapPlayers(lineupPlayers);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 4-2-2.
     */
    @Test
    public void testMapPlayersOn4_4_2Formation() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_4_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_4_2, false);
    }

    /**
     * Test that mapPlayers correctly maps the list into a map when the players is 4-2-2 and there
     * is defensive midfield player.
     */
    @Test
    public void testMapPlayersOn4_2_2FormationWIthDefensiveMidfield() {
        List<Player> players = this.generatePlayers(2, 1, 1, 1, 1, 0, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_4_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_4_2, false);
    }

    /**
     * Test that mapPlayers correctly maps the list into a map when the players is 4-2-2 and there
     * is secondary forward player.
     */
    @Test
    public void testMapPlayersOn4_2_2FormationWIthSecondaryForward() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 1, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_4_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_4_2, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 3-2-3-2 formation.
     */
    @Test
    public void testMapPlayersOn3_2_3_2Formation() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 2, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(3, 0, 0, 2, 1, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_3_2_3_2, false);
    }

    /**
     * Test that mapPlayer correctly map the list into a map on 3-2-3-2 formation and there
     * is a defensive midfield player.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithDefensiveMidfield() {
        List<Player> players = this.generatePlayers(3, 0, 0, 1, 1, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(3, 0, 0, 2, 1, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_3_2_3_2, false);
    }

    /**
     * Test that mapPlayer correctly map the list into a map on 3-2-3-2 formation and there
     * is a secondary striker player.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithSecondaryStriker() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 2, 1, 1, 1, 1, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(3, 0, 0, 2, 1, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_3_2_3_2, false);
    }

    /**
     * Test that mapPlayers correctly maps the list into a map on 3-2-3-2 formation with right
     * and left back.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithLeftAndRightBack() {
        List<Player> players = this.generatePlayers(1, 1, 1, 0, 2, 1, 1, 1, 2, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(1, 1, 1, 2, 1, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_3_2_3_2, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-2-3-1 formation.
     */
    @Test
    public void testMapPlayersOn4_2_3_1Formation() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 1, 1, 1, 1, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 1, 1, 1, 1));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_2_3_1, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_2_3_1, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-2-3-1 formation and there is
     * a defensive midfield player.
     */
    @Test
    public void testMapPlayersOn4_2_3_1FormationWithDefensiveMidfield() {
        List<Player> players = this.generatePlayers(2, 1, 1, 1, 1, 1, 1, 1, 1, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 1, 1, 1, 1));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_2_3_1, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_2_3_1, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-2-3-1 formation and there is a
     * secondary striker.
     */
    @Test
    public void testMapPlayersOn4_2_3_1FormationWithSecondaryStriker() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 1, 1, 1, 0, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 1, 1, 1, 1));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_2_3_1, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_2_3_1, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-3-3formation.
     */
    @Test
    public void testMapPlayersOn4_3_3Formation() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 3, 0, 0, 0, 3, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 3, 0, 0, 0, 3));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_3_3, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_3_3, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-3-3formation and there is a
     * defensive midfield player.
     */
    @Test
    public void testMapPlayersOn4_3_3FormationWithDefensiveMidfield() {
        List<Player> players = this.generatePlayers(2, 1, 1, 1, 2, 0, 0, 0, 3, 0);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 3, 0, 0, 0, 3));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_3_3, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_3_3, false);
    }

    /**
     * Test that mapPlayer correctly maps the list into a map on 4-3-3formation and there is a
     * secondary striker player.
     */
    @Test
    public void testMapPlayersOn4_3_3FormationWithSecondaryStriker() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 3, 0, 0, 0, 1, 2);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(2, 1, 1, 3, 0, 0, 0, 3));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_4_3_3, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_4_3_3, false);
    }

    /**
     * Test that mapP;ayers correctly maps the list into a map all positions id are different.
     */
    @Test
    public void testMapPlayersWithAllDifferentPositionsIds() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        List<Position> positions = this.getLineupPositions(players);
        LineupPlayers lineupPlayers = new LineupPlayers();
        lineupPlayers.setPlayers(players);
        lineupPlayers.setPositions(positions);
        when(positionUtils.countPositions(positions))
                .thenReturn(this.generateCountMap(1, 1, 1, 2, 1, 1, 1, 2));
        utils.mapPlayers(lineupPlayers);
        assertEquals(LineupUtils.FORMATION.F_3_2_3_2, lineupPlayers.getFormation());
        this.assertMapPlayers(players, lineupPlayers.getMappedPlayers(),
                LineupUtils.FORMATION.F_3_2_3_2, false);
    }

    /**
     * Test the behavior when generateMap is called with null formation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMapOnNullFormation() {
        utils.generateMap(null, new ArrayList<Player>(), mappedPositions);
    }

    /**
     * Test the behavior when generateMap is called with null players.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMapWithNullPlayers() {
        utils.generateMap(LineupUtils.FORMATION.F_4_4_2, null, mappedPositions);
    }

    /**
     * Test that generateMap correctly generates a map for the formation 4-4-2 and puts
     * the given players into the map.
     */
    @Test
    public void testGenerateMapOn4_4_2Formation() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_4_2, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_4_2, true);
    }

    /**
     * Test that generateMap correctly generates a map for the formation 4-4-2 and puts the p
     * layers into the map when the given list don't have 11 players.
     */
    @Test
    public void testGenerateMapOn4_4_2FormationWithNotElevenPlayers() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 0, 0, 0, 0, 0, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_4_2, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_4_2, true);
    }

    /**
     * Test that generateMap correctly generate a map for the formation 3-2-3-2 and puts the
     * players into the map.
     */
    @Test
    public void testGenerateMapOn3_2_3_2() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 2, 1, 1, 1, 2, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_3_2_3_2, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_3_2_3_2, true);
    }

    /**
     * Test that generateMap correctly generate a map for the formation 3-2-3-2 and puts the
     * players into the map when the given list don't  have 11 players.
     */
    @Test
    public void testGenerateMapOn3_2_3_2WithNotElevenPlayers() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 0, 0, 2, 0, 0, 1);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_3_2_3_2, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_3_2_3_2, true);
    }

    /**
     * Test that generateMap correctly generate a map for the formation 3-2-3-2 and puts the
     * players into the map.
     */
    @Test
    public void testGenerateMapOn4_2_3_1() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 1, 1, 1, 1, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_2_3_1, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_2_3_1, true);
    }

    /**
     * Test that generateMap correctly generate a map for the formation 3-2-3-2 and puts the
     * players into the map when given list don;t have 11 players.
     */
    @Test
    public void testGenerateMapOn4_2_3_1WithNotElevenPlayers() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 0, 0, 0, 0, 0, 1);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_2_3_1, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_2_3_1, true);
    }

    /**
     * Test that generateMap correctly generates a map for formation 4-3-3 and puts
     * the players into the map.
     */
    @Test
    public void testGenerateMapOn4_3_3() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 3, 0, 0, 0, 3, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_3_3, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_3_3, true);
    }

    /**
     * Test that generateMap correctly generates a map for formation 4-3-3 and puts
     * the players into the map when given list don't have eleven players.
     */
    @Test
    public void testGenerateMapOn4_3_3WithNotElevenPlayers() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 1, 0, 0, 1, 0, 0);
        Map<Integer, Player> result = utils
                .generateMap(LineupUtils.FORMATION.F_4_3_3, players, mappedPositions);
        this.assertMapPlayers(players, result, LineupUtils.FORMATION.F_4_3_3, true);
    }

    /**
     * Put the list of players into a map.
     *
     * @param players List of Player
     */
    private Map<Integer, Player> putPlayers(List<Player> players) {
        Map<Integer, Player> map = new HashMap<>();
        int i = 0;
        for (Player player : players) {
            map.put(i++, player);
        }
        return map;
    }

    /**
     * Test the behavior when getLineupPlayers is called with invalid map size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLineupPlayersOnInvalidMapSize() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 0, 0, 0, 1);
        utils.getLineupPlayers(this.putPlayers(players));
    }

    /**
     * Test the getLineupPlayers return the correct List of LineupPlayer.
     */
    @Test
    public void testGetLineupPlayers() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        List<LineupPlayer> lineupPlayers = utils.getLineupPlayers(this.putPlayers(players));
        assertEquals(players.size(), lineupPlayers.size());
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getId().intValue(), lineupPlayers.get(i).getPlayerId());
            assertEquals(players.get(i).getLineupPositionId(),
                    lineupPlayers.get(i).getPositionId());
        }
    }

    /**
     * Test the behavior when getLineupPlayers is called with invalid player id.
     */
    @Test
    public void testGetLineupPlayersOnInvalidPlayerId() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        players.get(4).setId(-10);
        List<LineupPlayer> lineupPlayers = utils.getLineupPlayers(this.putPlayers(players));
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getId().intValue(), lineupPlayers.get(i).getPlayerId());
            assertEquals(players.get(i).getLineupPositionId(),
                    lineupPlayers.get(i).getPositionId());
        }
    }

    /**
     * Test the behavior when getLineupPlayers is called with a player that
     * has a null for LineupPlayer.
     */
    @Test
    public void testGetLineupPlayersOnNullLineupPlayer() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        players.get(3).setLineupPlayer(null);
        List<LineupPlayer> lineupPlayers = utils.getLineupPlayers(this.putPlayers(players));
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getId().intValue(), lineupPlayers.get(i).getPlayerId());
            assertEquals(players.get(i).getLineupPositionId(),
                    lineupPlayers.get(i).getPositionId());
        }
    }

    /**
     * Test the behavior on getLineupPlayers called with invalid player lineup id.
     */
    @Test
    public void testGetLineupPlayersOnInvalidLineupPositionId() {
        List<Player> players = this.generatePlayers(1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        players.get(9).setLineupPlayer(new LineupPlayer(0, 0, 0));
        List<LineupPlayer> lineupPlayers = utils.getLineupPlayers(this.putPlayers(players));
        for (int i = 0; i < players.size(); i++) {
            assertEquals(players.get(i).getId().intValue(), lineupPlayers.get(i).getPlayerId());
            assertEquals(players.get(i).getLineupPositionId(),
                    lineupPlayers.get(i).getPositionId());
        }
    }

    /**
     * Test orderPlayers on lineup with formation 4-4-2.
     */
    @Test
    public void testOrderPlayersOn4_4_2() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 0, 1, 1, 2, 0);
        Map<Integer, Player> map = new HashMap<>();
        final int[] positions = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.leftWing, R.id.rightWing, R.id.leftCentreForward, R.id.rightCentreForward};
        for (int i = 0; i < 11; i++) {
            map.put(positions[i], players.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(11, result.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), result.get(i));
        }
    }

    /**
     * Test orderPlayers on lineup with formation 3-2-3-2.
     */
    @Test
    public void testOrderPlayersOn3_2_3_2() {
        List<Player> players = this.generatePlayers(3, 0, 0, 0, 2, 1, 1, 1, 2, 0);
        Map<Integer, Player> map = new HashMap<>();
        final int[] positions = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.centreCentreBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
                R.id.leftCentreForward, R.id.rightCentreForward};
        for (int i = 0; i < 11; i++) {
            map.put(positions[i], players.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(11, result.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), result.get(i));
        }
    }

    /**
     * Test orderPlayers on lineup with formation 4-2-3-1.
     */
    @Test
    public void testOrderPlayersOn4_2_3_1() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 2, 1, 1, 1, 1, 0);
        Map<Integer, Player> map = new HashMap<>();
        final int[] positions = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.attackingMidfield, R.id.leftWing, R.id.rightWing, R.id.centreCentreForward};
        for (int i = 0; i < 11; i++) {
            map.put(positions[i], players.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(11, result.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), result.get(i));
        }
    }

    /**
     * Test orderPlayers on lineup with formation 4-3-3.
     */
    @Test
    public void testOrderPlayersOn4_3_3() {
        List<Player> players = this.generatePlayers(2, 1, 1, 0, 3, 0, 0, 0, 3, 0);
        Map<Integer, Player> map = new HashMap<>();
        final int[] positions = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.centreCentreMidfield, R.id.leftCentreForward, R.id.rightCentreForward,
                R.id.centreCentreForward};
        for (int i = 0; i < 11; i++) {
            map.put(positions[i], players.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(11, result.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), result.get(i));
        }
    }
}