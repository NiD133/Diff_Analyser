package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StringUtils} class, focusing on PDF string escaping logic.
 */
public class StringUtilsTest {

    /**
     * Tests that the escapeString(byte[]) method correctly escapes a horizontal tab character
     * and wraps the entire byte array in parentheses, as required by the PDF specification for
     * string literals. Null bytes are expected to be passed through without modification.
     */
    @Test
    public void escapeString_withByteArrayOverload_shouldEscapeTabAndWrapInParentheses() {
        // Arrange: Create an input byte array containing a null byte, a tab character, and two more nulls.
        byte[] inputBytes = {0, (byte) '\t', 0, 0};

        // Expected output: The input wrapped in parentheses with the tab character escaped as "\\t".
        // PDF string literals are enclosed in '()'.
        // The tab character (byte 9) is escaped to the two-byte sequence for backslash and 't'.
        // The resulting string is: ( \0 \t \0 \0 )
        byte[] expectedEscapedBytes = {'(', 0, '\\', 't', 0, 0, ')'};

        // Act: Call the method under test.
        byte[] actualEscapedBytes = StringUtils.escapeString(inputBytes);

        // Assert: Verify that the output matches the expected escaped byte array.
        assertArrayEquals(expectedEscapedBytes, actualEscapedBytes);
    }

    /**
     * Tests that the escapeString(byte[], ByteBuffer) method correctly escapes characters
     * that have special meaning in PDF strings, such as parentheses and backslashes.
     * This test uses an already-escaped string as input to simulate a double-escaping scenario.
     */
    @Test
    public void escapeString_withByteBufferOverload_shouldEscapeParenthesesAndBackslash() {
        // Arrange: Create an input byte array that already contains special PDF characters: '(', ')', and '\\'.
        // This input corresponds to the PDF string literal "(\0\t\0\0)".
        byte[] inputBytes = {'(', 0, '\\', 't', 0, 0, ')'};
        ByteBuffer outputBuffer = new ByteBuffer();

        // Expected output: The input string wrapped in parentheses, with its own special characters escaped.
        // Original input: ( \0 \ t \0 \0 )
        // Expected escaped output: ( \( \0 \\ t \0 \0 \) )
        byte[] expectedEscapedBytes = {'(', '\\', '(', 0, '\\', '\\', 't', 0, 0, '\\', ')', ')'};

        // Act: Call the method under test to write the escaped string into the buffer.
        StringUtils.escapeString(inputBytes, outputBuffer);

        // Assert: Verify that the buffer contains the correctly double-escaped byte array and has the correct size.
        assertArrayEquals(expectedEscapedBytes, outputBuffer.toByteArray());
        assertEquals(expectedEscapedBytes.length, outputBuffer.size());
    }
}