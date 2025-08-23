package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PercentCodec}.
 */
class PercentCodecTest {

    @Test
    @DisplayName("Encoding a null Object should return null")
    void encodeObjectShouldReturnNullForNullInput() throws EncoderException {
        // Arrange
        final PercentCodec percentCodec = new PercentCodec();
        final Object nullObject = null;

        // Act
        final Object result = percentCodec.encode(nullObject);

        // Assert
        assertNull(result, "The contract for Encoder#encode(Object) states that a null input should return null.");
    }
}