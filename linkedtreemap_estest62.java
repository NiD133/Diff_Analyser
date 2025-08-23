package com.google.gson.internal;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link LinkedTreeMap} class.
 */
public class LinkedTreeMapTest {

    /**
     * Tests that calling `remove()` with a key of an incompatible type
     * does not modify the map or throw a ClassCastException.
     */
    @Test
    public void removeWithIncompatibleKeyTypeShouldNotModifyMap() {
        // Arrange
        Map<Integer, Integer> map = new LinkedTreeMap<>();
        assertTrue("Map should be empty initially", map.isEmpty());

        // An object of a type that cannot be a key in this map.
        // The map's own entrySet is used here as a convenient example.
        Object incompatibleKey = map.entrySet();

        // Act
        // This call should be gracefully ignored, returning null without modifying the map.
        map.remove(incompatibleKey);

        // Assert
        // The map's state should remain unchanged.
        assertTrue("Map should remain empty after the remove operation", map.isEmpty());
        assertEquals("Map size should still be 0", 0, map.size());
    }
}