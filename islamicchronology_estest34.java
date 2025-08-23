package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that the getInstance factory method throws a NullPointerException
     * when the leap year pattern is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void getInstance_whenLeapYearPatternIsNull_thenThrowNullPointerException() {
        // Arrange: The specific time zone is not relevant to this test case.
        // We use a fixed zone like UTC to ensure the test is deterministic.
        DateTimeZone anyZone = DateTimeZone.UTC;

        // Act & Assert: The factory method should throw a NullPointerException
        // because the leap year pattern type must not be null.
        IslamicChronology.getInstance(anyZone, null);
    }
}