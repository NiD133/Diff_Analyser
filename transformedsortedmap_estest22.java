package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TransformedSortedMap} focusing on the behavior of its sub-map views.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that calling headMap on a TransformedSortedMap, which decorates a
     * restricted sub-map, correctly throws an IllegalArgumentException if the
     * key is outside the sub-map's valid range.
     */
    @Test
    public void headMapShouldThrowExceptionWhenKeyIsOutOfRangeOfDecoratedSubMap() {
        // Arrange
        // 1. Create a base TreeMap.
        final TreeMap<Integer, String> baseMap = new TreeMap<>();

        // 2. Create a sub-map view with a defined upper bound.
        //    This sub-map can only contain keys less than -100.
        final int upperBound = -100;
        final SortedMap<Integer, String> restrictedSubMap = baseMap.headMap(upperBound);

        // 3. Decorate the sub-map with a TransformedSortedMap.
        //    No actual key/value transformers are needed for this test.
        final TransformedSortedMap<Integer, String> transformedMap =
                new TransformedSortedMap<>(restrictedSubMap, null, null);

        // 4. Define a key that is outside the valid range of the decorated sub-map.
        final int outOfRangeKey = 0; // 0 is greater than the upper bound of -100.

        // Act & Assert
        // Verify that calling headMap with the out-of-range key propagates the
        // exception from the underlying sub-map.
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transformedMap.headMap(outOfRangeKey)
        );

        // The specific exception message comes from the underlying TreeMap's sub-map implementation.
        assertEquals("toKey out of range", exception.getMessage());
    }
}