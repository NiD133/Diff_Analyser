package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities.EscapeMode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities#escape(String, Document.OutputSettings)} method.
 */
public class EntitiesTest {

    /**
     * Verifies that when using the XHTML escape mode, special characters like
     * double quotes (") and single quotes (') are correctly escaped to their
     * respective named or numeric entities.
     */
    @Test
    public void escapeWithXhtmlModeEscapesQuotesAndApostrophes() {
        // Arrange
        // An input string containing characters that require escaping in XHTML.
        String inputText = "\"PvE5H.,d+SC ,Q,}'xM";
        
        // Expected output where " is escaped to &quot; and ' is escaped to &#x27;
        String expectedEscapedText = "&quot;PvE5H.,d+SC ,Q,}&#x27;xM";

        // Configure the output settings to use the strict XHTML escape mode.
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.escapeMode(EscapeMode.xhtml);

        // Act
        // Escape the input string using the configured settings.
        String actualEscapedText = Entities.escape(inputText, outputSettings);

        // Assert
        // Check that the actual output matches the expected escaped string.
        assertEquals(expectedEscapedText, actualEscapedText);
    }
}