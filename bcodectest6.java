package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link BCodec} class.
 */
class BCodecTest {

    /**
     * The BCodec is designed to handle MIME 'B' encoding. According to its contract,
     * decoding a null input should result in a null output, representing an empty or
     * non-existent value, rather than throwing an exception. This test verifies that
     * this specific edge case is handled gracefully.
     */
    @Test
    @DisplayName("decode(String) should return null when the input string is null")
    void decodeWithNullInputShouldReturnNull() {
        // Arrange
        final BCodec bCodec = new BCodec();

        // Act
        final String result = bCodec.decode(null);

        // Assert
        assertNull(result, "Decoding a null string should result in a null value.");
    }
}