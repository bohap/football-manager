package com.android.finki.mpip.footballdreamteam.utility.validator;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerValidator {

    private Logger logger = LoggerFactory.getLogger(LineupPlayerValidator.class);

    /**
     * Test that the data in the given list of lineups players is valid.
     *
     * @param lineupPlayers List to be validated.
     * @return whatever the List of LineupPlayers is valid.
     */
    public boolean validate(List<LineupPlayer> lineupPlayers) {
        if (lineupPlayers.size() != 11) {
            String message = String.format("a lineup must contains 11 players, " +
                    "the given list contains %d players", lineupPlayers.size());
            logger.error(message);
            return false;
        }
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            if (lineupPlayer.getPlayerId() < 1) {
                String message = String.format("invalid player id, %d", lineupPlayer.getPlayerId());
                logger.error(message);
                return false;
            }
            if (lineupPlayer.getPositionId() < 1) {
                String message = String.format("invalid position id, %d",
                        lineupPlayer.getPositionId());
                logger.error(message);
                return false;
            }
        }
        Set<Integer> set = new HashSet<>();
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            set.add(lineupPlayer.getLineupId());
        }
        if (set.size() != 1) {
            String message = "all element in the List must have the same lineup id";
            logger.error(message);
            return false;
        }
        set = new HashSet<>();
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            set.add(lineupPlayer.getPlayerId());
        }
        if (set.size() != lineupPlayers.size()) {
            String message = String.format("all players must be different in a lineup, " +
                    "the given list has %d equal player id", lineupPlayers.size() - set.size());
            logger.error(message);
            return false;
        }
        return true;
    }
}