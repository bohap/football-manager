package com.android.finki.mpip.footballdreamteam.ui.presenter;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.android.finki.mpip.footballdreamteam.database.service.PositionDBService;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;
import com.android.finki.mpip.footballdreamteam.model.User;
import com.android.finki.mpip.footballdreamteam.model.helpers.SerializableList;
import com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils;
import com.android.finki.mpip.footballdreamteam.utility.LineupUtils.FORMATION;
import com.android.finki.mpip.footballdreamteam.utility.PlayerUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils;
import com.android.finki.mpip.footballdreamteam.utility.PositionUtils.POSITION_PLACE;
import com.android.finki.mpip.footballdreamteam.utility.validator.LineupPlayerValidator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.FORMATION_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_EDITABLE_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LINEUP_PLAYERS_KEY;
import static com.android.finki.mpip.footballdreamteam.ui.component.LineupFormationView.LIST_PLAYERS_KEY;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.formation_3_2_3_2_resourcesIds;
import static com.android.finki.mpip.footballdreamteam.utility.PositionUtils.resourcesIds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Borce on 18.08.2016.
 */
public class LineupFormationViewPresenterTest {

    @Mock
    private PositionDBService positionDBService;

    @Mock
    private LineupFormationView view;

    @Mock
    private LineupUtils lineupUtils;

    @Mock
    private PlayerUtils playerUtils;

    @Mock
    private PositionUtils positionUtils;

    @Mock
    private LineupPlayerValidator validator;

    @Captor
    private ArgumentCaptor<LineupPlayers> lineupPlayersCaptor;

    @Mock
    private Bundle args;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    @Captor
    private ArgumentCaptor<int[]> intArrayCaptor;

    @Captor
    private ArgumentCaptor<POSITION_PLACE> placeCaptor;

    private LineupFormationViewPresenter presenter;

//    private int[] positionsResourcesIds = {R.id.keeper, R.id.leftCentreBack,
//            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
//            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
//            R.id.centreCentreForward};
//    private int[] unUsedPositionsResourcesIds = {R.id.centreCentreBack,
//            R.id.leftCentreForward, R.id.rightCentreForward};

    private List<Position> positions;
    private List<Player> players;
    private List<Player> notElevenPlayers;
    private Map<Integer, Player> mappedPlayers;
    private Map<Integer, Player> generatedMapPlayers;
    private int[] resourceIds = formation_3_2_3_2_resourcesIds;
    private int[] unUsedPositionsResourcesIds;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.initMocks();
        presenter = new LineupFormationViewPresenter(view, positionDBService, lineupUtils,
                                                     playerUtils, positionUtils, validator);
    }

    /**
     * Init the mocks to return specific values on method calls.
     */
    @SuppressLint("UseSparseArrays")
    private void initMocks() {
        this.mockPositions();
        this.mockPlayers();
        this.mockMappedPlayers();
        this.mockGeneratedMapPlayers();
        this.setUnusedResourceIds();

        when(positionDBService.getAll()).thenReturn(positions);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                int id = (Integer) invocation.getArguments()[0];
                if (id < 1 || id > 11) {
                    return null;
                }
                return positions.get(id - 1);
            }
        }).when(positionDBService).get(anyInt());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
