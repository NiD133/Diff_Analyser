package org.joda.time;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that a Seconds object representing a whole number of minutes
     * is correctly converted to a Minutes object.
     */
    @Test
    public void toStandardMinutes_convertsSecondsToEquivalentMinutes() {
        // Arrange: Create a Seconds object representing 120 seconds.
        Seconds oneHundredAndTwentySeconds = Seconds.seconds(120);
        Minutes expectedTwoMinutes = Minutes.minutes(2);

        // Act: Convert the Seconds object to standard Minutes.
        Minutes actualMinutes = oneHundredAndTwentySeconds.toStandardMinutes();

        // Assert: The result should be exactly 2 minutes.
        assertEquals(expectedTwoMinutes, actualMinutes);
    }
}