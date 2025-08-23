package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

/**
 * This test class contains an improved version of the original test case.
 */
public class LinkedTreeMapImprovedTest {

    /**
     * Tests that a ClassCastException is thrown when attempting to insert a key
     * that is not mutually comparable with existing keys in the map.
     *
     * A LinkedTreeMap created with the default constructor uses natural ordering,
     * which requires all keys to implement the Comparable interface and be
     * comparable to one another.
     */
    @Test
    public void putWithIncompatibleKeyTypeThrowsClassCastException() {
        // Arrange: Create a map that uses natural ordering for its keys.
        // This requires all keys to be of a compatible, comparable type.
        LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();

        // Add an initial entry with a String key. All subsequent keys will be
        // compared to this key.
        map.put("a string key", "value1");

        // Act & Assert: Attempt to add an entry with an incompatible key type (Integer).
        // This should fail because an Integer cannot be compared to a String.
        Integer incompatibleKey = 123;

        // The map's internal logic will try to execute `((Comparable)incompatibleKey).compareTo("a string key")`,
        // which results in a ClassCastException.
        assertThrows(ClassCastException.class, () -> {
            map.put(incompatibleKey, "value2");
        });
    }
}