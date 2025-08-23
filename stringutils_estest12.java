package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StringUtils} class.
 */
public class StringUtilsTest {

    /**
     * Tests that escapeString correctly escapes special characters like tab
     * and also wraps the resulting byte sequence in parentheses. This behavior
     * was inferred from the original auto-generated test's assertions.
     */
    @Test
    public void escapeString_withTabAndNulls_shouldEscapeAndWrapInParentheses() {
        // Arrange: Create an input byte array containing a tab character and null bytes.
        byte[] inputBytes = {0, '\t', 0, 0};
        ByteBuffer outputBuffer = new ByteBuffer();

        // Define the expected output. The logic is that the input is escaped and then
        // wrapped in parentheses.
        // Input:    { 0x00, \t, 0x00, 0x00 }
        // Escaped:  { 0x00,  \,  t, 0x00, 0x00 }
        // Wrapped:  {  ( , 0x00,  \,  t, 0x00, 0x00,  ) }
        byte[] expectedBytes = {'(', 0, '\\', 't', 0, 0, ')'};

        // Act: Call the method under test to populate the output buffer.
        StringUtils.escapeString(inputBytes, outputBuffer);

        // Assert: Verify that the output buffer contains the exact expected byte sequence.
        // We check both the content and the size for a comprehensive test.
        assertArrayEquals("The output buffer's content should be correctly escaped and wrapped.",
                expectedBytes, outputBuffer.toByteArray());
        assertEquals("The output buffer should have the correct size.",
                expectedBytes.length, outputBuffer.size());
    }
}