package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 */
// The original class name "PercentCodecTestTest8" was likely auto-generated or a typo.
// A standard name like "PercentCodecTest" is more appropriate and conventional.
public class PercentCodecTest {

    /**
     * Tests that the {@link PercentCodec#encode(Object)} method throws an
     * {@link EncoderException} when provided with an object that is not a
     * byte array, as specified by its contract.
     */
    @Test
    void encodeObjectWithUnsupportedTypeShouldThrowException() {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        // The encode(Object) method is documented to only accept byte arrays.
        // We use a String here as a clear example of an unsupported type.
        final Object unsupportedInput = "test data";

        // Act & Assert
        // Verify that an EncoderException is thrown for non-byte[] input.
        assertThrows(EncoderException.class, () -> {
            percentCodec.encode(unsupportedInput);
        });
    }
}