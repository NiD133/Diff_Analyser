package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link NodeIterator}.
 */
public class NodeIteratorTest {

    /**
     * Creates a simple, readable string signature for a given Node.
     * This helps in asserting the sequence of traversed nodes.
     * @param node The node to create a signature for.
     * @return A string representation (e.g., "div#1", "p", "Some text").
     */
    private String getNodeSignature(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            if (el.hasAttr("id")) {
                return el.tagName() + "#" + el.id();
            }
            return el.tagName();
        }
        if (node instanceof TextNode) {
            return ((TextNode) node).text();
        }
        // For other node types like Document, Comment, etc.
        return node.nodeName();
    }

    @Test
    void iteratorCanBeRestartedToANewNode() {
        // Arrange: Set up the HTML document and the iterator
        String html = "<div id=1><p>One<p>Two</div><div id=2><p>Three<p>Four</div>";
        Document doc = Jsoup.parse(html);
        NodeIterator<Node> iterator = NodeIterator.from(doc);

        // Act & Assert: Verify the initial full traversal
        List<Node> allNodes = new ArrayList<>();
        iterator.forEachRemaining(allNodes::add); // Collect all nodes from the initial iteration

        List<String> actualSignatures = allNodes.stream()
            .map(this::getNodeSignature)
            .collect(Collectors.toList());

        List<String> expectedFullSignatures = List.of(
            "#root", "html", "head", "body", "div#1", "p", "One", "p", "Two", "div#2", "p", "Three", "p", "Four"
        );
        assertEquals(expectedFullSignatures, actualSignatures, "Should iterate through all nodes initially");

        // Arrange: Get the node from which the iteration should restart
        Element restartNode = doc.expectFirst("div#2");

        // Act: Restart the iterator at the new node
        iterator.restart(restartNode);

        // Assert: Verify the traversal from the new starting point
        List<Node> restartedNodes = new ArrayList<>();
        iterator.forEachRemaining(restartedNodes::add); // Collect nodes after restarting

        List<String> actualRestartedSignatures = restartedNodes.stream()
            .map(this::getNodeSignature)
            .collect(Collectors.toList());

        List<String> expectedRestartedSignatures = List.of(
            "div#2", "p", "Three", "p", "Four"
        );
        assertEquals(expectedRestartedSignatures, actualRestartedSignatures, "Should iterate from the new start node after restart");
    }
}