//                LineupPlayers lineupPlayers = (LineupPlayers) invocation.getArguments()[0];
//                mappedPlayers = new HashMap<>();
//                for (int i = 0; i < 11; i++) {
//                    mappedPlayers.put(positionsResourcesIds[i], players.get(0));
//                }
//                lineupPlayers.setMappedPlayers(mappedPlayers);
//                return lineupPlayers;
                return mappedPlayers;
            }
        }).when(lineupUtils).mapPlayers(any(FORMATION.class), anyListOf(Player.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
//                generatedMapPlayers = new HashMap<>();
//                for (int i = 0; i < 11; i++) {
//                    if (i > notElevenPlayers.size() - 1) {
//                        generatedMapPlayers.put(positionsResourcesIds[i], new Player());
//                    } else {
//                        generatedMapPlayers.put(positionsResourcesIds[i], notElevenPlayers.get(i));
//                    }
//                }
//                return generatedMapPlayers;
                return generatedMapPlayers;
            }
        }).when(lineupUtils).generateMap(any(FORMATION.class), anyListOf(Player.class));
    }

    /**
     * Create a mock list of positions.
     */
    private void mockPositions() {
        positions = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            positions.add(new Position(i, "Positions " + i));
        }
    }

    /**
     * Create a mock list of players.
     */
    private void mockPlayers() {
        this.players = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            Position position = this.positions.get(i - 1);
            LineupPlayer lineupPlayer = new LineupPlayer(0, 0, position.getId());
            this.players.add(new Player(i, 0, 0, "Player "+ i, lineupPlayer));
        }
        notElevenPlayers = this.players.subList(0, 5);
    }

    /**
     * Create a mock Map of players.
     */
    @SuppressLint("UseSparseArrays")
    private void mockMappedPlayers() {
        mappedPlayers = new HashMap<>();
        for (int i = 0; i < 11; i++) {
            mappedPlayers.put(resourceIds[i], players.get(i));
        }
    }

    /**
     * Create a mock Map of generated players.
     */
    @SuppressLint("UseSparseArrays")
    private void mockGeneratedMapPlayers() {
        generatedMapPlayers = new HashMap<>();
        for (int i = 0; i < resourceIds.length; i++) {
            if (i >= notElevenPlayers.size()) {
                generatedMapPlayers.put(resourceIds[i], new Player());
            } else {
                generatedMapPlayers.put(resourceIds[i], notElevenPlayers.get(i));
            }
        }
    }

    /**
     * Set the unused resource ids.
     */
    private void setUnusedResourceIds() {
        List<Integer> result = new ArrayList<>();
        for (int res1 : resourcesIds) {
            boolean match = false;
            for (int res2 : resourceIds) {
                if (res1 == res2) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                result.add(res1);
            }
        }
        unUsedPositionsResourcesIds = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            unUsedPositionsResourcesIds[i] = result.get(i);
        }
    }

    /**
     * Test the behavior on onViewCreated called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnFragmentCreatedOnNullParam() {
        presenter.onViewCreated(null);
    }

    /**
     * Test the behavior on onViewCreated when neither bundle lineup players or formation
     * key is set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnFragmentCreatedWithUnExistingBundleKey() {
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onViewCreated is called and the LINEUP_PLAYERS_KEY is set
     * with a null list.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithNullListOfPlayers() {
        SerializableList serList = new SerializableList(null);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onViewCreated is called and the LINEUP_PLAYERS_KEY is set
     * with a null list.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithInvalidTypeOfdListOfPlayers() {
        SerializableList serList = new SerializableList(Arrays.asList(new User(), new User()));
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onViewCreated is called and the LINEUP_PLAYERS_KEY is set
     * with a correct list of players.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testOnViewCreatedWithListOfPlayers() {
        final FORMATION formation = FORMATION.F_4_3_3;
        when(lineupUtils.getFormation(anyListOf(Player.class))).thenReturn(formation);

        SerializableList serList = new SerializableList(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();

        InOrder inOrder = inOrder(positionDBService);
        inOrder.verify(positionDBService).open();
        inOrder.verify(positionDBService).getAll();
        inOrder.verify(positionDBService).close();
        verify(positionUtils).setPositions(positions);
        verify(lineupUtils).mapPlayers(formation, players);
        verify(view).bindPlayers();
    }

    /**
     * Test the behavior on onViewCreated when formation key in the bundle is set but the
     * List of player for the formation is not.
     */
    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithFormationAndNullListOfPlayers() {
        SerializableList serList = new SerializableList(null);
        when(args.getSerializable(FORMATION_KEY)).thenReturn(FORMATION.F_4_4_2);
        when(args.getSerializable(LIST_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
    }

    /**
     * Test the behavior when onViewCreated when formation key in the bundle is set and the
     * list of players in the bundle is from incorrect type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnViewCreatedWithFormationAndInvalidTypeListOfPlayers() {
        final FORMATION formation = FORMATION.F_4_4_2;
        List<User> incorrectTypeList = Arrays.asList(new User(), new User());
        SerializableList<User> serList = new SerializableList<>(incorrectTypeList);
        when(args.getSerializable(FORMATION_KEY)).thenReturn(formation);
        when(args.getSerializable(LIST_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewLayoutCreated();
        presenter.onViewCreated(args);

        InOrder inOrder = inOrder(positionDBService);
        inOrder.verify(positionDBService).open();
        inOrder.verify(positionDBService).getAll();
        inOrder.verify(positionDBService).close();
        verify(positionUtils).setPositions(positions);
        verify(lineupUtils).generateMap(formation, players);
        verify(view).bindPlayers();
    }

    /**
     * Test the behavior when updateView is called and the lineup is valid.
     */
    @Test
    public void testUpdateViewLineupIsValid() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);
        presenter.onViewLayoutCreated();
        verify(view).bindPlayers();
        verify(validator).validate(anyListOf(LineupPlayer.class));
        verify(view).showValidLineup();
        verify(view, never()).showInvalidLineup();
    }

    /**
     * Test the behavior when updateView is called and the lineup is not valid.
     */
    @Test
    public void testUpdateViewLineupIsNotValid() {
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(false);
        presenter.onViewLayoutCreated();
        verify(view).bindPlayers();
        verify(validator).validate(anyListOf(LineupPlayer.class));
        verify(view).showInvalidLineup();
        verify(view, never()).showValidLineup();
    }

    /**
     * Test the behavior when getPlayerAt is called and the map for the players is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerAtWithNullMap() {
        presenter.getPlayerAt(positions.get(0).getId());
    }

    /**
     * Test the behavior on getPlayerAt called with un existing position map key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPlayerAtOnUnMappedPosition() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        presenter.getPlayerAt(unUsedPositionsResourcesIds[0]);
    }

    /**
     * Test the behavior on gePlayerAt when the map is not with empty players.
     */
    @Test
    public void testGetPlayerAtOnPositionWithNotEmptyPlayer() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        int positionResourceId = resourceIds[5];
        Player player = mappedPlayers.get(positionResourceId);
        when(playerUtils.getLastName(player.getName())).thenReturn(player.getName());
        String result = presenter.getPlayerAt(positionResourceId);
        assertEquals(player.getName(), result);
    }

    /**
     * Test the behavior on getPlayerAt when the map is with empty players.
     */
    @Test
    public void testGetPlayerAtOnPositionWithEmptyPlayer() {
        int positionResourceId = resourceIds[notElevenPlayers.size() + 1];
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(FORMATION_KEY)).thenReturn(FORMATION.F_3_2_3_2);
        when(args.getSerializable(LIST_PLAYERS_KEY)).thenReturn(serList);
        doThrow(IllegalArgumentException.class).when(playerUtils).getLastName(null);

        presenter.onViewCreated(args);
        String result = presenter.getPlayerAt(positionResourceId);
        assertEquals("", result);
    }

    /**
     * Test the behavior when onPlayerClick is called and map is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerClickWithNullMap() {
        presenter.onPlayerClick(positions.get(2).getId(), 0, 0);
    }

    /**
     * Test the behavior on onPlayerClick called with un existing map position.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerClickWithUnMappedPosition() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        presenter.onPlayerClick(unUsedPositionsResourcesIds[0], 0, 0);
    }

    /**
     * Test the behavior onPlayerClick called when the selected position in the
     * map has player with invalid id.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerClickWithInvalidPlayerId() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(FORMATION_KEY)).thenReturn(FORMATION.F_3_2_3_2);
        when(args.getSerializable(LIST_PLAYERS_KEY)).thenReturn(serList);

        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        int positionResourceId = resourceIds[3];
        Player player = mappedPlayers.get(positionResourceId);
        player.setId(-1);
        presenter.onPlayerClick(positionResourceId, 0, 0);
    }

    /**
     * Test the behavior on onPlayerClick when the selected position in the map
     * don't have empty player.
     */
    @Test
    public void testOnPlayerClickOnUnEmptyPlayerInTheMap() {
        final boolean editable = true;
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        when(args.getBoolean(LINEUP_EDITABLE_KEY, false)).thenReturn(editable);
        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();

        int positionResourceId = resourceIds[6];
        Player player = mappedPlayers.get(positionResourceId);
        presenter.onViewCreated(args);
        presenter.onPlayerClick(positionResourceId, 0, 0);
        verify(view).showPlayerDetailsView(player.getId(), editable);
    }

    /**
     * Test the behavior onPlayerClick when the selected position position
     * in the map has empty player.
     */
    @Test
    public void testOnPlayerClickOnEmptyPositionResource() {
        final POSITION_PLACE place = POSITION_PLACE.KEEPERS;
        final int[] aPlayersToExclude = new int[notElevenPlayers.size()];

        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(FORMATION_KEY)).thenReturn(FORMATION.F_3_2_3_2);
        when(args.getSerializable(LIST_PLAYERS_KEY)).thenReturn(serList);
        when(positionUtils.getPositionResourceIdPlace(anyInt())).thenReturn(place);

        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        int i = 0;
        for (Map.Entry<Integer, Player> entry : generatedMapPlayers.entrySet()) {
            Player player = entry.getValue();
            if (player.getId() > 0) {
                aPlayersToExclude[i++] = player.getId();
            }
        }
        presenter.onPlayerClick(resourceIds[notElevenPlayers.size() + 1], 0, 0);
        verify(view).showListPositionPlayersView(place, aPlayersToExclude, 0, 0);
    }

    /**
     * Test the behavior on updateLineupPosition when a position has't been selected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateLineupPositionOnUnSelectedPosition() {
        presenter.updateLineupPosition(new Player());
    }

    /**
     * Test the behavior when updateLineupPosition is called and the lineup is not yet valid.
     */
    @Test
    public void testUpdateLineupPositionOnInvalidLineup() {
        int positionResourceId = resourceIds[4];
        Player player = new Player(125, "Simple Player");

        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(false);

        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        reset(view);
        presenter.onPlayerClick(positionResourceId, 0, 0);
        presenter.updateLineupPosition(player);

        /* Set check for two times, because the first time is called in setFormation method */
        verify(view).bindPlayers();
        Player updatedPlayer = mappedPlayers.get(positionResourceId);
        assertSame(player, updatedPlayer);
        assertNotNull(updatedPlayer.getLineupPlayer());
        verify(view).showInvalidLineup();
        verify(view, never()).showValidLineup();
    }

    /**
     * Test the behavior when updateLineupPosition is called and the lineup is valid.
     */
    @Test
    public void testUpdatePlayerOnValidLineup() {
        int positionResourceId = resourceIds[7];
        Player player = new Player(324, "Simple Player");

        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        when(validator.validate(anyListOf(LineupPlayer.class))).thenReturn(true);

        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        reset(view);
        presenter.onPlayerClick(positionResourceId, 0, 0);
        presenter.updateLineupPosition(player);

        verify(view).bindPlayers();
        Player updatedPlayer = mappedPlayers.get(positionResourceId);
        assertSame(player, updatedPlayer);
        assertNotNull(updatedPlayer.getLineupPlayer());
        verify(view).showValidLineup();
        verify(view, never()).showInvalidLineup();
    }

    /**
     * Test that when updateLineupPlayer is called the selected lineup position is reseated.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectedPositionIsReseatedOnUpdateLineupPlayers() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        presenter.onPlayerClick(resourceIds[8], 0, 0);
        presenter.updateLineupPosition(new Player());
        presenter.updateLineupPosition(new Player());
    }

    /**
     * Test the behavior when remove selected player is called and a position
     * is not yet selected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSelectedPlayerOnUnSelectedPosition() {
        presenter.removeSelectedPlayer();
    }

    /**
     * Test the behavior when removeSelectedPlayer is called.
     */
    @Test
    public void testRemoveSelectedPlayer() {
        int positionResourceId = resourceIds[6];
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);

        presenter.onViewCreated(args);
        presenter.onViewLayoutCreated();
        reset(view);
        presenter.onPlayerClick(positionResourceId, 0, 0);
        presenter.removeSelectedPlayer();

        verify(view).bindPlayers();
        Player player = mappedPlayers.get(positionResourceId);
        assertNotNull(player);
        assertEquals(0, player.getId().intValue());
        assertEquals(null, player.getName());
    }

    /**
     * Test that when removeSelectedPlayer is called the selected position resource id is
     * reseated to -1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectedResourcePositionIsReseatedOnRemoveSelectedPlayer() {
        SerializableList<Player> serList = new SerializableList<>(players);
        when(args.getSerializable(LINEUP_PLAYERS_KEY)).thenReturn(serList);
        presenter.onViewCreated(args);
        presenter.onPlayerClick(resourceIds[4], 0, 0);
        presenter.removeSelectedPlayer();
        presenter.removeSelectedPlayer();
    }

    /**
     * Test the behavior when on player canceled is called and the
     * position resource id is not yet set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOnPlayerCanceledOnUnSelectedPosition() {
        presenter.onPlayerSelectCanceled();
    }

    /**
     * Test that the selected position resource id is reseated to -1
     * when onPlayerSelectCanceled is called.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectedPositionIsReseatedOnPlayerSelectCanceled() {
        presenter.onPlayerSelectCanceled();
        presenter.onPlayerSelectCanceled();
    }
}
