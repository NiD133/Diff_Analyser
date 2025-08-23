package com.google.gson.internal;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the LinkedTreeMap.
 * The original test class name 'LinkedTreeMap_ESTestTest10' is kept for reference.
 */
public class LinkedTreeMap_ESTestTest10 {

    /**
     * Verifies that keySet() on a map with one entry returns a correct Set
     * containing that entry's key, without modifying the original map.
     */
    @Test
    public void keySet_onNonEmptyMap_returnsCorrectKeys() {
        // Arrange: Create a map and add a single key-value pair.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Integer key = 1;
        String value = "one";
        map.put(key, value);

        // Act: Retrieve the key set from the map.
        Set<Integer> keySet = map.keySet();

        // Assert: Verify the state of both the map and the returned key set.
        // 1. The original map's size should remain unchanged.
        assertEquals("The map size should not change after calling keySet()", 1, map.size());

        // 2. The returned keySet should be valid.
        assertEquals("The keySet should contain one element", 1, keySet.size());
        assertTrue("The keySet should contain the added key", keySet.contains(key));
    }
}