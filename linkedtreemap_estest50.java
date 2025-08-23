package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedTreeMap;

/**
 * Contains tests for the {@link LinkedTreeMap} class.
 * This class focuses on improving the understandability of a specific test case.
 */
public class LinkedTreeMap_ESTestTest50 { // Original class name kept for context

    /**
     * Tests that `putIfAbsent` correctly adds a new key-value pair when the key
     * does not already exist in the map. It should return `null` and increase the map's size.
     */
    @Test
    public void putIfAbsent_whenKeyIsNew_shouldAddEntryAndReturnNull() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        // Pre-populate the map to ensure the test works on a non-empty map
        map.put(21, 21);
        map.put(0, 0);

        Integer newKey = -16;
        Integer valueForNewKey = null;

        // Act
        Integer previousValue = map.putIfAbsent(newKey, valueForNewKey);

        // Assert
        // 1. The method should return null, as there was no previous mapping for the key.
        assertNull("putIfAbsent should return null for a new key.", previousValue);

        // 2. The map should now contain the new key.
        assertTrue("The map should contain the newly added key.", map.containsKey(newKey));

        // 3. The value associated with the new key should be the one we inserted.
        assertNull("The value for the new key should be the one that was inserted.", map.get(newKey));

        // 4. The size of the map should have increased by one.
        assertEquals("The map size should increase by one.", 3, map.size());
    }
}