package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains improved versions of tests for {@link LinkedTreeMap}.
 * The original test was auto-generated and has been rewritten for clarity and maintainability.
 */
public class LinkedTreeMap_ESTestTest27 {

    /**
     * Tests that the internal removal method, `removeInternal`, correctly removes a specified node
     * from the map and updates the map's size accordingly.
     */
    @Test
    public void removeInternal_removesNodeAndDecrementsSize() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();

        // Add three elements to the map to create a known state.
        // The first is added via `putIfAbsent`.
        map.putIfAbsent(-1109, -1109);
        // The next two are added using the internal `find(key, create=true)` method,
        // which creates a new node with a null value if the key is not found.
        // This mimics the setup of the original auto-generated test.
        map.find(-968, true);
        LinkedTreeMap.Node<Integer, Integer> nodeToRemove = map.find(-1, true);

        // Verify the initial state before acting.
        assertEquals("Map should contain 3 elements before removal", 3, map.size());
        assertTrue("Key to be removed should exist before removal", map.containsKey(-1));

        // Act
        // Remove the node for key -1 directly using the internal method.
        map.removeInternal(nodeToRemove, true);

        // Assert
        // Check that the size has been updated and the correct element is gone.
        assertEquals("Map size should be 2 after removing one element", 2, map.size());
        assertFalse("The removed key should no longer be in the map", map.containsKey(-1));
        assertTrue("A non-removed key should still be in the map", map.containsKey(-1109));
        assertTrue("Another non-removed key should still be in the map", map.containsKey(-968));
    }
}