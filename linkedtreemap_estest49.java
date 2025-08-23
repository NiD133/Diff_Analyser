package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class focuses on the internal removal logic of {@link LinkedTreeMap}.
 */
public class LinkedTreeMap_ESTestTest49 {

    /**
     * Tests that calling the internal method {@code removeInternal} on a specific node
     * successfully removes it from the map and correctly updates the map's size.
     */
    @Test
    public void removeInternal_withPreviouslyFoundNode_updatesSizeAndRemovesElement() {
        // Arrange: Create a map and populate it with several elements.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        map.put(-2139, "value one");
        map.put(1995, "value two"); // This element will be removed.
        map.put(-1, "value three");
        map.put(3, "value four");

        // Sanity check to ensure the map is in the expected state before the test action.
        assertEquals("Map should contain 4 elements before removal.", 4, map.size());

        // Find the internal node corresponding to the key we want to remove.
        // The 'false' argument ensures we are finding an existing node, not creating one.
        LinkedTreeMap.Node<Integer, String> nodeToRemove = map.find(1995, false);
        assertNotNull("Precondition failed: Node to be removed must exist in the map.", nodeToRemove);

        // Act: Call the internal removal method on the specific node.
        // The 'true' argument indicates the node should also be unlinked from the iteration list.
        map.removeInternal(nodeToRemove, true);

        // Assert: Verify that the map's size has been reduced and the element is gone.
        assertEquals("Map size should decrease by one after removal.", 3, map.size());
        assertFalse("Map should no longer contain the removed key.", map.containsKey(1995));
        assertNull("Getting the removed key should return null.", map.get(1995));
    }
}