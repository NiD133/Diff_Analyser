package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Contains tests for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that calling nextTransition() with a very early instant throws an
     * IllegalArgumentException for a time zone with a known history of transitions.
     *
     * <p><b>Refactoring Rationale:</b>
     * <ul>
     *   <li><b>Determinism:</b> The original test used {@code DateTimeZone.getDefault()},
     *       making it unstable as its behavior depended on the execution environment's
     *       default time zone. For a fixed zone like UTC, no exception would be thrown.
     *       This version uses a specific, non-fixed zone ("Europe/London") to ensure
     *       the test is deterministic and reliable.</li>
     *   <li><b>Clarity of Intent:</b> The test now explicitly creates a
     *       {@code CachedDateTimeZone} instance to make it clear what class is under test.</li>
     *   <li><b>Best Practices:</b> The exception assertion was improved by replacing the
     *       empty try-catch block with the standard JUnit 4 {@code @Test(expected=...)}
     *       annotation, which is more concise and declarative.</li>
     * </ul>
     */
    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void nextTransition_forEarlyInstantInZoneWithTransitions_throwsIllegalArgumentException() {
        // Arrange: Use a specific, non-fixed time zone to ensure predictable behavior.
        // "Europe/London" has a long history of transitions (e.g., for DST).
        DateTimeZone london = DateTimeZone.forID("Europe/London");
        DateTimeZone cachedLondon = CachedDateTimeZone.forZone(london);

        // Act & Assert: Call nextTransition with an instant (-1L) that predates the
        // time zone's defined transition rules. This is expected to throw an exception.
        cachedLondon.nextTransition(-1L);
    }
}