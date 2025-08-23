package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the behavior of HTML entity escaping based on the document's output character set.
 */
public class EntitiesEscapingTest {

    @Test
    void letterLikeEntitiesAreEscapedForAsciiButNotForUtf8() {
        // Arrange
        String letterLikeEntities = "&sup1;&sup2;&sup3;&frac14;&frac12;&frac34;";
        String html = "<p>" + letterLikeEntities + "</p>";
        String decodedText = "¹²³¼½¾";

        Document doc = Jsoup.parse(html);
        Element p = doc.selectFirst("p");

        // --- Scenario 1: ASCII output ---
        // When the output charset is ASCII, characters that are not in the ASCII set
        // (like superscripts and fractions) must be escaped as named entities.
        doc.outputSettings().charset("ascii");

        // Assert
        assertEquals(letterLikeEntities, p.html(), "Entities should be preserved for ASCII output");
        assertEquals(decodedText, p.text(), ".text() should always be unescaped");

        // --- Scenario 2: UTF-8 output ---
        // When the output charset is UTF-8, these characters have a direct representation
        // and should be output as literal characters, not entities.
        doc.outputSettings().charset("UTF-8");

        // Assert
        assertEquals(decodedText, p.html(), "Entities should be decoded for UTF-8 output");
    }
}