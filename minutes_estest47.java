package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Minutes} class, focusing on comparison logic.
 */
public class MinutesTest {

    @Test
    public void isGreaterThan_shouldReturnTrue_whenComparingLargerToSmaller() {
        // Arrange: Create two Minutes instances where one is clearly larger than the other.
        Minutes twoMinutes = Minutes.TWO;
        Minutes zeroMinutes = Minutes.ZERO;

        // Act: Call the isGreaterThan method to perform the comparison.
        boolean result = twoMinutes.isGreaterThan(zeroMinutes);

        // Assert: Verify that the result is true, as 2 minutes is greater than 0 minutes.
        assertTrue("Two minutes should be considered greater than zero minutes.", result);
    }
}