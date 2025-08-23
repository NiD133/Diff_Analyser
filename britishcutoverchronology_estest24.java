package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link BritishCutoverChronology}.
 * This class focuses on the behavior of the date(TemporalAccessor) method.
 */
// The original test class name and inheritance are preserved to show a direct refactoring.
// In a real-world scenario, this might be renamed to "BritishCutoverChronologyTest".
public class BritishCutoverChronology_ESTestTest24 extends BritishCutoverChronology_ESTest_scaffolding {

    /**
     * Tests that calling date(TemporalAccessor) with a null input
     * throws a NullPointerException as per the method's contract.
     */
    @Test
    public void date_withNullTemporalAccessor_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        // Using the singleton is preferred over the deprecated constructor.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act & Assert: Verify that a NullPointerException is thrown.
        // The assertThrows method is a standard, clear way to test for exceptions.
        // It ensures the test fails if the expected exception is NOT thrown.
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> chronology.date(null)
        );

        // Optional but recommended: Assert on the exception message for more specific testing.
        // This confirms the exception originates from the expected parameter validation.
        assertEquals("temporal", exception.getMessage());
    }
}