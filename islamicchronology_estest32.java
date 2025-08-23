package org.joda.time.chrono;

import org.junit.Test;
import org.joda.time.Chronology;

/**
 * Unit tests for {@link IslamicChronology}.
 */
public class IslamicChronologyTest {

    /**
     * This test verifies that methods relying on the leap year pattern fail fast
     * when the pattern is null. This scenario is not possible via the public API
     * but is tested to ensure internal robustness.
     */
    @Test(expected = NullPointerException.class)
    public void calculateFirstDayOfYearMillis_shouldThrowNullPointerException_whenLeapYearPatternIsNull() {
        // Arrange: Create an IslamicChronology instance with a null leap year pattern.
        // This requires using the package-private constructor to simulate this internal state.
        Chronology baseChronology = GJChronology.getInstanceUTC();
        Object dummyParam = new Object(); // This parameter is not relevant for the test's logic.
        IslamicChronology.LeapYearPatternType nullPattern = null;

        IslamicChronology chronologyWithNullPattern = new IslamicChronology(baseChronology, dummyParam, nullPattern);

        // Act: Attempt to calculate the start of a year, which requires the leap year pattern.
        // Assert: A NullPointerException is expected, as declared in the @Test annotation.
        chronologyWithNullPattern.calculateFirstDayOfYearMillis(1593);
    }
}