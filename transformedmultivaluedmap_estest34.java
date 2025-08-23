package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link TransformedMultiValuedMap} class, focusing on the
 * behavior of its putAll method.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that calling putAll() with an empty iterable of values
     * returns false and does not modify the map.
     */
    @Test
    public void putAllWithEmptyIterableShouldReturnFalseAndNotModifyMap() {
        // Arrange
        // Create a standard MultiValuedMap to be decorated.
        final MultiValuedMap<String, Integer> underlyingMap = new ArrayListValuedHashMap<>();

        // Decorate the map with a TransformedMultiValuedMap.
        // Using null for the transformers indicates that no transformation should occur.
        // This is sufficient for this test, as the method should return before
        // any transformation is attempted on the key or values.
        final MultiValuedMap<String, Integer> transformedMap =
                TransformedMultiValuedMap.transformingMap(underlyingMap, null, null);

        final String key = "anyKey";
        final Iterable<Integer> emptyValues = Collections.emptyList();

        // Act
        // Attempt to add an empty collection of values for the given key.
        final boolean wasMapChanged = transformedMap.putAll(key, emptyValues);

        // Assert
        // The putAll method should return false, indicating the map was not changed.
        assertFalse("putAll with an empty iterable should return false.", wasMapChanged);

        // The map should remain empty, confirming no modifications were made.
        assertTrue("The map should not be modified when putting an empty iterable.", transformedMap.isEmpty());
    }
}