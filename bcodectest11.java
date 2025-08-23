package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BCodec} class, focusing on its handling of null inputs.
 */
public class BCodecTest {

    /**
     * Tests that the core protected encoding and decoding methods return null
     * when given a null input. This is the expected behavior for many codecs
     * in the library.
     */
    @Test
    public void doEncodingAndDoDecodingShouldReturnNullForNullInput() {
        // Arrange
        final BCodec codec = new BCodec();

        // Act & Assert
        assertNull(codec.doEncoding(null), "Encoding a null byte array should result in null.");
        assertNull(codec.doDecoding(null), "Decoding a null byte array should result in null.");
    }
}