package org.apache.commons.collections4.multimap;

import org.apache.commons.collections4.MultiValuedMap;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest {

    /**
     * Tests that an exception from the decorated map's putAll method
     * is correctly propagated by the TransformedMultiValuedMap.
     */
    @Test
    public void putAllWithMapShouldPropagateExceptionFromDecoratedMap() {
        // Arrange: Create a backing map that is guaranteed to fail.
        // The LinkedHashSetValuedLinkedHashMap uses an internal HashMap. By providing a
        // negative initial capacity, we ensure its constructor will throw an
        // IllegalArgumentException when it's eventually initialized during a putAll call.
        final MultiValuedMap<Integer, Integer> failingBackingMap =
                new LinkedHashSetValuedLinkedHashMap<>(-1);

        // The transformers are irrelevant for this test, so they are set to null.
        final TransformedMultiValuedMap<Integer, Integer> transformedMap =
                new TransformedMultiValuedMap<>(failingBackingMap, null, null);

        final Map<Integer, Integer> mapToAdd = Collections.singletonMap(1, 1);

        // Act & Assert
        try {
            transformedMap.putAll(mapToAdd);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // Verify that the expected exception from the underlying map was propagated.
            assertEquals("Illegal initial capacity: -1", e.getMessage());
        }
    }
}