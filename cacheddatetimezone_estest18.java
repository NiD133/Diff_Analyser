package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link CachedDateTimeZone} class, focusing on its handling of time zone transitions.
 */
public class CachedDateTimeZoneTest {

    /**
     * Verifies that `previousTransition` returns the given instant when no earlier
     * transitions exist in the time zone's history.
     *
     * <p>This behavior is crucial for handling dates far in the past, ensuring the method
     * terminates correctly. To create a reliable and deterministic test, we use:
     * <ul>
     *   <li>A specific, non-fixed time zone ("Europe/London") with a known history.</li>
     *   <li>An instant ({@code Long.MIN_VALUE}) guaranteed to be before any historical transition.</li>
     * </ul>
     * This approach replaces the original test's non-deterministic and undocumented exception check
     * with a test for a valid, specified behavior.
     */
    @Test
    public void previousTransition_whenInstantIsBeforeFirstTransition_returnsTheInstant() {
        // Arrange: Use a well-known, non-fixed time zone to make the test deterministic.
        // "Europe/London" has a known history of DST transitions.
        DateTimeZone londonZone = DateTimeZone.forID("Europe/London");
        DateTimeZone cachedZone = CachedDateTimeZone.forZone(londonZone);

        // An instant guaranteed to be before any historical time zone transitions.
        long instantBeforeAllTransitions = Long.MIN_VALUE;

        // Act: Request the transition prior to the earliest possible instant.
        long result = cachedZone.previousTransition(instantBeforeAllTransitions);

        // Assert: The method should return the original instant, as no earlier transition exists.
        assertEquals(instantBeforeAllTransitions, result);
    }
}