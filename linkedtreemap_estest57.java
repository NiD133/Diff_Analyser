package com.google.gson.internal;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

// Note: The original test was part of an auto-generated suite (LinkedTreeMap_ESTestTest57).
// This version is rewritten for clarity and focuses on a single, understandable test case.
public class LinkedTreeMapTest {

    /**
     * Verifies that calling `findByEntry` on an empty map does not cause errors
     * and does not modify the map's state.
     */
    @Test
    public void findByEntryOnEmptyMapReturnsNullAndHasNoSideEffects() {
        // Arrange
        // Create an empty map that uses natural ordering for its keys.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Create a standalone entry to search for. This entry is not part of the map.
        // The Node(boolean) constructor creates a node with a null key, which is a
        // valid scenario to test.
        Map.Entry<String, Integer> nonExistentEntry = new LinkedTreeMap.Node<>(false);

        // Act
        // Attempt to find the entry within the empty map.
        LinkedTreeMap.Node<String, Integer> result = map.findByEntry(nonExistentEntry);

        // Assert
        // The method should return null because the entry does not exist in the map.
        assertNull("findByEntry should return null for a non-existent entry.", result);

        // The find operation should not modify the map.
        assertEquals("The map size should remain unchanged after the find operation.", 0, map.size());
    }
}