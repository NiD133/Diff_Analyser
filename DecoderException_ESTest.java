package org.apache.commons.codec;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoderExceptionTest {

    @Test
    void testConstructorWithCause() {
        // Given
        Throwable cause = new NullPointerException("Simulated cause");

        // When
        DecoderException decoderException = new DecoderException(cause);

        // Then
        assertNotNull(decoderException);
        assertEquals(cause, decoderException.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        // Given
        String message = "Invalid data encountered during decoding.";

        // When
        DecoderException decoderException = new DecoderException(message);

        // Then
        assertNotNull(decoderException);
        assertEquals(message, decoderException.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        // Given
        String message = "Decoding failed due to underlying error.";
        Throwable cause = new IllegalArgumentException("Illegal argument provided.");

        // When
        DecoderException decoderException = new DecoderException(message, cause);

        // Then
        assertNotNull(decoderException);
        assertEquals(message, decoderException.getMessage());
        assertEquals(cause, decoderException.getCause());
    }

    @Test
    void testConstructorWithoutArguments() {
        // When
        DecoderException decoderException = new DecoderException();

        // Then
        assertNotNull(decoderException);
        assertNull(decoderException.getMessage()); // Default message is null
        assertNull(decoderException.getCause()); // Default cause is null
    }

    @Test
    void testNestingExceptions() {
        //Given
        DecoderException innerException = new DecoderException("Inner exception");

        //When
        DecoderException outerException = new DecoderException("Outer Exception", innerException);

        //Then
        assertEquals("Outer Exception", outerException.getMessage());
        assertEquals(innerException, outerException.getCause());
    }
}