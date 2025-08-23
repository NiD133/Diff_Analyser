package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the Base16 class, focusing on encoding byte arrays.
 *
 * Note: The original class name 'Base16TestTest31' was preserved,
 * but a more descriptive name like 'Base16EncodingTest' would be conventional.
 */
public class Base16TestTest31 {

    private final Base16 base16 = new Base16();

    /**
     * Provides test data for encoding byte triplets. The test cases cover all 16
     * possible values for the final byte (from 0 to 15), ensuring the full
     * Base16 alphabet ('0'-'9', 'A'-'F') is correctly used for encoding.
     *
     * @return A stream of arguments, where each argument is a pair containing a
     *         byte array to be encoded and its expected hexadecimal string representation.
     */
    private static Stream<Arguments> provideByteTripletsForEncoding() {
        // Programmatically generate test cases for values 0x00 to 0x0F.
        return IntStream.range(0, 16).mapToObj(i -> {
            final byte[] inputBytes = {0, 0, (byte) i};
            // The first two bytes (0, 0) encode to "0000".
            // The last byte 'i' is encoded to its two-character hex representation.
            final String expectedHex = "0000" + String.format("%02X", i);
            return Arguments.of(inputBytes, expectedHex);
        });
    }

    /**
     * Tests that encoding a 3-byte array where the last byte varies from 0 to 15
     * produces the correct hexadecimal string. This systematically verifies the
     * encoding logic across the entire Base16 character set.
     *
     * @param inputBytes       The 3-byte array to encode.
     * @param expectedEncoding The expected Base16 (hex) string.
     */
    @ParameterizedTest(name = "Encoding byte array {0} should result in \"{1}\"")
    @MethodSource("provideByteTripletsForEncoding")
    void testEncodeWithVaryingFinalByte(final byte[] inputBytes, final String expectedEncoding) {
        // Act: Encode the byte array using the Base16 codec.
        final String actualEncoding = new String(base16.encode(inputBytes));

        // Assert: Verify the encoded string matches the expected hex value.
        assertEquals(expectedEncoding, actualEncoding);
    }
}