package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Test class for {@link InternationalFixedChronology}.
 * This refactored test focuses on the behavior of the zonedDateTime method with null input.
 */
public class InternationalFixedChronology_ESTestTest23 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that calling zonedDateTime() with a null TemporalAccessor throws a NullPointerException.
     * The method is expected to fail fast when given invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void zonedDateTime_withNullInput_throwsNullPointerException() {
        // Arrange: The singleton instance of the chronology is used, as recommended.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act & Assert: Call the method with null, which is expected to throw a NullPointerException.
        // The assertion is handled by the @Test(expected=...) annotation.
        chronology.zonedDateTime(null);
    }
}