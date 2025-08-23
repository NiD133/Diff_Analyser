package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test cases for the factory methods of the {@link Minutes} class.
 */
public class MinutesTest {

    //-----------------------------------------------------------------------
    // Test factory method: minutes(int)
    //-----------------------------------------------------------------------

    /**
     * Tests that the minutes(int) factory method returns cached, singleton
     * instances for commonly used constant values.
     */
    @Test
    public void minutes_whenCalledWithConstantValue_returnsCachedInstance() {
        assertSame("Should return ZERO singleton for 0", Minutes.ZERO, Minutes.minutes(0));
        assertSame("Should return ONE singleton for 1", Minutes.ONE, Minutes.minutes(1));
        assertSame("Should return TWO singleton for 2", Minutes.TWO, Minutes.minutes(2));
        assertSame("Should return THREE singleton for 3", Minutes.THREE, Minutes.minutes(3));
        assertSame("Should return MAX_VALUE singleton", Minutes.MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE));
        assertSame("Should return MIN_VALUE singleton", Minutes.MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE));
    }

    /**
     * Tests that the minutes(int) factory method creates a new instance with the
     * correct value for inputs that do not have a cached constant.
     */
    @Test
    public void minutes_whenCalledWithNonConstantValue_returnsCorrectInstance() {
        assertEquals("Should create a new instance for -1", -1, Minutes.minutes(-1).getMinutes());
        assertEquals("Should create a new instance for 4", 4, Minutes.minutes(4).getMinutes());
    }
}