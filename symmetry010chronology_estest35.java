package org.threeten.extra.chrono;

import org.junit.Test;

/**
 * Test class for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that localDateTime() throws a NullPointerException when the input is null,
     * as specified by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void localDateTime_shouldThrowNullPointerException_whenInputIsNull() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation will assert that a NullPointerException is thrown.
        chronology.localDateTime(null);
    }
}