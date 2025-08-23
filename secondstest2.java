package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link Seconds} class, focusing on the factory methods.
 */
public class SecondsTest {

    /**
     * Tests that the {@code seconds(int)} factory method returns cached singleton instances
     * for commonly used values (0, 1, 2, 3) and boundary values.
     * This caching is an important performance optimization.
     */
    @Test
    public void secondsFactory_forSpecialValues_returnsCachedInstances() {
        assertSame("Should return ZERO singleton for 0", Seconds.ZERO, Seconds.seconds(0));
        assertSame("Should return ONE singleton for 1", Seconds.ONE, Seconds.seconds(1));
        assertSame("Should return TWO singleton for 2", Seconds.TWO, Seconds.seconds(2));
        assertSame("Should return THREE singleton for 3", Seconds.THREE, Seconds.seconds(3));
        assertSame("Should return MAX_VALUE singleton", Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame("Should return MIN_VALUE singleton", Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
    }

    /**
     * Tests that the {@code seconds(int)} factory method creates a new instance
     * with the correct value for inputs that do not correspond to a cached constant.
     */
    @Test
    public void secondsFactory_forOtherValues_createsInstanceWithCorrectValue() {
        assertEquals("Should create a Seconds object for -1", -1, Seconds.seconds(-1).getSeconds());
        assertEquals("Should create a Seconds object for 4", 4, Seconds.seconds(4).getSeconds());
        assertEquals("Should create a Seconds object for 5", 5, Seconds.seconds(5).getSeconds());
    }
}