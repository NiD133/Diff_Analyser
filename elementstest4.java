package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Elements#hasAttr(String)} with the "abs:" prefix.
 */
public class ElementsTestTest4 {

    @Test
    @DisplayName("hasAttr with 'abs:' prefix should return true if any element in the collection has an absolute attribute")
    public void hasAttrWithAbsPrefix_shouldReturnTrue_ifAnyElementHasAbsoluteUrl() {
        // Arrange: Create a document with one relative and one absolute link.
        // A base URI is provided to allow resolution of the relative URL.
        String html = "<a id='relative-link' href='/foo'>Relative</a> " +
                      "<a id='absolute-link' href='https://jsoup.org'>Absolute</a>";
        Document doc = Jsoup.parse(html, "https://example.com/");

        Elements elementsWithRelativeLink = doc.select("#relative-link");
        Elements elementsWithAbsoluteLink = doc.select("#absolute-link");
        Elements allElements = doc.select("a");

        // Act & Assert

        // 1. Verify that an element with only a relative URL does not have the absolute attribute.
        assertFalse(elementsWithRelativeLink.hasAttr("abs:href"),
            "Should return false for an element with a relative URL.");

        // 2. Verify that an element with an absolute URL has the absolute attribute.
        assertTrue(elementsWithAbsoluteLink.hasAttr("abs:href"),
            "Should return true for an element with an absolute URL.");

        // 3. Verify that a collection returns true if at least one element has the absolute attribute.
        // This is the core behavior being tested for the Elements collection.
        assertTrue(allElements.hasAttr("abs:href"),
            "The collection should return true because one of its elements has an absolute URL.");
    }
}