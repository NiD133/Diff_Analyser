package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Base16} class.
 *
 * <p>This test suite focuses on verifying the correctness of the Base16 encoding
 * and the integrity of the encode-decode round-trip process.
 */
public class Base16Test {

    private final Base16 base16 = new Base16();

    /**
     * Provides a stream of arguments for the parameterized test, mapping byte values
     * to their expected Base16 string representation when prefixed with a zero byte.
     *
     * @return A stream of {@link Arguments} containing the test data.
     */
    private static Stream<Arguments> knownBytePairsProvider() {
        return Stream.of(
            arguments((byte) 0, "0000"),
            arguments((byte) 1, "0001"),
            arguments((byte) 2, "0002"),
            arguments((byte) 3, "0003"),
            arguments((byte) 4, "0004"),
            arguments((byte) 5, "0005"),
            arguments((byte) 6, "0006"),
            arguments((byte) 7, "0007"),
            arguments((byte) 8, "0008"),
            arguments((byte) 9, "0009"),
            arguments((byte) 10, "000A"),
            arguments((byte) 11, "000B"),
            arguments((byte) 12, "000C"),
            arguments((byte) 13, "000D"),
            arguments((byte) 14, "000E"),
            arguments((byte) 15, "000F"),
            arguments((byte) 16, "0010"),
            arguments((byte) 17, "0011")
        );
    }

    @DisplayName("Should encode a byte array of [0, x] to the correct Base16 string")
    @ParameterizedTest(name = "input byte: {0} -> expected: \"{1}\"")
    @MethodSource("knownBytePairsProvider")
    void testEncodeWithKnownValues(final byte secondByte, final String expectedHex) {
        final byte[] dataToEncode = {0, secondByte};
        final String actualHex = new String(base16.encode(dataToEncode));
        assertEquals(expectedHex, actualHex);
    }

    @Test
    @DisplayName("Encode then decode should return the original data for all possible byte values")
    void testEncodeDecodeRoundTripForAllByteValues() {
        // This test iterates through all possible byte values (-128 to 127).
        // For each value, it creates a two-byte array, encodes it, decodes it,
        // and asserts that the result is identical to the original array.
        // This ensures the codec's integrity for the full byte range.
        IntStream.rangeClosed(Byte.MIN_VALUE, Byte.MAX_VALUE).forEach(i -> {
            final byte[] originalData = {(byte) i, (byte) i};
            final byte[] encodedData = base16.encode(originalData);
            final byte[] decodedData = base16.decode(encodedData);
            assertArrayEquals(originalData, decodedData,
                () -> "Round-trip failed for byte value: " + i);
        });
    }
}