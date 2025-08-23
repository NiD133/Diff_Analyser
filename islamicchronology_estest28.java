package org.joda.time.chrono;

import org.junit.Test;

/**
 * Contains a test case for the {@link IslamicChronology} class, focusing on its behavior
 * when instantiated with invalid internal state.
 */
public class IslamicChronology_ESTestTest28 extends IslamicChronology_ESTest_scaffolding {

    /**
     * Verifies that calling hashCode() on an IslamicChronology instance that was
     * constructed with a null LeapYearPatternType results in a NullPointerException.
     * <p>
     * This scenario tests the internal robustness of the class. It uses the
     * package-private constructor to create an instance in an invalid state
     * (with a null leap year pattern), which is not possible through the public API.
     */
    @Test(expected = NullPointerException.class)
    public void hashCode_throwsNullPointerException_whenConstructedWithNullLeapYearPattern() {
        // Arrange: Create an IslamicChronology instance with a null LeapYearPatternType.
        // This is achieved by directly calling the package-private constructor, bypassing
        // the public factory methods which would prevent this invalid state.
        ISOChronology baseChronology = ISOChronology.getInstanceUTC();
        IslamicChronology.LeapYearPatternType nullLeapYearPattern = null;

        // The second constructor parameter ('param') is not relevant to this test case.
        // We pass the baseChronology instance to mirror the original test's setup.
        IslamicChronology islamicChronology = new IslamicChronology(
                baseChronology, baseChronology, nullLeapYearPattern);

        // Act & Assert:
        // The hashCode() method internally calls getLeapYearPatternType().hashCode().
        // Since the pattern is null, this is expected to throw a NullPointerException.
        islamicChronology.hashCode();
    }
}