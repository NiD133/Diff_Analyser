package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.CharacterReader.BufferSize;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the Jsoup Tokeniser, focusing on buffer-related edge cases.
 */
public class TokeniserTest {

    /**
     * Tests if the parser can handle an attribute name that is significantly longer
     * than the internal buffer size ({@link CharacterReader#BufferSize}). This ensures
     * that the tokeniser's internal buffers can resize or otherwise accommodate
     * oversized token components without losing data or failing.
     */
    @Test
    @DisplayName("should parse an attribute name longer than the internal buffer")
    void parsesAttributeNameLongerThanInternalBuffer() {
        // Arrange: Create an attribute name that exceeds the parser's internal buffer size.
        StringBuilder longAttrNameBuilder = new StringBuilder(BufferSize);
        do {
            longAttrNameBuilder.append("VeryLongAttributeName");
        } while (longAttrNameBuilder.length() < BufferSize);
        String longAttrName = longAttrNameBuilder.toString();

        String expectedValue = "foo";
        String expectedText = "One";
        String html = "<p " + longAttrName + "=" + expectedValue + ">" + expectedText + "</p>";

        // Act
        Document doc = Jsoup.parse(html);

        // Assert
        // 1. Verify the element can be found by its long attribute name.
        Elements elements = doc.getElementsByAttribute(longAttrName);
        assertEquals(1, elements.size(), "Should find one element by the long attribute name.");

        // 2. Verify the element's content and attributes.
        Element element = elements.first();
        assertNotNull(element, "The parsed element should not be null.");
        assertEquals(expectedText, element.text());

        // 3. Verify the attribute's key (is lower-cased) and value.
        Attribute attribute = element.attributes().iterator().next(); // Use iterator for single-item check
        assertEquals(longAttrName.toLowerCase(), attribute.getKey(), "Attribute key should be lower-cased.");
        assertEquals(expectedValue, attribute.getValue(), "Attribute value should be correct.");
    }
}