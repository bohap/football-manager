package com.android.finki.mpip.footballdreamteam.utility;

import android.annotation.SuppressLint;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Lineup;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_TYPE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 10.08.2016.
 */
public class LineupUtilsTest {

    @Mock
    protected PositionUtils positionUtils;

    private LineupUtils utils;

    private Position posKeeper = new Position(1, "Keeper");
    private Position posCentreBack = new Position(2, "Centre Back");
    private Position posRightBack = new Position(3, "Right Back");
    private Position posLeftBack = new Position(4, "Left Back");
    private Position posDefensiveMidfield = new Position(5, "Defensive Midfield");
    private Position posCentreMidfield = new Position(6, "Centre Midfield");
    private Position posAttackingMidfield = new Position(7, "Attacking Midfield");
    private Position posRightWing = new Position(8, "Right Wing");
    private Position posLeftWing = new Position(9, "Left Wing");
    private Position posCentreForward = new Position(10, "Centre Forward");
    private Position posSecondaryForward = new Position(11, "Secondary Forward");
    private List<Position> positions = Arrays.asList(
            posKeeper, posCentreBack, posRightBack, posLeftBack, posDefensiveMidfield,
            posCentreMidfield, posAttackingMidfield, posRightWing, posLeftWing,
            posCentreForward, posSecondaryForward);
    private List<POSITION_TYPE> positionTypes = Arrays.asList(
            POSITION_TYPE.KEEPER, POSITION_TYPE.CENTRE_BACK, POSITION_TYPE.RIGHT_BACK,
            POSITION_TYPE.LEFT_BACK, POSITION_TYPE.DEFENSIVE_MIDFIELD,
            POSITION_TYPE.CENTRE_MIDFIELD, POSITION_TYPE.ATTACKING_MIDFIELD,
            POSITION_TYPE.RIGHT_WING, POSITION_TYPE.LEFT_WING, POSITION_TYPE.CENTRE_FORWARD,
            POSITION_TYPE.SECONDARY_FORWARD);

