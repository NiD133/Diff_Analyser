package com.google.gson.internal;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class LinkedTreeMap_ESTestTest70 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that findByEntry correctly locates an existing entry
     * without altering the map's state.
     */
    @Test
    public void findByEntry_whenEntryExists_shouldReturnTheEntryAndNotChangeMapSize() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Integer key = -1960;

        // The find(key, create=true) method inserts a new node if the key is not present.
        // We use it here, as the original test did, to set up the map state with a single node.
        LinkedTreeMap.Node<Integer, String> nodeToFind = map.find(key, true);
        assertEquals("Pre-condition failed: Map should contain one entry after setup.", 1, map.size());

        // Act
        // Attempt to find the node that was just inserted.
        Map.Entry<Integer, String> foundNode = map.findByEntry(nodeToFind);

        // Assert
        // Verify that the correct node was returned and the map's size remains unchanged.
        assertSame("findByEntry should return the exact same node instance.", nodeToFind, foundNode);
        assertEquals("Map size should not change after finding an entry.", 1, map.size());
    }
}