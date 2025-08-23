package org.apache.commons.codec.net;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the constructor of {@link PercentCodec}.
 */
public class PercentCodecConstructorTest {

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenAlwaysEncodeCharsContainsNegativeByte() {
        // Arrange: Create an array of bytes for the 'alwaysEncodeChars' parameter
        // that contains an invalid negative value. The constructor is expected to reject it.
        final byte[] invalidCharsToEncode = {(byte) -50};
        final boolean plusForSpace = true;
        final String expectedMessage = "byte must be >= 0";

        // Act & Assert: Verify that creating a PercentCodec with the invalid input
        // throws an IllegalArgumentException with the correct message.
        // Using JUnit's assertThrows for clear and concise exception testing.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new PercentCodec(invalidCharsToEncode, plusForSpace)
        );

        assertEquals(expectedMessage, thrown.getMessage());
    }
}