package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the NodeKey class.
 */
public class NodeKeyTest {

    /**
     * Verifies that two NodeKey instances are not considered equal if they have
     * the same node but different stage values. The equals() method should be
     * symmetric.
     */
    @Test
    public void equals_shouldReturnFalse_whenStagesDifferAndNodesAreSame() {
        // Arrange: Create two NodeKey instances with the same node but different stages.
        Integer commonNode = 100;
        NodeKey<Integer> keyWithStage1 = new NodeKey<>(1, commonNode);
        NodeKey<Integer> keyWithStage2 = new NodeKey<>(2, commonNode);

        // Act & Assert: The keys should not be equal to each other.
        // Test for equality
        assertFalse("Keys with different stages should not be equal.",
                keyWithStage1.equals(keyWithStage2));

        // Test for symmetry of the equals method
        assertFalse("Symmetry check: Keys with different stages should not be equal.",
                keyWithStage2.equals(keyWithStage1));
    }
}