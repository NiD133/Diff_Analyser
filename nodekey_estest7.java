package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the equals() method correctly returns false when a NodeKey
     * instance is compared with a null object, adhering to the general contract
     * of Object.equals().
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithNull() {
        // Arrange: Create an instance of NodeKey.
        // The specific values for stage and node are not critical for this test.
        NodeKey<String> nodeKey = new NodeKey<>(1, "A");

        // Act & Assert: Call equals() with null and assert the result is false.
        assertFalse("A NodeKey instance should not be equal to null.", nodeKey.equals(null));
    }
}