package org.apache.commons.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DecoderException} class.
 */
class DecoderExceptionTest {

    // Test message used for exception testing
    private static final String TEST_MESSAGE = "TEST";

    // Test throwable used for exception testing
    private static final Throwable TEST_THROWABLE = new Exception();

    /**
     * Test the default constructor of DecoderException.
     * It should initialize the exception with no message and no cause.
     */
    @Test
    void testDefaultConstructor() {
        final DecoderException exception = new DecoderException();
        assertNull(exception.getMessage(), "Default constructor should result in null message");
        assertNull(exception.getCause(), "Default constructor should result in null cause");
    }

    /**
     * Test the constructor of DecoderException with a message.
     * It should initialize the exception with the specified message and no cause.
     */
    @Test
    void testConstructorWithMessage() {
        final DecoderException exception = new DecoderException(TEST_MESSAGE);
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Constructor with message should set the message");
        assertNull(exception.getCause(), "Constructor with message should result in null cause");
    }

    /**
     * Test the constructor of DecoderException with a message and a cause.
     * It should initialize the exception with the specified message and cause.
     */
    @Test
    void testConstructorWithMessageAndCause() {
        final DecoderException exception = new DecoderException(TEST_MESSAGE, TEST_THROWABLE);
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Constructor with message and cause should set the message");
        assertEquals(TEST_THROWABLE, exception.getCause(), "Constructor with message and cause should set the cause");
    }

    /**
     * Test the constructor of DecoderException with a cause.
     * It should initialize the exception with a message derived from the cause and the specified cause.
     */
    @Test
    void testConstructorWithCause() {
        final DecoderException exception = new DecoderException(TEST_THROWABLE);
        assertEquals(TEST_THROWABLE.getClass().getName(), exception.getMessage(), "Constructor with cause should set the message to the cause's class name");
        assertEquals(TEST_THROWABLE, exception.getCause(), "Constructor with cause should set the cause");
    }
}