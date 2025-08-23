package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that dividing Weeks.MAX_VALUE by -1 results in a Weeks object
     * holding the correctly negated value.
     */
    @Test
    public void dividedBy_whenDividingMaxValueByNegativeOne_returnsNegatedValue() {
        // Arrange: Start with the maximum possible Weeks value.
        Weeks maxWeeks = Weeks.MAX_VALUE;
        int expectedWeeks = -Integer.MAX_VALUE;

        // Act: Divide by -1.
        Weeks result = maxWeeks.dividedBy(-1);

        // Assert: The result should hold the negated value of Integer.MAX_VALUE.
        assertEquals(expectedWeeks, result.getWeeks());
    }
}