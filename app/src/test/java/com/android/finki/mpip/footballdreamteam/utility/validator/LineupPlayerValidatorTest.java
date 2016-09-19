package com.android.finki.mpip.footballdreamteam.utility.validator;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Borce on 04.08.2016.
 */
@Ignore
public class LineupPlayerValidatorTest {

    private LineupPlayerValidator validator = new LineupPlayerValidator();

    /**
     * Test the behavior on validate method called with list size different then 11.
     */
    @Test
    public void testValidateOnInvalidListSize() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with invalid player id.
     */
    @Test
    public void testValidateDataOnInvalidPlayerId() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 4, 2));
        lineupPlayers.add(new LineupPlayer(1, 3, 5));
        lineupPlayers.add(new LineupPlayer(1, 0, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with invalid position id.
     */
    @Test
    public void testValidateDataOnInvalidPositionId() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 4, 0));
        lineupPlayers.add(new LineupPlayer(1, 3, 5));
        lineupPlayers.add(new LineupPlayer(1, 8, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 0));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with different lineups ids.
     */
    @Test
    public void testValidateOnDifferentLineupsId() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 4, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 5));
        lineupPlayers.add(new LineupPlayer(2, 8, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with different lineups ids.
     */
    @Test
    public void testValidateOnEqualsPlayersId() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 4, 8));
        lineupPlayers.add(new LineupPlayer(1, 3, 5));
        lineupPlayers.add(new LineupPlayer(1, 8, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 9));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method passes.
     */
    @Test
    public void testSuccessValidate() {
        List<LineupPlayer> lineupPlayers = new ArrayList<>();
        lineupPlayers.add(new LineupPlayer(1, 1, 1));
        lineupPlayers.add(new LineupPlayer(1, 2, 1));
        lineupPlayers.add(new LineupPlayer(1, 3, 1));
        lineupPlayers.add(new LineupPlayer(1, 69, 1));
        lineupPlayers.add(new LineupPlayer(1, 50, 8));
        lineupPlayers.add(new LineupPlayer(1, 123, 5));
        lineupPlayers.add(new LineupPlayer(1, 8, 1));
        lineupPlayers.add(new LineupPlayer(1, 9, 1));
        lineupPlayers.add(new LineupPlayer(1, 10, 9));
        lineupPlayers.add(new LineupPlayer(1, 11, 1));
        lineupPlayers.add(new LineupPlayer(1, 7, 1));
        assertTrue(validator.validate(lineupPlayers));
    }
}