package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that putAll() returns false when adding a key-value pair that, after
     * transformation, already exists in a map backed by a Set. The putAll() method
     * should only return true if the underlying collection is actually modified.
     */
    @Test
    public void putAllShouldReturnFalseWhenAddingDuplicateTransformedValueToSetBasedMap() {
        // Arrange
        // The backing map uses a Set to store values, which prevents duplicate entries for a given key.
        final MultiValuedMap<Integer, Object> backingMap = new LinkedHashSetValuedLinkedHashMap<>();

        // A transformer that converts any input key or value to the integer 0.
        // This ensures we are always attempting to add the same transformed key and value.
        final Transformer<Object, Object> constantTransformer = ignored -> 0;

        final TransformedMultiValuedMap<Integer, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(backingMap, constantTransformer, constantTransformer);

        final Integer key = 1; // An arbitrary key, will be transformed to 0.
        final Iterable<Integer> valuesToAdd = Collections.singletonList(100); // An arbitrary value, will be transformed to 0.

        // Act & Assert: First call
        // The first call should modify the map by adding the transformed key and value.
        // The map's state becomes {0=[0]}.
        boolean wasModified = transformedMap.putAll(key, valuesToAdd);
        assertTrue("putAll should return true when the map is modified for the first time.", wasModified);

        // Act & Assert: Second call
        // The second call uses a different key and value, but they transform to the same results.
        // Since the backing map is Set-based, adding the existing value '0' for key '0'
        // does not change the map. Therefore, putAll should return false.
        final Integer anotherKey = 2;
        final Iterable<Integer> anotherValue = Collections.singletonList(200);
        wasModified = transformedMap.putAll(anotherKey, anotherValue);
        assertFalse("putAll should return false when adding a pre-existing transformed value.", wasModified);
    }
}