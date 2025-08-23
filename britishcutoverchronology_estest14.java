package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * This test class contains an improved test case for the BritishCutoverChronology class.
 */
public class BritishCutoverChronology_ESTestTest14 extends BritishCutoverChronology_ESTest_scaffolding {

    /**
     * Tests that zonedDateTime() throws a NullPointerException when the temporal accessor is null.
     *
     * The method under test, zonedDateTime(TemporalAccessor), is expected to delegate to
     * other java.time methods that perform a null-check on the input, leading to an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void zonedDateTime_whenTemporalIsNull_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology, as the constructor is deprecated.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // Act & Assert: Calling zonedDateTime with a null input should throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        chronology.zonedDateTime(null);
    }
}