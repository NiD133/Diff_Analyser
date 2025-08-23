package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void toStandardHours_withZeroMinutes_returnsZeroHours() {
        // Arrange: Start with a Minutes object representing zero minutes.
        Minutes zeroMinutes = Minutes.ZERO;
        Hours expectedHours = Hours.ZERO;

        // Act: Convert the Minutes object to Hours.
        Hours actualHours = zeroMinutes.toStandardHours();

        // Assert: The result should be an Hours object representing zero hours.
        assertEquals(expectedHours, actualHours);
    }
}