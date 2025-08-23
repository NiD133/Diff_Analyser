package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    /**
     * Verifies that the predefined constants in the Minutes class hold their
     * expected integer values.
     */
    @Test
    public void testConstants() {
        assertEquals("Minutes.ZERO should be 0", 0, Minutes.ZERO.getMinutes());
        assertEquals("Minutes.ONE should be 1", 1, Minutes.ONE.getMinutes());
        assertEquals("Minutes.TWO should be 2", 2, Minutes.TWO.getMinutes());
        assertEquals("Minutes.THREE should be 3", 3, Minutes.THREE.getMinutes());
        assertEquals("Minutes.MAX_VALUE should be Integer.MAX_VALUE", Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals("Minutes.MIN_VALUE should be Integer.MIN_VALUE", Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }
}