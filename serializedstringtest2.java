package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test suite verifies the behavior of {@link SerializedString} methods
 * when the destination buffer is too small to hold the resulting content.
 */
@DisplayName("SerializedString: Buffer Boundary Checks")
public class SerializedStringTestTest2 extends com.fasterxml.jackson.core.JUnit5TestBase {

    // A constant that clearly communicates the expected return value for an insufficient buffer.
    private static final int INSUFFICIENT_SPACE_INDICATOR = -1;
    private static final String TEST_STRING = "A simple test string";

    private SerializableString serializedString;

    @BeforeEach
    void setUp() {
        // Arrange: Create a common SerializedString instance for all tests.
        serializedString = new SerializedString(TEST_STRING);
    }

    @Nested
    @DisplayName("when destination buffer is too small")
    class WhenDestinationBufferIsTooSmall {

        @Test
        @DisplayName("appendQuoted() should return -1")
        void appendQuotedWithInsufficientCharBuffer() {
            // Arrange: Create a char buffer that is guaranteed to be too small.
            char[] tooSmallBuffer = new char[TEST_STRING.length() - 1];

            // Act
            int result = serializedString.appendQuoted(tooSmallBuffer, 0);

            // Assert: The method should indicate failure by returning the indicator.
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when char buffer is too small for quoted content");
        }

        @Test
        @DisplayName("appendQuotedUTF8() should return -1")
        void appendQuotedUTF8WithInsufficientByteArray() {
            // Arrange: Create a byte buffer that is guaranteed to be too small.
            byte[] tooSmallBuffer = new byte[TEST_STRING.length() - 1];

            // Act
            int result = serializedString.appendQuotedUTF8(tooSmallBuffer, 0);

            // Assert
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when byte buffer is too small for quoted UTF-8 content");
        }

        @Test
        @DisplayName("putQuotedUTF8() should return -1")
        void putQuotedUTF8WithInsufficientByteBuffer() {
            // Arrange: Create a ByteBuffer that is guaranteed to be too small.
            ByteBuffer tooSmallBuffer = ByteBuffer.allocate(TEST_STRING.length() - 1);

            // Act
            int result = serializedString.putQuotedUTF8(tooSmallBuffer);

            // Assert
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when ByteBuffer is too small for quoted UTF-8 content");
        }

        @Test
        @DisplayName("appendUnquoted() should return -1")
        void appendUnquotedWithInsufficientCharBuffer() {
            // Arrange: Create a char buffer smaller than the string itself.
            char[] tooSmallBuffer = new char[TEST_STRING.length() - 1];

            // Act
            int result = serializedString.appendUnquoted(tooSmallBuffer, 0);

            // Assert
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when char buffer is too small for unquoted content");
        }

        @Test
        @DisplayName("appendUnquotedUTF8() should return -1")
        void appendUnquotedUTF8WithInsufficientByteArray() {
            // Arrange: Create a byte buffer smaller than the string itself.
            byte[] tooSmallBuffer = new byte[TEST_STRING.length() - 1];

            // Act
            int result = serializedString.appendUnquotedUTF8(tooSmallBuffer, 0);

            // Assert
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when byte buffer is too small for unquoted UTF-8 content");
        }

        @Test
        @DisplayName("putUnquotedUTF8() should return -1")
        void putUnquotedUTF8WithInsufficientByteBuffer() {
            // Arrange: Create a ByteBuffer smaller than the string itself.
            ByteBuffer tooSmallBuffer = ByteBuffer.allocate(TEST_STRING.length() - 1);

            // Act
            int result = serializedString.putUnquotedUTF8(tooSmallBuffer);

            // Assert
            assertEquals(INSUFFICIENT_SPACE_INDICATOR, result,
                "Should return -1 when ByteBuffer is too small for unquoted UTF-8 content");
        }
    }
}