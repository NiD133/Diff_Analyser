package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * This test focuses on the behavior of the EntrySet returned by a LinkedTreeMap,
 * specifically its contains() method.
 */
public class LinkedTreeMapEntrySetTest {

    @Test
    public void contains_onEmptySet_shouldReturnFalse() {
        // Arrange
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();

        // Create a sample entry that is not in the map.
        // Using AbstractMap.SimpleEntry is a standard way to create a Map.Entry for testing.
        Map.Entry<Integer, Integer> nonExistentEntry = new AbstractMap.SimpleEntry<>(-1, 100);

        // Act
        boolean result = entrySet.contains(nonExistentEntry);

        // Assert
        assertFalse("contains() on an empty set should always return false.", result);
        assertEquals("The size of an empty entry set should be 0.", 0, entrySet.size());
    }
}