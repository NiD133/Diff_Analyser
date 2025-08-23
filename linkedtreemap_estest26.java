package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for the inner class {@link LinkedTreeMap.Node}.
 */
public class LinkedTreeMapNodeTest {

    @Test
    public void first_whenNodeHasLeftChild_returnsLeftmostNode() {
        // Arrange: Create a parent node and set its left child.
        // The generic types are simplified to <String, String> because the actual
        // key-value types are irrelevant for testing the tree structure.
        LinkedTreeMap.Node<String, String> parentNode = new LinkedTreeMap.Node<>(true);
        LinkedTreeMap.Node<String, String> leftChild = new LinkedTreeMap.Node<>(true);
        parentNode.left = leftChild;

        // Act: Call the first() method to find the first node in the subtree.
        LinkedTreeMap.Node<String, String> result = parentNode.first();

        // Assert: The method should return the leftmost node, which is the left child we set.
        assertSame("The first() method should return the leftmost node in the subtree.", leftChild, result);
    }
}