package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the sub-map views of a {@link TransformedSortedMap}.
 */
public class TransformedSortedMap_ESTestTest5 {

    /**
     * Tests that the subMap() method returns a non-empty view when the
     * specified range contains an element from the underlying map.
     */
    @Test
    public void subMapShouldReturnNonEmptyViewForValidRange() {
        // Arrange
        final SortedMap<Integer, Integer> sourceMap = new TreeMap<>();
        final Integer keyInMap = -1;
        final Integer valueInMap = 3152;
        sourceMap.put(keyInMap, valueInMap);

        // A transformer is required by the constructor, but its transformation logic
        // is not relevant for this test, as we only verify the subMap view's content.
        final Transformer<Integer, Integer> dummyTransformer = ConstantTransformer.constantTransformer(null);

        final TransformedSortedMap<Integer, Integer> transformedMap =
                new TransformedSortedMap<>(sourceMap, dummyTransformer, dummyTransformer);

        // Define a range that includes the key present in the map.
        final Integer fromKey = -1;
        final Integer toKey = 4000;

        // Act
        final SortedMap<Integer, Integer> subMap = transformedMap.subMap(fromKey, toKey);

        // Assert
        assertFalse("The subMap should not be empty as the range includes an element.", subMap.isEmpty());
        assertEquals("The subMap should contain exactly one element.", 1, subMap.size());
        assertEquals("The subMap's first key should match the key within the range.", keyInMap, subMap.firstKey());
    }
}