package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the Weeks class, focusing on the factory method.
 */
public class WeeksTest {

    //-----------------------------------------------------------------------
    // Test factory method: weeks(int)
    //-----------------------------------------------------------------------

    @Test
    public void weeks_factory_returnsCachedInstancesForConstants() {
        // The weeks(int) factory is expected to return cached singleton instances
        // for commonly used values and for the min/max values.
        assertSame("0 weeks should return the ZERO singleton", Weeks.ZERO, Weeks.weeks(0));
        assertSame("1 week should return the ONE singleton", Weeks.ONE, Weeks.weeks(1));
        assertSame("2 weeks should return the TWO singleton", Weeks.TWO, Weeks.weeks(2));
        assertSame("3 weeks should return the THREE singleton", Weeks.THREE, Weeks.weeks(3));
        assertSame("Integer.MAX_VALUE should return the MAX_VALUE singleton", Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame("Integer.MIN_VALUE should return the MIN_VALUE singleton", Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));
    }

    @Test
    public void weeks_factory_createsCorrectInstanceForOtherValues() {
        // The weeks(int) factory should create a new Weeks instance
        // with the correct value for numbers that are not cached constants.
        assertEquals("Value of -1 weeks should be -1", -1, Weeks.weeks(-1).getWeeks());
        assertEquals("Value of 4 weeks should be 4", 4, Weeks.weeks(4).getWeeks());
    }
}