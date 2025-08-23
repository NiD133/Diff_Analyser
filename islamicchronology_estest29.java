package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * This test class contains an improved version of a test for the IslamicChronology class.
 * The original class name and inheritance structure are preserved to demonstrate the refactoring.
 */
public class IslamicChronology_ESTestTest29 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Verifies that calling getYear() throws a NullPointerException if the
     * IslamicChronology instance was constructed with a null LeapYearPatternType.
     *
     * This test covers the scenario where the package-private constructor is used
     * to create an object in an invalid state. It ensures that methods relying on
     * the leap year pattern fail predictably instead of returning incorrect data.
     */
    @Test(expected = NullPointerException.class)
    public void getYear_whenConstructedWithNullLeapYearPattern_throwsNullPointerException() {
        // Arrange: Create an IslamicChronology instance with a null leap year pattern.
        // The constructor does not validate this argument, deferring the failure
        // until a calculation method is called. The other constructor arguments are
        // not relevant to this specific test case.
        IslamicChronology.LeapYearPatternType nullPattern = null;
        IslamicChronology chronologyWithNullPattern = new IslamicChronology(
                (Chronology) null, null, nullPattern);

        // Act: Attempt to get the year. This action requires the leap year pattern
        // for its calculation and is expected to throw the exception.
        chronologyWithNullPattern.getYear(1L);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // in the @Test annotation.
    }
}