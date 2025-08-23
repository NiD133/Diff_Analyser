package com.fasterxml.jackson.core.format;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;

import static org.junit.jupiter.api.Assertions.*;

class DataFormatMatcherTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    // Helper factory method for readability
    private static DataFormatMatcher matcher(InputStream in,
                                             byte[] buffer,
                                             int start,
                                             int length,
                                             JsonFactory match,
                                             MatchStrength strength) {
        return new DataFormatMatcher(in, buffer, start, length, match, strength);
    }

    @Test
    void getDataStream_returnsEmptyStream_whenOnlyProbeBufferHasZeroBytes() throws IOException {
        // Arrange: 2-byte probe buffer but expose an empty window (start=1, length=0)
        byte[] probeBuffer = new byte[2];
        DataFormatMatcher m = matcher(null, probeBuffer, 1, 0, null, MatchStrength.WEAK_MATCH);

        // Act + Assert
        try (InputStream in = m.getDataStream()) {
            assertEquals(0, in.available(), "No bytes should be available");
            assertEquals(-1, in.read(), "Stream should be at EOF immediately");
        }
    }

    @Test
    void constructor_throwsOnInvalidStartAndLengthCombination() {
        // Arrange
        byte[] empty = new byte[0];

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                matcher(null, empty, 2, 1, JSON_FACTORY, MatchStrength.NO_MATCH));

        // Assert
        verifyException(ex, "Illegal start/length");
    }

    @Test
    void getMatchedFormatName_returnsNameWhenFactoryMatched() {
        // Arrange: pretend we matched JSON with a solid strength
        DataFormatMatcher m = matcher(null, new byte[2], 1, 0, JSON_FACTORY, MatchStrength.SOLID_MATCH);

        // Act + Assert
        assertEquals(JsonFactory.FORMAT_NAME_JSON, m.getMatchedFormatName());
    }

    @Test
    void dataFormatDetector_configuration_isImmutableAndKeepsDefaults() {
        DataFormatDetector detector = new DataFormatDetector(JSON_FACTORY);

        // Defaults: optimal=SOLID, minimal=WEAK, maxLookahead=DEFAULT
        assertSame(detector, detector.withOptimalMatch(MatchStrength.SOLID_MATCH));
        assertSame(detector, detector.withMinimalMatch(MatchStrength.WEAK_MATCH));
        assertSame(detector, detector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD));

        // Changing any setting should return a new instance
        assertNotSame(detector, detector.withOptimalMatch(MatchStrength.FULL_MATCH));
        assertNotSame(detector, detector.withMinimalMatch(MatchStrength.SOLID_MATCH));
        assertNotSame(detector, detector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5));

        // toString should be safe to call
        assertNotNull(detector.toString());
    }
}