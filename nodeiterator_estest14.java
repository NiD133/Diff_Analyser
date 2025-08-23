package org.jsoup.nodes;

import org.junit.Test;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;

/**
 * Test suite for the NodeIterator class.
 */
public class NodeIteratorTest {

    /**
     * Tests that a NodeIterator initialized with an empty document correctly traverses
     * only the root Document node itself and then stops.
     */
    @Test
    public void iteratorForEmptyDocumentShouldReturnOnlyDocumentNode() {
        // Arrange: Create an empty document, which consists of a single root node.
        Document emptyDocument = new Document("");

        // Act: Create an iterator to traverse all nodes starting from the root.
        NodeIterator<Node> iterator = new NodeIterator<>(emptyDocument, Node.class);

        // Assert: The iterator should first find the document node itself.
        assertTrue("Iterator should have the root document node.", iterator.hasNext());
        Node firstNode = iterator.next();
        assertSame("The first and only node should be the document itself.", emptyDocument, firstNode);

        // Assert: After the root, the iterator should be exhausted.
        assertFalse("Iterator should have no more nodes after the root.", iterator.hasNext());

        // Assert: Calling next() again should throw an exception.
        assertThrows("Should throw NoSuchElementException when no elements are left.",
            NoSuchElementException.class,
            iterator::next);
    }
}