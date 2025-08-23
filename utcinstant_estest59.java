package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    /**
     * Tests that subtracting a zero duration from a UtcInstant results in an
     * equal UtcInstant. This verifies the identity property of subtraction with zero.
     */
    @Test
    public void subtractingZeroDurationShouldReturnAnEquivalentInstant() {
        // Arrange: Create an initial instant and a zero duration.
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        Duration zeroDuration = Duration.ZERO;

        // Act: Subtract the zero duration from the initial instant.
        UtcInstant resultInstant = initialInstant.minus(zeroDuration);

        // Assert: The resulting instant should be equal to the initial instant.
        assertEquals(initialInstant, resultInstant);
    }
}