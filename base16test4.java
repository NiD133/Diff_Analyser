package org.apache.commons.codec.binary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link Base16} encoding, focusing on encoding sub-arrays.
 */
// Renamed from Base16TestTest4 for clarity and to follow standard naming conventions.
public class Base16Test {

    // Extracted test data into constants to improve readability and avoid "magic values".
    private static final String PLAIN_TEXT_FIXTURE = "Hello World";
    private static final String BASE16_ENCODED_FIXTURE = "48656C6C6F20576F726C64";

    /**
     * A parameterized test helper for verifying the {@link Base16#encode(byte[], int, int)}
     * method. It constructs a byte array with padding around the actual content to
     * ensure that the offset and length parameters are respected during encoding.
     *
     * @param prefixPaddingSize The number of padding bytes to add before the content.
     * @param suffixPaddingSize The number of padding bytes to add after the content.
     */
    // Renamed from testBase16InBuffer for clarity.
    // Parameter names (e.g., startPasSize) were corrected and made more descriptive.
    private void testEncodeSubArray(final int prefixPaddingSize, final int suffixPaddingSize) {
        // Arrange: Set up the test data and the object under test.
        final Base16 base16 = new Base16();
        final byte[] plainTextBytes = PLAIN_TEXT_FIXTURE.getBytes(StandardCharsets.UTF_8);

        // Create a larger buffer with the plain text bytes surrounded by padding.
        // The padding consists of zero-bytes, which helps verify that only the
        // specified sub-array is processed.
        byte[] buffer = ArrayUtils.addAll(new byte[prefixPaddingSize], plainTextBytes);
        buffer = ArrayUtils.addAll(buffer, new byte[suffixPaddingSize]);

        // Act: Call the method being tested.
        // Replaced custom StringUtils with standard Java APIs for better self-containment.
        final byte[] encodedBytes = base16.encode(buffer, prefixPaddingSize, plainTextBytes.length);
        final String actualEncodedString = new String(encodedBytes, StandardCharsets.UTF_8);

        // Assert: Verify the result.
        // The assertion message is now more descriptive.
        final String assertionMessage = String.format(
            "Encoding should be correct for a sub-array with %d bytes of prefix and %d bytes of suffix padding.",
            prefixPaddingSize, suffixPaddingSize);
        assertEquals(BASE16_ENCODED_FIXTURE, actualEncodedString, assertionMessage);
    }

    @Test
    // @DisplayName provides a human-readable name for the test in reports.
    @DisplayName("encode(byte[], int, int) should correctly encode data at the start of a buffer with suffix padding")
    // Renamed from testBase16AtBufferStart to better describe the specific scenario.
    void testEncodeWithDataAtBufferStart() {
        // This test case checks the scenario where the data to be encoded starts at
        // offset 0 in a larger buffer that contains irrelevant data (padding) after it.
        // It validates that the 'length' parameter is correctly handled.
        testEncodeSubArray(0, 100);
    }
}