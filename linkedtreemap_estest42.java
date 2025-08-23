package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link LinkedTreeMap.Node} inner class.
 */
public class LinkedTreeMapNodeTest {

    /**
     * Tests the reflexive property of the equals() method.
     * A Node instance should always be equal to itself.
     */
    @Test
    public void nodeShouldBeEqualToItself() {
        // Arrange: Create a header node instance.
        // The constructor `new Node(boolean)` is used for creating the special header node.
        LinkedTreeMap.Node<Object, Integer> node = new LinkedTreeMap.Node<>(true);

        // Act & Assert: Verify that the node's equals() method returns true when
        // compared with the same instance.
        boolean isEqual = node.equals(node);
        assertTrue("A node instance should always be equal to itself.", isEqual);
    }
}