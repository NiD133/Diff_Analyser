package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that calling skip() with a negative number of characters
     * throws an IllegalArgumentException.
     */
    @Test
    public void skip_withNegativeArgument_throwsIllegalArgumentException() {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test data");
        long negativeSkipValue = -1L;

        // Act & Assert
        // Guava's Preconditions should throw an exception for invalid arguments.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> reader.skip(negativeSkipValue)
        );

        // Verify the exception message for clarity and to confirm the source of the error.
        // The message format "n (%s) may not be negative" comes from Preconditions.checkArgument.
        assertEquals("n (" + negativeSkipValue + ") may not be negative", exception.getMessage());
    }
}