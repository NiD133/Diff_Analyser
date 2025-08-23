package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link LinkedTreeMap.Node} inner class, focusing on its equals() method.
 */
public class LinkedTreeMapNodeTest {

    /**
     * Tests that two Node instances are not considered equal if they have the same key (null)
     * but different values.
     */
    @Test
    public void nodeEquals_shouldReturnFalse_whenNodesHaveDifferentValues() {
        // Arrange
        // Create two default nodes. By default, their keys and values are null.
        // Using simple <String, String> types for better readability.
        LinkedTreeMap.Node<String, String> nodeA = new LinkedTreeMap.Node<>(false);
        LinkedTreeMap.Node<String, String> nodeB = new LinkedTreeMap.Node<>(false);

        // At this point, nodeA.equals(nodeB) would be true. To test the inequality case,
        // we set a value on one node, making it different from the other.
        String newValue = "a value";
        String previousValue = nodeB.setValue(newValue);

        // A quick sanity check to ensure the setup is correct: setValue should return the old value.
        assertNull("The previous value of the node should have been null.", previousValue);

        // Act
        // Compare the node with a value to the node that still has a null value.
        boolean areNodesEqual = nodeB.equals(nodeA);

        // Assert
        // The nodes should not be equal because their values differ.
        assertFalse("Nodes with different values should not be equal.", areNodesEqual);
    }
}