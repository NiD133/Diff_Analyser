package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * Tests for {@link IslamicChronology}.
 * This focuses on behavior when the chronology is constructed with invalid parameters.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that attempting to perform a date calculation throws a NullPointerException
     * if the IslamicChronology was constructed with a null LeapYearPatternType.
     *
     * The getDayOfMonth() method internally depends on the leap year pattern to determine
     * the structure of the calendar. A null pattern makes these calculations impossible,
     * leading to the expected exception.
     */
    @Test(expected = NullPointerException.class)
    public void getDayOfMonth_whenConstructedWithNullLeapYearPattern_throwsNullPointerException() {
        // Arrange: Create an IslamicChronology instance with a null leap year pattern.
        // The base chronology and param object are arbitrary non-null values required by the constructor.
        Chronology baseChronology = CopticChronology.getInstance();
        IslamicChronology.LeapYearPatternType nullPattern = null;

        IslamicChronology islamicChronology = new IslamicChronology(baseChronology, new Object(), nullPattern);

        // Act: Call a method that relies on the leap year pattern.
        // This is expected to fail and throw a NullPointerException.
        islamicChronology.getDayOfMonth(1L);
    }
}