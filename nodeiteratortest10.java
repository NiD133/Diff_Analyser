package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the NodeIterator, focusing on its behavior when the
 * underlying document structure is modified during iteration.
 */
public class NodeIteratorTest {

    private final String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    /**
     * This test verifies that the NodeIterator can correctly handle a node being
     * wrapped with another element during traversal. The iterator should continue
     * its traversal without errors, and the final document structure should reflect the change.
     */
    @Test
    void wrappingNodeDuringIterationIsHandledCorrectly() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        boolean wasNodeInsideWrappedElementSeen = false;

        // Act: Iterate through the document and wrap a node when it's found.
        // The NodeIterator is designed to handle such structural modifications during traversal.
        while (iterator.hasNext()) {
            Node node = iterator.next();

            // When the div with id=1 is found, wrap it in a new outer div.
            if (node instanceof Element && "1".equals(((Element) node).id())) {
                node.wrap("<div id=outer>");
            }

            // Sanity check to ensure we are still traversing children of the original node.
            if (node instanceof TextNode && "One".equals(((TextNode) node).text())) {
                wasNodeInsideWrappedElementSeen = true;
            }
        }

        // Assert
        // 1. Verify that the iterator continued correctly after the modification.
        assertTrue(wasNodeInsideWrappedElementSeen,
            "Iterator should continue traversing children of a wrapped node.");

        // 2. Verify the final structure of the document by performing a fresh traversal.
        // The new 'div#outer' should now be part of the document structure.
        String expectedTraversal = "#root;html;head;body;div#outer;div#1;p;One;p;Two;div#2;p;Three;p;Four;";
        assertTraversalOf(doc, expectedTraversal);
    }

    // Test Helper Methods

    /**
     * Asserts that a fresh traversal of the given element produces a specific sequence of nodes.
     * @param el The element to traverse.
     * @param expected A string representing the expected node sequence.
     */
    private static void assertTraversalOf(Element el, String expected) {
        NodeIterator<Node> it = NodeIterator.from(el);
        assertIterationProduces(it, expected);
    }

    /**
     * Iterates through the given iterator and asserts that the nodes visited match the expected sequence.
     * @param it The iterator to test.
     * @param expected A string representing the expected node sequence.
     */
    private static <T extends Node> void assertIterationProduces(Iterator<T> it, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (it.hasNext()) {
            Node node = it.next();
            assertNotNull(node, "Iterator should not return null nodes.");
            assertNotSame(previous, node, "Iterator should not return the same node consecutively.");
            trackSeenNode(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString(), "The sequence of traversed nodes should match the expected sequence.");
    }

    /**
     * Generates a string representation of a node for traversal verification.
     * The format is: tagName#id; or text; or nodeName;
     * @param node The node to track.
     * @param actual The StringBuilder to append the representation to.
     */
    private static void trackSeenNode(Node node, StringBuilder actual) {
        if (node instanceof Element) {
            Element el = (Element) node;
            actual.append(el.tagName());
            if (el.hasAttr("id"))
                actual.append("#").append(el.id());
        } else if (node instanceof TextNode)
            actual.append(((TextNode) node).text());
        else
            actual.append(node.nodeName());
        actual.append(";");
    }
}