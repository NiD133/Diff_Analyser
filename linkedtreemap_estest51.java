package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link LinkedTreeMap} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class LinkedTreeMap_ESTestTest51 {

    /**
     * Tests that the map's size is correctly updated after a series of insertions
     * and a subsequent removal. This sequence of operations is designed to trigger
     * the tree's internal rebalancing logic.
     */
    @Test
    public void sizeIsCorrectAfterAddingAndRemovingElements() {
        // Arrange: Create a map and populate it with a specific sequence of keys
        // that will cause several internal tree rotations (rebalancing).
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer keyToRemove = -2139;

        map.putIfAbsent(keyToRemove, -2139);
        map.putIfAbsent(-36, null);

        // Add an element by calling the internal `find(key, create=true)` method.
        // This is equivalent to adding a key with a null value if it doesn't exist.
        map.find(-1109, true);

        map.putIfAbsent(1, -2139);

        // Pre-condition check: The map should have 4 elements before the removal.
        assertEquals("Map should contain 4 elements before removal", 4, map.size());

        // Act: Remove an element, which will also trigger rebalancing.
        map.remove(keyToRemove);

        // Assert: Verify that the size is correctly reported as 3 after the removal.
        assertEquals("Map size should be 3 after removing one element", 3, map.size());
    }
}