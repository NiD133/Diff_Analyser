package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TransformedSortedMap} class, focusing on view methods.
 */
public class TransformedSortedMapTest {

    /**
     * Tests that calling tailMap() on an empty TransformedSortedMap returns an empty map.
     * The view returned by tailMap should reflect the emptiness of the original decorated map.
     */
    @Test
    public void tailMapOnEmptyMapShouldReturnEmptyMap() {
        // Arrange
        // The specific transformers are not critical for this test since the map is empty,
        // but we provide a non-null key transformer and a null (no-op) value transformer.
        final Transformer<Integer, Integer> keyTransformer = (key) -> key * 2; // Example key transformer
        final Transformer<Object, Object> valueTransformer = null; // No value transformation

        // Create an empty source map that will be decorated.
        final SortedMap<Integer, Object> emptySourceMap = new TreeMap<>();

        // Decorate the empty map. The 'transformedSortedMap' factory method is used.
        final SortedMap<Integer, Object> transformedMap =
                TransformedSortedMap.transformedSortedMap(emptySourceMap, keyTransformer, valueTransformer);

        // Act
        // Request a tailMap view from the empty transformed map.
        final SortedMap<Integer, Object> tailMap = transformedMap.tailMap(-100);

        // Assert
        // The resulting tailMap view must also be empty.
        assertTrue("The tailMap of an empty map should itself be empty", tailMap.isEmpty());
    }
}