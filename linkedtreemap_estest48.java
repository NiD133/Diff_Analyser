package com.google.gson.internal;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the LinkedTreeMap class.
 * The original test class name and inheritance from the scaffolding are preserved.
 */
public class LinkedTreeMap_ESTestTest48 extends LinkedTreeMap_ESTest_scaffolding {

    /**
     * Verifies that calling entrySet() on a newly created, empty LinkedTreeMap
     * returns a set that is also empty.
     */
    @Test
    public void entrySet_onNewMap_returnsEmptySet() {
        // Arrange: Create a new, empty LinkedTreeMap.
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act: Get the entry set from the map.
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        // Assert: The resulting entry set should be empty.
        assertTrue("The entry set of a new map should be empty.", entrySet.isEmpty());
    }
}