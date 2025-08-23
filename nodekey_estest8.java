package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A collection of tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Tests the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void testEquals_isReflexive() {
        // Arrange: Create a NodeKey instance.
        NodeKey<String> nodeKey = new NodeKey<>(1, "NodeA");

        // Act & Assert: An object must be equal to itself.
        // The assertEquals method uses the .equals() method for comparison.
        assertEquals("A NodeKey instance should be equal to itself.", nodeKey, nodeKey);
    }
}