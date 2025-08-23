package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link LinkedTreeMap} class.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that the size of the map is correctly reported as 1
     * after a single element is added to an empty map.
     */
    @Test
    public void size_returnsOne_afterAddingFirstElement() {
        // Arrange: Create an empty map.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();

        // Act: Add a single key-value pair.
        // Note: The original test used putIfAbsent(), which for an empty map
        // behaves identically to put(). Using put() is more direct and clear.
        map.put(1, "value1");

        // Assert: Verify that the size of the map is now 1.
        assertEquals(1, map.size());
    }
}