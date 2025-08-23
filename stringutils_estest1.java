package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Verifies that convertCharsToBytes correctly transforms a char array into a byte array,
     * with each character being represented by two bytes (high byte, then low byte).
     */
    @Test
    public void convertCharsToBytes_shouldConvertEachCharToTwoBytes() {
        // Arrange: Create a simple and clear input array.
        char[] inputChars = {'H', 'i'};

        // The expected output for {'H', 'i'}.
        // Java chars are 16-bit. 'H' is 0x0048 and 'i' is 0x0069.
        // The method should produce a byte array: {high_byte, low_byte, high_byte, low_byte, ...}
        byte[] expectedBytes = {0, 'H', 0, 'i'};

        // Act: Call the method under test.
        byte[] actualBytes = StringUtils.convertCharsToBytes(inputChars);

        // Assert: Verify both the length and the content of the resulting byte array.
        // The output length must be exactly twice the input length.
        assertEquals("The resulting byte array should be twice the length of the char array.",
                inputChars.length * 2, actualBytes.length);

        // The byte-for-byte content must match the expected conversion.
        assertArrayEquals("The char array was not converted to bytes correctly.",
                expectedBytes, actualBytes);
    }
}