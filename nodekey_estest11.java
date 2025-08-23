package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that cloning a NodeKey results in a new instance that is equal
     * to the original but is a distinct object in memory.
     */
    @Test
    public void clone_shouldCreateEqualButDistinctInstance() throws CloneNotSupportedException {
        // Arrange: Create an original NodeKey instance.
        int stage = 2281;
        Integer node = 3;
        NodeKey<Integer> originalKey = new NodeKey<>(stage, node);

        // Act: Clone the original instance.
        NodeKey<Integer> clonedKey = (NodeKey<Integer>) originalKey.clone();

        // Assert: The cloned instance should be equal to the original, but not the same instance.
        assertNotSame("A cloned object should be a new instance in memory.", originalKey, clonedKey);
        assertEquals("A cloned object should be equal to the original.", originalKey, clonedKey);

        // Explicitly verify that the internal state was copied correctly.
        assertEquals("The stage of the cloned key should match the original.", stage, clonedKey.getStage());
        assertEquals("The node of the cloned key should match the original.", node, clonedKey.getNode());
    }
}