package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.ui.fragment.LineupFormationFragment;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.PlayerUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 18.08.2016.
 */
@Ignore
public class LineupFormationFragmentPresenterTest {

//    @Mock
//    private PositionDBService positionDBService;
//
//    @Mock
//    private LineupFormationFragment fragment;
//
//    @Mock
//    private LineupUtils lineupUtils;
//
//    @Mock
//    private PlayerUtils playerUtils;
//
//    @Mock
//    private PositionUtils positionUtils;
//
//    @Mock
//    private LineupPlayerValidator validator;
//
//    @Captor
//    private ArgumentCaptor<LineupPlayers> lineupPlayersCaptor;
//
//    @Mock
//    private Bundle args;
//
//    @Captor
//    private ArgumentCaptor<Integer> intCaptor;
//
//    @Captor
//    private ArgumentCaptor<int[]> intArrayCaptor;
//
//    @Captor
//    private ArgumentCaptor<PositionUtils.POSITION_PLACE> placeCaptor;
//
//    @Rule
//    public TestName testName = new TestName();
//
//    private LineupFormationViewPresenter presenter;
//
//    private int[] positionsResourcesIds = {R.id.keeper, R.id.leftCentreBack,
//            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
//            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
//            R.id.centreCentreForward};
//    private int[] unUsedPositionsResourcesIds = {R.id.centreCentreBack,
//            R.id.leftCentreForward, R.id.rightCentreForward};
//
//    private Position position1 = new Position(1, "Position 1");
//    private Position position2 = new Position(2, "Position 2");
//    private Position position3 = new Position(3, "Position 3");
//    private Position position4 = new Position(4, "Position 4");
//    private Position position5 = new Position(5, "Position 5");
//    private Position position6 = new Position(6, "Position 6");
//    private Position position7 = new Position(7, "Position 7");
//    private Position position8 = new Position(8, "Position 8");
//    private Position position9 = new Position(9, "Position 9");
//    private Position position10 = new Position(10, "Position 10");
//    private Position position11 = new Position(11, "Position 11");
//    private Position unExistingPosition = new Position(12, "Un Existing Position");
//    private List<Position> positions = Arrays.asList(position1, position2, position3, position4,
//            position5, position6, position7, position8, position9, position10, position11);
//    private List<Player> players = Arrays.asList(
//            new Player(1, 0, 0, "Player 1", new LineupPlayer(0, 0, position1.getId())),
//            new Player(2, 0, 0, "Player 2", new LineupPlayer(0, 0, position2.getId())),
//            new Player(3, 0, 0, "Player 3", new LineupPlayer(0, 0, position3.getId())),
//            new Player(4, 0, 0, "Player 4", new LineupPlayer(0, 0, position4.getId())),
//            new Player(5, 0, 0, "Player 5", new LineupPlayer(0, 0, position5.getId())),
//            new Player(6, 0, 0, "Player 6", new LineupPlayer(0, 0, position6.getId())),
//            new Player(7, 0, 0, "Player 7", new LineupPlayer(0, 0, position7.getId())),
//            new Player(8, 0, 0, "Player 8", new LineupPlayer(0, 0, position8.getId())),
//            new Player(9, 0, 0, "Player 9", new LineupPlayer(0, 0, position9.getId())),
//            new Player(10, 0, 0, "Player 10", new LineupPlayer(0, 0, position10.getId())),
//            new Player(11, 0, 0, "Player 11", new LineupPlayer(0, 0, position11.getId())));
//    private List<Player> notElevenPlayers = Arrays.asList(
//            new Player(1, 0, 0, "Player 1", new LineupPlayer(0, 0, position1.getId())),
//            new Player(2, 0, 0, "Player 2", new LineupPlayer(0, 0, position2.getId())),
//            new Player(3, 0, 0, "Player 3", new LineupPlayer(0, 0, position3.getId())),
//            new Player(4, 0, 0, "Player 4", new LineupPlayer(0, 0, position4.getId())),
//            new Player(5, 0, 0, "Player 5", new LineupPlayer(0, 0, position5.getId())));
//    private Map<Integer, Player> mappedPlayers;
//    private Map<Integer, Player> mappedEmptyPlayers;
//    private Map<PositionUtils.POSITION, Integer> mappedPositions = new HashMap<>();
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        this.initMocks();
//        presenter = new LineupFormationViewPresenter(fragment, positionDBService,
//                lineupUtils, playerUtils, positionUtils, validator);
//    }
//
//    /**
//     * Init the mocks to return specific values on method calls.
//     */
//    private void initMocks() {
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                int id = (Integer) invocation.getArguments()[0];
//                if (id < 1 || id > 11) {
//                    return null;
//                }
//                return positions.get(id - 1);
//            }
//        }).when(positionDBService).get(anyInt());
//
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                LineupPlayers lineupPlayers = (LineupPlayers) invocation.getArguments()[0];
//                mappedPlayers = new HashMap<>();
//                for (int i = 0; i < 11; i++) {
//                    mappedPlayers.put(positionsResourcesIds[i], players.get(0));
//                }
//                lineupPlayers.setMappedPlayers(mappedPlayers);
//                return lineupPlayers;
//            }
//        }).when(lineupUtils).mapPlayers(any(LineupPlayers.class));
//
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//                mappedEmptyPlayers = new HashMap<>();
//                for (int i = 0; i < 11; i++) {
//                    if (i > notElevenPlayers.size() - 1) {
//                        mappedEmptyPlayers.put(positionsResourcesIds[i], new Player());
//                    } else {
//                        mappedEmptyPlayers.put(positionsResourcesIds[i], notElevenPlayers.get(i));
//                    }
//                }
//                return mappedEmptyPlayers;
//            }
//        }).when(lineupUtils).generateMap(any(LineupUtils.FORMATION.class),
//                anyListOf(Player.class), anyMapOf(PositionUtils.POSITION.class, Integer.class));
//        when(positionDBService.mapPositions()).thenReturn(mappedPositions);
//    }
//
//    /**
//     * Test the behavior on onViewCreated called with null param.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnFragmentCreatedOnNullParam() {
//        presenter.onViewCreated(null);
//    }
//
//    /**
//     * Test the behavior on onViewCreated when neither bundle lineup players or formation
//     * key is set.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnFragmentCreatedWithUnExistingBundleKey() {
//        presenter.onViewCreated(args);
//    }
//
//    /**
//     * Test the behavior on onViewCreated when formation key in the bundle is set bu the
//     * List of player for the fromation is not.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnFragmentCreatedWith() {
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_4_2);
//        presenter.onViewCreated(args);
//    }
//
//    /**
//     * Test the behavior on onViewCreated when bundle lineup players key is set
//     * with invalid players size.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testSetPlayersWithInvalidPlayersSize() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(Arrays.asList(new Player(), new Player()), false));
//        presenter.onViewCreated(args);
//    }
//
//    /**
//     * Test the behavior when setPlayers is called with a player that has a null lineup player.
//     */
//    @Test
//    public void testSetPlayersOnPlayerWithNullLineupPlayer() {
//        final int position = 3;
//        players.get(position).setLineupPlayer(null);
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        try {
//            presenter.onViewCreated(args);
//            fail();
//        } catch (IllegalArgumentException exp) {
//            verify(positionDBService).open();
//            verify(positionDBService, times(position)).get(anyInt());
//            verify(positionDBService).close();
//        }
//    }
//
//    /**
//     * Test the behavior on onViewCreated when bundle lineup players key is set
//     * with un existing position id.
//     */
//    @Test
//    public void testSetPlayersWithUnExistingPositionId() {
//        final int position = 5;
//        players.get(position).getLineupPlayer().setPositionId(unExistingPosition.getId());
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        try {
//            presenter.onViewCreated(args);
//            fail();
//        } catch (IllegalArgumentException exp) {
//            verify(positionDBService).open();
//            verify(positionDBService, times(position + 1)).get(anyInt());
//            verify(positionDBService).close();
//        }
//    }
//
//    /**
//     * Test the behavior on onViewCreated when bundle lineup players key is set.
//     */
//    @Test
//    public void testSetPlayers() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        verify(positionDBService).open();
//        verify(positionDBService).close();
//        verify(lineupUtils).mapPlayers(lineupPlayersCaptor.capture());
//        verify(positionDBService, times(players.size())).get(anyInt());
//        List<Player> players = lineupPlayersCaptor.getValue().getPlayers();
//        assertEquals(this.players, players);
//        for (int i = 0; i < players.size(); i++) {
//            assertEquals(this.players.get(i).getId(), players.get(i).getId());
//            assertEquals(this.players.get(i).getName(), players.get(i).getName());
//            assertNotNull(players.get(i).getLineupPlayer());
//            assertNotNull(players.get(i).getLineupPlayer().getPosition());
//        }
//        List<Position> positions = lineupPlayersCaptor.getValue().getPositions();
//        assertEquals(this.positions.size(), positions.size());
//        for (int i = 0; i < positions.size(); i++) {
//            assertSame(positions.get(i), lineupPlayersCaptor.getValue().getPositions().get(i));
//        }
//    }
//
//    /**
//     * Test the behavior on setFormation called with a player that has a null lineup player.
//     */
//    @Test
//    public void testSetFormationOnPlayerWithNullLineupPlayer() {
//        final int position = 2;
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        notElevenPlayers.get(position).setLineupPlayer(null);
//        try {
//            presenter.onViewCreated(args);
//            fail();
//        } catch (IllegalArgumentException exp) {
//            verify(positionDBService).open();
//            verify(positionDBService, times(position)).get(anyInt());
//            verify(positionDBService).close();
//        }
//    }
//
//    /**
//     * Test the behavior when setFormation is called with a player that has
//     * un existing lineup position.
//     */
//    @Test
//    public void testSetFormationOnUnExistingPlayerPosition() {
//        final int position = 3;
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_3_3);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        notElevenPlayers.get(position).getLineupPlayer()
//                .setPositionId(unExistingPosition.getId());
//        try {
//            presenter.onViewCreated(args);
//            fail();
//        } catch (IllegalArgumentException exp) {
//            verify(positionDBService).open();
//            verify(positionDBService, times(position + 1)).get(anyInt());
//            verify(positionDBService).close();
//        }
//    }
//
//    /**
//     * Test that setFormation method works.
//     */
//    @Test
//    public void testSetFormation() {
//        final LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_2_3_1;
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY)).thenReturn(formation);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        presenter.onViewCreated(args);
//        verify(positionDBService).open();
//        verify(positionDBService, times(notElevenPlayers.size())).get(anyInt());
//        verify(positionDBService).close();
//        verify(lineupUtils).generateMap(formation, notElevenPlayers, mappedPositions);
//    }
//
//    /**
//     * Test the behavior on setFormation called with a player that has already set lineup position.
//     */
//    @Test
//    public void testSetFormationOnPlayerWithSetLineupPosition() {
//        final LineupUtils.FORMATION formation = LineupUtils.FORMATION.F_4_3_3;
//        notElevenPlayers.get(3).getLineupPlayer().setPosition(position1);
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY)).thenReturn(formation);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        presenter.onViewCreated(args);
//        verify(positionDBService).open();
//        verify(positionDBService, times(notElevenPlayers.size() - 1)).get(anyInt());
//        verify(positionDBService).close();
//        verify(lineupUtils).generateMap(formation, notElevenPlayers, mappedPositions);
//    }
//
//    /**
//     * Test the behavior when setFormation is called and the lineup is not yet valid.
//     */
//    @Test
//    public void testSetFormationOnInvalidLineup() {
//        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(false);
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        presenter.onViewCreated(args);
////        verify(fragment).lineupInvalid();
//    }
//
//    /**
//     * Test the behavior when setFormation is called and the lineup is valid.
//     */
//    @Test
//    public void testSetFormationOnValidLineup() {
//        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        presenter.onViewCreated(args);
////        verify(fragment).lineupValid();
//    }
//
//    /**
//     * Test the behavior when getPlayerAt is called and the map for the players is not yet set.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetPlayerAtWithNullMap() {
//        presenter.getPlayerAt(position1.getId());
//    }
//
//    /**
//     * Test the behavior on getPlayerAt called with un existing position map key.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testGetPlayerAtOnUnMappedPosition() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        presenter.getPlayerAt(unUsedPositionsResourcesIds[0]);
//    }
//
//    /**
//     * Test the behavior on gePlayerAt when the map is not with empty players.
//     */
//    @Test
//    public void testGetPlayerAtOnPositionWithNotEmptyPlayer() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        int positionResourceId = positionsResourcesIds[5];
//        Player player = mappedPlayers.get(positionResourceId);
//        when(playerUtils.getLastName(player.getName())).thenReturn(player.getName());
//        String result = presenter.getPlayerAt(positionResourceId);
//        assertEquals(player.getName(), result);
//    }
//
//    /**
//     * Test the behavior on getPlayerAt when the map is with empty players.
//     */
//    @Test
//    public void testGetPlayerAtOnPositionWithEmptyPlayer() {
//        int positionResourceId = positionsResourcesIds[notElevenPlayers.size() + 1];
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        doThrow(IllegalArgumentException.class).when(playerUtils).getLastName(null);
//        presenter.onViewCreated(args);
//        String result = presenter.getPlayerAt(positionResourceId);
//        assertEquals("", result);
//    }
//
//    /**
//     * Test the behavior when onPlayerClick is called and map is not yet set.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnPlayerClickWithNullMap() {
//        presenter.onPlayerClick(position2.getId());
//    }
//
//    /**
//     * Test the behavior on onPlayerClick called with un existing map position.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnPlayerClickWithUnMappedPosition() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(unUsedPositionsResourcesIds[0]);
//    }
//
//    /**
//     * Test the behavior onPlayerClick called when the selected position in the
//     * map has player with invalid id.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnPlayerClickWithInvalidPlayerId() {
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//
//        int positionResourceId = positionsResourcesIds[3];
//        Player player = mappedPlayers.get(positionResourceId);
//        player.setId(-1);
//        presenter.onPlayerClick(positionResourceId);
//    }
//
//    /**
//     * Test the behavior on onPlayerClick when the selected position in the map
//     * don't have empty player.
//     */
//    @Test
//    public void testOnPlayerClickOnUnEmptyPlayerInTheMap() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        int positionResourceId = positionsResourcesIds[6];
//        Player player = mappedPlayers.get(positionResourceId);
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionResourceId);
////        verify(fragment).showPlayerDetailsDialog(player.getId(), false);
//    }
//
//    /**
//     * Test the behavior onPlayerClick when the selected position position
//     * in the map has empty player.
//     */
//    @Test
//    public void testOnPlayerClickOnEmptyPositionResource() {
//        final PositionUtils.POSITION_PLACE place = PositionUtils.POSITION_PLACE.KEEPERS;
//        final int[] aPlayersToExclude = new int[notElevenPlayers.size()];
//        when(positionUtils.getPositionPlace(anyInt())).thenReturn(place);
//        when(args.getSerializable(LineupFormationFragment.FORMATION_KEY))
//                .thenReturn(LineupUtils.FORMATION.F_4_2_3_1);
//        when(args.getSerializable(LineupFormationFragment.LIST_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(notElevenPlayers));
//        int i = 0;
//        presenter.onViewCreated(args);
//        for (Map.Entry<Integer, Player> entry : mappedEmptyPlayers.entrySet()) {
//            Player player = entry.getValue();
//            if (player.getId() > 0) {
//                aPlayersToExclude[i++] = player.getId();
//            }
//        }
//        presenter.onPlayerClick(positionsResourcesIds[notElevenPlayers.size() + 1]);
//        verify(fragment).showListPositionPlayersView(place, aPlayersToExclude);
//    }
//
//    /**
//     * Test the behavior on updateLineupPosition when a position has't been set.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testUpdateLineupPositionOnUnSelectedPosition() {
//        presenter.updateLineupPosition(new Player());
//    }
//
//    /**
//     * Test the behavior when updateLineupPosition is called and the lineup is not yet valid.
//     */
//    @Test
//    public void testUpdateLineupPositionOnInvalidLineup() {
//        final Map<PositionUtils.POSITION, Integer> mappedPositions = new HashMap<>();
//        int positionResourceId = positionsResourcesIds[4];
//        int positionId = 10;
//        Player player = new Player(125, "Simple Player");
//
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        when(positionDBService.mapPositions()).thenReturn(mappedPositions);
//        when(positionUtils.getPositionId(positionResourceId, mappedPositions))
//                .thenReturn(positionId);
//        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(false);
//
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionResourceId);
//        presenter.updateLineupPosition(player);
//
//        /* Set check for two times, because the first time is called in setFormation method */
//        verify(positionDBService, times(2)).open();
//        verify(positionDBService, times(2)).close();
//        verify(positionDBService).mapPositions();
//        verify(fragment).bindPlayers();
//        verify(positionUtils).getPositionId(positionResourceId, mappedPositions);
//        Player updatedPlayer = mappedPlayers.get(positionResourceId);
//        assertSame(player, updatedPlayer);
//        assertNotNull(updatedPlayer.getLineupPlayer());
////        verify(fragment).lineupInvalid();
////        verify(fragment, never()).lineupValid();
//    }
//
//    /**
//     * Test the behavior when updateLineupPosition is called and the lineup is valid.
//     */
//    @Test
//    public void testUpdatePlayerOnValidLineup() {
//        final Map<PositionUtils.POSITION, Integer> mappedPositions = new HashMap<>();
//        int positionResourceId = positionsResourcesIds[7];
//        int positionId = 43;
//        Player player = new Player(324, "Simple Player");
//
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        when(positionDBService.mapPositions()).thenReturn(mappedPositions);
//        when(positionUtils.getPositionId(positionResourceId, mappedPositions))
//                .thenReturn(positionId);
//        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
//
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionResourceId);
//        presenter.updateLineupPosition(player);
//
//        verify(positionDBService, times(2)).open();
//        verify(positionDBService, times(2)).close();
//        verify(positionDBService).mapPositions();
//        verify(fragment).bindPlayers();
//        verify(positionUtils).getPositionId(positionResourceId, mappedPositions);
//        Player updatedPlayer = mappedPlayers.get(positionResourceId);
//        assertSame(player, updatedPlayer);
//        assertNotNull(updatedPlayer.getLineupPlayer());
////        verify(fragment).lineupValid();
////        verify(fragment, never()).lineupInvalid();
//    }
//
//    /**
//     * Test that when updateLineupPlayer is called the selected lineup position is reseated.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testSelectedPositionIsReseatedOnUpdateLineupPlayers() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionsResourcesIds[8]);
//        presenter.updateLineupPosition(new Player());
//        presenter.updateLineupPosition(new Player());
//    }
//
//    /**
//     * Test the behavior when removeLike selected player is called and a position
//     * is not yet selected.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testRemoveSelectedPlayerOnUnSelectedPosition() {
//        presenter.removeSelectedPlayer();
//    }
//
//    /**
//     * Test the behavior when removeSelectedPlayer is called.
//     */
//    @Test
//    public void testRemoveSelectedPlayer() {
//        int positionResourceId = positionsResourcesIds[6];
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionResourceId);
//        presenter.removeSelectedPlayer();
//
//        verify(fragment).bindPlayers();
//        Player player = mappedPlayers.get(positionResourceId);
//        assertNotNull(player);
//        assertEquals(0, player.getId().intValue());
//        assertEquals(null, player.getName());
//    }
//
//    /**
//     * Test that when removeSelectedPlayer is called the selected position resource id is
//     * reseated to -1.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testSelectedResourcePositionIsReseatedOnRemoveSelectedPlayer() {
//        when(args.getSerializable(LineupFormationFragment.LINEUP_PLAYERS_KEY))
//                .thenReturn(new LineupPlayers(players));
//        presenter.onViewCreated(args);
//        presenter.onPlayerClick(positionsResourcesIds[4]);
//        presenter.removeSelectedPlayer();
//        presenter.removeSelectedPlayer();
//    }
//
//    /**
//     * Test the behavior when on player canceled is called and the
//     * position resource id is not yet set.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testOnPlayerCanceledOnUnSelectedPosition() {
//        presenter.onPlayerSelectCanceled();
//    }
//
//    /**
//     * Test that the selected position resource id is reseated to -1
//     * when onPlayerSelectCanceled is called.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void testSelectedPositionIsReseatedOnPlayerSelectCanceled() {
//        presenter.onPlayerSelectCanceled();
//        presenter.onPlayerSelectCanceled();
//    }
}
