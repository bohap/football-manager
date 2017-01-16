package com.android.finki.mpip.footballdreamteam.utility;

import android.annotation.SuppressLint;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 10.08.2016.
 */
public class LineupUtils {

    private PositionUtils positionUtils;

    public enum FORMATION {
        F_4_4_2,
        F_3_2_3_2,
        F_4_2_3_1,
        F_4_3_3
    }

    public LineupUtils(PositionUtils positionUtils) {
        this.positionUtils = positionUtils;
    }

    /**
     * Get the lineup formation for the given list of players.
     *
     * @param players List of players that are in the lineup
     * @return Lineup formation
     */
    public FORMATION getFormation(List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("players can't be null");
        }
        if (players.size() != 11) {
            throw new IllegalArgumentException(String
                    .format("incorrect players size, required 11, got %d", players.size()));
        }
        Map<PositionUtils.POSITION_TYPE, Integer> mapped =
                positionUtils.countLineupPlayersPositions(players);
        int centreBacks = mapped.get(PositionUtils.POSITION_TYPE.CENTRE_BACK);
        int rightBacks = mapped.get(PositionUtils.POSITION_TYPE.RIGHT_BACK);
        int leftBacks = mapped.get(PositionUtils.POSITION_TYPE.LEFT_BACK);
        int centreMidfield = mapped.get(PositionUtils.POSITION_TYPE.CENTRE_MIDFIELD);
        int rightWings = mapped.get(PositionUtils.POSITION_TYPE.RIGHT_WING);
        int leftWings = mapped.get(PositionUtils.POSITION_TYPE.LEFT_WING);
        int attackingMidfield = mapped.get(PositionUtils.POSITION_TYPE.ATTACKING_MIDFIELD);
        int centreForwards = mapped.get(PositionUtils.POSITION_TYPE.CENTRE_FORWARD);

        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 &&
                centreMidfield == 2 && rightWings == 1 && leftWings == 1 && centreForwards == 2) {
            return FORMATION.F_4_4_2;
        }
        if (centreBacks == 3 && centreMidfield == 2 && rightWings == 1 && leftWings == 1
                && attackingMidfield == 1 && centreForwards == 2) {
            return FORMATION.F_3_2_3_2;
        }
        if (centreBacks == 1 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 2
                && rightWings == 1 && attackingMidfield == 1 && leftWings == 1 &&
                centreForwards == 2) {
            return FORMATION.F_3_2_3_2;
        }
        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 2 &&
                rightWings == 1 && leftWings == 1 && attackingMidfield == 1 &&
                centreForwards == 1) {
            return FORMATION.F_4_2_3_1;
        }
        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 3 &&
                centreForwards == 3) {
            return FORMATION.F_4_3_3;
        }
        throw new IllegalArgumentException("can't determine lineup formation");
    }

    /**
     * Get the positions resources ids for the given formation.
     *
     * @param formation Lineup formation
     * @return positions resources ids
     */
    private int[] getPositionsResourcesIds(FORMATION formation) {
        if (formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }
        if (formation == FORMATION.F_4_4_2) {
            return PositionUtils.formation_4_4_2_resourcesIds;
        }
        if (formation == FORMATION.F_3_2_3_2) {
            return PositionUtils.formation_3_2_3_2_resourcesIds;
        }
        if (formation == FORMATION.F_4_2_3_1) {
            return PositionUtils.formation_4_2_3_1_resourcesIds;
        }
        if (formation == FORMATION.F_4_3_3) {
            return PositionUtils.formation_4_3_3_resourcesIds;
        }
        throw new IllegalArgumentException("invalid formation");
    }

    /**
     * Creates a map for the given list. The lineup formation is determined from the given list
     * and the players are put in the map depending on their lineup position which must be set.
     *
     * @param formation Lineup formation
     * @param players   List of players that are in the lineup
     * @return Map where the id is the player id mapped with android positions resource
     * id and is mapped to the player position in the lineup
     */
    @SuppressLint("UseSparseArrays")
    public Map<Integer, Player> mapPlayers(FORMATION formation, List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("players can'e be null");
        }
        if (players.size() != 11) {
            String msg = String.format("incorrect players size, required 11, got %d",
                                       players.size());
            throw new IllegalArgumentException(msg);
        }
        int[] resourceIds = this.getPositionsResourcesIds(formation);
        Map<Integer, Player> map = new HashMap<>();
        List<Player> tPlayers = new ArrayList<>(players);
        for (int resourceId : resourceIds) {
            PositionUtils.POSITION_TYPE positionType =
                    positionUtils.getPositionResourceIdType(resourceId);
            Iterator<Player> iterator = tPlayers.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                LineupPlayer lineupPlayer = player.getLineupPlayer();
                if (lineupPlayer == null) {
                    throw new IllegalArgumentException("lineup player can't be null");
                }
                PositionUtils.POSITION_TYPE playerPositionType;
                if (lineupPlayer.getPosition() != null &&
                        lineupPlayer.getPosition().getName() != null) {
                    playerPositionType =
                            positionUtils.getPositionType(lineupPlayer.getPosition());
                } else {
                    playerPositionType =
                            positionUtils.getPositionType(lineupPlayer.getPositionId());
                }
                boolean remove = false;
                if (playerPositionType == PositionUtils.POSITION_TYPE.RIGHT_BACK &&
                        formation == FORMATION.F_3_2_3_2) {
                    if (map.get(R.id.rightCentreBack) != null) {
                        map.put(R.id.centreCentreBack, map.get(R.id.rightCentreBack));
                    }
                    map.put(R.id.rightCentreBack, player);
                    remove = true;
                } else if (playerPositionType == PositionUtils.POSITION_TYPE.LEFT_BACK &&
                        formation == FORMATION.F_3_2_3_2) {
                    if (map.get(R.id.leftCentreBack) != null) {
                        map.put(R.id.centreCentreBack, map.get(R.id.leftCentreBack));
                    }
                    map.put(R.id.leftCentreBack, player);
                    remove = true;
                } else if (positionUtils.samePositions(positionType, playerPositionType)) {
                    map.put(resourceId, player);
                    remove = true;
                }
                if (remove) {
                    iterator.remove();
                    break;
                }
            }
        }
        if (map.size() != 11) {
            throw new IllegalArgumentException("not all players can be mapped");
        }
        return map;
    }

    /**
     * Generates a map for the given formation and fills the map with the List of players.
     *
     * @param formation lineup formation
     * @param players   players that are in the lineup, not necessarily has 11 records
     * @return Map where the key is a android resource position id that is in the lineup formation
     * and the value is null
     */
    @SuppressLint("UseSparseArrays")
    public Map<Integer, Player> generateMap(FORMATION formation, List<Player> players) {
        if (formation == null) {
            throw new IllegalArgumentException("formation can't be null");
        }
        if (players == null) {
            throw new IllegalArgumentException("players can't be null");
        }
        int[] resourcesIds = this.getPositionsResourcesIds(formation);
        Map<Integer, Player> map = new HashMap<>();
        for (int i = 0; i < resourcesIds.length; i++) {
            Player player;
            if (i > players.size() - 1) {
                player = new Player();
            } else {
                player = players.get(i);
                int positionId = positionUtils.getPositionId(resourcesIds[i]);
                player.setLineupPlayer(new LineupPlayer(0, player.getId(), positionId));
            }
            map.put(resourcesIds[i], player);
        }
        return map;
    }

    /**
     * Extract the lineup players from the given map.
     *
     * @param mappedPlayers Map containing the players
     * @return List of LineupPlayer
     */
    public List<LineupPlayer> getLineupPlayers(Map<Integer, Player> mappedPlayers) {
        if (mappedPlayers == null) {
            throw new IllegalArgumentException("mapped players can't be null");
        }
        if (mappedPlayers.size() != 11) {
            throw new IllegalArgumentException(String
                    .format("invalid map size, requested 11, got %d", mappedPlayers.size()));
        }
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        for (Map.Entry<Integer, Player> entry : mappedPlayers.entrySet()) {
            Player player = entry.getValue();
            if (player.getLineupPlayer() == null) {
                throw new IllegalArgumentException("lineup player can't be null");
            }
            int lineupId = player.getLineupPlayer().getLineupId();
            int playerId = player.getLineupPlayer().getPlayerId();
            int positionId = player.getLineupPlayer().getPositionId();
            lineupPlayers.add(new LineupPlayer(lineupId, playerId, positionId));
        }
        return lineupPlayers;
    }

    /**
     * Put all players that are not empty (have id bigger than 0) in the map into a list ordered
     * by their position in the lineup.
     *
     * @param playerMap Map of player in the lineup
     * @return List of players in the lineup
     */
    public List<Player> orderPlayers(Map<Integer, Player> playerMap) {
        List<Player> players = new ArrayList<>();
        for (int position : PositionUtils.resourcesIds) {
            Player player = playerMap.get(position);
            if (player != null && player.getId() > 0) {
                players.add(player);
            }
        }
        return players;
    }
}
