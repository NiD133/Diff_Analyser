package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link NodeIterator} class.
 */
public class NodeIteratorTest {

    /**
     * Verifies that hasNext() returns true for an iterator created from a non-empty document,
     * indicating that there is at least one node to traverse (the document itself).
     */
    @Test
    public void hasNextReturnsTrueForNonEmptyDocument() {
        // 1. Arrange: Create a simple document with some content.
        Document doc = Parser.parseBodyFragment("<p>Hello</p>", "");

        // 2. Act: Create a NodeIterator starting from the document root.
        NodeIterator<Node> iterator = NodeIterator.from(doc);

        // 3. Assert: The iterator should report that it has a next element.
        assertTrue("Iterator should have a next element for a document with content.", iterator.hasNext());
    }
}