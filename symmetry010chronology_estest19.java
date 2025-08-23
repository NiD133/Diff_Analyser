package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on edge cases and invalid inputs.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that calling the range() method with a null ChronoField argument
     * correctly throws a NullPointerException. The method's contract requires a non-null field.
     */
    @Test(expected = NullPointerException.class)
    public void testRangeWithNullFieldThrowsNullPointerException() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act: Call the method under test with null input.
        // An exception is expected.
        chronology.range(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}