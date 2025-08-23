package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link Elements#textNodes()} method.
 */
public class ElementsTextNodesTest {

    /**
     * Verifies that textNodes() correctly finds and returns the direct child TextNodes
     * from a collection of elements.
     */
    @Test
    public void textNodesReturnsDirectChildTextNodes() {
        // Arrange: Create a document where the <body> element contains a direct text node.
        // The parsed structure will be: <html><head></head><body>Tm</body></html>
        Document doc = Parser.parseBodyFragment("Tm", "");
        Elements allElements = doc.getAllElements(); // This will include <html>, <head>, and <body>

        // Act: Call the method under test to find all direct child text nodes.
        List<TextNode> foundTextNodes = allElements.textNodes();

        // Assert: The method should find the single text node within the <body> element.
        assertFalse("The list of text nodes should not be empty.", foundTextNodes.isEmpty());
        assertEquals("Should find exactly one text node.", 1, foundTextNodes.size());
        assertEquals("The text content of the found node should match.", "Tm", foundTextNodes.get(0).text());
    }
}