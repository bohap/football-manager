package com.android.finki.mpip.footballdreamteam.utility.validator;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                logger.error(String.format("invalid player id, %d", lineupPlayer.getPlayerId()));
                return false;
            }
            if (lineupPlayer.getPositionId() < 1) {
                logger.error(String.format("invalid position id, %d",
                        lineupPlayer.getPositionId()));
                return false;
            }
        }
        Set<Integer> set = new LinkedHashSet<>();
        for (LineupPlayer lineupPlayer : lineupPlayers) {
            set.add(lineupPlayer.getPlayerId());
        }
        if (set.size() != lineupPlayers.size()) {
            logger.error(String.format("all players must be different in a lineup, " +
                    "the given list has %d equal player id", lineupPlayers.size() - set.size()));
            return false;
        }
        return true;
    }
}