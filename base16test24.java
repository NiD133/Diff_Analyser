package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Base16} class.
 */
final class Base16Test {

    /**
     * Tests that the generic {@code encode(Object)} method throws an {@link EncoderException}
     * when provided with an input that is not a byte array. The underlying implementation
     * from {@code BaseNCodec} is specified to only handle {@code byte[]}.
     */
    @Test
    void encodeObjectShouldThrowExceptionWhenInputIsNotAByteArray() {
        // Arrange: Create a Base16 encoder and an invalid input type (String).
        final Base16 base16 = new Base16();
        final String invalidInput = "This is a string, not a byte array";

        // Act & Assert: Verify that calling encode with the invalid type throws an EncoderException.
        assertThrows(EncoderException.class, () -> base16.encode(invalidInput));
    }
}