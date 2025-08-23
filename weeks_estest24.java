package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Verifies that converting a period of zero minutes to standard weeks
     * results in a Weeks object representing zero weeks.
     */
    @Test
    public void getWeeks_shouldReturnZero_whenCreatedFromZeroMinutes() {
        // Arrange: Define the input and the expected outcome.
        Minutes zeroMinutes = Minutes.ZERO;
        int expectedWeeks = 0;

        // Act: Perform the action under test - converting minutes to weeks.
        Weeks actualWeeks = zeroMinutes.toStandardWeeks();

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedWeeks, actualWeeks.getWeeks());
    }
}