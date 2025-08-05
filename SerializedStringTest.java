package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link SerializedString} that verify its serialization,
 * appending, and writing capabilities.
 */
class SerializedStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // A raw string containing characters that require JSON escaping (quotes and backslashes).
    private static final String RAW_STRING_WITH_SPECIAL_CHARS = "\"quo\\ted\"";

    // The expected JSON-escaped representation of the raw string above.
    private static final String JSON_ESCAPED_STRING = "\\\"quo\\\\ted\\\"";

    @Test
    void constructorShouldStoreValueAndCalculateLength() {
        // Arrange
        final String text = "some text";

        // Act
        SerializableString serializedString = new SerializedString(text);

        // Assert
        assertEquals(text, serializedString.getValue());
        assertEquals(text.length(), serializedString.charLength());
    }

    @Test
    void asQuotedMethodsShouldReturnJsonEscapedValue() {
        // Arrange
        SerializableString serializedString = new SerializedString(RAW_STRING_WITH_SPECIAL_CHARS);

        // Act
        char[] quotedChars = serializedString.asQuotedChars();
        byte[] quotedUtf8 = serializedString.asQuotedUTF8();

        // Assert
        assertEquals(JSON_ESCAPED_STRING, new String(quotedChars));
        assertEquals(JSON_ESCAPED_STRING, new String(quotedUtf8, StandardCharsets.UTF_8));
    }

    @Test
    void asUnquotedUTF8ShouldReturnRawValueAsBytes() {
        // Arrange
        SerializableString serializedString = new SerializedString(RAW_STRING_WITH_SPECIAL_CHARS);

        // Act
        byte[] unquotedUtf8 = serializedString.asUnquotedUTF8();

        // Assert
        assertEquals(RAW_STRING_WITH_SPECIAL_CHARS, new String(unquotedUtf8, StandardCharsets.UTF_8));
    }

    @Test
    void writeUtf8MethodsShouldWriteToOutputStream() throws IOException {
        // Arrange
        SerializableString serializedString = new SerializedString(RAW_STRING_WITH_SPECIAL_CHARS);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Act: Quoted
        int quotedLen = serializedString.writeQuotedUTF8(out);

        // Assert: Quoted
        assertEquals(JSON_ESCAPED_STRING.length(), quotedLen);
        assertEquals(JSON_ESCAPED_STRING, out.toString("UTF-8"));

        // Arrange: Unquoted
        out.reset();

        // Act: Unquoted
        int unquotedLen = serializedString.writeUnquotedUTF8(out);

        // Assert: Unquoted
        assertEquals(RAW_STRING_WITH_SPECIAL_CHARS.length(), unquotedLen);
        assertEquals(RAW_STRING_WITH_SPECIAL_CHARS, out.toString("UTF-8"));
    }

    @Test
    void appendUtf8MethodsShouldAppendToByteArrayWithOffset() {
        // Arrange
        SerializableString serializedString = new SerializedString(RAW_STRING_WITH_SPECIAL_CHARS);
        byte[] buffer = new byte[100];
        final int offset = 5;

        // Act: Quoted
        int quotedLen = serializedString.appendQuotedUTF8(buffer, offset);

        // Assert: Quoted
        assertEquals(JSON_ESCAPED_STRING.length(), quotedLen);
        assertEquals(JSON_ESCAPED_STRING, new String(buffer, offset, quotedLen, StandardCharsets.UTF_8));

        // Arrange: Unquoted
        Arrays.fill(buffer, (byte) 0);

        // Act: Unquoted
        int unquotedLen = serializedString.appendUnquotedUTF8(buffer, offset);

        // Assert: Unquoted
        assertEquals(RAW_STRING_WITH_SPECIAL_CHARS.length(), unquotedLen);
        assertEquals(RAW_STRING_WITH_SPECIAL_CHARS, new String(buffer, offset, unquotedLen, StandardCharsets.UTF_8));
    }

    @Test
    void appendAndPutMethodsShouldReturnNegativeOnInsufficientBuffer() {
        // Arrange
        final String text = "A reasonably long string";
        SerializableString serializedString = new SerializedString(text);

        // Create buffers that are intentionally too small to hold the content
        final byte[] smallByteBuffer = new byte[text.length() - 1];
        final char[] smallCharBuffer = new char[text.length() - 1];
        final ByteBuffer smallNioBuffer = ByteBuffer.allocate(text.length() - 1);

        // Act & Assert for append methods
        assertEquals(-1, serializedString.appendQuotedUTF8(smallByteBuffer, 0));
        assertEquals(-1, serializedString.appendUnquotedUTF8(smallByteBuffer, 0));
        assertEquals(-1, serializedString.appendQuoted(smallCharBuffer, 0));
        assertEquals(-1, serializedString.appendUnquoted(smallCharBuffer, 0));

        // Act & Assert for put methods
        assertEquals(-1, serializedString.putQuotedUTF8(smallNioBuffer));
        smallNioBuffer.rewind(); // Reset for the next call
        assertEquals(-1, serializedString.putUnquotedUTF8(smallNioBuffer));
    }

    @Test
    void appendQuotedUTF8ShouldCorrectlyEscapeStringThatAlreadyContainsEscapes() {
        // This test verifies that a string that already contains JSON escape sequences
        // is itself escaped correctly.

        // Arrange
        // The input string is what you get after escaping "\"quo\ted\"" once.
        final String stringToEscape = "\\\"quo\\\\ted\\\""; // Represents the literal string: \"quo\ted\"

        // The expected result is after escaping the above string again.
        // Each backslash becomes a double backslash (\\) and each quote becomes a backslash-quote (\").
        // So, \"quo\ted\" becomes \\\"quo\\ted\\\"
        // The Java literal for the expected string is complex:
        // \\\" -> "\\\\\\"
        // quo -> "quo"
        // \\ -> "\\\\"
        // ted -> "ted"
        // \\\" -> "\\\\\\"
        final String expectedEscapedString = "\\\\\\\"quo\\\\\\\\ted\\\\\\\"";

        SerializableString serializedString = new SerializedString(stringToEscape);
        byte[] buffer = new byte[100];
        final int offset = 3;

        // Act
        final int len = serializedString.appendQuotedUTF8(buffer, offset);
        final String resultString = new String(buffer, offset, len, StandardCharsets.UTF_8);

        // Assert
        assertEquals(expectedEscapedString, resultString);
    }

    @Test
    void shouldPreserveStateAndBehaviorAfterJdkSerialization() throws IOException, ClassNotFoundException {
        // Arrange
        final String originalValue = "Test with \"quotes\" and \\slashes\\";
        SerializableString original = new SerializedString(originalValue);

        // Act
        byte[] bytes = jdkSerialize(original);
        SerializableString deserialized = jdkDeserialize(bytes);

        // Assert
        assertNotNull(deserialized);
        // 1. Core state must be restored
        assertEquals(original.getValue(), deserialized.getValue());
        // 2. Behavior must be identical (lazily-computed values should be reproducible)
        assertArrayEquals(original.asQuotedUTF8(), deserialized.asQuotedUTF8());
        assertArrayEquals(original.asUnquotedUTF8(), deserialized.asUnquotedUTF8());
    }
}