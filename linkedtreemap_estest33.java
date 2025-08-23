package com.google.gson.internal;

import org.junit.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link LinkedTreeMap.EntrySet} class.
 */
public class LinkedTreeMapEntrySetTest {

    /**
     * Tests that calling remove() on an EntrySet with a Node object
     * that belongs to a different LinkedTreeMap instance does not remove the node
     * and correctly returns false.
     */
    @Test
    public void remove_whenGivenNodeFromDifferentMap_shouldReturnFalseAndNotModifyMaps() {
        // Arrange
        // Create the primary map from which we'll get the EntrySet.
        LinkedTreeMap<Integer, Object> map = new LinkedTreeMap<>();
        Set<Map.Entry<Integer, Object>> entrySet = map.entrySet();

        // Create a second, different map and add a node to it.
        LinkedTreeMap<Integer, Integer> anotherMap = new LinkedTreeMap<>();
        Integer key = -2139;
        // The find(key, true) method creates and adds the node if it doesn't exist.
        LinkedTreeMap.Node<Integer, Integer> nodeFromAnotherMap = anotherMap.find(key, true);

        // Sanity check to ensure the setup is correct.
        assertEquals(1, anotherMap.size());
        assertTrue(anotherMap.containsKey(key));
        assertTrue(map.isEmpty());

        // Act
        // Attempt to remove the node from the *first* map's entry set,
        // using the node object that belongs to the *second* map.
        boolean wasRemoved = entrySet.remove(nodeFromAnotherMap);

        // Assert
        // The remove operation should fail because the node is not part of the first map.
        assertFalse("remove() should return false for a node from a different map", wasRemoved);

        // Verify that neither map was modified by the operation.
        assertEquals("The original map should remain empty", 0, map.size());
        assertEquals("The other map's size should not be affected", 1, anotherMap.size());
    }
}