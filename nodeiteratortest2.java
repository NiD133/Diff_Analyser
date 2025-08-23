package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link NodeIterator}.
 * This focuses on the iterator's behavior and traversal correctness.
 */
public class NodeIteratorTest { // Renamed from NodeIteratorTestTest2

    private static final String HTML_STRUCTURE =
        "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    /**
     * Verifies that calling hasNext() multiple times is idempotent (does not advance the iterator)
     * and that the iterator correctly traverses the entire node tree.
     */
    @Test
    void hasNextIsIdempotentAndDoesNotAdvanceIterator() {
        // Arrange
        Document doc = Jsoup.parse(HTML_STRUCTURE);
        NodeIterator<Node> iterator = NodeIterator.from(doc);

        // The expected sequence is a semicolon-separated string representing each node.
        // Format is defined in the appendNodeDescription() helper method.
        String expectedSequence = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";

        // Act & Assert

        // 1. Verify hasNext() is idempotent before iteration starts.
        assertTrue(iterator.hasNext(), "hasNext() should be true for a non-empty iterator");
        assertTrue(iterator.hasNext(), "Calling hasNext() again should not advance the iterator");

        // 2. Verify the complete and correct traversal of all nodes.
        assertIteratorProducesSequence(iterator, expectedSequence);

        // 3. Verify hasNext() is false after the iteration is complete.
        assertFalse(iterator.hasNext(), "hasNext() should be false at the end of the iteration");
    }

    /**
     * Asserts that an iterator produces a specific sequence of nodes.
     * <p>
     * This helper iterates through all nodes, builds a string representation of the sequence,
     * and compares it against an expected string. The format of the string is defined by
     * {@link #appendNodeDescription(Node, StringBuilder)}.
     *
     * @param iterator The iterator to consume and verify.
     * @param expectedSequence A string representing the expected sequence of nodes.
     */
    private static <T extends Node> void assertIteratorProducesSequence(Iterator<T> iterator, String expectedSequence) {
        StringBuilder actualSequence = new StringBuilder();
        Node previousNode = null;

        while (iterator.hasNext()) {
            Node currentNode = iterator.next();

            assertNotNull(currentNode, "Iterator should not return null nodes.");
            assertNotSame(previousNode, currentNode, "Iterator should not return the same node instance consecutively.");

            appendNodeDescription(currentNode, actualSequence);
            previousNode = currentNode;
        }

        assertEquals(expectedSequence, actualSequence.toString(), "The sequence of iterated nodes should match the expected order.");
    }

    /**
     * Appends a simplified string description of a Node to a StringBuilder for verification.
     * <ul>
     *     <li><b>Element:</b> "tagName" or "tagName#id" if an ID exists.</li>
     *     <li><b>TextNode:</b> The text content of the node.</li>
     *     <li><b>Other Nodes:</b> The result of {@link Node#nodeName()}.</li>
     * </ul>
     * Each description is followed by a semicolon separator.
     *
     * @param node The node to describe.
     * @param builder The StringBuilder to append the description to.
     */
    private static void appendNodeDescription(Node node, StringBuilder builder) {
        if (node instanceof Element) {
            Element el = (Element) node;
            builder.append(el.tagName());
            if (el.hasAttr("id")) {
                builder.append("#").append(el.id());
            }
        } else if (node instanceof TextNode) {
            builder.append(((TextNode) node).text());
        } else {
            builder.append(node.nodeName());
        }
        builder.append(";");
    }
}