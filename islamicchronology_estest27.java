package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * Contains tests for the {@link IslamicChronology} class.
 */
public class IslamicChronologyTest {

    /**
     * Verifies that a NullPointerException is thrown when a method requiring a leap
     * year pattern is called on an IslamicChronology instance that was improperly
     * constructed with a null pattern.
     * <p>
     * This is a white-box test for an invalid internal state, as the constructor
     * used to create this state is not public.
     */
    @Test(expected = NullPointerException.class)
    public void setYear_whenConstructedWithNullLeapYearPattern_throwsNullPointerException() {
        // Arrange: Create an IslamicChronology instance using its package-private
        // constructor, intentionally passing a null LeapYearPatternType.
        // The specific base chronology (Coptic) is not important for this test.
        Chronology baseChronology = CopticChronology.getInstanceUTC();
        IslamicChronology islamicChronologyWithNullPattern = new IslamicChronology(baseChronology, baseChronology, null);

        // Act: Call a method that internally depends on the leap year pattern.
        // The setYear() method eventually calls isLeapYear(), which will attempt to
        // dereference the null LeapYearPatternType, causing the expected NPE.
        long anyInstant = 380L;
        int anyYear = 1;
        islamicChronologyWithNullPattern.setYear(anyInstant, anyYear);

        // Assert: The @Test(expected) annotation handles the verification that a
        // NullPointerException was thrown.
    }
}