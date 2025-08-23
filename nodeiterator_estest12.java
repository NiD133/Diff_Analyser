package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the {@link NodeIterator} class.
 * This specific test focuses on the behavior of the restart() method.
 */
public class NodeIteratorTest {

    /**
     * Verifies that calling the restart() method with a null node argument
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void restartWithNullNodeThrowsNullPointerException() {
        // Arrange: Create an initial valid NodeIterator instance.
        // The specific node and type used for initialization are not important for this test.
        Document initialNode = new Document("");
        NodeIterator<Node> iterator = new NodeIterator<>(initialNode, Node.class);

        // Act: Attempt to restart the iterator with a null node.
        // Assert: The @Test(expected) annotation handles the assertion,
        // ensuring a NullPointerException is thrown.
        iterator.restart(null);
    }
}