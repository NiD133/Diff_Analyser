package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class contains tests for the LinkedTreeMap class.
 * This particular test focuses on the behavior of the get() method with a null key.
 */
public class LinkedTreeMap_ESTestTest58 { // Retaining original class name for context

    /**
     * Verifies that calling get() with a null key on an empty map
     * returns null and does not alter the map's state.
     */
    @Test
    public void get_whenKeyIsNullOnEmptyMap_returnsNullAndDoesNotModifyMap() {
        // Arrange: Create an empty LinkedTreeMap.
        // A simple key-value type (String, Integer) is used for clarity.
        // The default constructor is sufficient as the behavior for get(null)
        // is independent of the comparator.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act: Attempt to retrieve a value using a null key.
        Integer value = map.get(null);

        // Assert: Verify the behavior is correct.
        assertNull("The get(null) method should always return null.", value);
        assertEquals("The map's size should remain 0.", 0, map.size());
    }
}