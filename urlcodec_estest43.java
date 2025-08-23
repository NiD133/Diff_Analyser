package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Test cases for the URLCodec class.
 */
public class URLCodecTest {

    /**
     * Verifies that attempting to decode an object of an unsupported type (i.e., not a String or byte[])
     * results in a DecoderException with a specific error message.
     */
    @Test
    public void decode_withUnsupportedObjectType_shouldThrowDecoderException() {
        // Arrange: Create a URLCodec instance and an object of an unsupported type.
        URLCodec urlCodec = new URLCodec();
        Object unsupportedInput = new Object();
        String expectedMessage = "Objects of type java.lang.Object cannot be URL decoded";

        // Act & Assert: Verify that a DecoderException is thrown with the correct message.
        // This uses the modern assertThrows from JUnit 4.13+ for cleaner exception testing.
        DecoderException thrown = assertThrows(DecoderException.class, () -> {
            urlCodec.decode(unsupportedInput);
        });

        assertEquals(expectedMessage, thrown.getMessage());
    }

    /**
     * Below is an alternative implementation using a traditional try-catch block,
     * which is also clear and works with older versions of JUnit 4.
     */
    @Test
    public void decode_withUnsupportedObjectType_shouldThrowDecoderException_withTryCatch() {
        // Arrange
        URLCodec urlCodec = new URLCodec();
        Object unsupportedInput = new Object();
        String expectedMessage = "Objects of type java.lang.Object cannot be URL decoded";

        // Act & Assert
        try {
            urlCodec.decode(unsupportedInput);
            fail("Expected a DecoderException to be thrown, but it was not.");
        } catch (DecoderException e) {
            // This is the expected outcome. Now, verify the exception message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}