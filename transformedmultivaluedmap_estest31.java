package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link TransformedMultiValuedMap} class.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll() with an empty map does not modify the
     * destination map and correctly returns false.
     */
    @Test
    public void putAllWithEmptyMapShouldReturnFalse() {
        // Arrange
        // Create the map that will be decorated.
        final MultiValuedMap<String, Integer> decoratedMap = new HashSetValuedHashMap<>();
        decoratedMap.put("A", 1); // Pre-populate to ensure it's not empty.

        // Use a No-Operation transformer, as the transformation logic is not relevant
        // for this test case.
        final Transformer<Object, Object> nopTransformer = NOPTransformer.nopTransformer();

        final TransformedMultiValuedMap<String, Integer> transformedMap =
                new TransformedMultiValuedMap<>(decoratedMap, nopTransformer, nopTransformer);

        // The source map for the putAll operation is empty.
        final MultiValuedMap<String, Integer> emptySourceMap = new HashSetValuedHashMap<>();

        // Act
        // Attempt to add all entries from the empty source map.
        final boolean wasModified = transformedMap.putAll(emptySourceMap);

        // Assert
        // The contract of putAll states it should return false if the collection was not changed.
        assertFalse("putAll should return false when the source map is empty", wasModified);
    }
}