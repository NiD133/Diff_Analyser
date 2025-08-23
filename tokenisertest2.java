package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for edge cases in the Jsoup Tokeniser.
 * Note: The class name "TokeniserTestTest2" could be improved to "TokeniserTest"
 * or a more descriptive name for better project organization.
 */
public class TokeniserTestTest2 {

    @Test
    void parserShouldHandleTagNameLongerThanInternalBuffer() {
        // This test verifies that the parser can handle tag names that are longer
        // than its internal buffer (CharacterReader.BufferSize). This is an important
        // edge case for robustness, ensuring the tokeniser resizes correctly without data loss.

        // Arrange
        final String tagNamePart = "LongTagNamePart";
        final StringBuilder tagNameBuilder = new StringBuilder(BufferSize + tagNamePart.length());

        // Create a tag name that is guaranteed to be longer than the buffer size.
        while (tagNameBuilder.length() <= BufferSize) {
            tagNameBuilder.append(tagNamePart);
        }
        final String longTagName = tagNameBuilder.toString();
        final String content = "One";
        final String html = String.format("<%s>%s</%s>", longTagName, content, longTagName);

        // Act
        // Use a parser with case preservation to ensure the long tag name is not lower-cased.
        Document document = Parser.htmlParser()
            .settings(ParseSettings.preserveCase)
            .parseInput(html, "");
        Elements elements = document.select(longTagName);

        // Assert
        assertEquals(1, elements.size(), "Should find exactly one element with the long tag name.");

        Element element = elements.first();
        assertAll("Properties of the parsed element",
            () -> assertEquals(longTagName, element.tagName(), "Tag name should be parsed and preserved correctly."),
            () -> assertEquals(content, element.text(), "Element's text content should be parsed correctly.")
        );
    }
}