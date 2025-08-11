package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

/**
 * Unit tests for the {@link StringUtils} class.
 * These tests verify the correct conversion and escaping of strings for PDF content.
 */
public class StringUtilsTest {

    // =========================================================================
    // Tests for convertCharsToBytes(char[] chars)
    // =========================================================================

    @Test
    public void convertCharsToBytes_withEmptyArray_returnsEmptyArray() {
        // Arrange
        char[] emptyArray = new char[0];

        // Act
        byte[] result = StringUtils.convertCharsToBytes(emptyArray);

        // Assert
        assertArrayEquals("Converting an empty char array should produce an empty byte array.", new byte[0], result);
    }

    @Test
    public void convertCharsToBytes_withAsciiChars_returnsBigEndianBytes() {
        // Arrange
        char[] inputChars = {'H', 'i'};
        byte[] expectedBytes = {0, 'H', 0, 'i'}; // Each char becomes two bytes (big-endian)

        // Act
        byte[] result = StringUtils.convertCharsToBytes(inputChars);

        // Assert
        assertArrayEquals("ASCII characters should be converted to their big-endian byte representation.", expectedBytes, result);
    }

    @Test
    public void convertCharsToBytes_withUnicodeChar_returnsBigEndianBytes() {
        // Arrange
        // The Euro sign '€' is Unicode character U+20AC
        char[] inputChars = {'€'};
        byte[] expectedBytes = {(byte) 0x20, (byte) 0xAC};

        // Act
        byte[] result = StringUtils.convertCharsToBytes(inputChars);

        // Assert
        assertArrayEquals("Unicode characters should be converted to their big-endian byte representation.", expectedBytes, result);
    }

    @Test(expected = NullPointerException.class)
    public void convertCharsToBytes_withNullInput_throwsNullPointerException() {
        // Act
        StringUtils.convertCharsToBytes(null);
    }

    // =========================================================================
    // Tests for escapeString(byte[] bytes)
    // =========================================================================

    @Test
    public void escapeString_withByteArray_escapesSpecialCharsAndWrapsInParentheses() {
        // Arrange
        byte[] inputBytes = "A string with a \t tab and a \n newline.".getBytes(StandardCharsets.US_ASCII);
        byte[] expectedBytes = "(A string with a \\t tab and a \\n newline.)".getBytes(StandardCharsets.US_ASCII);

        // Act
        byte[] result = StringUtils.escapeString(inputBytes);

        // Assert
        assertArrayEquals("The resulting byte array should have escaped characters and be wrapped in parentheses.", expectedBytes, result);
    }

    @Test(expected = NullPointerException.class)
    public void escapeString_withNullByteArray_throwsNullPointerException() {
        // Act
        StringUtils.escapeString((byte[]) null);
    }

    // =========================================================================
    // Tests for escapeString(byte[] bytes, ByteBuffer content)
    // =========================================================================

    @Test
    public void escapeString_withNoSpecialChars_appendsWrappedStringToBuffer() {
        // Arrange
        ByteBuffer buffer = new ByteBuffer();
        byte[] inputBytes = "Simple".getBytes(StandardCharsets.US_ASCII);

        // Act
        StringUtils.escapeString(inputBytes, buffer);

        // Assert
        assertBufferContentEquals("The string should be wrapped in parentheses.", "(Simple)", buffer);
    }

    @Test
    public void escapeString_withSpecialChars_escapesAndAppendsToBuffer() {
        // Arrange
        ByteBuffer buffer = new ByteBuffer();
        // PDF string literals must escape these characters: \n, \r, \t, \b, \f, (, ), \
        byte[] inputBytes = "()\r\n\t\b\f\\".getBytes(StandardCharsets.US_ASCII);

        // Act
        StringUtils.escapeString(inputBytes, buffer);

        // Assert
        String expected = "(\\(\\)\\r\\n\\t\\b\\f\\\\)";
        assertBufferContentEquals("All special characters should be escaped and the result wrapped in parentheses.", expected, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void escapeString_withNullByteBuffer_throwsNullPointerException() {
        // Arrange
        byte[] anyBytes = {'a'};

        // Act
        StringUtils.escapeString(anyBytes, null);
    }

    /**
     * Helper method to compare the content of a ByteBuffer with an expected string.
     * It converts the string to bytes using the ISO-8859-1 charset, which is
     * standard for PDF string literals.
     *
     * @param expected The expected string content.
     * @param buffer   The ByteBuffer to check.
     */
    private void assertBufferContentEquals(String message, String expected, ByteBuffer buffer) {
        byte[] expectedBytes = expected.getBytes(StandardCharsets.ISO_8859_1);
        byte[] actualBytes = buffer.toByteArray();
        assertArrayEquals(message, expectedBytes, actualBytes);
    }
}