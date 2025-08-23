package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link CachedDateTimeZone}.
 */
public class CachedDateTimeZoneTest {

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // To ensure tests are not affected by the system's time zone,
        // we save the original and set a consistent one (UTC) for all tests.
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(DateTimeZone.UTC);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone to avoid side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void forZone_whenCalledMultipleTimesForSameZone_shouldReturnSameInstance() {
        // This test verifies that the factory method `forZone` caches and reuses
        // the wrapper instance for the same underlying DateTimeZone.

        // Arrange: Get a base DateTimeZone instance.
        DateTimeZone parisZone = DateTimeZone.forID("Europe/Paris");

        // Act: Request a cached wrapper for the same zone twice.
        DateTimeZone cachedZone1 = CachedDateTimeZone.forZone(parisZone);
        DateTimeZone cachedZone2 = CachedDateTimeZone.forZone(parisZone);

        // Assert: Verify that both calls returned the exact same object instance.
        assertSame("Expected the same instance to be returned for the same zone", cachedZone1, cachedZone2);
    }
}