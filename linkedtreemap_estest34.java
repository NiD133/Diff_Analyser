package com.google.gson.internal;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the EntrySet from LinkedTreeMap.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class LinkedTreeMapEntrySetTest {

    @Test
    public void entrySetContains_shouldReturnTrue_forExistingEntry() {
        // Arrange: Create a map and add a key-value pair.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Integer key = 100;
        String value = "ValueFor100";
        map.put(key, value);

        // Create a Map.Entry object equivalent to the one in the map.
        Map.Entry<Integer, String> existingEntry = new AbstractMap.SimpleEntry<>(key, value);
        Set<Map.Entry<Integer, String>> entrySet = map.entrySet();

        // Act: Check if the entry set contains the existing entry.
        boolean result = entrySet.contains(existingEntry);

        // Assert: Verify that the entry was found and the map size is correct.
        assertTrue("The entry set should contain an entry that was explicitly added to the map.", result);
        assertEquals("The map size should be 1 after adding one element.", 1, map.size());
    }
}