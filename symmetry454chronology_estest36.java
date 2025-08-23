package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Unit tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that calling the date() method with a null TemporalAccessor
     * argument correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void date_withNullTemporalAccessor_throwsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act & Assert: Calling date(null) should throw NullPointerException.
        // The assertion is handled by the @Test(expected=...) annotation.
        chronology.date(null);
    }
}