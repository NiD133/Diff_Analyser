package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeIterator}.
 */
public class NodeIteratorTest {

    // A simple HTML structure for testing traversal.
    private static final String HTML = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    /**
     * Asserts that an iterator traverses a sequence of nodes that matches the expected representation.
     * It also performs checks during iteration:
     * <ul>
     *     <li>Each node is not null.</li>
     *     <li>Each node is distinct from the one before it.</li>
     * </ul>
     *
     * @param iterator the Node iterator to test.
     * @param expected a string representing the expected sequence of nodes, formatted by {@link #appendNodeRepresentation}.
     * @param <T> the type of Node
     */
    private static <T extends Node> void assertTraversalYields(Iterator<T> iterator, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            assertNotNull(node, "Iterator should not return null nodes.");
            assertNotSame(previous, node, "Iterator should not return the same node consecutively.");
            appendNodeRepresentation(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString());
    }

    /**
     * A convenience helper to assert the complete, unfiltered traversal of an element's descendants.
     * @param el the root element to traverse from.
     * @param expected a string representing the expected sequence of all nodes.
     */
    private static void assertCompleteTraversal(Element el, String expected) {
        NodeIterator<Node> iterator = NodeIterator.from(el);
        assertTraversalYields(iterator, expected);
    }

    /**
     * Appends a string representation of a Node to a StringBuilder for testing purposes.
     * The format is:
     * <ul>
     *     <li><b>Element:</b> tagName[#id] (e.g., "div#1;")</li>
     *     <li><b>TextNode:</b> text content (e.g., "One;")</li>
     *     <li><b>Other Nodes:</b> node name (e.g., "#root;")</li>
     * </ul>
     * Each representation is followed by a semicolon ';'.
     *
     * @param node the node to represent.
     * @param builder the StringBuilder to append to.
     */
    private static void appendNodeRepresentation(Node node, StringBuilder builder) {
        if (node instanceof Element) {
            Element el = (Element) node;
            builder.append(el.tagName());
            if (el.hasAttr("id"))
                builder.append("#").append(el.id());
        } else if (node instanceof TextNode)
            builder.append(((TextNode) node).text());
        else
            builder.append(node.nodeName());
        builder.append(";");
    }

    @Test
    void iterator_whenFilteredByClass_iteratesOverMatchingNodesOnly() {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        NodeIterator<TextNode> textNodeIterator = new NodeIterator<>(doc, TextNode.class);

        // Expected format is a semicolon-separated list of text contents.
        String expectedTextNodes = "One;Two;Three;Four;";

        // Act & Assert
        assertTraversalYields(textNodeIterator, expectedTextNodes);
    }

    @Test
    void iterator_whenUnfiltered_traversesAllNodesInDocumentOrder() {
        // Arrange
        Document doc = Jsoup.parse(HTML);

        // The expected string represents all nodes in document order.
        // See appendNodeRepresentation() for format details.
        String expectedAllNodes = "#root;html;head;body;div#1;p;One;p;Two;div#2;p;Three;p;Four;";

        // Act & Assert
        assertCompleteTraversal(doc, expectedAllNodes);
    }
}