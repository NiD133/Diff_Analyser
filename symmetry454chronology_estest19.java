package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Verifies that the range(ChronoField) method throws a NullPointerException
     * when a null field is provided as input.
     */
    @Test(expected = NullPointerException.class)
    public void range_withNullArgument_throwsNullPointerException() {
        // Arrange: Obtain the singleton instance of the chronology, as recommended by its Javadoc.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;

        // Act & Assert: Calling the range() method with a null argument is expected
        // to throw a NullPointerException. The assertion is handled by the 'expected'
        // parameter of the @Test annotation.
        chronology.range(null);
    }
}