package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for {@link NodeIterator}.
 * This class focuses on edge cases and exception handling.
 */
public class NodeIteratorExceptionTest {

    /**
     * Verifies that the iterator throws a NullPointerException if it encounters a Node
     * with a null childNodes list during traversal. This tests the iterator's robustness
     * against unexpected or corrupt node structures.
     */
    @Test(expected = NullPointerException.class)
    public void nextThrowsNullPointerExceptionWhenChildNodesListIsNull() {
        // Arrange: Create a document and manually set its internal childNodes list to null
        // to simulate a corrupt or invalid state.
        Document document = new Document("");
        document.childNodes = null; // This is the specific invalid state under test.

        NodeIterator<Node> iterator = new NodeIterator<>(document, Node.class);

        // Act: Attempting to advance the iterator should fail when it tries to access
        // the null childNodes list.
        iterator.next();
    }
}