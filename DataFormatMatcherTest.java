package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 */
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException for invalid buffer start/length")
    void shouldThrowExceptionForInvalidBufferBounds() {
        // Arrange: Define buffer and bounds that are out of range.
        // Here, start + length (2 + 1 = 3) is greater than the buffer's length (0).
        byte[] emptyBuffer = new byte[0];
        int start = 2;
        int length = 1;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DataFormatMatcher(null, emptyBuffer, start, length,
                    JSON_F, MatchStrength.NO_MATCH);
        });

        // Verify the exception message for clarity
        assertTrue(exception.getMessage().contains("Illegal start/length"));
    }

    @Test
    @DisplayName("getMatchedFormatName() should return the name of the matched format")
    void shouldReturnFormatNameWhenMatchExists() {
        // Arrange: Create a matcher that has a definite match.
        // The buffer details are irrelevant for this test.
        DataFormatMatcher matcher = new DataFormatMatcher(null, new byte[0], 0, 0,
                JSON_F, MatchStrength.SOLID_MATCH);

        // Act
        String formatName = matcher.getMatchedFormatName();

        // Assert
        assertEquals(JsonFactory.FORMAT_NAME_JSON, formatName);
    }

    @Test
    @DisplayName("getDataStream() should return an empty stream if the buffered data length is zero")
    void shouldReturnEmptyStreamForZeroLengthBuffer() throws IOException {
        // Arrange: Create a matcher with a zero-length view into a buffer.
        byte[] buffer = new byte[10];
        int start = 5;
        int length = 0; // The key condition for this test
        DataFormatMatcher matcher = new DataFormatMatcher(null, buffer, start, length,
                null, MatchStrength.INCONCLUSIVE);

        // Act
        try (InputStream dataStream = matcher.getDataStream()) {
            // Assert: The stream should be empty because the specified length was 0.
            assertEquals(0, dataStream.available());
            assertEquals(-1, dataStream.read(), "Reading from an empty stream should return -1");
        }
    }
}