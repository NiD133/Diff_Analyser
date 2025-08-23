package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that multiplying a Weeks instance by a negative scalar
     * correctly calculates the resulting negative number of weeks.
     */
    @Test
    public void multipliedBy_withNegativeScalar_returnsCorrectNegativeResult() {
        // Arrange: Define the initial period, the multiplier, and the expected result.
        final Weeks threeWeeks = Weeks.THREE;
        final int multiplier = -617;
        final int expectedWeeks = 3 * multiplier; // Expected: -1851

        // Act: Perform the multiplication operation.
        final Weeks actualWeeks = threeWeeks.multipliedBy(multiplier);

        // Assert: Verify that the result has the expected number of weeks.
        assertEquals(expectedWeeks, actualWeeks.getWeeks());
    }
}