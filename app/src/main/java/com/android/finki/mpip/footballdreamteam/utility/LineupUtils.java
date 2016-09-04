package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayers;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 10.08.2016.
 */
public class LineupUtils {

    private static Logger logger = LoggerFactory.getLogger(LineupUtils.class);
    private PositionUtils positionUtils;

    public LineupUtils(PositionUtils positionUtils) {
        this.positionUtils = positionUtils;
    }

    /**
     * Get the lineup formation for the given list of players.
     *
     * @param positions List of positions
     * @return Lineup formation
     */
    public LineupPlayers.FORMATION getFormation(List<Position> positions) {
        if (positions.size() != 11) {
            String message = String.format("incorrect players size, required 11, got %d",
                    positions.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        Map<Position.POSITION, Integer> mapped = positionUtils.countPositions(positions);
        int centreBacks = mapped.get(Position.POSITION.CENTRE_BACK);
        int rightBacks = mapped.get(Position.POSITION.RIGHT_BACK);
        int leftBacks = mapped.get(Position.POSITION.LEFT_BACK);
        int centreMidfield = mapped.get(Position.POSITION.CENTRE_MIDFIELD);
        int rightWings = mapped.get(Position.POSITION.RIGHT_WING);
        int leftWings = mapped.get(Position.POSITION.LEFT_WING);
        int attackingMidfield = mapped.get(Position.POSITION.ATTACKING_MIDFIELD);
        int centreForwards = mapped.get(Position.POSITION.CENTRE_FORWARD);

        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 &&
                centreMidfield == 2 && rightWings == 1 && leftWings == 1 && centreForwards == 2) {
            return LineupPlayers.FORMATION.F_4_4_2;
        }
        if (centreBacks == 3 && centreMidfield == 2 && rightWings == 1 && leftWings == 1
                && attackingMidfield == 1 && centreForwards == 2) {
            return LineupPlayers.FORMATION.F_3_2_3_2;
        }
        if (centreBacks == 1 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 2
                && rightWings == 1 && attackingMidfield == 1 && leftWings == 1 &&
                centreForwards == 2) {
            return LineupPlayers.FORMATION.F_3_2_3_2;
        }
        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 2 &&
                rightWings == 1 && leftWings == 1 && attackingMidfield == 1 && centreForwards == 1) {
            return LineupPlayers.FORMATION.F_4_2_3_1;
        }
        if (centreBacks == 2 && rightBacks == 1 && leftBacks == 1 && centreMidfield == 3 &&
                centreForwards == 3) {
            return LineupPlayers.FORMATION.F_4_3_3;
        }
        return null;
    }

