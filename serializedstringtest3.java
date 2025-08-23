package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializedStringTestTest3 extends com.fasterxml.jackson.core.JUnit5TestBase {

    /**
     * Tests that {@link SerializedString#appendQuotedUTF8(byte[], int)} correctly
     * escapes special characters and appends the result to a buffer at a given offset.
     */
    @Test
    void appendQuotedUTF8_whenStringHasSpecialChars_thenAppendsJsonEscapedBytesWithOffset() throws IOException {
        // Arrange

        // The input string contains characters that require JSON escaping:
        // a backslash (\) and a double quote (").
        // The actual string value is: \"quo\\ted\"
        final String originalValue = "\\\"quo\\\\ted\\\"";

        // The expected JSON-escaped representation of the original string.
        // According to JSON rules: \ becomes \\ and " becomes \"
        // So, \"quo\\ted\" is escaped to \\\"quo\\\\ted\\\"
        final String expectedEscapedValue = "\\\\\\\"quo\\\\\\\\ted\\\\\\\"";
        final byte[] expectedEscapedBytes = expectedEscapedValue.getBytes(StandardCharsets.UTF_8);

        final SerializedString serializedString = new SerializedString(originalValue);
        final byte[] targetBuffer = new byte[100];
        final int offset = 3;

        // Act
        final int bytesAppended = serializedString.appendQuotedUTF8(targetBuffer, offset);

        // Assert

        // 1. Verify the number of bytes appended is correct.
        assertEquals(expectedEscapedBytes.length, bytesAppended,
                "Should return the number of bytes appended.");

        // 2. Verify the content of the buffer is correctly written at the specified offset.
        byte[] actualAppendedBytes = Arrays.copyOfRange(targetBuffer, offset, offset + bytesAppended);
        assertArrayEquals(expectedEscapedBytes, actualAppendedBytes,
                "The buffer should contain the correctly escaped UTF-8 bytes at the given offset.");

        // 3. Verify that the buffer before the offset is untouched.
        // A new byte array is initialized with zeros.
        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) 0, targetBuffer[i], "Buffer before offset should not be modified.");
        }
        
        // 4. Sanity check that the original value is stored correctly.
        assertEquals(originalValue, serializedString.getValue());
    }
}