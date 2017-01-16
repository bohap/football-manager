package com.android.finki.mpip.footballdreamteam.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Borce on 12.08.2016.
 */
public class PlayerUtilsTest {

    private PlayerUtils utils = new PlayerUtils();

    /**
     * Test the behavior when getLastName is called with null param.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLastNameWithNullParam() {
        utils.getLastName(null);
    }

    /**
     * Test the getLastName method will return the player last name.
     */
    @Test
    public void testGetLastName() {
        String name = "Wayne Rooney";
        String result = utils.getLastName(name);
        assertNotNull(result);
        assertEquals("Rooney", result);
    }

    /**
     * Test the getLastName method works when the last name contains multiple words.
     */
    @Test
    public void testGetLastNameOnMultipleWordsLastName() {
        String name = "David de Gea";
        String result = utils.getLastName(name);
        assertNotNull(result);
        assertEquals("de Gea", result);
    }

    /**
     * Test the behavior when player name don't have empty space.
     */
    @Test
    public void testGetLastNameOnNameWithNoEmptySpace() {
        String name = "Rooney";
        String result = utils.getLastName(name);
        assertEquals(name, result);
    }
}
