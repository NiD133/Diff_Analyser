package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LinkedTreeMapTest {

    /**
     * Tests that {@link LinkedTreeMap#containsKey(Object)} returns {@code false}
     * when called with an object of an incompatible type.
     * <p>
     * The implementation of {@code containsKey} for a tree-based map often involves
     * casting the key to a {@code Comparable} to perform a search. This test ensures
     * that if a key of an incompatible type is passed (which would cause a
     * {@code ClassCastException}), the method handles it gracefully and returns {@code false}.
     */
    @Test
    public void containsKeyShouldReturnFalseForIncompatibleType() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        map.put(1, "one");

        // An object of a type that cannot be cast to the map's key type (Integer).
        Object incompatibleKey = new Object();

        // Act
        boolean found = map.containsKey(incompatibleKey);

        // Assert
        assertFalse("containsKey should return false for an incompatible key type.", found);
        assertEquals("The map's size should remain unchanged after the check.", 1, map.size());
    }
}