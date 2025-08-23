package org.apache.commons.collections4.map;

import static org.junit.Assert.assertThrows;

import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.Test;

/**
 * Unit tests for {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that headMap() throws a NullPointerException when given a null key,
     * because the underlying TreeMap does not support nulls.
     */
    @Test
    public void testHeadMapWithNullKeyThrowsNullPointerException() {
        // Arrange: Create a TransformedSortedMap wrapping a TreeMap, with no transformers.
        // TreeMap is used as the underlying map, which does not permit null keys.
        final SortedMap<Object, Object> baseMap = new TreeMap<>();
        final SortedMap<Object, Object> transformedMap =
                TransformedSortedMap.transformedSortedMap(baseMap, null, null);

        // Act & Assert: Verify that calling headMap with a null key propagates
        // the NullPointerException from the underlying TreeMap.
        assertThrows(NullPointerException.class, () -> transformedMap.headMap(null));
    }
}