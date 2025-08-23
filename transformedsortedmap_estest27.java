package org.apache.commons.collections4.map;

import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Contains tests for the sub-map functionality of {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that calling subMap() with a null 'fromKey' throws a NullPointerException.
     * This behavior is expected because the method delegates to the underlying TreeMap,
     * which does not permit null keys by default.
     */
    @Test(expected = NullPointerException.class)
    public void subMapWithNullFromKeyShouldThrowNullPointerException() {
        // Arrange
        // Create a TransformedSortedMap backed by a standard TreeMap.
        // No transformers are used, so it behaves like a regular SortedMap.
        final SortedMap<String, String> backingMap = new TreeMap<>();
        final SortedMap<String, String> transformedMap =
            TransformedSortedMap.transformingSortedMap(backingMap, null, null);

        // Act & Assert
        // Attempt to create a sub-map with a null 'fromKey'.
        // This should fail and throw a NullPointerException, which is asserted
        // by the @Test(expected=...) annotation.
        transformedMap.subMap(null, "anyKey");
    }
}