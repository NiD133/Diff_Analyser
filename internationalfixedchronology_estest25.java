package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for the {@link InternationalFixedChronology#range(java.time.temporal.ChronoField)} method.
 */
// The original test class name and inheritance are kept for context.
public class InternationalFixedChronology_ESTestTest25 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that calling range() with a null ChronoField throws a NullPointerException,
     * as the method contract does not allow null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void range_whenFieldIsNull_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act: Call the range method with a null argument.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        chronology.range(null);
    }
}