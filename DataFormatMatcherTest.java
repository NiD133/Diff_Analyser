package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;

import static org.junit.jupiter.api.Assertions.*;

class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {
    private final JsonFactory JSON_F = new JsonFactory();

    @Test
    void getDataStream_WhenBufferedDataIsEmpty_ReturnsZeroAvailableBytes() throws IOException {
        // Arrange: Create matcher with empty buffer
        byte[] buffer = new byte[2];
        DataFormatMatcher matcher = new DataFormatMatcher(
            null, buffer, 1, 0, null, MatchStrength.WEAK_MATCH
        );

        // Act
        InputStream stream = matcher.getDataStream();
        
        // Assert
        assertEquals(0, stream.available(), 
            "InputStream should report 0 available bytes for empty buffer");
        stream.close();
    }

    @Test
    void constructor_WhenStartAndLengthExceedBufferBounds_ThrowsIllegalArgumentException() {
        // Arrange: Invalid parameters (start=2, length=1) for empty buffer
        byte[] emptyBuffer = new byte[0];
        
        // Act & Assert
        try {
            new DataFormatMatcher(null, emptyBuffer, 2, 1, JSON_F, MatchStrength.NO_MATCH);
            fail("Expected IllegalArgumentException for invalid buffer parameters");
        } catch (IllegalArgumentException e) {
            verifyException(e, "Illegal start/length");
        }
    }

    @Test
    void getMatchedFormatName_WhenSolidMatch_ReturnsJsonFormatName() {
        // Arrange: Create matcher with SOLID_MATCH strength
        DataFormatMatcher matcher = new DataFormatMatcher(
            null, new byte[2], 1, 0, JSON_F, MatchStrength.SOLID_MATCH
        );

        // Act & Assert
        assertEquals(JsonFactory.FORMAT_NAME_JSON, matcher.getMatchedFormatName(),
            "Solid match should return JSON format name");
    }

    @Test
    void dataFormatDetector_withSameOptimalMatch_returnsSameInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertSame(detector, detector.withOptimalMatch(MatchStrength.SOLID_MATCH),
            "Same optimal match should return original instance");
    }

    @Test
    void dataFormatDetector_withSameMinimalMatch_returnsSameInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertSame(detector, detector.withMinimalMatch(MatchStrength.WEAK_MATCH),
            "Same minimal match should return original instance");
    }

    @Test
    void dataFormatDetector_withSameMaxLookahead_returnsSameInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertSame(detector, detector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD),
            "Same max lookahead should return original instance");
    }

    @Test
    void dataFormatDetector_withDifferentOptimalMatch_returnsNewInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertNotSame(detector, detector.withOptimalMatch(MatchStrength.FULL_MATCH),
            "Different optimal match should return new instance");
    }

    @Test
    void dataFormatDetector_withDifferentMinimalMatch_returnsNewInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertNotSame(detector, detector.withMinimalMatch(MatchStrength.SOLID_MATCH),
            "Different minimal match should return new instance");
    }

    @Test
    void dataFormatDetector_withDifferentMaxLookahead_returnsNewInstance() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertNotSame(detector, detector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5),
            "Different max lookahead should return new instance");
    }

    @Test
    void dataFormatDetector_toString_returnsNonNull() {
        DataFormatDetector detector = new DataFormatDetector(JSON_F);
        assertNotNull(detector.toString(), "toString() should never return null");
    }
}