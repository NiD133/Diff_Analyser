package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link Base16} class, focusing on its implementation of the
 * {@link org.apache.commons.codec.BinaryEncoder} and {@link org.apache.commons.codec.BinaryDecoder}
 * interfaces which operate on {@code Object} types.
 */
public class Base16ObjectApiTest {

    /**
     * Tests a round-trip scenario: encoding a byte array via the {@code encode(Object)}
     * method and then decoding the result back to the original byte array.
     * <p>
     * This test verifies the compatibility between {@code encode(Object)} and
     * {@code decode(byte[])}.
     * </p>
     */
    @Test
    void testObjectEncodeAndDecodeRoundtrip() throws EncoderException, DecoderException {
        // Arrange
        final String originalString = "Hello World!";
        final byte[] originalBytes = originalString.getBytes(StandardCharsets.UTF_8);
        final Base16 base16 = new Base16();

        // Act
        // 1. Encode the byte array (as an Object)
        final Object encodedObject = base16.encode(originalBytes);

        // 2. Decode the resulting object (which is a byte[]) back to a byte array
        final byte[] decodedBytes = base16.decode((byte[]) encodedObject);

        // Assert
        // The decoded bytes should be identical to the original bytes.
        assertArrayEquals(originalBytes, decodedBytes);

        // For good measure, confirm the string representation is also correct.
        final String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        assertEquals(originalString, decodedString);
    }
}