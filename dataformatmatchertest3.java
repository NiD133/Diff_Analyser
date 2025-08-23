package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 */
// Renamed class from "DataFormatMatcherTestTest3" to the more standard "DataFormatMatcherTest".
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    @Test
    @DisplayName("getMatchedFormatName() should return the correct format name when a match is found")
    void shouldReturnFormatNameForSuccessfulMatch() {
        // Arrange: Create a DataFormatMatcher representing a successful, solid match.
        // The input stream and buffer details are not relevant for this test, so we use
        // minimal values to satisfy the constructor's requirements. The key inputs are
        // the non-null JsonFactory and a positive MatchStrength.
        DataFormatMatcher matcher = new DataFormatMatcher(
                null, // InputStream is not used in this test's logic
                new byte[0], // Buffer content is irrelevant
                0, // bufferStart
                0, // bufferLength
                JSON_FACTORY, // The factory that "matched" the format
                MatchStrength.SOLID_MATCH);

        // Act: Call the method under test.
        String actualFormatName = matcher.getMatchedFormatName();

        // Assert: Verify that the format name from the provided factory is returned.
        assertEquals(JsonFactory.FORMAT_NAME_JSON, actualFormatName,
                "A successful match should return the name of the matched format.");
    }
}