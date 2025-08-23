package org.joda.time.tz;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Test suite for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that the forZone() factory method throws a NullPointerException
     * when passed a null DateTimeZone, as it's an invalid argument.
     */
    @Test(expected = NullPointerException.class)
    public void forZone_whenZoneIsNull_throwsNullPointerException() {
        // Act: Attempt to create a CachedDateTimeZone with a null delegate.
        CachedDateTimeZone.forZone(null);

        // Assert: Handled by the 'expected' attribute of the @Test annotation.
        // The test will pass only if a NullPointerException is thrown.
    }
}