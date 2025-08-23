package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behavior of NodeIterator when the underlying document is modified during iteration.
 */
public class NodeIteratorModificationTest {

    private Document document;

    @BeforeEach
    void setUp() {
        String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
        document = Jsoup.parse(html);
    }

    /**
     * This test verifies that the NodeIterator correctly handles modifications to the document
     * while performing a filtered iteration.
     *
     * It ensures two things:
     * 1. The iterator completes its traversal over all expected elements without being affected
     *    by the content changes.
     * 2. The modifications made to the elements during the traversal are correctly persisted in the document.
     */
    @Test
    void whenModifyingElementsDuringFilteredIteration_IteratorCompletesAndDocumentIsUpdated() {
        // Arrange: Create an iterator that will only return nodes of type Element.
        NodeIterator<Element> elementIterator = new NodeIterator<>(document, Element.class);
        StringBuilder traversedElementsLog = new StringBuilder();

        // Act: Iterate over the elements and modify their text content.
        // The iterator should be robust enough to handle these concurrent modifications.
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            if (!element.ownText().isEmpty()) {
                element.text(element.ownText() + "++");
            }
            trackNode(element, traversedElementsLog);
        }

        // Assert
        // 1. Verify that the iterator traversed all the expected *elements*. The log should
        //    show the sequence of elements visited, proving the iteration was not disrupted.
        String expectedTraversalPath = "#root;html;head;body;div#1;p;p;div#2;p;p;";
        assertEquals(expectedTraversalPath, traversedElementsLog.toString(),
            "Iterator should traverse all elements despite modifications.");

        // 2. Verify the final state of the document. A new, full traversal (including text nodes)
        //    should show that the text modifications were successful.
        String expectedFinalContent = "#root;html;head;body;div#1;p;One++;p;Two++;div#2;p;Three++;p;Four++;";
        assertDocumentContents(document, expectedFinalContent);
    }

    // --- Test Helper Methods ---

    /**
     * Asserts that a full, unfiltered traversal of the element's nodes matches an expected string signature.
     *
     * @param rootElement the element to traverse.
     * @param expectedContent the expected string signature of the traversed nodes.
     * @see #assertNodeTraversal(Iterator, String)
     */
    private static void assertDocumentContents(Element rootElement, String expectedContent) {
        NodeIterator<Node> it = NodeIterator.from(rootElement);
        assertNodeTraversal(it, expectedContent);
    }

    /**
     * Consumes an iterator, generates a string signature of the traversed nodes, and asserts it matches an expected value.
     * Also asserts that each node is non-null and distinct from the previous one.
     *
     * @param iterator the iterator to test.
     * @param expected the expected string signature.
     * @param <T> the type of Node being iterated.
     */
    private static <T extends Node> void assertNodeTraversal(Iterator<T> iterator, String expected) {
        Node previous = null;
        StringBuilder actual = new StringBuilder();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            assertNotNull(node);
            assertNotSame(previous, node, "Iterator should not return the same node twice in a row.");
            trackNode(node, actual);
            previous = node;
        }
        assertEquals(expected, actual.toString());
    }

    /**
     * Appends a string representation of a Node to a StringBuilder for logging and assertion.
     * - Element: tagName#id (if id exists)
     * - TextNode: its text content
     * - Other nodes: nodeName()
     * Each entry is followed by a semicolon.
     *
     * @param node the node to track.
     * @param log the StringBuilder to append to.
     */
    private static void trackNode(Node node, StringBuilder log) {
        if (node instanceof Element) {
            Element el = (Element) node;
            log.append(el.tagName());
            if (el.hasAttr("id"))
                log.append("#").append(el.id());
        } else if (node instanceof TextNode) {
            log.append(((TextNode) node).text());
        } else {
            log.append(node.nodeName());
        }
        log.append(";");
    }
}