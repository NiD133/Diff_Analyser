package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.ConcurrentModificationException;

/**
 * Tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll with the map itself as the source results in a
     * ConcurrentModificationException. This occurs because the method attempts
     * to modify the map while iterating over it.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void putAll_whenSourceIsTheSameMap_throwsConcurrentModificationException() {
        // Arrange
        // A transformer that converts any input key or value to the integer 0.
        final Transformer<Object, Integer> transformer = ConstantTransformer.constantTransformer(0);
        final MultiValuedMap<Integer, Integer> backingMap = new ArrayListValuedHashMap<>();
        final TransformedMultiValuedMap<Integer, Integer> transformedMap =
                new TransformedMultiValuedMap<>(backingMap, transformer, transformer);

        // Add an initial entry to ensure the map is not empty, so that iteration occurs.
        transformedMap.put(1, 2); // This will be stored as {0=[0]}

        // Act
        // Attempt to add all entries from the map back into itself.
        // This will fail because the underlying iterator detects a modification
        // during iteration.
        transformedMap.putAll(transformedMap);
    }
}