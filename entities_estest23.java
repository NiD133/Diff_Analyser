package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the static {@link Entities#escape(String, Document.OutputSettings)} method.
 */
public class EntitiesTest {

    @Test
    public void escapeWithAsciiCharsetDoesNotAlterAsciiString() {
        // Arrange: Create output settings with an ASCII charset and define an ASCII input string.
        String input = "A simple string with numbers 123 and symbols !@#$.";
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.charset("US-ASCII");

        // Act: Call the escape method.
        String escapedString = Entities.escape(input, outputSettings);

        // Assert: The escaped string should be identical to the input because all characters
        // are valid in the US-ASCII charset and are not special HTML characters.
        assertEquals(input, escapedString);
    }
}