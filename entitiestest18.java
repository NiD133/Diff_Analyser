package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the handling of entities, particularly focusing on how different parsers
 * treat special characters like control characters.
 */
public class EntitiesTest {

    @Test
    @DisplayName("Control characters should be preserved in HTML but stripped from XML")
    void controlCharactersAreHandledDifferentlyInHtmlAndXml() {
        // This test addresses issue #1556, verifying that control characters are
        // escaped for legibility in HTML but removed by the XML parser, which
        // considers them invalid.

        // Arrange
        // The input contains control characters: &#x1b; (ESC) and &#x7; (BELL).
        String htmlWithControlChars = "<a foo=\"&#x1b;esc&#x7;bell\">Text &#x1b; &#x7;</a>";
        String expectedXmlOutput = "<a foo=\"escbell\">Text  </a>";

        // Act
        // Parse the input using both the standard HTML parser and the strict XML parser.
        Document htmlDoc = Jsoup.parse(htmlWithControlChars);
        Document xmlDoc = Jsoup.parse(htmlWithControlChars, "", Parser.xmlParser());

        // Assert
        // The HTML parser should preserve the control character entities as-is.
        assertEquals(htmlWithControlChars, htmlDoc.body().html(),
            "HTML output should preserve numeric entities for control characters.");

        // The XML parser should strip the invalid control characters.
        assertEquals(expectedXmlOutput, xmlDoc.html(),
            "XML output should strip invalid control characters.");
    }
}