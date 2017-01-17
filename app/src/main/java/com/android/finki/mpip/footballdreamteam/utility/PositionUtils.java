package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;
import com.android.finki.mpip.footballdreamteam.model.Player;
import com.android.finki.mpip.footballdreamteam.model.Position;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 10.08.2016.
 */
public class PositionUtils {

    private List<Position> positions;
    public static final int[] resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftBack, R.id.rightBack,
            R.id.leftCentreMidfield, R.id.rightCentreMidfield, R.id.centreCentreMidfield,
            R.id.attackingMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};
    public static final int[] formation_4_4_2_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward};
    public static final int[] formation_3_2_3_2_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.leftCentreForward, R.id.rightCentreForward};
    public static final int[] formation_4_2_3_1_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.centreCentreForward};
    public static final int[] formation_4_3_3_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.centreCentreMidfield, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};

    public enum POSITION_TYPE {
        KEEPER("keeper", "GK", Collections.singletonList(R.id.keeper)),
        RIGHT_BACK("right back", "RB", Collections.singletonList(R.id.rightBack)),
        LEFT_BACK("left back", "LB", Collections.singletonList(R.id.leftBack)),
        CENTRE_BACK("centre back", "CB", Arrays.asList(R.id.leftCentreBack,
                R.id.rightCentreBack, R.id.centreCentreBack)),
        DEFENSIVE_MIDFIELD("defensive midfield", "DM", Collections.<Integer>emptyList()),
        CENTRE_MIDFIELD("centre midfield", "CM", Arrays.asList(R.id.leftCentreMidfield,
                R.id.rightCentreMidfield, R.id.centreCentreMidfield)),
        ATTACKING_MIDFIELD("attacking midfield", "AM",
                Collections.singletonList(R.id.attackingMidfield)),
        RIGHT_WING("right wing", "RW", Collections.singletonList(R.id.rightWing)),
        LEFT_WING("left wing", "LW", Collections.singletonList(R.id.leftWing)),
        CENTRE_FORWARD("centre forward", "CF", Arrays.asList(R.id.leftCentreForward,
                R.id.rightCentreForward, R.id.centreCentreForward)),
        SECONDARY_FORWARD("secondary forward", "SF", Collections.<Integer>emptyList());

        private final String name;
        private final String shortName;
        private final List<Integer> resourcesIds;

        POSITION_TYPE(String name, String shortName, List<Integer> resourcesIds) {
            this.name = name;
            this.shortName = shortName;
            this.resourcesIds = resourcesIds;
        }

        public String getName() {
            return this.name;
        }

        public String getShortName() {
            return shortName;
        }

        public List<Integer> getResourcesIds() {
            return resourcesIds;
        }
    }

    public enum POSITION_PLACE {
        KEEPERS("Keepers", Collections.singletonList(R.id.keeper)),
        DEFENDERS("Defenders", Arrays.asList(R.id.leftCentreBack, R.id.rightCentreBack,
                R.id.centreCentreBack, R.id.leftBack, R.id.rightBack)),
        MIDFIELDERS("Midfielders", Arrays.asList(R.id.leftCentreMidfield, R.id.rightCentreMidfield,
                R.id.centreCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.attackingMidfield)),
        ATTACKERS("Attackers", Arrays.asList(R.id.leftCentreForward, R.id.rightCentreForward,
                R.id.centreCentreForward));

        private final String name;
        private final List<Integer> resourcesIds;

        POSITION_PLACE(String name, List<Integer> resourcesIds) {
            this.name = name;
            this.resourcesIds = resourcesIds;
        }

        public String getName() {
            return this.name;
        }

        public List<Integer> getResourcesIds() {
            return resourcesIds;
        }
    }

    /**
     * Set the positions for the utils.
     *
     * @param positions List of Position
     */
    public void setPositions(List<Position> positions) {
        if (positions == null) {
            throw new IllegalArgumentException("positions can't be null");
        }
        this.positions = positions;
    }

    /**
     * Get the position type for the given position.
     *
     * @param position Position that will be searched
     * @return position type
     */
    POSITION_TYPE getPositionType(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("position can't be null");
        }
        if (position.getName() == null) {
            throw new IllegalArgumentException("position name can't be null");
        }
        for (POSITION_TYPE positionType : POSITION_TYPE.values()) {
            if (position.getName().toLowerCase().equals(positionType.getName())) {
                return positionType;
            }
        }
        throw new IllegalArgumentException(String
                .format("can't find position with id %d and name %s",
                        position.getId(), position.getName()));
    }

    /**
     * Get the position type for the position with the given id.
     *
     * @param id position id
     * @return position type
     */
    POSITION_TYPE getPositionType(int id) {
        if (positions == null) {
            throw new IllegalArgumentException("positions list is not set");
        }
        int index = positions.indexOf(new Position(id));
        if (index == -1) {
            throw new IllegalArgumentException(
                    String.format("can't find position with id %d", id));
        }
        return this.getPositionType(positions.get(index));
    }

    /**
     * Get the position type for the given position resource id.
     *
     * @param resourceId position resource id
     * @return position type
     */
    POSITION_TYPE getPositionResourceIdType(int resourceId) {
        for (POSITION_TYPE type : POSITION_TYPE.values()) {
            if (type.getResourcesIds().contains(resourceId)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String
                .format("unknown position resource id, %d", resourceId));
    }

    /**
     * Get the part of the field for the given position resource id.
     *
     * @param resourceId position resource id
     * @return position resource id place of the field
     */
    public POSITION_PLACE getPositionResourceIdPlace(int resourceId) {
        for (POSITION_PLACE place : POSITION_PLACE.values()) {
            if (place.getResourcesIds().contains(resourceId)) {
                return place;
            }
        }
        throw new IllegalArgumentException(String
                .format("unknown position resource id, %d", resourceId));
    }

    /**
     * Get the short name for the given position.
     *
     * @param position Position holding the name
     * @return position short name
     */
    public String getShortName(Position position) {
        return this.getPositionType(position).getShortName();
    }

    /**
     * Get the position id for the given position resource id.
     *
     * @param positionResourceId position resource id
     * @return position id for the given resource
     */
    public int getPositionId(int positionResourceId) {
        if (positions == null) {
            throw new IllegalArgumentException("positions list not set");
        }
        for (Position position : positions) {
            POSITION_TYPE positionType = this.getPositionType(position);
            if (positionType.getResourcesIds().contains(positionResourceId)) {
                return position.getId();
            }
        }
        throw new IllegalArgumentException(String
                .format("unknown position resource id, %d", positionResourceId));
    }

    /**
     * Checks if the given positions types are same.
     *
     * @param position1 first POSITION_TYPE
     * @param position2 second POSITION_TYPE
     * @return whatever the positions types are same
     */
    boolean samePositions(POSITION_TYPE position1, POSITION_TYPE position2) {
        return position1.equals(position2) ||
                (position1.equals(POSITION_TYPE.DEFENSIVE_MIDFIELD) &&
                        position2.equals(POSITION_TYPE.CENTRE_MIDFIELD)) ||
                (position1.equals(POSITION_TYPE.CENTRE_MIDFIELD) &&
                        position2.equals(POSITION_TYPE.DEFENSIVE_MIDFIELD)) ||
                (position1.equals(POSITION_TYPE.CENTRE_FORWARD) &&
                        position2.equals(POSITION_TYPE.SECONDARY_FORWARD)) ||
                (position1.equals(POSITION_TYPE.SECONDARY_FORWARD) &&
                        position2.equals(POSITION_TYPE.CENTRE_FORWARD));
    }

    /**
     * Count for every positions how many players in the lineup are there on that position.
     *
     * @param players List of players that will be counted
     * @return map where the key is the POSITION_TYPE and the value is the number of players
     * on that position
     */
    Map<POSITION_TYPE, Integer> countLineupPlayersPositions(List<Player> players) {
        if (players == null) {
            throw new IllegalArgumentException("players can't be null");
        }
        Map<POSITION_TYPE, Integer> result = new HashMap<>();
        result.put(POSITION_TYPE.CENTRE_BACK, 0);
        result.put(POSITION_TYPE.RIGHT_BACK, 0);
        result.put(POSITION_TYPE.LEFT_BACK, 0);
        result.put(POSITION_TYPE.CENTRE_MIDFIELD, 0);
        result.put(POSITION_TYPE.RIGHT_WING, 0);
        result.put(POSITION_TYPE.LEFT_WING, 0);
        result.put(POSITION_TYPE.ATTACKING_MIDFIELD, 0);
        result.put(POSITION_TYPE.CENTRE_FORWARD, 0);
        for (Player player : players) {
            LineupPlayer lineupPlayer = player.getLineupPlayer();
            if (lineupPlayer == null) {
                String msg = String.format("lineup player for player with id %d is not set",
                                           player.getId());
                throw new IllegalArgumentException(msg);
            }
            POSITION_TYPE positionType;
            if (lineupPlayer.getPosition() != null &&
                    lineupPlayer.getPosition().getName() != null) {
                positionType = this.getPositionType(lineupPlayer.getPosition());
            } else {
                positionType = this.getPositionType(lineupPlayer.getPositionId());
            }
            if (positionType.equals(POSITION_TYPE.DEFENSIVE_MIDFIELD)) {
                positionType = POSITION_TYPE.CENTRE_MIDFIELD;
            }
            if (positionType.equals(POSITION_TYPE.SECONDARY_FORWARD)) {
                positionType = POSITION_TYPE.CENTRE_FORWARD;
            }
            if (result.get(positionType) != null) {
                int value = result.get(positionType);
                result.put(positionType, value + 1);
            }
        }
        return result;
    }
}