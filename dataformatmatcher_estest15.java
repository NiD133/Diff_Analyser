package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;

/**
 * This test class contains tests for the {@link DataFormatMatcher} class.
 * The original test was automatically generated and has been improved for clarity.
 */
public class DataFormatMatcher_ESTestTest15 { // Note: Class name kept from original for context.

    /**
     * Verifies that getMatchedFormatName() returns null when the DataFormatMatcher
     * represents a state where no data format was matched.
     *
     * This is simulated by creating a DataFormatMatcher with a null JsonFactory.
     */
    @Test
    public void getMatchedFormatName_shouldReturnNull_whenNoMatchIsFound() {
        // Arrange: Set up a scenario representing an inconclusive format match.
        byte[] inputData = new byte[16]; // The content and size are arbitrary for this test.
        InputStream inputStream = new ByteArrayInputStream(inputData);
        MatchStrength strength = MatchStrength.INCONCLUSIVE;

        // A null JsonFactory signifies that no format was identified.
        JsonFactory noMatchingFactory = null;

        DataFormatMatcher matcher = new DataFormatMatcher(
                inputStream,
                inputData,
                0, // bufferedStart
                0, // bufferedLength
                noMatchingFactory,
                strength
        );

        // Act: Call the method under test.
        String matchedFormatName = matcher.getMatchedFormatName();

        // Assert: Verify that the result is null as expected.
        assertNull("The matched format name should be null when no JsonFactory is provided.", matchedFormatName);
    }
}