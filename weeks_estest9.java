package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting a period of zero weeks to standard hours
     * correctly results in zero hours.
     */
    @Test
    public void toStandardHours_forZeroWeeks_returnsZeroHours() {
        // Arrange: Define the input and the expected outcome.
        Weeks zeroWeeks = Weeks.ZERO;
        int expectedHours = 0;

        // Act: Perform the conversion from weeks to hours.
        Hours actualHours = zeroWeeks.toStandardHours();

        // Assert: Verify that the actual result matches the expected result.
        assertEquals(expectedHours, actualHours.getHours());
    }
}