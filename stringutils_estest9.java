package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Tests that the escapeString method correctly escapes a carriage return character ('\r')
     * into the two-character sequence "\\r", while leaving other bytes unchanged.
     */
    @Test
    public void escapeString_whenCarriageReturnIsPresent_shouldBeEscaped() {
        // Arrange: Create an input byte array containing a carriage return and other characters.
        byte[] inputBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\r', 'W', 'o', 'r', 'l', 'd'};
        ByteBuffer outputBuffer = new ByteBuffer();

        // Define the expected output where '\r' is replaced by '\\' and 'r'.
        byte[] expectedBytes = new byte[]{'H', 'e', 'l', 'l', 'o', '\\', 'r', 'W', 'o', 'r', 'l', 'd'};

        // Act: Call the method under test.
        StringUtils.escapeString(inputBytes, outputBuffer);
        byte[] actualBytes = outputBuffer.toByteArray();

        // Assert: Verify that the output content and size are correct.
        assertArrayEquals("The output byte array should have the carriage return correctly escaped.",
                expectedBytes, actualBytes);

        // This assertion on size is a good secondary check.
        // The size increases by one because one byte ('\r') is replaced by two bytes ("\\r").
        assertEquals("The output buffer size should be one greater than the input size.",
                inputBytes.length + 1, outputBuffer.size());
    }
}