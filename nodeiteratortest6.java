package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link NodeIterator}, focusing on specific edge cases.
 */
public class NodeIteratorTest {

    /**
     * Verifies that when a NodeIterator starts from a leaf node (a node with no children),
     * it correctly returns only that starting node and then terminates.
     * This is an important edge case to ensure the iterator doesn't incorrectly
     * traverse to siblings or other unrelated nodes.
     */
    @Test
    @DisplayName("Iterator from a node with no children should return only the start node")
    void iteratorFromNodeWithNoChildren_returnsOnlyStartNode() {
        // Arrange: Set up a document with a leaf element (<p id=1>) and its siblings.
        // The iterator should only traverse the start node and its descendants, not its siblings.
        String html = "<div><p id=1></p><p id=2>Sibling</p></div>";
        Document doc = Jsoup.parse(html);
        Element startNode = doc.expectFirst("#1");

        // Sanity check to ensure our start node is indeed a leaf (has no children).
        assertTrue(startNode.childNodes().isEmpty(), "Pre-condition failed: Start node should be a leaf.");

        // Act: Create an iterator starting from the leaf node.
        NodeIterator<Node> iterator = NodeIterator.from(startNode);

        // Assert: The iterator should yield the start node once, and then be exhausted.
        // 1. It should have a next element (the start node itself).
        assertTrue(iterator.hasNext(), "Iterator should have the start node available.");

        // 2. The next element should be the start node instance.
        Node firstNode = iterator.next();
        assertSame(startNode, firstNode, "The first item from the iterator should be the start node itself.");

        // 3. After yielding the start node, the iterator should be exhausted.
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after returning the single leaf node.");

        // 4. Calling next() again should throw an exception.
        assertThrows(NoSuchElementException.class, iterator::next,
            "Calling next() on an exhausted iterator should throw an exception.");
    }
}