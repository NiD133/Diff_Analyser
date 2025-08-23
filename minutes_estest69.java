package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    @Test
    public void minutesBetween_withSameStartAndEndInstant_shouldReturnZero() {
        // Arrange: Define two identical instants in time.
        Instant sameInstant = Instant.now();

        // Act: Calculate the number of minutes between the start and end instants.
        Minutes result = Minutes.minutesBetween(sameInstant, sameInstant);

        // Assert: The result should be zero minutes.
        assertEquals(Minutes.ZERO, result);
    }
}