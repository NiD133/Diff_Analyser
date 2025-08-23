package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that adding a positive duration to a UtcInstant results in a later instant.
     */
    @Test
    public void plus_withPositiveDuration_createsLaterInstant() {
        // Arrange: Create an initial instant and a positive duration to add.
        // We use simple, understandable values instead of large, arbitrary "magic numbers".
        long initialMjd = 50000L; // A specific Modified Julian Day.
        long initialNanoOfDay = 1_000_000_000L; // 1 second into the day.
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);

        Duration tenSeconds = Duration.ofSeconds(10);

        // Act: Add the duration to the initial instant.
        UtcInstant resultingInstant = initialInstant.plus(tenSeconds);

        // Assert: Verify that the resulting instant is indeed after the initial one
        // and that its internal state is as expected.
        assertTrue(
            "The resulting instant should be after the initial one.",
            initialInstant.isBefore(resultingInstant)
        );

        // Also, verify the components of the new instant for correctness.
        long expectedNanoOfDay = initialNanoOfDay + tenSeconds.toNanos();
        assertEquals(
            "The Modified Julian Day should not change for this small duration.",
            initialMjd,
            resultingInstant.getModifiedJulianDay()
        );
        assertEquals(
            "The nano-of-day should be increased by the duration.",
            expectedNanoOfDay,
            resultingInstant.getNanoOfDay()
        );
    }
}