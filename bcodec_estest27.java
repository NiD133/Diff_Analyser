package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that attempting to encode an object of an unsupported type results in an
     * {@link EncoderException}. The {@code BCodec#encode(Object)} method is designed
     * to handle {@code String} and {@code byte[]} inputs, and should fail for any other type.
     */
    @Test
    public void encodeUnsupportedObjectTypeShouldThrowEncoderException() {
        // Arrange: Create a BCodec instance and an object of an unsupported type.
        // In this case, we use another BCodec instance as the invalid input.
        final BCodec codec = new BCodec();
        final Object unsupportedObject = new BCodec();

        // Act & Assert: Verify that the encode operation throws the correct exception
        // with the expected message.
        try {
            codec.encode(unsupportedObject);
            fail("Expected an EncoderException to be thrown for an unsupported object type.");
        } catch (final EncoderException e) {
            // The exception is expected. Now, we verify its message for correctness.
            final String expectedMessage = "Objects of type " +
                    unsupportedObject.getClass().getName() +
                    " cannot be encoded using " +
                    codec.getClass().getName();
            assertEquals("The exception message did not match the expected format.", expectedMessage, e.getMessage());
        }
    }
}