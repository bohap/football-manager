package com.android.finki.mpip.footballdreamteam.utility.validator;

import com.android.finki.mpip.footballdreamteam.model.LineupPlayer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Borce on 04.08.2016.
 */
public class LineupPlayerValidatorTest {

    private LineupPlayerValidator validator = new LineupPlayerValidator();

    /**
     * Generate a List of LineupPlayers.
     *
     * @param lineupId     the id of the lineup
     * @param playersIds   array of players ids that are in the lineup
     * @param positionsIds array of positions ids that are in the lineup
     * @return List of LineupPlayers
     */
    private List<LineupPlayer> generateLineupPlayers(int lineupId, int[] playersIds,
                                                     int[] positionsIds) {
        List<LineupPlayer> result = new ArrayList<>();
        assertEquals(playersIds.length, positionsIds.length);
        for (int i = 0; i < playersIds.length; i++) {
            result.add(new LineupPlayer(lineupId, playersIds[i], positionsIds[i]));
        }
        return result;
    }

    /**
     * Test the behavior on validate method called with list size different then 11.
     */
    @Test
    public void testValidateOnInvalidListSize() {
        List<LineupPlayer> lineupPlayers =
                this.generateLineupPlayers(1, new int[]{1, 2, 3}, new int[]{1, 1, 1});
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with invalid player id.
     */
    @Test
    public void testValidateOnInvalidPlayerId() {
        final int[] playersIds = {1, 2, 3, 4, 5, 0, 7, 8, 9, 10, 11};
        final int[] positionsIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with invalid position id.
     */
    @Test
    public void testValidateOnInvalidPositionId() {
        final int[] playersIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        final int[] positionsIds = {1, 2, 3, 4, 5, 6, 7, 0, 9, 10, 11};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with two players that have equal ids.
     */
    @Test
    public void testValidateWithTwoEqualsPlayersId() {
        final int[] playersIds = {1, 2, 3, 4, 5, 6, 7, 8, 1, 10, 11};
        final int[] positionsIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test the behavior on validate method called with more then two players that have equal ids.
     */
    @Test
    public void testValidateWithMoreThenTwoEqualsPlayersId() {
        final int[] playersIds = {1, 2, 3, 4, 5, 6, 7, 8, 8, 2, 3};
        final int[] positionsIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertFalse(validator.validate(lineupPlayers));
    }

    /**
     * Test that validate method returns true when the given List of players has equal
     * positions ids.
     */
    @Test
    public void testValidateOnEqualsPositionsIds() {
        final int[] playersIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        final int[] positionsIds = {1, 1, 1, 2, 3, 4, 5, 6, 6, 7, 8};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertTrue(validator.validate(lineupPlayers));
    }

    /**
     * Test that validate method returns true when the given List of players is valid.
     */
    @Test
    public void testSuccessValidate() {
        final int[] playersIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        final int[] positionsIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        List<LineupPlayer> lineupPlayers = this.generateLineupPlayers(1, playersIds, positionsIds);
        assertTrue(validator.validate(lineupPlayers));
    }
}