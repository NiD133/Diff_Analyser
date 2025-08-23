package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LinkedTreeMap_ESTestTest53 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that calling the internal method `removeInternal` on an existing node
     * correctly removes the associated entry and decrements the map's size.
     */
    @Test
    public void removeInternal_whenNodeExists_removesEntryAndDecrementsSize() {
        // Arrange: Create a map and add two distinct entries.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Integer keyToRemove = 1;
        Integer keyToKeep = 2;

        map.put(keyToRemove, "value to remove");
        map.put(keyToKeep, "value to keep");

        // Pre-condition check to ensure the map is set up correctly.
        assertEquals("Map should contain two entries before removal.", 2, map.size());

        // This is a white-box test, so we find the internal node for the key we want to remove.
        LinkedTreeMap.Node<Integer, String> nodeToRemove = map.find(keyToRemove, false);
        assertNotNull("The node to be removed must exist in the map.", nodeToRemove);

        // Act: Remove the node directly using the internal method under test.
        map.removeInternal(nodeToRemove, true);

        // Assert: Verify the map's state is correct after the removal.
        assertEquals("Map size should be 1 after removing one entry.", 1, map.size());
        assertFalse("The removed key should no longer be present.", map.containsKey(keyToRemove));
        assertTrue("The other key should remain in the map.", map.containsKey(keyToKeep));
    }
}