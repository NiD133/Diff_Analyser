package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the internal {@link LinkedTreeMap#find(Object, boolean)} method.
 */
public class LinkedTreeMapFindTest {

    /**
     * Verifies that calling find(key, false) on an empty map does not
     * create a new node or modify the map in any way.
     */
    @Test
    public void findWithCreateFalse_onEmptyMap_shouldNotAddNode() {
        // Arrange: Create an empty LinkedTreeMap.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        String keyToFind = "some_key";

        // Act: Attempt to find a key without creating a new node if it's absent.
        LinkedTreeMap.Node<String, Integer> foundNode = map.find(keyToFind, false);

        // Assert: Verify that the map remains empty and the method returns null.
        assertNull("find() should return null for a non-existent key when create is false.", foundNode);
        assertTrue("The map should remain empty after the find operation.", map.isEmpty());
    }
}