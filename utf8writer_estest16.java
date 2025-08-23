package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link UTF8Writer} class, focusing on its helper methods.
 */
public class UTF8WriterTest {

    /**
     * Verifies that the {@code illegalSurrogate()} method throws an {@link IOException}
     * with a correctly formatted error message when passed an invalid character code.
     */
    @Test
    public void illegalSurrogateShouldThrowIOExceptionForInvalidCodePoint() {
        // Arrange: Define an invalid code point and the expected error message.
        // The value -554 is used as an example of an illegal character code.
        int invalidCodePoint = -554;
        String expectedMessage = "Illegal character point (0xfffffdd6) to output";

        // Act & Assert: Call the method and verify the exception.
        try {
            UTF8Writer.illegalSurrogate(invalidCodePoint);
            fail("Expected an IOException to be thrown for an invalid surrogate code.");
        } catch (IOException e) {
            // Verify that the exception message is exactly as expected.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}