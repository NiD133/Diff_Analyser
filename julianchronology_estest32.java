package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for the {@link JulianChronology} class, focusing on edge cases and invalid inputs.
 */
public class JulianChronologyTest {

    /**
     * Verifies that calling the date(TemporalAccessor) method with a null argument
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void date_fromNullTemporalAccessor_shouldThrowNullPointerException() {
        // Arrange: Get the singleton instance of the JulianChronology.
        // The constructor is deprecated, so the static INSTANCE should be used.
        JulianChronology julianChronology = JulianChronology.INSTANCE;

        // Act: Attempt to create a date from a null TemporalAccessor.
        // The @Test(expected) annotation handles the assertion, ensuring that
        // this line throws a NullPointerException for the test to pass.
        julianChronology.date(null);
    }
}