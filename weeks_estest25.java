package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that the getWeeks() method on the MIN_VALUE constant
     * correctly returns Integer.MIN_VALUE.
     */
    @Test
    public void getWeeks_fromMinValue_returnsIntegerMinValue() {
        // The Weeks.MIN_VALUE constant is defined as a Weeks object
        // wrapping Integer.MIN_VALUE.
        final int expectedWeeks = Integer.MIN_VALUE;
        final Weeks minWeeks = Weeks.MIN_VALUE;

        // Act: retrieve the value from the constant
        final int actualWeeks = minWeeks.getWeeks();

        // Assert: the retrieved value should match the integer constant
        assertEquals(expectedWeeks, actualWeeks);
    }
}