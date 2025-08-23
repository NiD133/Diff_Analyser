package org.joda.time;

import junit.framework.TestCase;

/**
 * Unit tests for the {@link Seconds} class, focusing on conversions to other standard period types.
 */
public class TestSeconds extends TestCase {

    /**
     * Tests that {@link Seconds#toStandardHours()} correctly converts a duration
     * in seconds to the equivalent number of whole hours.
     */
    public void testToStandardHours() {
        // Arrange: Define a period of 2 hours, both in Seconds and Hours.
        final int hoursValue = 2;
        final Seconds twoHoursInSeconds = Seconds.seconds(hoursValue * DateTimeConstants.SECONDS_PER_HOUR);
        final Hours expectedHours = Hours.hours(hoursValue);

        // Act: Convert the Seconds object to Hours.
        final Hours actualHours = twoHoursInSeconds.toStandardHours();

        // Assert: The converted value should match the expected Hours object.
        assertEquals("Conversion from seconds to hours should be correct", expectedHours, actualHours);
    }
}