    /**
     * Creates a map for the given list. The lineup formation is determined from the given list
     * and the players are put in the map depending on their lineup position which must be set.
     *
     * @param lineupPlayers Lineup players and positions.
     * @return Map where the id is the player id mapped with android positions resource
     * id and is mapped to the player position in the lineup
     */
    public LineupPlayers mapPlayers(LineupPlayers lineupPlayers) {
        if (lineupPlayers.getPlayers().size() != 11) {
            String message = String.format("incorrect players size, required 11, got %d",
                    lineupPlayers.getPlayers().size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        LineupPlayers.FORMATION formation = this.getFormation(lineupPlayers.getPositions());
        if (formation == null) {
            String message = "lineup formation can't be determined";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        lineupPlayers.setFormation(formation);
        Map<Integer, Player> map = new HashMap<>();
        for (Player player : lineupPlayers.getPlayers()) {
            if (player.getLineupPlayer() == null) {
                String message = "lineup player can't be null";
                logger.info(message);
                throw new IllegalArgumentException(message);
            }
            if (player.getLineupPlayer().getPosition() == null) {
                String message = "lineup player position can't be null";
                logger.info(message);
                throw new IllegalArgumentException(message);
            }
            switch (positionUtils.getPosition(player.getLineupPlayer().getPosition())) {
                case KEEPER:
                    map.put(R.id.keeper, player);
                    break;
                case RIGHT_BACK:
                    if (formation == LineupPlayers.FORMATION.F_3_2_3_2) {
                        if (map.get(R.id.rightCentreBack) != null) {
                            map.put(R.id.centreCentreBack, map.get(R.id.rightCentreBack));
                        }
                        map.put(R.id.rightCentreBack, player);
                    } else {
                        map.put(R.id.rightBack, player);
                    }
                    break;
                case LEFT_BACK:
                    if (formation == LineupPlayers.FORMATION.F_3_2_3_2) {
                        if (map.get(R.id.leftCentreBack) != null) {
                            map.put(R.id.centreCentreBack, map.get(R.id.leftCentreBack));
                        }
                        map.put(R.id.leftCentreBack, player);
                    } else {
                        map.put(R.id.leftBack, player);
                    }
                    break;
                case CENTRE_BACK:
                    if (map.get(R.id.leftCentreBack) == null) {
                        map.put(R.id.leftCentreBack, player);
                    } else if (map.get(R.id.rightCentreBack) == null) {
                        map.put(R.id.rightCentreBack, player);
                    } else {
                        map.put(R.id.centreCentreBack, player);
                    }
                    break;
                case DEFENSIVE_MIDFIELD:
                case CENTRE_MIDFIELD:
                    if (map.get(R.id.leftCentreMidfield) == null) {
                        map.put(R.id.leftCentreMidfield, player);
                    } else if (map.get(R.id.rightCentreMidfield) == null) {
                        map.put(R.id.rightCentreMidfield, player);
                    } else {
                        map.put(R.id.centreCentreMidfield, player);
                    }
                    break;
                case RIGHT_WING:
                    map.put(R.id.rightWing, player);
                    break;
                case LEFT_WING:
                    map.put(R.id.leftWing, player);
                    break;
                case ATTACKING_MIDFIELD:
                    map.put(R.id.attackingMidfield, player);
                    break;
                case CENTRE_FORWARD:
                case SECONDARY_FORWARD:
                    if (formation == LineupPlayers.FORMATION.F_4_2_3_1) {
                        map.put(R.id.centreCentreForward, player);
                    } else if (map.get(R.id.leftCentreForward) == null) {
                        map.put(R.id.leftCentreForward, player);
                    } else if (map.get(R.id.rightCentreForward) == null) {
                        map.put(R.id.rightCentreForward, player);
                    } else {
                        map.put(R.id.centreCentreForward, player);
                    }
                    break;
            }
        }
        lineupPlayers.setMappedPlayers(map);
        return lineupPlayers;
    }

    /**
     * Generates a map for the given formation and fills the map with the List of players.
     *
     * @param formation lineup formation
     * @param players   players that are in the lineup, not necessarily has 11 records
     * @return Map where the key is a android resource position id that is in the lineup formation
     * and the value is null
     */
    public Map<Integer, Player> generateMap(LineupPlayers.FORMATION formation,
                                           List<Player> players) {
        if (formation == null) {
            String message = "formation can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (players == null) {
            String message = "players can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        int i = 0;
        Map<Integer, Player> map = new HashMap<>();
        map.put(R.id.keeper, this.getPlayer(players, i++));
        /* Generate the defenders */
        map.put(R.id.leftCentreBack, this.getPlayer(players, i++));
        map.put(R.id.rightCentreBack, this.getPlayer(players, i++));
        if (formation == LineupPlayers.FORMATION.F_3_2_3_2) {
            map.put(R.id.centreCentreBack, this.getPlayer(players, i++));
        } else {
            map.put(R.id.leftBack, this.getPlayer(players, i++));
            map.put(R.id.rightBack, this.getPlayer(players, i++));
        }
        /* Generate the midfielders */
        map.put(R.id.leftCentreMidfield, this.getPlayer(players, i++));
        map.put(R.id.rightCentreMidfield, this.getPlayer(players, i++));
        if (formation == LineupPlayers.FORMATION.F_4_3_3) {
            map.put(R.id.centreCentreMidfield, this.getPlayer(players, i++));
        } else {
            if (formation == LineupPlayers.FORMATION.F_4_2_3_1 ||
                    formation == LineupPlayers.FORMATION.F_3_2_3_2) {
                map.put(R.id.attackingMidfield, this.getPlayer(players, i++));
            }
            map.put(R.id.leftWing, this.getPlayer(players, i++));
            map.put(R.id.rightWing, this.getPlayer(players, i++));
        }
        /* Generate the attackers. */
        if (formation != LineupPlayers.FORMATION.F_4_2_3_1) {
            map.put(R.id.leftCentreForward, this.getPlayer(players, i++));
            map.put(R.id.rightCentreForward, this.getPlayer(players, i++));
        }
        if (formation == LineupPlayers.FORMATION.F_4_2_3_1 ||
                formation == LineupPlayers.FORMATION.F_4_3_3) {
            map.put(R.id.centreCentreForward, this.getPlayer(players, i));
        }
        return map;
    }

    /**
     * Get the player from the list on the given position or if the position is bigger
     * than the list size return a empty player.
     *
     * @param players  List of players
     * @param position index in the list
     * @return player from the list or empty player
     */
    private Player getPlayer(List<Player> players, int position) {
        if (position > players.size() - 1) {
            return new Player();
        }
        return players.get(position);
    }

    /**
     * Extract the lineup players from the given map.
     *
     * @param mappedPlayers Map containing the players
     * @return List of LineupPlayer
     */
    public List<LineupPlayer> getLineupPlayers(Map<Integer, Player> mappedPlayers) {
        if (mappedPlayers.size() != 11) {
            String message = String.format("invalid map size, requested 11, got %d",
                    mappedPlayers.size());
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        for (Map.Entry<Integer, Player> entry : mappedPlayers.entrySet()) {
            Player player = entry.getValue();
            int playerId = player.getId();
            int lineupId = player.getLineupPositionId();
            lineupPlayers.add(new LineupPlayer(0, playerId, lineupId));
        }
        return lineupPlayers;
    }

    /**
     * Put all players in the map into a list ordered by their position in the lineup.
     *
     * @param playerMap Map of player in the lineup
     * @return List of players in the lineup
     */
    public List<Player> orderPlayers(Map<Integer, Player> playerMap) {
        List<Player> players = new ArrayList<>();
        for (int position : Position.resourcesIds) {
            Player player = playerMap.get(position);
            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }
}
