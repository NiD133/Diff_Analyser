package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link DataFormatMatcher} - validates format detection results
 * and stream handling functionality.
 */
class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase
{
    private final JsonFactory JSON_FACTORY = new JsonFactory();

    @Test
    void shouldReturnEmptyStreamWhenNoDataBuffered() throws IOException {
        // Given: A matcher with empty buffer (0 length, starting at position 0)
        byte[] emptyBuffer = new byte[2];
        int bufferStart = 0;
        int bufferLength = 0;
        
        DataFormatMatcher matcher = new DataFormatMatcher(
            null,                           // no original stream
            emptyBuffer,                    // buffer with data
            bufferLength,                   // start position in buffer  
            bufferStart,                    // length of buffered data
            null,                           // no matching factory
            MatchStrength.WEAK_MATCH        // match strength
        );
        
        // When: Getting the data stream
        InputStream dataStream = matcher.getDataStream();
        
        // Then: Stream should be empty
        assertEquals(0, dataStream.available(), "Stream should have no available data");
        dataStream.close();
    }

    @Test
    void shouldThrowExceptionWhenBufferParametersAreInvalid() {
        // Given: Invalid buffer parameters (start=1, length=2 for empty array)
        byte[] emptyBuffer = new byte[0];
        int invalidStart = 1;
        int invalidLength = 2;
        
        // When & Then: Creating matcher should throw IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DataFormatMatcher(
                null,
                emptyBuffer,
                invalidLength,      // Note: parameters seem swapped in original
                invalidStart,       // This suggests a bug in the original test
                JSON_FACTORY,
                MatchStrength.NO_MATCH
            ),
            "Should throw exception for invalid buffer bounds"
        );
        
        verifyException(exception, "Illegal start/length");
    }

    @Test
    void shouldReturnFormatNameWhenMatchFound() {
        // Given: A matcher with a solid match to JSON format
        byte[] buffer = new byte[2];
        
        DataFormatMatcher matcherWithJsonFormat = new DataFormatMatcher(
            null,                           // no original stream
            buffer,                         // buffer with data
            1,                              // start position
            0,                              // length of data
            JSON_FACTORY,                   // matched JSON factory
            MatchStrength.SOLID_MATCH       // strong match
        );
        
        // When: Getting the matched format name
        String formatName = matcherWithJsonFormat.getMatchedFormatName();
        
        // Then: Should return JSON format name
        assertEquals(JsonFactory.FORMAT_NAME_JSON, formatName, 
            "Should return JSON format name for matched JSON factory");
    }

    @Test
    void shouldMaintainImmutabilityWhenConfiguringDetector() {
        // Given: A data format detector with default settings
        DataFormatDetector originalDetector = new DataFormatDetector(JSON_FACTORY);
        
        // When & Then: Setting same values should return same instance (optimization)
        assertSame(originalDetector, 
            originalDetector.withOptimalMatch(MatchStrength.SOLID_MATCH),
            "Should return same instance when setting default optimal match");
            
        assertSame(originalDetector, 
            originalDetector.withMinimalMatch(MatchStrength.WEAK_MATCH),
            "Should return same instance when setting default minimal match");
            
        assertSame(originalDetector, 
            originalDetector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD),
            "Should return same instance when setting default lookahead");

        // When & Then: Setting different values should return new instances
        assertNotSame(originalDetector, 
            originalDetector.withOptimalMatch(MatchStrength.FULL_MATCH),
            "Should return new instance when changing optimal match strength");
            
        assertNotSame(originalDetector, 
            originalDetector.withMinimalMatch(MatchStrength.SOLID_MATCH),
            "Should return new instance when changing minimal match strength");
            
        assertNotSame(originalDetector, 
            originalDetector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5),
            "Should return new instance when changing lookahead value");

        // When & Then: toString should work regardless of configuration
        assertNotNull(originalDetector.toString(), 
            "toString() should return non-null value");
    }
}