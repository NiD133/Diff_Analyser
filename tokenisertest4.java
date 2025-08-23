package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the Parser, focusing on its ability to handle large inputs,
 * a concern related to the Tokeniser and CharacterReader.
 */
public class ParserLargeInputTest {

    /**
     * Tests the parser's ability to handle a text node that is significantly larger
     * than its internal character buffer ({@link CharacterReader#BufferSize}).
     * This ensures the parser correctly manages buffer refills and concatenates
     * the text content without data loss.
     */
    @Test
    public void parsesTextLargerThanInternalBuffer() {
        // Arrange
        // 1. Create a string that is intentionally larger than the parser's internal buffer
        //    to verify the buffer-refilling logic.
        final String repeatedPhrase = "A Large Amount of Text. ";
        StringBuilder stringBuilder = new StringBuilder(BufferSize + repeatedPhrase.length());
        do {
            stringBuilder.append(repeatedPhrase);
        } while (stringBuilder.length() < BufferSize);
        final String longText = stringBuilder.toString();
        final String html = "<p>" + longText + "</p>";

        // Act
        // 2. Parse the HTML containing the large text.
        Document doc = Jsoup.parse(html);
        Element pElement = doc.selectFirst("p");

        // Assert
        // 3. Verify the element was parsed and its text content is complete and correct.
        assertNotNull(pElement, "The <p> element should be parsed successfully.");
        assertEquals(longText, pElement.text(), "The text content of the element should match the original large string.");
    }
}