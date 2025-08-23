package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void yearsBetween_withSameStartAndEndInstant_shouldReturnZero() {
        // Arrange: Define the start and end points for the period calculation.
        // In this case, they are the same instant (the Unix epoch).
        Instant sameInstant = Instant.EPOCH;

        // Act: Calculate the number of years between the start and end instants.
        Years result = Years.yearsBetween(sameInstant, sameInstant);

        // Assert: The result should be zero years.
        assertEquals(Years.ZERO, result);
    }
}