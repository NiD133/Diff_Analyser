package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link LinkedTreeMap.Node#equals(Object)} method.
 */
public class LinkedTreeMapNodeEqualsTest {

    /**
     * Tests that a regular node (with a non-null key) is not considered equal
     * to a special "header" node (which has a null key).
     */
    @Test
    public void nodeEquals_withRegularNodeAndHeaderNode_shouldReturnFalse() {
        // Arrange: Create a header node and a regular node.
        // A header node is a special node used internally by LinkedTreeMap, with a null key.
        LinkedTreeMap.Node<Integer, Integer> headerNode = new LinkedTreeMap.Node<>(true);

        // A regular node represents a map entry and has a key.
        Integer key = -1;
        LinkedTreeMap.Node<Integer, Integer> regularNode = new LinkedTreeMap.Node<>(
                true, headerNode, key, headerNode, headerNode);

        // Act: Compare the regular node with the header node.
        boolean areEqual = regularNode.equals(headerNode);

        // Assert: The nodes should not be equal because their keys are different (one is -1, the other is null).
        assertFalse("A regular node should not be equal to a header node", areEqual);
    }
}