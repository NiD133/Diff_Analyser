package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeIterator}.
 * This suite focuses on verifying the traversal logic from various starting points and with type filtering.
 */
class NodeIteratorTest {

    private final String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
    private Document doc;

    @BeforeEach
    void setUp() {
        doc = Jsoup.parse(html);
    }

    @Nested
    @DisplayName("when iterating from an element with only a text node child")
    class WhenIteratingFromLeafLikeElement {

        @Test
        @DisplayName("an unfiltered iterator should yield the element and its text node")
        void unfilteredIterationYieldsElementAndText() {
            // Arrange
            Element startNode = doc.expectFirst("p:contains(Two)"); // The <p>Two</p> element
            // The expected trace is "p;" for the element, and "Two;" for its text node.
            String expectedTrace = "p;Two;";

            // Act
            NodeIterator<Node> iterator = NodeIterator.from(startNode);

            // Assert
            assertNodeTrace(iterator, expectedTrace);
        }

        @Test
        @DisplayName("an element-filtered iterator should yield only the starting element")
        void elementFilteredIterationYieldsOnlyElement() {
            // Arrange
            Element startNode = doc.expectFirst("p:contains(Two)");

            // Act
            NodeIterator<Element> elementIterator = new NodeIterator<>(startNode, Element.class);

            // Assert
            assertTrue(elementIterator.hasNext(), "Iterator should have the starting element.");
            Element foundElement = elementIterator.next();
            assertSame(startNode, foundElement, "The found element should be the start node.");
            assertFalse(elementIterator.hasNext(), "Iterator should have no more elements.");
        }
    }

    // --- Helper Methods for Traversal Verification ---

    /**
     * Asserts that an iterator produces a sequence of nodes matching an expected trace string.
     *
     * @param iterator       The iterator to test.
     * @param expectedTrace  A semicolon-separated string representing the expected nodes.
     * @see #appendNodeDescription(Node, StringBuilder) for details on the trace format.
     */
    private static <T extends Node> void assertNodeTrace(Iterator<T> iterator, String expectedTrace) {
        Node previousNode = null;
        StringBuilder actualTrace = new StringBuilder();

        while (iterator.hasNext()) {
            Node currentNode = iterator.next();
            assertNotNull(currentNode);
            assertNotSame(previousNode, currentNode, "Iterator should not return the same node instance consecutively.");
            appendNodeDescription(currentNode, actualTrace);
            previousNode = currentNode;
        }
        assertEquals(expectedTrace, actualTrace.toString());
    }

    /**
     * Appends a compact, descriptive string of a Node to a StringBuilder for tracing.
     * <ul>
     *     <li><b>Element:</b> tagName[#id] (e.g., "div#1")</li>
     *     <li><b>TextNode:</b> its text content (e.g., "Two")</li>
     *     <li><b>Other Nodes:</b> node's name (e.g., "#document")</li>
     * </ul>
     * Each description is terminated with a semicolon ';'.
     *
     * @param node   The node to describe.
     * @param traceBuilder The StringBuilder to append the description to.
     */
    private static void appendNodeDescription(Node node, StringBuilder traceBuilder) {
        if (node instanceof Element) {
            Element el = (Element) node;
            traceBuilder.append(el.tagName());
            if (el.hasAttr("id")) {
                traceBuilder.append("#").append(el.id());
            }
        } else if (node instanceof TextNode) {
            traceBuilder.append(((TextNode) node).text());
        } else {
            traceBuilder.append(node.nodeName());
        }
        traceBuilder.append(";");
    }
}