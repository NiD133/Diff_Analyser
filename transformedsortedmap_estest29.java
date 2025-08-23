package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This test suite contains improvements to an existing test for TransformedSortedMap.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that calling tailMap() with a null key throws a NullPointerException
     * when the decorated map is a TreeMap, which does not permit null keys.
     * This verifies that the decorator correctly propagates exceptions from the
     * underlying map.
     */
    @Test(expected = NullPointerException.class)
    public void tailMapShouldPropagateNullPointerExceptionFromUnderlyingMap() {
        // Arrange: Create a TransformedSortedMap that wraps a TreeMap.
        // A TreeMap is used because it does not support null keys.
        // No transformers are supplied, so the map only acts as a decorator.
        final SortedMap<Object, Object> baseMap = new TreeMap<>();
        final SortedMap<Object, Object> transformedMap =
            TransformedSortedMap.transformedSortedMap(baseMap, null, null);

        // Act: Attempt to get a tail map using a null key.
        // This action is delegated to the underlying TreeMap, which will throw
        // a NullPointerException.
        transformedMap.tailMap(null);

        // Assert: The test succeeds if a NullPointerException is thrown,
        // as specified by the @Test(expected) annotation.
    }
}