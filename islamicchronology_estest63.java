package org.joda.time.chrono;

import org.junit.Test;
import org.joda.time.Chronology;

// Note: The original test class name "IslamicChronology_ESTestTest63" and its scaffolding
// are preserved as they were part of the original code provided for improvement.
public class IslamicChronology_ESTestTest63 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Verifies that getDaysInYear() throws a NullPointerException if the chronology
     * is constructed with a null LeapYearPatternType.
     *
     * This test ensures the internal robustness of the class when it's put into an
     * invalid state using its package-private constructor, which is a scenario that
     * could occur within the package.
     */
    @Test(expected = NullPointerException.class)
    public void getDaysInYear_throwsNPE_whenConstructedWithNullLeapYearPattern() {
        // Arrange: Create an IslamicChronology instance with a null leap year pattern.
        // This is done using the package-private constructor to test an internal state.
        IslamicChronology.LeapYearPatternType nullPattern = null;
        IslamicChronology chronology = new IslamicChronology(null, null, nullPattern);

        // Act: Attempt to get the number of days in a year. This is expected to fail
        // because the underlying isLeapYear() check will dereference the null pattern.
        chronology.getDaysInYear(1);

        // Assert: The test passes if a NullPointerException is thrown, as specified
        // by the @Test(expected) annotation.
    }
}