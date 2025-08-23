package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Base16#decode(Object)} method.
 */
class Base16ObjectDecodeTest {

    /**
     * This test verifies that the decode(Object) method can correctly decode a
     * Base16-encoded byte array back to its original form. It performs a round-trip
     * (encode -> decode) to ensure consistency.
     */
    @Test
    void decodeObjectShouldReturnOriginalDataForEncodedBytes() throws DecoderException, EncoderException {
        // Arrange
        final String originalString = "Hello World!";
        final byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        final Base16 base16 = new Base16();

        // The encode(byte[]) method returns a byte[], which is a valid Object for decode(Object).
        final Object encodedObject = base16.encode(originalBytes);

        // Act
        final Object decodedObject = base16.decode(encodedObject);

        // Assert
        // The decoded object should be a byte array identical to the original.
        final byte[] decodedBytes = (byte[]) decodedObject;
        assertArrayEquals(originalBytes, decodedBytes, "The decoded bytes should match the original byte array.");
    }
}