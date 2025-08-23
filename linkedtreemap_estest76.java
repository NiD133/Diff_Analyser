package com.google.gson.internal;

import org.junit.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for the EntrySet view of the LinkedTreeMap.
 * This focuses on the behavior of the clear() method.
 */
public class LinkedTreeMapEntrySetTest {

    @Test
    public void entrySetClear_removesAllEntriesFromBackingMap() {
        // Arrange: Create a LinkedTreeMap and add several entries.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        // The entrySet is a view of the map, so it should not be empty.
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        assertFalse("Precondition failed: Map should not be empty", map.isEmpty());
        assertEquals("Precondition failed: Entry set size should match map size", 3, entrySet.size());

        // Act: Clear the entry set.
        entrySet.clear();

        // Assert: The entry set and the backing map should both be empty.
        assertTrue("The entry set should be empty after clear()", entrySet.isEmpty());
        assertEquals("The entry set size should be 0 after clear()", 0, entrySet.size());

        assertTrue("The backing map should be empty after clearing its entry set", map.isEmpty());
        assertEquals("The backing map size should be 0 after clearing its entry set", 0, map.size());
    }
}