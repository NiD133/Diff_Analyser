package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling put() on a TransformedMultiValuedMap throws
     * an UnsupportedOperationException if the map is decorating an unmodifiable map.
     * The put() operation should be delegated to the decorated map, which then
     * throws the exception.
     */
    @Test
    public void putOnUnmodifiableDecoratedMapShouldThrowException() {
        // Arrange
        // Create a base map and wrap it to make it unmodifiable.
        final MultiValuedMap<String, String> baseMap = new ArrayListValuedLinkedHashMap<>();
        final MultiValuedMap<String, String> unmodifiableMap =
                UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(baseMap);

        // Use a no-op transformer, as the transformation logic is not the focus of this test.
        final Transformer<String, String> keyTransformer = NOPTransformer.nopTransformer();
        final Transformer<String, String> valueTransformer = NOPTransformer.nopTransformer();

        // Create the TransformedMultiValuedMap decorating the unmodifiable map.
        final MultiValuedMap<String, String> transformedMap =
                TransformedMultiValuedMap.transformingMap(unmodifiableMap, keyTransformer, valueTransformer);

        // Act & Assert
        // An attempt to add an element should fail because the underlying map is unmodifiable.
        assertThrows(UnsupportedOperationException.class, () -> transformedMap.put("key", "value"));
    }
}