package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

// Note: Unused imports from the original file have been removed for clarity.
// The original test class name is kept as requested.
public class LinkedTreeMap_ESTestTest37 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that {@link LinkedTreeMap.Node#last()} returns the node with the largest key
     * within the subtree rooted at the current node.
     */
    @Test
    public void nodeLast_whenCalledOnRoot_returnsNodeWithLargestKey() {
        // Arrange
        // Create a map and add two elements. Due to the natural ordering of integers,
        // the node with key -1 will be the root, and the node with key 1 will be its right child.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer keyMinusOne = -1;
        Integer keyOne = 1;
        map.put(keyMinusOne, -1);
        map.put(keyOne, 1);

        // Act
        // Find the node for key -1, which is the root of the entire tree.
        LinkedTreeMap.Node<Integer, Integer> rootNode = map.find(keyMinusOne, false);
        // Call last() on the root node. This should find the rightmost node in its subtree.
        LinkedTreeMap.Node<Integer, Integer> lastNodeInSubtree = rootNode.last();

        // Assert
        // The last node in the subtree of the root should be the node with the largest key in the map.
        assertNotNull("The last node should not be null", lastNodeInSubtree);
        assertEquals("The last node should have the key 1", keyOne, lastNodeInSubtree.getKey());
        assertNotSame("The last node should be a different object than the root node",
                rootNode, lastNodeInSubtree);
    }
}