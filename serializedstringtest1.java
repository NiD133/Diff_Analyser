package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its various
 * serialization and appending methods.
 */
@DisplayName("SerializedString")
class SerializedStringTest {

    // The raw string value containing characters that require JSON escaping (a quote and a backslash).
    private static final String RAW_VALUE = "\"quo\\ted\"";

    // The expected JSON-escaped representation of RAW_VALUE.
    // In JSON: " -> \" and \ -> \\
    private static final String EXPECTED_ESCAPED_VALUE = "\\\"quo\\\\ted\\\"";

    private SerializableString serializedString;

    @BeforeEach
    void setUp() {
        serializedString = new SerializedString(RAW_VALUE);
    }

    @Test
    @DisplayName("getValue() should return the original, unquoted string")
    void getValue_shouldReturnOriginalString() {
        assertEquals(RAW_VALUE, serializedString.getValue());
    }

    @Test
    @DisplayName("asQuotedChars() should return the JSON-escaped characters")
    void asQuotedChars_shouldReturnJsonEscapedChars() {
        String actualEscapedChars = new String(serializedString.asQuotedChars());
        assertEquals(EXPECTED_ESCAPED_VALUE, actualEscapedChars);
    }

    @Test
    @DisplayName("writeQuotedUTF8() should write JSON-escaped bytes to an OutputStream")
    void writeQuotedUTF8_shouldWriteEscapedBytesToStream() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        int bytesWritten = serializedString.writeQuotedUTF8(out);

        // Assert
        assertEquals(EXPECTED_ESCAPED_VALUE.length(), bytesWritten, "Should return the number of bytes written");
        assertEquals(EXPECTED_ESCAPED_VALUE, out.toString("UTF-8"), "The stream content should match the escaped string");
    }

    @Test
    @DisplayName("writeUnquotedUTF8() should write raw bytes to an OutputStream")
    void writeUnquotedUTF8_shouldWriteRawBytesToStream() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act
        int bytesWritten = serializedString.writeUnquotedUTF8(out);

        // Assert
        assertEquals(RAW_VALUE.length(), bytesWritten, "Should return the number of bytes written");
        assertEquals(RAW_VALUE, out.toString("UTF-8"), "The stream content should match the raw string");
    }

    @Test
    @DisplayName("appendQuotedUTF8() should append JSON-escaped bytes to a buffer at an offset")
    void appendQuotedUTF8_shouldAppendEscapedBytesToArray() {
        // Arrange
        byte[] buffer = new byte[100];
        final int offset = 3;

        // Act
        int bytesAppended = serializedString.appendQuotedUTF8(buffer, offset);

        // Assert
        assertEquals(EXPECTED_ESCAPED_VALUE.length(), bytesAppended, "Should return the number of bytes appended");
        String appendedString = new String(buffer, offset, bytesAppended);
        assertEquals(EXPECTED_ESCAPED_VALUE, appendedString, "The appended portion of the buffer should match the escaped string");
    }

    @Test
    @DisplayName("appendUnquotedUTF8() should append raw bytes to a buffer at an offset")
    void appendUnquotedUTF8_shouldAppendRawBytesToArray() {
        // Arrange
        byte[] buffer = new byte[100];
        final int offset = 5;

        // Act
        int bytesAppended = serializedString.appendUnquotedUTF8(buffer, offset);

        // Assert
        assertEquals(RAW_VALUE.length(), bytesAppended, "Should return the number of bytes appended");
        String appendedString = new String(buffer, offset, bytesAppended);
        assertEquals(RAW_VALUE, appendedString, "The appended portion of the buffer should match the raw string");
    }
}