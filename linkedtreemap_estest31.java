package com.google.gson.internal;

import org.junit.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkedTreeMap_ESTestTest31 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Tests that removing an existing entry via the EntrySet view succeeds.
     *
     * <p>When an entry that is present in the map is passed to {@code entrySet.remove()},
     * the method should return true, and the entry should be removed from the map.
     */
    @Test
    public void entrySetRemove_whenEntryExists_shouldRemoveEntryAndReturnTrue() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.put(1, 100); // Add an entry to the map.

        // To test remove(Object), we must pass the exact Entry object from the map.
        // The most reliable way to get it is from the entry set itself.
        Map.Entry<Integer, Integer> entryToRemove = map.entrySet().iterator().next();
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();

        assertEquals("Precondition: map should contain one entry before removal.", 1, map.size());

        // Act
        boolean wasRemoved = entrySet.remove(entryToRemove);

        // Assert
        assertTrue("remove() should return true for an existing entry.", wasRemoved);
        assertTrue("Map should be empty after removing its only entry.", map.isEmpty());
        assertEquals("Entry set size should be 0 after removal.", 0, entrySet.size());
    }
}