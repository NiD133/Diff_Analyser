package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Base16} class, focusing on encoding single-byte inputs
 * and ensuring encode-decode round-trip integrity for all possible byte values.
 */
public class Base16Test {

    private Base16 base16;

    @BeforeEach
    void setUp() {
        // Use the default upper-case Base16 alphabet for tests.
        base16 = new Base16();
    }

    /**
     * Provides a stream of all 256 possible byte values, represented as integers from 0 to 255.
     * This is used as a source for the parameterized tests.
     *
     * @return An IntStream of values from 0 to 255.
     */
    private static IntStream allByteValues() {
        return IntStream.range(0, 256);
    }

    @DisplayName("Each byte should encode to its correct two-character hex string")
    @ParameterizedTest(name = "byte {0} -> \"{1}\"")
    @MethodSource("allByteValues")
    void testEncodeSingleByte(final int byteValue) {
        // Arrange
        final byte[] original = {(byte) byteValue};
        // The expected hex string is the standard 2-character, zero-padded, upper-case representation.
        final String expectedHex = String.format("%02X", byteValue);

        // Act
        final byte[] encodedBytes = base16.encode(original);
        final String actualHex = new String(encodedBytes, StandardCharsets.US_ASCII);

        // Assert
        assertEquals(expectedHex, actualHex);
    }

    @DisplayName("All byte values should remain unchanged after an encode-decode round trip")
    @ParameterizedTest(name = "Round trip for byte {0}")
    @MethodSource("allByteValues")
    void testEncodeDecodeRoundTripForAllByteValues(final int byteValue) {
        // Arrange
        final byte[] original = {(byte) byteValue};

        // Act
        final byte[] encoded = base16.encode(original);
        final byte[] decoded = base16.decode(encoded);

        // Assert
        assertArrayEquals(original, decoded, "Round-trip failed for byte value: " + byteValue);
    }

    @Test
    @DisplayName("Encoding a sub-array within a larger buffer should work correctly")
    void testEncodeSubArray() {
        // Arrange
        final String content = "Hello World";
        final byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        final String expectedEncoded = "48656C6C6F20576F726C64";

        // Create a buffer with padding at the start and end
        final byte[] prefix = {0x01, 0x02, 0x03};
        final byte[] suffix = {0x04, 0x05};
        byte[] buffer = new byte[prefix.length + contentBytes.length + suffix.length];
        System.arraycopy(prefix, 0, buffer, 0, prefix.length);
        System.arraycopy(contentBytes, 0, buffer, prefix.length, contentBytes.length);
        System.arraycopy(suffix, 0, buffer, prefix.length + contentBytes.length, suffix.length);

        // Act
        final byte[] encodedBytes = base16.encode(buffer, prefix.length, contentBytes.length);
        final String actualEncoded = new String(encodedBytes, StandardCharsets.US_ASCII);

        // Assert
        assertEquals(expectedEncoded, actualEncoded, "Encoding of 'Hello World' from a sub-array failed.");
    }
}