package com.fasterxml.jackson.core.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 */
// Renamed from DataFormatMatcherTestTest1 for clarity and adherence to standard naming conventions.
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    @Test
    @DisplayName("getDataStream() should return an empty stream when buffer length is zero and original stream is null")
    void getDataStream_whenBufferIsEmptyAndNoOriginalStream_returnsEmptyStream() throws IOException {
        // Arrange: Create a DataFormatMatcher with a zero-length view into the buffer
        // and no original input stream.
        byte[] bufferedData = new byte[]{'a', 'b'}; // Non-empty to ensure offset/length are respected.
        int bufferStart = 1;
        int bufferLength = 0; // The key condition: the matched part of the buffer is empty.

        DataFormatMatcher matcher = new DataFormatMatcher(
                null, // No original input stream.
                bufferedData,
                bufferStart,
                bufferLength,
                null, // No matching factory is needed for this test.
                MatchStrength.INCONCLUSIVE);

        // Act: Get the combined data stream.
        // Use try-with-resources to ensure the stream is closed automatically.
        try (InputStream dataStream = matcher.getDataStream()) {
            // Assert: The stream should be empty.
            // It's constructed from a zero-length buffer segment and a null original stream.
            assertEquals(0, dataStream.available(), "Stream should report 0 available bytes.");
            assertEquals(-1, dataStream.read(), "Reading from the stream should immediately indicate end-of-stream.");
        }
    }
}