package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeIterator}.
 */
public class NodeIteratorTest {

    // The HTML structure used for the tests.
    // Note: Jsoup will add html, head, and body tags, and a #root document node.
    private final String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";

    /**
     * Tests that the NodeIterator, when configured with a specific type (Element.class),
     * traverses the DOM tree and returns only nodes of that type.
     */
    @Test
    void iteratorReturnsOnlyElementsWhenFiltered() {
        // Arrange
        Document doc = Jsoup.parse(html);
        NodeIterator<Element> elementIterator = new NodeIterator<>(doc, Element.class);

        // The expected signature is a semicolon-separated list of node representations.
        // For Elements, the format is: tagName[#id]
        String expectedTraversal = "#root;html;head;body;div#1;p;p;div#2;p;p;";

        // Act & Assert
        // We use a custom assertion helper to check the entire traversal sequence.
        assertTraversal(elementIterator, expectedTraversal);
    }

    /**
     * A custom assertion that iterates through the provided iterator, builds a string
     * signature of the traversed nodes, and compares it to an expected signature.
     * It also verifies that each node is non-null and distinct from the previous one.
     *
     * @param iterator the iterator to test.
     * @param expected the expected traversal signature string.
     * @param <T>      the type of Node being iterated.
     */
    private static <T extends Node> void assertTraversal(Iterator<T> iterator, String expected) {
        StringBuilder actualTraversal = new StringBuilder();
        Node previousNode = null;

        while (iterator.hasNext()) {
            Node currentNode = iterator.next();

            assertNotNull(currentNode, "Iterator should not return null nodes.");
            assertNotSame(previousNode, currentNode, "Iterator should not return the same node instance consecutively.");

            appendNodeSignature(currentNode, actualTraversal);
            previousNode = currentNode;
        }

        assertEquals(expected, actualTraversal.toString());
    }

    /**
     * Appends a simple string representation of a Node to a StringBuilder for assertion purposes.
     * <ul>
     *     <li><b>Element:</b> tagName[#id] (e.g., "div#1;")</li>
     *     <li><b>TextNode:</b> text content (e.g., "One;")</li>
     *     <li><b>Other Nodes:</b> node name (e.g., "#document;")</li>
     * </ul>
     *
     * @param node   The node to represent.
     * @param builder The StringBuilder to append the signature to.
     */
    private static void appendNodeSignature(Node node, StringBuilder builder) {
        if (node instanceof Element) {
            Element el = (Element) node;
            builder.append(el.tagName());
            if (el.hasAttr("id")) {
                builder.append("#").append(el.id());
            }
        } else if (node instanceof TextNode) {
            builder.append(((TextNode) node).text());
        } else {
            // For other node types like Document, Comment, etc.
            builder.append(node.nodeName());
        }
        builder.append(";");
    }
}