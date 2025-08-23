package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite contains tests for the {@link CachedDateTimeZone} class.
 * This particular test file focuses on the behavior of the equals() method.
 */
public class CachedDateTimeZone_ESTestTest25 { // Retaining original class name for context

    /**
     * Tests that the equals() method is asymmetric when comparing a cached zone
     * with its original, uncached version.
     *
     * This asymmetry exists because:
     * 1. `DateTimeZone.equals()` only checks the ID, so `uncached.equals(cached)` returns true.
     * 2. `CachedDateTimeZone.equals()` requires the other object to also be a `CachedDateTimeZone`,
     *    so `cached.equals(uncached)` returns false.
     */
    @Test
    public void equalsIsAsymmetricWhenComparingCachedAndUncachedZones() {
        // Arrange: Use a specific, non-fixed time zone to ensure the test is deterministic.
        // "America/New_York" is a complex zone with daylight saving transitions, making it a good candidate.
        DateTimeZone uncachedZone = DateTimeZone.forID("America/New_York");
        CachedDateTimeZone cachedZone = CachedDateTimeZone.forZone(uncachedZone);

        // Sanity check to ensure they represent the same zone ID.
        assertEquals("A cached zone should have the same ID as its uncached source.",
                uncachedZone.getID(), cachedZone.getID());

        // Act & Assert

        // The base DateTimeZone.equals() implementation compares only the ID.
        // Since CachedDateTimeZone is a DateTimeZone, this comparison succeeds.
        assertTrue("An uncached zone should be equal to its cached wrapper.",
                uncachedZone.equals(cachedZone));

        // The CachedDateTimeZone.equals() implementation checks if the other object
        // is also an instance of CachedDateTimeZone, which is false here.
        assertFalse("A cached zone should NOT be equal to its unwrapped, uncached source.",
                cachedZone.equals(uncachedZone));
    }
}