package org.apache.commons.collections4.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

/**
 * Unit tests for {@link TransformedSortedMap}.
 * This class focuses on providing clear, understandable, and maintainable tests.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that calling {@code headMap()} on an empty TransformedSortedMap
     * correctly returns an empty SortedMap.
     */
    @Test
    public void testHeadMapOnEmptyMapReturnsEmptyMap() {
        // Arrange
        final SortedMap<Integer, String> emptySourceMap = new TreeMap<>();
        
        // The transformers are not relevant for this test as the map is empty.
        // We pass null to indicate that no transformation should occur.
        final TransformedSortedMap<Integer, String> transformedMap =
            TransformedSortedMap.transformedSortedMap(emptySourceMap, null, null);

        final Integer toKey = 100;

        // Act
        final SortedMap<Integer, String> headMap = transformedMap.headMap(toKey);

        // Assert
        assertNotNull("headMap should not return null", headMap);
        assertTrue("The headMap of an empty map should also be empty", headMap.isEmpty());
    }
}