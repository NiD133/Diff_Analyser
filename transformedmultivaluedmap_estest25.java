package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 * This specific test focuses on ensuring that exceptions from the backing map
 * during a putAll operation are correctly propagated.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that if the decorated map throws an exception when creating a new
     * value collection, the exception is propagated up through the
     * TransformedMultiValuedMap's putAll method.
     */
    @Test
    public void testPutAllShouldPropagateExceptionWhenBackingMapFailsToCreateValueCollection() {
        // Arrange
        final int illegalListCapacity = -1;
        final int initialMapCapacity = 16; // A valid capacity for the map itself.

        // 1. Create a backing map that is deliberately misconfigured.
        // The original test used an incorrect constructor call which would fail immediately.
        // We use the correct two-argument constructor to set an illegal capacity
        // for the internal lists that hold values. This setup ensures the map
        // is created successfully but will fail later when a new key is added.
        MultiValuedMap<Integer, Object> backingMap =
                new ArrayListValuedLinkedHashMap<>(initialMapCapacity, illegalListCapacity);

        // 2. Define transformers. Using a null transformer ensures that when we add
        // an element to the empty map, the transformed key (null) is guaranteed
        // to be new. This triggers the creation of a new value collection in the
        // backing map, which in turn triggers our expected exception.
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        Transformer<Object, Object> valueTransformer = ConstantTransformer.nullTransformer();

        // 3. Create the map to be tested, decorating the faulty backing map.
        TransformedMultiValuedMap<Integer, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(backingMap, keyTransformer, valueTransformer);

        final Integer keyToAdd = 1270;
        final Iterable<Integer> valuesToAdd = Collections.singletonList(keyToAdd);

        // Act & Assert
        try {
            // This call will fail because the backing map tries to create a new
            // ArrayList for the transformed key (null) with the illegal capacity.
            transformedMap.putAll(keyToAdd, valuesToAdd);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception is the one we expect from ArrayList's constructor.
            assertEquals("Illegal Capacity: " + illegalListCapacity, e.getMessage());
        }
    }
}