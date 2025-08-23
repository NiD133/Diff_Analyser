package org.apache.commons.codec.binary;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BinaryCodec}.
 */
public class BinaryCodecTest {

    @Test
    public void decode_withUnsupportedObjectType_shouldThrowDecoderException() {
        // Arrange
        BinaryCodec binaryCodec = new BinaryCodec();
        // The decode(Object) method expects a byte[], char[], or String.
        // We pass a generic Object to test the invalid input path.
        Object invalidInput = new Object();

        // Act & Assert
        try {
            binaryCodec.decode(invalidInput);
            fail("Expected a DecoderException to be thrown for an unsupported input type.");
        } catch (DecoderException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("argument not a byte array", e.getMessage());
        }
    }
}