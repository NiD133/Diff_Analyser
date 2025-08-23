package org.jsoup.select;

import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Elements#textNodes()} method.
 */
public class ElementsTextNodesTest {

    @Test
    public void textNodesShouldReturnEmptyListWhenElementsIsEmpty() {
        // Arrange: Create an empty Elements collection.
        Elements emptyElements = new Elements();

        // Act: Get the list of text nodes from the empty collection.
        List<TextNode> textNodes = emptyElements.textNodes();

        // Assert: Verify that the resulting list is empty.
        assertTrue("Expected an empty list of text nodes for an empty Elements collection.", textNodes.isEmpty());
    }
}