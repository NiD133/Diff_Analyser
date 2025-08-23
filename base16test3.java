package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Base16} class.
 */
class Base16Test {

    @Test
    void shouldCorrectlyEncodeSubArrayWithOffsetAndLength() {
        // Arrange
        final String originalData = "Hello World";
        final byte[] dataToEncode = originalData.getBytes(StandardCharsets.UTF_8);
        final String expectedEncodedData = "48656C6C6F20576F726C64";

        final int prefixPaddingSize = 100;
        final int suffixPaddingSize = 100;

        // Create a larger buffer with the data to be encoded placed in the middle,
        // surrounded by padding. This setup is designed to verify that the encode method
        // correctly handles the offset and length parameters, only processing the
        -        // intended segment of the buffer.
        final byte[] prefix = new byte[prefixPaddingSize];
        final byte[] suffix = new byte[suffixPaddingSize];
        final byte[] buffer = ArrayUtils.addAll(ArrayUtils.addAll(prefix, dataToEncode), suffix);

        final Base16 base16 = new Base16();

        // Act
        // Execute the encode operation on the middle part of the buffer.
        final byte[] encodedBytes = base16.encode(buffer, prefixPaddingSize, dataToEncode.length);
        final String actualEncodedData = new String(encodedBytes, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedEncodedData, actualEncodedData);
    }
}