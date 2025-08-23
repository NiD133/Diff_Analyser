package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the NodeIterator.
 * This version focuses on clarity by using parameterized tests and straightforward assertions.
 */
class NodeIteratorTest {

    private static final String HTML = "<div id=1><p>One</p><p>Two</p></div><div id=2><p>Three</p><p>Four</p></div>";

    /**
     * Provides test cases for iterating over different subtrees.
     * Each argument consists of a CSS selector to find the starting node
     * and a list of expected node representations in traversal order.
     */
    private static Stream<Arguments> subTreeTraversalProvider() {
        return Stream.of(
            Arguments.of(
                "div#1",
                List.of("div#1", "p", "One", "p", "Two")
            ),
            Arguments.of(
                "div#2",
                List.of("div#2", "p", "Three", "p", "Four")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("subTreeTraversalProvider")
    @DisplayName("Should iterate through all nodes in a subtree in document order")
    void iteratesOverSubTree(String startNodeSelector, List<String> expectedNodeRepresentations) {
        // Arrange
        Document doc = Jsoup.parse(HTML);
        Element startNode = doc.selectFirst(startNodeSelector);
        assertNotNull(startNode, "Test setup failed: start node not found with selector " + startNodeSelector);

        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // Act
        List<Node> traversedNodes = new ArrayList<>();
        iterator.forEachRemaining(traversedNodes::add); // Drain the iterator into a list

        List<String> actualNodeRepresentations = traversedNodes.stream()
            .map(NodeIteratorTest::getNodeRepresentation)
            .collect(Collectors.toList());

        // Assert
        assertEquals(expectedNodeRepresentations, actualNodeRepresentations);
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after traversal");
    }

    @Test
    @DisplayName("next() should throw NoSuchElementException when iterator is exhausted")
    void nextThrowsWhenExhausted() {
        // Arrange
        Document doc = Jsoup.parse("<p>One</p>");
        Element p = doc.expectFirst("p");
        NodeIterator<Node> iterator = NodeIterator.from(p);

        // Act: exhaust the iterator
        while (iterator.hasNext()) {
            iterator.next();
        }

        // Assert
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next,
            "Calling next() on an exhausted iterator should throw NoSuchElementException.");
    }

    /**
     * A simple helper to convert a Node into a readable string representation for assertions.
     * This replaces the complex `trackSeen` and `assertIterates` methods from the original test.
     * @param node The node to represent.
     * @return A string representation of the node.
     */
    private static String getNodeRepresentation(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            return el.hasAttr("id") ? el.tagName() + "#" + el.id() : el.tagName();
        }
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        // Fallback for other node types like DocumentType, Comment, etc.
        return node.nodeName();
    }
}