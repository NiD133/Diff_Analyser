package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the equals() method returns false for two NodeKey instances
     * that have the same stage but different node values. This test also ensures
     * that equality checking works correctly against a cloned object.
     */
    @Test
    public void equals_withSameStageAndDifferentNode_returnsFalse() throws CloneNotSupportedException {
        // Arrange
        final int commonStage = 2281;
        NodeKey<Integer> nodeKeyA = new NodeKey<>(commonStage, 3);
        NodeKey<Integer> nodeKeyB = new NodeKey<>(commonStage, 2281);

        // Act
        // Clone one of the keys to ensure the equals method is robust and not
        // relying on object identity.
        NodeKey<Integer> clonedNodeKeyB = (NodeKey<Integer>) nodeKeyB.clone();

        // Assert
        // The two keys should not be equal because their node values differ.
        assertNotEquals(nodeKeyA, clonedNodeKeyB);
    }
}