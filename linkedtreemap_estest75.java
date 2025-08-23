package com.google.gson.internal;

import org.junit.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the inner EntrySet class of {@link LinkedTreeMap}.
 */
public class LinkedTreeMapEntrySetTest {

    @Test
    public void sizeOfEntrySetForNewMapShouldBeZero() {
        // Arrange: Create a new, empty LinkedTreeMap.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act: Get the entry set from the map.
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        // Assert: The size of the entry set should be 0.
        assertEquals(0, entrySet.size());
    }
}