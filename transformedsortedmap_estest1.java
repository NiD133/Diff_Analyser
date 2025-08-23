package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Tests for {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    @Test
    public void subMapOnEmptyMapShouldReturnEmptyMap() {
        // Arrange
        // Create an empty sorted map to be decorated. The transformers are null
        // as the transformation logic is not relevant to this test case.
        final SortedMap<Integer, Integer> emptySourceMap = new TreeMap<>();
        final TransformedSortedMap<Integer, Integer> transformedMap =
                new TransformedSortedMap<>(emptySourceMap, null, null);

        final Integer fromKey = -100;
        final Integer toKey = 100;

        // Act
        // Retrieve a sub map from the empty transformed map.
        final SortedMap<Integer, Integer> subMap = transformedMap.subMap(fromKey, toKey);

        // Assert
        // The resulting sub map should also be empty.
        assertTrue("subMap of an empty map should be empty", subMap.isEmpty());
    }
}