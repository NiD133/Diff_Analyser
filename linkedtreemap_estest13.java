package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link LinkedTreeMap} class, focusing on specific edge cases.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that findByObject() returns null when called with a key of an incompatible type,
     * without modifying the map.
     */
    @Test
    public void findByObject_withIncompatibleKeyType_shouldReturnNullAndNotModifyMap() {
        // Arrange: Create an empty map that expects Integer keys.
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        Object incompatibleKey = "this is a string, not an integer";

        // Act: Attempt to find a node using a key of an incompatible type.
        // The findByObject method is expected to catch the internal ClassCastException
        // that occurs when casting the key and return null.
        LinkedTreeMap.Node<Integer, String> foundNode = map.findByObject(incompatibleKey);

        // Assert: The method should return null, and the map should remain unchanged.
        assertNull("findByObject should return null for an incompatible key type.", foundNode);
        assertEquals("The map's size should remain 0 after a failed search.", 0, map.size());
    }
}