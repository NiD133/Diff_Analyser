package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link Entities} escape functionality.
 */
public class EntitiesEscapeTest {

    @Test
    public void escapeWithXhtmlModeEscapesNonBreakingSpaceNumerically() {
        // Arrange
        // The input string contains a non-breaking space character (\u00A0).
        // This character is not one of the basic XML entities (&lt; &gt; &amp; &quot;).
        String inputText = "yen\u00A0";
        
        OutputSettings settings = new Document.OutputSettings()
                                          .escapeMode(Entities.EscapeMode.xhtml);

        // Act
        String escapedText = Entities.escape(inputText, settings);

        // Assert
        // In xhtml mode, non-ASCII characters should be escaped as numeric entities
        // to ensure strict XML validity. The non-breaking space (U+00A0) is
        // expected to be escaped to its hexadecimal numeric entity representation.
        assertEquals("yen&#xa0;", escapedText);
    }
}