package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the internal {@link LinkedTreeMap.Node} class.
 */
public class LinkedTreeMap_ESTestTest43 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies the reflexivity property of the Node.equals() method.
     * An object must be equal to itself.
     */
    @Test
    public void nodeEquals_shouldReturnTrueForSameInstance() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1;

        // Act
        // Use the package-private find(key, create=true) method to create and retrieve a node.
        // This also adds the new node to the map.
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);

        // Assert
        // First, confirm the map's state to ensure the setup was successful.
        assertEquals("Map size should be 1 after creating a node.", 1, map.size());

        // The main assertion: A node must be equal to itself.
        assertTrue("A node instance should be equal to itself.", node.equals(node));
    }
}