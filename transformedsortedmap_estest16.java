package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the factory method {@link TransformedSortedMap#transformedSortedMap(SortedMap, Transformer, Transformer)},
 * focusing on error handling when transforming existing map elements.
 */
public class TransformedSortedMapTest {

    @Test
    public void transformedSortedMap_whenExistingKeyIsTransformedToNonComparable_shouldThrowClassCastException() {
        // Arrange
        // Create a source map that will be used both as the input map and as the
        // non-comparable object returned by the transformer.
        final SortedMap<Object, Object> sourceMap = new TreeMap<>();
        sourceMap.put(1, 1);

        // Create a transformer that always returns the sourceMap itself.
        // A TreeMap instance is not Comparable, which will cause an issue when it's used as a key.
        final Transformer<Object, Object> transformer = ConstantTransformer.constantTransformer(sourceMap);

        // Act & Assert
        // The transformedSortedMap() method processes existing entries. When it applies the
        // transformer to the key '1', the result is 'sourceMap'. It then tries to
        // put this new key into the underlying sorted map, which fails because a TreeMap
        // key must be Comparable.
        try {
            TransformedSortedMap.transformedSortedMap(sourceMap, transformer, transformer);
            fail("Expected a ClassCastException because the transformed key is not Comparable.");
        } catch (final ClassCastException e) {
            // Verify that the exception is thrown for the expected reason.
            assertEquals("java.util.TreeMap cannot be cast to java.lang.Comparable", e.getMessage());
        }
    }
}