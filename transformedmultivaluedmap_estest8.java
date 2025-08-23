package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains improved tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that putAll(MultiValuedMap) correctly applies both key and value
     * transformers to every entry from the source map before adding them.
     * This specific case verifies the behavior when transformers convert
     * all keys and values to null.
     */
    @Test
    public void testPutAllWithMultiValuedMapTransformsEntriesToNull() {
        // Arrange
        // 1. Create a source map with a non-null key and value.
        final MultiValuedMap<Integer, String> sourceMap = new ArrayListValuedHashMap<>();
        sourceMap.put(1, "VALUE");

        // 2. Create the transformer that will convert any input to null.
        final Transformer<Object, Object> alwaysNullTransformer = ConstantTransformer.nullTransformer();

        // 3. Create the destination map which will be decorated.
        final MultiValuedMap<Object, Object> decoratedDestinationMap = new HashSetValuedHashMap<>();

        // 4. Create the TransformedMultiValuedMap under test.
        // It will transform keys and values from the sourceMap before putting them
        // into the decoratedDestinationMap.
        final TransformedMultiValuedMap<Object, Object> transformedDestinationMap =
                new TransformedMultiValuedMap<>(decoratedDestinationMap, alwaysNullTransformer, alwaysNullTransformer);

        // Act
        // Add all entries from the source map to the transformed map.
        // The entry (1, "VALUE") should be transformed to (null, null).
        final boolean wasModified = transformedDestinationMap.putAll(sourceMap);

        // Assert
        // The operation should report that the map was changed.
        assertTrue("putAll should return true as the map was modified.", wasModified);

        // The underlying map should now contain one entry.
        assertEquals("The destination map should contain one entry.", 1, decoratedDestinationMap.size());

        // The entry should be the result of the transformation: (null, null).
        assertTrue("The destination map should contain the transformed key (null).",
                   decoratedDestinationMap.containsKey(null));
        assertTrue("The values for the null key should contain the transformed value (null).",
                   decoratedDestinationMap.get(null).contains(null));
    }
}