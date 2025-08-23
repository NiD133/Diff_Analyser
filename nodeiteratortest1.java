package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Understandable tests for the {@link NodeIterator}.
 * These tests verify the correct traversal order and the iterator's behavior at the end of the stream.
 */
public class NodeIteratorTest { // Renamed from NodeIteratorTestTest1 for clarity.

    private Document doc;

    @BeforeEach
    void setUp() {
        // Arrange: A consistent HTML structure is parsed before each test.
        String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
        doc = Jsoup.parse(html);
    }

    /**
     * A private helper to convert a Node into a simple, readable string.
     * This makes the test's expected output easy to read and verify.
     * @param node The node to represent as a string.
     * @return A simplified string representation of the node.
     */
    private String toSimpleString(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            // Format as "tagName#id" if an ID exists, otherwise just "tagName".
            return el.hasAttr("id") ? el.tagName() + "#" + el.id() : el.tagName();
        }
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        // For other node types like Document, Comment, etc., use the node's name.
        return node.nodeName();
    }

    @Test
    @DisplayName("Iterator should traverse all nodes in document order")
    void iteratorTraversesAllNodesInDocumentOrder() {
        // Arrange: Define the expected sequence of nodes. This clearly documents
        // the expected depth-first traversal order.
        List<String> expectedNodeSequence = Arrays.asList(
            "#root",    // Document node
            "html",
            "head",
            "body",
            "div#1",
            "p",
            "One",      // TextNode
            "p",
            "Two",      // TextNode
            "div#2",
            "p",
            "Three",    // TextNode
            "p",
            "Four"      // TextNode
        );
        NodeIterator<Node> iterator = NodeIterator.from(doc);
        List<Node> traversedNodes = new ArrayList<>();

        // Act: Use the iterator to collect all traversed nodes.
        iterator.forEachRemaining(traversedNodes::add);

        // Create a list of strings from the traversed nodes for comparison.
        List<String> actualNodeSequence = traversedNodes.stream()
            .map(this::toSimpleString)
            .collect(Collectors.toList());

        // Assert: Verify that the actual sequence of nodes matches the expected sequence.
        assertEquals(expectedNodeSequence, actualNodeSequence);
    }

    @Test
    @DisplayName("Iterator should behave correctly after traversal is complete")
    void iteratorBehavesCorrectlyAtEndOfStream() {
        // Arrange
        NodeIterator<Node> iterator = NodeIterator.from(doc);

        // Act: Exhaust the iterator by consuming all its elements.
        while (iterator.hasNext()) {
            iterator.next();
        }

        // Assert:
        // 1. hasNext() should now return false.
        assertFalse(iterator.hasNext(), "hasNext() must be false after the iterator is exhausted.");

        // 2. next() should throw an exception.
        // Using assertThrows is the standard, readable way to test for exceptions in JUnit 5.
        assertThrows(NoSuchElementException.class, iterator::next,
            "next() must throw NoSuchElementException after the iterator is exhausted.");
    }
}