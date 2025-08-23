package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    @Test
    public void previousTransition_whenNoPriorTransitionExists_shouldReturnTheSameInstant() {
        // Arrange
        // A fixed time zone like UTC is guaranteed to have no transitions.
        // Using a fixed zone makes the test deterministic and clearly defines the scenario.
        DateTimeZone fixedZone = DateTimeZone.UTC;
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(fixedZone);
        
        long instant = 1L;

        // Act
        // Per the DateTimeZone contract, if no transition exists before the given
        // instant, the method should return the instant itself.
        long transitionInstant = cachedZone.previousTransition(instant);

        // Assert
        assertEquals("For a zone with no transitions, previousTransition should be a no-op.",
                instant, transitionInstant);
    }
}