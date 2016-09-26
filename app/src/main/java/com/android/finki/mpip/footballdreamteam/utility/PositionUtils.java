package com.android.finki.mpip.footballdreamteam.utility;

import com.android.finki.mpip.footballdreamteam.R;
import com.android.finki.mpip.footballdreamteam.model.Position;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Borce on 10.08.2016.
 */
public class PositionUtils {

    public static final int[] resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftBack, R.id.rightBack,
            R.id.leftCentreMidfield, R.id.rightCentreMidfield, R.id.centreCentreMidfield,
            R.id.attackingMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};
    public static final int[] formation4_4_2_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.leftWing, R.id.rightWing, R.id.leftCentreForward,
            R.id.rightCentreForward};
    public static final int[] formation3_2_3_2_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.centreCentreBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.leftCentreForward, R.id.rightCentreForward};
    public static final int[] formation4_2_3_1_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.attackingMidfield, R.id.leftWing, R.id.rightWing,
            R.id.centreCentreForward};
    public static final int[] formation4_3_3_resourcesIds = {R.id.keeper, R.id.leftCentreBack,
            R.id.rightCentreBack, R.id.leftBack, R.id.rightBack, R.id.leftCentreMidfield,
            R.id.rightCentreMidfield, R.id.centreCentreMidfield, R.id.leftCentreForward,
            R.id.rightCentreForward, R.id.centreCentreForward};

    public enum POSITION {
        KEEPER("keeper"),
        RIGHT_BACK("right back"),
        LEFT_BACK("left back"),
        CENTRE_BACK("centre back"),
        DEFENSIVE_MIDFIELD("defensive midfield"),
        CENTRE_MIDFIELD("centre midfield"),
        ATTACKING_MIDFIELD("attacking midfield"),
        RIGHT_WING("right wing"),
        LEFT_WING("left wing"),
        CENTRE_FORWARD("centre forward"),
        SECONDARY_FORWARD("secondary forward");

        private final String name;

        POSITION(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum POSITION_PLACE {
        KEEPERS("Keepers"),
        DEFENDERS("Defenders"),
        MIDFIELDERS("Midfielders"),
        ATTACKERS("Attackers");

        private  final String name;

        POSITION_PLACE(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    private static Logger logger = LoggerFactory.getLogger(PositionUtils.class);

    /**
     * Check if the position with the given id is a keeper.
     *
     * @param position Position to be checked
     * @return whatever the position is keeper
     */
    boolean isKeeper(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.KEEPER.getName());
    }

    /**
     * Check if the position with the given id is centre back.
     *
     * @param position Position to be checked
     * @return whatever the position is a centre back
     */
    boolean isCentreBack(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.CENTRE_BACK.getName());
    }

    /**
     * Check if the position with the given id is a left back.
     *
     * @param position Position to be checked
     * @return whatever the given position is a left back
     */
    boolean isRightBack(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.RIGHT_BACK.getName());
    }

    /**
     * Check if the given position is a left back.
     *
     * @param position Position to be checked
     * @return whatever the positions is a left back
     */
    boolean isLeftBack(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.LEFT_BACK.getName());
    }

    /**
     * Check if the given position is a defensive midfield.
     *
     * @param position Position to be checked
     * @return whatever the position is a centre midfield
     */
    boolean isDefensiveMidfield(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.DEFENSIVE_MIDFIELD.getName());
    }

    /**
     * Check if the position with the given id is a centre midfield.
     *
     * @param position to be checked
     * @return whatever the position is a centre midfield
     */
    boolean isCentreMidfield(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.CENTRE_MIDFIELD.getName());
    }

    /**
     * Check if the given position is a right wing.
     *
     * @param position Position to be checked
     * @return whatever the position is a left wing
     */
    boolean isLeftWing(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.LEFT_WING.getName());
    }

    /**
     * Check if the given position is a left wing.
     *
     * @param position Position to be checked
     * @return whatever the position is a left wing
     */
    boolean isRightWing(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.RIGHT_WING.getName());
    }

    /**
     * Check if hte given position is a attacking midfield.
     *
     * @param position Position to be checked
     * @return whatever the position is a attacking midfield
     */
    boolean isAttackingMidfield(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.ATTACKING_MIDFIELD.getName());
    }

    /**
     * Check if the given position is a centre forward.
     *
     * @param position Position to be checked
     * @return whatever the position is centre forward
     */
    boolean isCentreForward(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.CENTRE_FORWARD.getName());
    }

    /**
     * Checks if the given position is a secondary forward.
     *
     * @param position Position to be checked
     * @return whatever the position is a secondary forward
     */
    boolean isSecondaryForward(Position position) {
        return position != null && position.getName().toLowerCase()
                .equals(POSITION.SECONDARY_FORWARD.getName());
    }

    /**
     * Checks if the given position is in the defence.
     *
     * @param position Position to be checked
     * @return whatever the position is in the defence
     */
    boolean isInDefence(Position position) {
        return this.isRightBack(position) || this.isLeftBack(position)
                || this.isCentreBack(position);
    }

    /**
     * Check if the given position is in the midfield.
     *
     * @param position Position to be checked
     * @return whatever the position is in the midfield
     */
    boolean isInMidfield(Position position) {
        return this.isDefensiveMidfield(position) || this.isCentreMidfield(position) ||
                this.isLeftWing(position) || this.isRightWing(position) ||
                this.isAttackingMidfield(position);
    }

    /**
     * Checks if the given position is in the attack.
     *
     * @param position Position to be checked
     * @return whatever the position is in the attack
     */
    boolean isInAttack(Position position) {
        return this.isCentreForward(position) || this.isSecondaryForward(position);
    }

    /**
     * Get the position name for the given id.
     *
     * @param position Position to be checked
     * @return position name
     */
    public POSITION getPosition(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getName() == null) {
            String message = "position name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (this.isKeeper(position)) {
            return POSITION.KEEPER;
        }
        if (this.isLeftBack(position)) {
            return POSITION.LEFT_BACK;
        }
        if (this.isRightBack(position)) {
            return POSITION.RIGHT_BACK;
        }
        if (this.isCentreBack(position)) {
            return POSITION.CENTRE_BACK;
        }
        if (this.isDefensiveMidfield(position)) {
            return POSITION.DEFENSIVE_MIDFIELD;
        }
        if (this.isCentreMidfield(position)) {
            return POSITION.CENTRE_MIDFIELD;
        }
        if (this.isRightWing(position)) {
            return POSITION.RIGHT_WING;
        }
        if (this.isLeftWing(position)) {
            return POSITION.LEFT_WING;
        }
        if (this.isAttackingMidfield(position)) {
            return POSITION.ATTACKING_MIDFIELD;
        }
        if (this.isCentreForward(position)) {
            return POSITION.CENTRE_FORWARD;
        }
        if (this.isSecondaryForward(position)) {
            return POSITION.SECONDARY_FORWARD;
        }
        String message = String.format("can't find position with id %d and name %s",
                position.getId(), position.getName());
        logger.error(message);
        throw new IllegalArgumentException(message);
    }

    /**
     * Get the part on the field where the position is placed.
     *
     * @param position Position to be checked
     * @return position place on the field
     */
    POSITION_PLACE getPositionPlace(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getName() == null) {
            String message = "position name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (this.isKeeper(position)) {
            return POSITION_PLACE.KEEPERS;
        }
        if (isInDefence(position)) {
            return POSITION_PLACE.DEFENDERS;
        }
        if (isInMidfield(position)) {
            return POSITION_PLACE.MIDFIELDERS;
        }
        if (isInAttack(position)) {
            return POSITION_PLACE.ATTACKERS;
        }
        String message = "unknown position";
        logger.error(message);
        throw new IllegalArgumentException(message);
    }

    /**
     * Get the part of the field where the position is place.
     *
     * @param positionResourceId position resource id
     * @return position place of the field
     */
    public POSITION_PLACE getPositionPlace(int positionResourceId) {
        switch (positionResourceId) {
            case R.id.keeper:
                return POSITION_PLACE.KEEPERS;
            case R.id.leftCentreBack:
            case R.id.rightCentreBack:
            case R.id.centreCentreBack:
            case R.id.leftBack:
            case R.id.rightBack:
                return POSITION_PLACE.DEFENDERS;
            case R.id.leftCentreMidfield:
            case R.id.rightCentreMidfield:
            case R.id.centreCentreMidfield:
            case R.id.attackingMidfield:
            case R.id.leftWing:
            case R.id.rightWing:
                return POSITION_PLACE.MIDFIELDERS;
            case R.id.leftCentreForward:
            case R.id.rightCentreForward:
            case R.id.centreCentreForward:
                return POSITION_PLACE.ATTACKERS;
        }
        String message = "unknown position resource id";
        logger.error(message);
        throw new IllegalArgumentException(message);
    }

    /**
     * Count for every position how many ids has in the given array.
     *
     * @param positions List of positions
     * @return map in where the position is mapped with the number of ids in the array
     */
    Map<POSITION, Integer> countPositions(List<Position> positions) {
        if (positions == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        int centreBacks = 0, rightBacks = 0, leftBacks = 0, centreMidfielders = 0,
                rightWings = 0, leftWings = 0, attackingMidfielders = 0, centreForwards = 0;
        for (Position position : positions) {
            switch (this.getPosition(position)) {
                case KEEPER:
                    break;
                case CENTRE_BACK:
                    centreBacks++;
                    break;
                case RIGHT_BACK:
                    rightBacks++;
                    break;
                case LEFT_BACK:
                    leftBacks++;
                    break;
                case DEFENSIVE_MIDFIELD:
                case CENTRE_MIDFIELD:
                    centreMidfielders++;
                    break;
                case RIGHT_WING:
                    rightWings++;
                    break;
                case LEFT_WING:
                    leftWings++;
                    break;
                case ATTACKING_MIDFIELD:
                    attackingMidfielders++;
                    break;
                case CENTRE_FORWARD:
                case SECONDARY_FORWARD:
                    centreForwards++;
                    break;
                default:
                    String message = String.format("invalid position id, %d", position.getId());
                    logger.error(message);
                    throw new IllegalArgumentException(message);
            }
        }
        Map<POSITION, Integer> result = new HashMap<>();
        result.put(POSITION.CENTRE_BACK, centreBacks);
        result.put(POSITION.RIGHT_BACK, rightBacks);
        result.put(POSITION.LEFT_BACK, leftBacks);
        result.put(POSITION.CENTRE_MIDFIELD, centreMidfielders);
        result.put(POSITION.RIGHT_WING, rightWings);
        result.put(POSITION.LEFT_WING, leftWings);
        result.put(POSITION.ATTACKING_MIDFIELD, attackingMidfielders);
        result.put(POSITION.CENTRE_FORWARD, centreForwards);
        return result;
    }

    /**
     * Get the short name for the position.
     *
     * @param position Position holding the name
     * @return position short name
     */
    public String getShortName(Position position) {
        if (position == null) {
            String message = "position can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (position.getName() == null) {
            String message = "position name can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        if (this.isKeeper(position)) {
            return "GK";
        }
        if (this.isCentreBack(position)) {
            return "CB";
        }
        if (this.isLeftBack(position)) {
            return "LB";
        }
        if (this.isRightBack(position)) {
            return "RB";
        }
        if (this.isDefensiveMidfield(position)) {
            return "DM";
        }
        if (this.isCentreMidfield(position)) {
            return "CM";
        }
        if (this.isLeftWing(position)) {
            return "LW";
        }
        if (this.isRightWing(position)) {
            return "RW";
        }
        if (this.isAttackingMidfield(position)) {
            return "AM";
        }
        if (this.isCentreForward(position)) {
            return "CF";
        }
        if (this.isSecondaryForward(position)) {
            return "SF";
        }
        String message = String.format("can't find position with id %d and name %s",
                position.getId(), position.getName());
        logger.error(message);
        throw new IllegalArgumentException(message);
    }

    /**
     * Get the position id for the given position resource id.
     *
     * @param positionResourceId position resource id
     * @param mappedPositions    mapped position where the key is POSITION and
     *                           the value is positions id.
     * @return position id for the given resource
     */
    public int getPositionId(int positionResourceId,
                             Map<POSITION, Integer> mappedPositions) {
        if (mappedPositions == null) {
            String message = "mappedPositions can't be null";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
        switch (positionResourceId) {
            case R.id.keeper:
                return mappedPositions.get(POSITION.KEEPER);
            case R.id.leftCentreBack:
            case R.id.rightCentreBack:
            case R.id.centreCentreBack:
                return mappedPositions.get(POSITION.CENTRE_BACK);
            case R.id.leftBack:
                return mappedPositions.get(POSITION.LEFT_BACK);
            case R.id.rightBack:
                return mappedPositions.get(POSITION.RIGHT_BACK);
            case R.id.leftCentreMidfield:
            case R.id.rightCentreMidfield:
            case R.id.centreCentreMidfield:
                return mappedPositions.get(POSITION.CENTRE_MIDFIELD);
            case R.id.leftWing:
                return mappedPositions.get(POSITION.LEFT_WING);
            case R.id.rightWing:
                return mappedPositions.get(POSITION.RIGHT_WING);
            case R.id.attackingMidfield:
                return mappedPositions.get(POSITION.ATTACKING_MIDFIELD);
            case R.id.leftCentreForward:
            case R.id.rightCentreForward:
            case R.id.centreCentreForward:
                return mappedPositions.get(POSITION.CENTRE_FORWARD);
        }
        String message = "unknown position resource id";
        logger.error(message);
        throw new IllegalArgumentException(message);
    }
}