package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransformedSortedMap_ESTestTest17 {

    /**
     * Tests that TransformedSortedMap propagates IllegalArgumentException from the
     * decorated map when a method is called with an out-of-range key.
     */
    @Test
    public void tailMapShouldThrowExceptionWhenKeyIsOutOfDecoratedSubMapRange() {
        // ARRANGE
        // Create a base map and a sub-map with a restricted key range.
        // The sub-map's range is [512, 512), so it's always empty, and any key
        // supplied to its range methods (headMap, tailMap, etc.) will be out of range.
        final TreeMap<Integer, Integer> baseMap = new TreeMap<>();
        final Integer outOfRangeKey = 512;
        final SortedMap<Integer, Integer> restrictedSubMap = baseMap.subMap(outOfRangeKey, outOfRangeKey);

        // Create a TransformedSortedMap decorating the restricted sub-map.
        // The transformers are not relevant to this test's logic, so we pass null.
        final SortedMap<Integer, Integer> transformedMap =
                TransformedSortedMap.transformingSortedMap(restrictedSubMap, null, null);

        // ACT & ASSERT
        // Calling tailMap with a key outside the sub-map's allowed range
        // should propagate the IllegalArgumentException from the underlying sub-map.
        try {
            transformedMap.tailMap(outOfRangeKey);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown by the underlying TreeMap.subMap.
            assertEquals("fromKey out of range", e.getMessage());
        }
    }
}