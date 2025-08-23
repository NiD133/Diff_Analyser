package org.apache.commons.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PatternOptionBuilder} with URL patterns.
 */
// The original class name 'PatternOptionBuilderTestTest10' was redundant.
// A more standard name is used.
public class PatternOptionBuilderTest {

    @Test
    @DisplayName("URL pattern ('/') should parse valid URLs and return null for invalid ones")
    void testUrlPatternParsing() throws Exception {
        // Arrange
        // The pattern "u/v/" defines two options, 'u' and 'v'.
        // The '/' character specifies that each option expects a URL as its value.
        final String urlPattern = "u/v/";
        final Options options = PatternOptionBuilder.parsePattern(urlPattern);

        final String validUrlString = "https://commons.apache.org";
        // "foo://" is not a standard protocol, so it's expected to fail URL parsing.
        final String invalidUrlString = "foo://commons.apache.org";
        final String[] args = {
            "-u", validUrlString,
            "-v", invalidUrlString
        };

        final CommandLineParser parser = new PosixParser();

        // Act
        final CommandLine cmd = parser.parse(options, args);

        // Assert
        // 1. Verify that the valid URL string was correctly converted to a URL object.
        final URL expectedUrl = new URL(validUrlString);
        assertEquals(expectedUrl, cmd.getOptionObject("u"),
            "A valid URL string should be parsed into a URL object.");

        // 2. Verify that the invalid URL string resulted in a null value.
        // This happens because the internal TypeHandler catches the MalformedURLException
        // during parsing and returns null.
        assertNull(cmd.getOptionObject("v"),
            "An invalid URL string should result in a null object.");
    }
}