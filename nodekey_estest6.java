package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the equals() method returns false when a NodeKey is
     * compared to an object of a different, unrelated type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithObjectOfDifferentType() {
        // Arrange
        NodeKey<Integer> nodeKey = new NodeKey<>(2281, 3);
        Object otherObject = new Object();

        // Act
        boolean isEqual = nodeKey.equals(otherObject);

        // Assert
        assertFalse(isEqual);
    }
}