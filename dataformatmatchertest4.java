package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the configuration of the {@link DataFormatDetector}.
 * This test focuses on verifying default settings and the immutability
 * of the detector's configuration methods (the "wither" pattern).
 */
// The original class name "DataFormatMatcherTestTest4" was misleading, as this
// test class verifies the behavior of DataFormatDetector, not DataFormatMatcher.
@DisplayName("DataFormatDetector Configuration")
class DataFormatDetectorConfigTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    @Test
    @DisplayName("should be created with default configuration settings")
    void shouldBeCreatedWithDefaultSettings() {
        // Given
        DataFormatDetector detector = new DataFormatDetector(JSON_FACTORY);

        // Then: The detector should be configured with default values.
        // We verify this by checking that re-applying the default settings
        // returns the same instance, which is an optimization for immutable objects.
        assertSame(detector, detector.withOptimalMatch(MatchStrength.SOLID_MATCH),
                "Default optimal match strength should be SOLID_MATCH");

        assertSame(detector, detector.withMinimalMatch(MatchStrength.WEAK_MATCH),
                "Default minimal match strength should be WEAK_MATCH");

        assertSame(detector, detector.withMaxInputLookahead(DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD),
                "Default max input lookahead should be the defined constant");
    }

    @Test
    @DisplayName("should return a new instance when configuration is changed")
    void shouldReturnNewInstanceWhenConfigurationIsChanged() {
        // Given
        DataFormatDetector originalDetector = new DataFormatDetector(JSON_FACTORY);

        // When a configuration value is changed, a new, distinct instance of the
        // detector should be returned, demonstrating immutability.
        assertNotSame(originalDetector, originalDetector.withOptimalMatch(MatchStrength.FULL_MATCH),
                "Changing optimal match strength should produce a new instance");

        assertNotSame(originalDetector, originalDetector.withMinimalMatch(MatchStrength.SOLID_MATCH),
                "Changing minimal match strength should produce a new instance");

        assertNotSame(originalDetector, originalDetector.withMaxInputLookahead(
                        DataFormatDetector.DEFAULT_MAX_INPUT_LOOKAHEAD + 5),
                "Changing max input lookahead should produce a new instance");
    }

    @Test
    @DisplayName("toString() should provide a non-null string representation")
    void toStringShouldProvideNonNullRepresentation() {
        // Given
        DataFormatDetector detector = new DataFormatDetector(JSON_FACTORY);

        // When & Then
        // The toString() method should execute without errors and return a non-null string.
        assertNotNull(detector.toString());
    }
}