package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the constructor of {@link DataFormatMatcher}.
 */
// Renamed from DataFormatMatcherTestTest2 for clarity and conciseness.
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory JSON_FACTORY = new JsonFactory();

    @Test
    // Renamed for descriptiveness: clearly states the specific behavior being tested.
    void constructorShouldThrowExceptionForInvalidBufferBounds() {
        // Arrange: Define an empty buffer and offsets that are intentionally invalid.
        // The start offset (2) plus the data length (1) is 3, which is greater than the buffer's length (0).
        // This violates the constructor's precondition.
        final byte[] emptyBuffer = new byte[0];
        final int startOffset = 2;
        final int dataLength = 1;

        // Act & Assert: Verify that creating a DataFormatMatcher with these invalid
        // parameters throws an IllegalArgumentException.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DataFormatMatcher(
                null, // InputStream is not relevant for this validation check
                emptyBuffer,
                startOffset,
                dataLength,
                JSON_FACTORY,
                MatchStrength.NO_MATCH);
        });

        // Assert: Further check the exception message for the expected content.
        String expectedMessageContent = "Illegal start/length";
        assertTrue(
            exception.getMessage().contains(expectedMessageContent),
            "Exception message should indicate an illegal start or length."
        );
    }
}