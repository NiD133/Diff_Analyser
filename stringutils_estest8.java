package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Tests that the escapeString method correctly escapes the backspace character ('\b')
     * while leaving other bytes unchanged.
     *
     * In PDF string literals, a backspace must be represented by the two-character
     * sequence '\b'. This test verifies that transformation.
     */
    @Test
    public void escapeString_shouldEscapeBackspaceAndPassThroughOtherBytes() {
        // Arrange: Define an input byte array containing a backspace character
        // and other non-special bytes (in this case, null bytes).
        byte[] inputBytes = new byte[]{0, 'A', '\b', 'C', 0};

        // The backspace character '\b' should be escaped to the two-byte sequence of '\\' and 'b'.
        // All other bytes should be passed through unchanged.
        byte[] expectedBytes = new byte[]{0, 'A', '\\', 'b', 'C', 0};

        ByteBuffer outputBuffer = new ByteBuffer();

        // Act: Call the method under test to escape the input bytes.
        StringUtils.escapeString(inputBytes, outputBuffer);

        // Assert: Verify that the content of the output buffer exactly matches the
        // expected byte sequence, rather than just checking its size.
        assertArrayEquals(expectedBytes, outputBuffer.toByteArray());
    }
}