package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that two NodeKey instances are not considered equal if they have
     * the same node but different stage values.
     */
    @Test
    public void equals_shouldReturnFalse_whenStagesAreDifferent() throws CloneNotSupportedException {
        // Arrange: Create two NodeKey instances with the same node but different stages.
        Integer commonNode = 0;
        NodeKey<Integer> key1 = new NodeKey<>(-1934, commonNode);
        NodeKey<Integer> key2 = new NodeKey<>(-3799, commonNode);

        // Act: For the sake of comparison, create a clone of the second key.
        // This also helps verify that clone() produces an equal object.
        NodeKey<Integer> key2Clone = (NodeKey<Integer>) key2.clone();

        // Assert:
        // First, confirm the clone is equal to its original.
        assertEquals("A cloned key should be equal to its original.", key2, key2Clone);

        // The main assertion: key1 should not be equal to the clone of key2,
        // as their stages are different.
        assertNotEquals("Keys with different stages should not be equal.", key1, key2Clone);
    }
}