    private Lineup lineup = new Lineup();
    private Player player = new Player();
    private Player keeper =
            new Player(1, 0, 0, new LineupPlayer(lineup, player, posKeeper));
    private Player leftCentreBack =
            new Player(2, 0, 0, new LineupPlayer(lineup, player, posCentreBack));
    private Player rightCentreBack =
            new Player(3, 0, 0, new LineupPlayer(lineup, player, posCentreBack));
    private Player centreCentreBack =
            new Player(4, 0, 0, new LineupPlayer(lineup, player, posCentreBack));
    private Player leftBack =
            new Player(5, 0, 0, new LineupPlayer(lineup, player, posLeftBack));
    private Player rightBack =
            new Player(6, 0, 0, new LineupPlayer(lineup, player, posRightBack));
    private Player leftCentreMidfield =
            new Player(7, 0, 0, new LineupPlayer(lineup, player, posCentreMidfield));
    private Player rightCentreMidfield =
            new Player(8, 0, 0, new LineupPlayer(lineup, player, posCentreMidfield));
    private Player centreCentreMidfield =
            new Player(9, 0, 0, new LineupPlayer(lineup, player, posCentreMidfield));
    private Player attackingMidfield =
            new Player(10, 0, 0, new LineupPlayer(lineup, player, posAttackingMidfield));
    private Player leftWing =
            new Player(11, 0, 0, new LineupPlayer(lineup, player, posLeftWing));
    private Player rightWing =
            new Player(12, 0, 0, new LineupPlayer(lineup, player, posRightWing));
    private Player rightCentreForward =
            new Player(13, 0, 0, new LineupPlayer(lineup, player, posCentreForward));
    private Player centreCentreForward =
            new Player(14, 0, 0, new LineupPlayer(lineup, player, posCentreForward));
    private Player leftCentreForward =
            new Player(15, 0, 0, new LineupPlayer(lineup, player, posCentreForward));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockPositionUtils();
        utils = new LineupUtils(positionUtils);
    }

    /**
     * Mock the PositionUtils to return specific values in method calls.
     */
    private void mockPositionUtils() {
        for (int i = 0; i < positions.size(); i++) {
            Position position = positions.get(i);
            when(positionUtils.getPositionType(position)).thenReturn(positionTypes.get(i));
            when(positionUtils.getPositionType(position.getId())).thenReturn(positionTypes.get(i));
        }
        for (POSITION_TYPE type : POSITION_TYPE.values()) {
            for (int resourceId : type.getResourcesIds()) {
                when(positionUtils.getPositionResourceIdType(resourceId)).thenReturn(type);
            }
        }
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                POSITION_TYPE type1 = (POSITION_TYPE) invocation.getArguments()[0];
                POSITION_TYPE type2 = (POSITION_TYPE) invocation.getArguments()[1];
                return type1.equals(type2);
            }
        }).when(positionUtils).samePositions(any(POSITION_TYPE.class), any(POSITION_TYPE.class));
    }

    /**
     * Generate a mock list of players.
     *
     * @return mock list of players
     */
    private List<Player> generatePlayers() {
        List<Player> result = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            result.add(new Player());
        }
        return result;
    }

    /**
     * Generate a map where the key is a POSITION_TYPE and is mapped with the number of players on
     * that position.
     *
     * @param cbs   number of centre backs
     * @param rbs   number of right backs
     * @param lbs   number of left backs
     * @param cms   number of centre midfielders
     * @param ams   number of attacking midfielders
     * @param rws   number of right wings
     * @param lws   number of left wings
     * @param cfs   number of centre forwards
     * @return      generated map
     */
    private Map<POSITION_TYPE, Integer> generateCountMap(int cbs, int rbs, int lbs, int cms,
                                                         int ams, int rws, int lws, int cfs) {
        Map<PositionUtils.POSITION_TYPE, Integer> result = new HashMap<>();
        result.put(POSITION_TYPE.CENTRE_BACK, cbs);
        result.put(POSITION_TYPE.RIGHT_BACK, rbs);
        result.put(POSITION_TYPE.LEFT_BACK, lbs);
        result.put(POSITION_TYPE.CENTRE_MIDFIELD, cms);
        result.put(POSITION_TYPE.ATTACKING_MIDFIELD, ams);
        result.put(POSITION_TYPE.RIGHT_WING, rws);
        result.put(POSITION_TYPE.LEFT_WING, lws);
        result.put(POSITION_TYPE.CENTRE_FORWARD, cfs);
        return result;
    }

    /**
     * Test the behavior when getFormation is called with null players param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormationWithNullParam() {
        utils.getFormation(null);
    }

    /**
     * Test the behavior on the getFormation method called with incorrect players size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFormationOnInvalidPlayersSize() {
        utils.getFormation(Arrays.asList(new Player(), new Player(), new Player()));
    }

    /**
     * Test that getFormation works for 4-2-2 formation.
     */
    @Test
    public void testGetFormationOnF_4_2_2() {
        List<Player> players = this.generatePlayers();
        when(positionUtils.countLineupPlayersPositions(players))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 0, 1, 1, 2));
        FORMATION formation = utils.getFormation(players);
        assertNotNull(formation);
        assertEquals(FORMATION.F_4_4_2, formation);
    }

    /**
     * Test that getFormation method returns a 3-2-3-2 formation when there are three center backs.
     */
    @Test
    public void testGetFormationOn3_2_2_2() {
        List<Player> players = this.generatePlayers();
        when(positionUtils.countLineupPlayersPositions(players))
                .thenReturn(this.generateCountMap(3, 0, 0, 2, 1, 1, 1, 2));
        FORMATION formation = utils.getFormation(players);
        assertNotNull(formation);
        assertEquals(FORMATION.F_3_2_3_2, formation);
    }

    /**
     * Test that getFormation method returns a 3-2-3-2 formation when there is only one centre,
     * left and right back.
     */
    @Test
    public void testGetFormationOn3_2_3_2WithLeftAndRightBack() {
        List<Player> players = this.generatePlayers();
        when(positionUtils.countLineupPlayersPositions(players))
                .thenReturn(this.generateCountMap(1, 1, 1, 2, 1, 1, 1, 2));
        FORMATION formation = utils.getFormation(players);
        assertNotNull(formation);
        assertEquals(FORMATION.F_3_2_3_2, formation);
    }

    /**
     * Test that getFormation works for 4-2-3-1.
     */
    @Test
    public void testGetFormationOn4_2_3_1() {
        List<Player> players = this.generatePlayers();
        when(positionUtils.countLineupPlayersPositions(players))
                .thenReturn(this.generateCountMap(2, 1, 1, 2, 1, 1, 1, 1));
        FORMATION formation = utils.getFormation(players);
        assertNotNull(formation);
        assertEquals(FORMATION.F_4_2_3_1, formation);
    }

    /**
     * Test that getFormation works for 4-3-3 lineup.
     */
    @Test
    public void testGetFormationOn4_3_3() {
        List<Player> players = this.generatePlayers();
        when(positionUtils.countLineupPlayersPositions(players))
                .thenReturn(this.generateCountMap(2, 1, 1, 3, 0, 0, 0, 3));
        FORMATION formation = utils.getFormation(players);
        assertNotNull(formation);
        assertEquals(FORMATION.F_4_3_3, formation);
    }

    /**
     * Test the behavior when mapPlayers is called with null for the players param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersWithNullPlayersParam() {
        utils.mapPlayers(FORMATION.F_3_2_3_2, null);
    }

    /**
     * Test the behavior when mapPlayers is called with invalid players size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnInvalidSize() {
        utils.mapPlayers(FORMATION.F_3_2_3_2, Arrays.asList(new Player(), new Player()));
    }

    /**
     * Test the behavior when mapPlayers is called with null formation param
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersWithNullFormation() {
        utils.mapPlayers(null, this.generatePlayers());
    }

    /**
     * Test the behavior when mapPlayers is called with a List of Players that contains a players
     * on which the lineup player field is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersOnPlayerWithNullLineupPlayer() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack, leftBack,
                rightBack, leftCentreMidfield, rightCentreMidfield, leftWing, rightWing,
                leftCentreForward, rightCentreForward);
        players.get(7).setLineupPlayer(null);
        utils.mapPlayers(FORMATION.F_4_4_2, players);
    }

    /**
     * Assert that the given List of players is correctly mapped in a map for the given
     * resources ids.
     *
     * @param resourceIds array of positions resources ids
     * @param players     List of players in the lineup
     * @param map         map containing the players for the given resource id
     */
    private void assertMappedPlayers(int[] resourceIds, List<Player> players,
                                     Map<Integer, Player> map) {
        assertEquals(11, resourceIds.length);
        assertEquals(11, players.size());
        assertEquals(11, map.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), map.get(resourceIds[i]));
        }
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 4-2-2
     * and the players order in the list is mixed.
     */
    @Test
    public void testMapPlayersOn4_4_2Formation() {
        final int[] resourceIds = {
                R.id.keeper, R.id.leftBack, R.id.leftCentreBack, R.id.rightBack, R.id.leftWing,
                R.id.rightWing, R.id.leftCentreForward, R.id.rightCentreForward,
                R.id.rightCentreBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(
                keeper, leftBack, leftCentreBack, rightBack, leftWing, rightWing,
                leftCentreForward, rightCentreForward, rightCentreBack, leftCentreMidfield,
                rightCentreMidfield);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_4_4_2, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 3-2-3-2
     * when there are three players that plays on the centre back and there is a players where
     * lineup position is null and only lineup position id is set.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithThreeCentreBackAndNullPlayerLineupPosition() {
        final int[] resourceIds = {
                R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack, R.id.centreCentreBack,
                R.id.leftWing, R.id.rightWing, R.id.leftCentreForward, R.id.rightCentreForward,
                R.id.attackingMidfield, R.id.leftCentreMidfield, R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                centreCentreBack, leftWing, rightWing, leftCentreForward, rightCentreForward,
                attackingMidfield, leftCentreMidfield, rightCentreMidfield);
        LineupPlayer lineupPlayer = players.get(4).getLineupPlayer();
        lineupPlayer.setPositionId(lineupPlayer.getPosition().getId());
        lineupPlayer.setPosition(null);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_3_2_3_2, players);
        verify(positionUtils, atLeastOnce()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 3-2-3-2
     * when there are there is a centre back, left back and right back and the centre back player
     * is placed in the list before the left back player.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithLeftAndRightBackAndCentreBeforeLeft() {
        final int[] resourceIds = {R.id.keeper, R.id.centreCentreBack, R.id.leftCentreBack,
                R.id.rightCentreBack, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
                R.id.rightCentreForward, R.id.attackingMidfield, R.id.leftCentreMidfield,
                R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(keeper, centreCentreBack, leftBack,
                rightBack, leftWing, rightWing, leftCentreForward, rightCentreForward,
                attackingMidfield, leftCentreMidfield, rightCentreMidfield);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_3_2_3_2, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 3-2-3-2
     * and there are there is centre back, left back and right back and the centre back player
     * is placed in the list before the right back player.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithLeftAndRightBackAndCentreBeforeRight() {
        final int[] resourceIds = {R.id.keeper, R.id.leftCentreBack, R.id.centreCentreBack,
                R.id.rightCentreBack, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
                R.id.rightCentreForward, R.id.attackingMidfield, R.id.leftCentreMidfield,
                R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(keeper, leftBack, centreCentreBack,
                rightBack, leftWing, rightWing, leftCentreForward, rightCentreForward,
                attackingMidfield, leftCentreMidfield, rightCentreMidfield);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_3_2_3_2, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 3-2-3-2
     * when there are there are centre back, left back and right back and the centre back player
     * is placed in the list after the left and right back player.
     */
    @Test
    public void testMapPlayersOn3_2_3_2FormationWithLeftAndRightBackAndCentreAfterRightAndLeft() {
        final int[] resourceIds = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.centreCentreBack, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
                R.id.rightCentreForward, R.id.attackingMidfield, R.id.leftCentreMidfield,
                R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(keeper, leftBack, rightBack,
                centreCentreBack, leftWing, rightWing, leftCentreForward, rightCentreForward,
                attackingMidfield, leftCentreMidfield, rightCentreMidfield);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_3_2_3_2, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the mapPlayers correctly maps the list into a map when the players formation is 4-2-3-1
     * and there is a player in the List on which the lineup position name is null.
     */
    @Test
    public void testMapPlayersWith4_2_3_1FormationAndNullPlayerLineupPositionName() {
        final int[] resourceIds = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftWing, R.id.rightWing,
                R.id.centreCentreForward, R.id.attackingMidfield, R.id.leftCentreMidfield,
                R.id.rightCentreMidfield};
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftWing, rightWing, centreCentreForward, attackingMidfield,
                leftCentreMidfield, rightCentreMidfield);
        LineupPlayer lineupPlayer = players.get(6).getLineupPlayer();
        lineupPlayer.getPosition().setName(null);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_4_2_3_1, players);
        verify(positionUtils, atLeastOnce()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test that mapPlayers correctly maps the list into a map when the players formation is 4-3-3.
     */
    @Test
    public void testMapPlayersWith4_3_3Formation() {
        final int[] resourceIds = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.centreCentreMidfield, R.id.leftCentreForward, R.id.rightCentreForward,
                R.id.centreCentreForward};
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftCentreMidfield, rightCentreMidfield, centreCentreMidfield,
                leftCentreForward, rightCentreForward, centreCentreForward);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_4_3_3, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the behavior on mapPlayers when not all players can be mapped with the resource id for
     * the given formation. This can happen if a wrong formation is given or the List not contains
     * a player that should be in the formation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMapPlayersWhenNotAllPlayersCanBeMapped() {
        final int[] resourceIds = {R.id.keeper, R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.leftWing, R.id.rightWing, R.id.leftCentreForward, R.id.rightCentreForward};
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftCentreMidfield, rightCentreMidfield, leftWing, rightWing,
                leftCentreForward, rightCentreForward);
        Map<Integer, Player> result = utils.mapPlayers(FORMATION.F_4_2_3_1, players);
        verify(positionUtils, never()).getPositionType(anyInt());
        this.assertMappedPlayers(resourceIds, players, result);
    }

    /**
     * Test the behavior when generateMap is called with null formation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMapWithNullFormation() {
        utils.generateMap(null, new ArrayList<Player>());
    }

    /**
     * Test the behavior when generateMap is called with null players.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateMapWithNullPlayers() {
        utils.generateMap(LineupUtils.FORMATION.F_4_4_2, null);
    }

    /**
     * Assert that the map is correctly generated and the players are put in the map.
     *
     * @param resourcesIds array of positions resources ids
     * @param players      players in the lineup, may not has size 11
     * @param map          generated map
     */
    private void assertGeneratedMap(int[] resourcesIds, List<Player> players,
                                    Map<Integer, Player> map) {
        assertEquals(11, resourcesIds.length);
        assertEquals(11, map.size());
        for (int i = 0; i < 11; i++) {
            Player player = map.get(resourcesIds[i]);
            assertNotNull(player);
            if (i >= players.size()) {
                assertTrue(player.same(new Player()));
            } else {
                assertSame(players.get(i), player);
            }
        }
    }

    /**
     * Test that generateMap correctly generates a map for the formation 4-4-2.
     */
    @Test
    public void testGenerateMapWith4_4_2Formation() {
        List<Player> players = Arrays.asList(keeper, leftBack, rightBack, leftCentreBack,
                rightCentreBack, leftCentreForward);
        Map<Integer, Player> result = utils.generateMap(FORMATION.F_4_4_2, players);
        this.assertGeneratedMap(PositionUtils.formation_4_4_2_resourcesIds, players, result);
    }

    /**
     * Test that generateMap correctly generates a map for the formation 3-2-3-2.
     */
    @Test
    public void testGenerateMapWith3_2_3_2Formation() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                centreCentreBack, leftCentreMidfield, rightCentreMidfield, leftWing, rightWing,
                attackingMidfield, leftCentreForward, rightCentreForward);
        Map<Integer, Player> result = utils.generateMap(FORMATION.F_3_2_3_2, players);
        this.assertGeneratedMap(PositionUtils.formation_3_2_3_2_resourcesIds, players, result);
    }

    /**
     * Test that generateMap correctly generates a map for the formation 4-2-3-1.
     */
    @Test
    public void testGenerateMapWith3_2_3_1Formation() {
        List<Player> players = Arrays.asList(keeper, leftBack, centreCentreForward);
        Map<Integer, Player> result = utils.generateMap(FORMATION.F_4_2_3_1, players);
        this.assertGeneratedMap(PositionUtils.formation_4_2_3_1_resourcesIds, players, result);
    }

    /**
     * Test that generateMap correctly generates a map for the formation 4-3-3.
     */
    @Test
    public void testGenerateMapWith4_3_3Formation() {
        List<Player> players = Arrays.asList(keeper, leftBack, rightBack, leftWing, rightWing);
        Map<Integer, Player> result = utils.generateMap(FORMATION.F_4_3_3, players);
        this.assertGeneratedMap(PositionUtils.formation_4_3_3_resourcesIds, players, result);
    }

    /**
     * Put the list of players into a map.
     *
     * @param players List of Player
     */
    @SuppressLint("UseSparseArrays")
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
    public void testGetLineupPlayersWithNullParam() {
        utils.getLineupPlayers(null);
    }

    /**
     * Test the behavior when getLineupPlayers is called with invalid map size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLineupPlayersWithInvalidMapSize() {
        List<Player> players = Arrays.asList(keeper, leftBack, rightWing);
        utils.getLineupPlayers(this.putPlayers(players));
    }

    /**
     * Test that getLineupPlayers maps correctly the given map into a list of LineupPlayer.
     */
    @Test
    public void testGetLineupPlayersMapsTheGivenMapIntoAListOfLineupPlayer() {
        List<Player> players = Arrays.asList(keeper, leftBack, rightWing, leftCentreBack,
                rightCentreBack, leftCentreMidfield, rightCentreMidfield, leftWing, rightWing,
                leftCentreForward, rightCentreForward);
        List<LineupPlayer> result = utils.getLineupPlayers(this.putPlayers(players));
        assertEquals(players.size(), result.size());
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            LineupPlayer lineupPlayer = result.get(i);
            assertNotNull(lineupPlayer);
            assertEquals(player.getLineupPlayer().getLineupId(), lineupPlayer.getLineupId());
            assertEquals(player.getLineupPlayer().getPositionId(), lineupPlayer.getPositionId());
            assertEquals(player.getLineupPlayer().getPlayerId(), lineupPlayer.getPlayerId());
        }
    }

    /**
     * Test orderPlayers on lineup with formation 4-4-2.
     */
    @SuppressLint("UseSparseArrays")
    @Test
    public void testOrderPlayersOn4_4_2() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack, leftBack,
                rightBack, leftCentreMidfield, rightCentreMidfield, leftWing, rightWing,
                leftCentreForward, rightCentreForward);
        int[] positionsResourceIds = PositionUtils.formation_4_4_2_resourcesIds;
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(positionsResourceIds[i], players.get(i));
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
    @SuppressLint("UseSparseArrays")
    @Test
    public void testOrderPlayersOn3_2_3_2() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                centreCentreBack, leftCentreMidfield, rightCentreMidfield, attackingMidfield,
                leftWing, rightWing, leftCentreForward, rightCentreForward);
        int[] positionsResourceIds = PositionUtils.formation_3_2_3_2_resourcesIds;
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(positionsResourceIds[i], players.get(i));
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
    @SuppressLint("UseSparseArrays")
    @Test
    public void testOrderPlayersOn4_2_3_1() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftCentreMidfield, rightCentreMidfield, attackingMidfield,
                leftWing, rightWing, centreCentreForward);
        int[] positionsResourceIds = PositionUtils.formation_4_2_3_1_resourcesIds;
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(positionsResourceIds[i], players.get(i));
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
    @SuppressLint("UseSparseArrays")
    @Test
    public void testOrderPlayersOn4_3_3() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftCentreMidfield, rightCentreMidfield, centreCentreMidfield,
                leftCentreForward, rightCentreForward, centreCentreForward);
        int[] positionsResourceIds = PositionUtils.formation_4_3_3_resourcesIds;
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(positionsResourceIds[i], players.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(11, result.size());
        for (int i = 0; i < 11; i++) {
            assertSame(players.get(i), result.get(i));
        }
    }

    /**
     * Test the behavior when orderPlayers is called with a List that contains a null player.
     */
    @SuppressLint("UseSparseArrays")
    @Test
    public void testOrderPlayersWithNullPlayer() {
        List<Player> players = Arrays.asList(keeper, leftCentreBack, rightCentreBack,
                leftBack, rightBack, leftCentreMidfield, centreCentreMidfield,
                leftCentreForward, rightCentreForward, centreCentreForward);
        List<Player> actualPlayers = new ArrayList<>(players);
        actualPlayers.add(3, null);
        int[] positionsResourceIds = PositionUtils.formation_4_3_3_resourcesIds;
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            map.put(positionsResourceIds[i], actualPlayers.get(i));
        }
        List<Player> result = utils.orderPlayers(map);
        assertEquals(players.size(), result.size());
        for (int i = 0; i < players.size(); i++) {
            assertSame(players.get(i), result.get(i));
        }
    }
}