package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for the {@link Symmetry010Chronology} class.
 */
public class Symmetry010ChronologyTest {

    /**
     * Verifies that attempting to create a date from a null TemporalAccessor
     * throws a NullPointerException, as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void date_whenTemporalAccessorIsNull_throwsNullPointerException() {
        // Arrange: Obtain the singleton instance of the chronology.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act: Call the date() method with a null argument.
        // The @Test(expected) annotation handles the assertion that an exception is thrown.
        chronology.date(null);
    }
}