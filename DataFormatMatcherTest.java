package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class {@link DataFormatMatcher}.
 */
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private final JsonFactory jsonFactory = new JsonFactory();

    @Test
    void testGetDataStreamReturnsEmptyStream() throws IOException {
        // Arrange
        byte[] emptyByteArray = new byte[2];
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;
        DataFormatMatcher dataFormatMatcher = new DataFormatMatcher(
                null,
                emptyByteArray,
                1,
                0,
                null,
                matchStrength
        );

        // Act
        InputStream inputStream = dataFormatMatcher.getDataStream();

        // Assert
        assertEquals(0, inputStream.available(), "Expected the input stream to be empty");
        inputStream.close();
    }

    @Test
    void testDataFormatMatcherThrowsExceptionForInvalidBuffer() {
        // Arrange & Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new DataFormatMatcher(
                    null,
                    new byte[0],
                    2,
                    1,
                    jsonFactory,
                    MatchStrength.NO_MATCH
            );
        });

        // Assert
        assertTrue(exception.getMessage().contains("Illegal start/length"), "Expected exception message to contain 'Illegal start/length'");
    }

    @Test
    void testGetMatchedFormatNameReturnsCorrectName() {
        // Arrange
        DataFormatMatcher dataFormatMatcher = new DataFormatMatcher(
                null,
                new byte[2],
                1,
                0,
                jsonFactory,
                MatchStrength.SOLID_MATCH
        );

        // Act & Assert
        assertEquals(JsonFactory.FORMAT_NAME_JSON, dataFormatMatcher.getMatchedFormatName(), "Expected format name to match JSON");
    }

    @Test
    void testDetectorConfiguration() {
        // Arrange
        DataFormatDetector dataFormatDetector = new DataFormatDetector(jsonFactory);

        // Act & Assert
        // Verify default configurations
        assertSame(dataFormatDetector, dataFormatDetector.withOptimalMatch(MatchStrength.SOLID_MATCH), "Expected optimal match to remain unchanged");
        assertSame(dataFormatDetector, dataFormatDetector.withMinimalMatch(MatchStrength.WEAK_MATCH), "Expected minimal match to remain unchanged");
        assertSame(dataFormatDetector, dataFormatDetector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD), "Expected max input lookahead to remain unchanged");

        // Verify changes in configurations
        assertNotSame(dataFormatDetector, dataFormatDetector.withOptimalMatch(MatchStrength.FULL_MATCH), "Expected optimal match to change");
        assertNotSame(dataFormatDetector, dataFormatDetector.withMinimalMatch(MatchStrength.SOLID_MATCH), "Expected minimal match to change");
        assertNotSame(dataFormatDetector, dataFormatDetector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5), "Expected max input lookahead to change");

        // Verify toString() method
        assertNotNull(dataFormatDetector.toString(), "Expected toString() to return a non-null value");
    }
}