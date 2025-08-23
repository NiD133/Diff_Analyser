package com.google.gson.internal;

import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains tests for the {@link LinkedTreeMap.Node#equals(Object)} method.
 */
public class LinkedTreeMap_ESTestTest39 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that the Node.equals() method returns false when two nodes have the
     * same key but different values.
     */
    @Test(timeout = 4000)
    public void nodeEquals_shouldReturnFalse_whenNodesHaveSameKeyButDifferentValues() {
        // Arrange
        // The Node.equals() method compares nodes based on their key and value.
        // We will create two nodes that have the same key (null) but different values.

        // Create a node with a null key and a specific value.
        // The simple Node constructor initializes the key to null.
        Node<Integer, Integer> nodeWithAValue = new Node<>(false);
        nodeWithAValue.value = -2;

        // Create another node with a null key and no assigned value (i.e., value is null).
        Node<Integer, Integer> nodeWithoutAValue = new Node<>(false);

        // Act
        // Compare the two nodes. The expected result is false because their values differ.
        boolean areEqual = nodeWithoutAValue.equals(nodeWithAValue);

        // Assert
        assertFalse("Nodes with different values should not be considered equal.", areEqual);
    }
}