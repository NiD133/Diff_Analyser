package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that the toString() method returns the correct ISO8601 period format
     * for a single week. The expected format is "PnW", where n is the number of weeks.
     */
    @Test
    public void toString_forOneWeek_shouldReturnCorrectISO8601Format() {
        // Arrange: Use the predefined constant for one week.
        Weeks oneWeek = Weeks.ONE;
        String expectedIsoFormat = "P1W";

        // Act: Call the method under test.
        String actualIsoFormat = oneWeek.toString();

        // Assert: Verify that the output string matches the expected ISO format.
        assertEquals(expectedIsoFormat, actualIsoFormat);
    }
}