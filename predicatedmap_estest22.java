package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains tests for the {@link PredicatedMap#put(Object, Object)} method.
 */
public class PredicatedMapTest {

    /**
     * Tests that calling put() on an existing key returns the previous value
     * when no key or value predicates are defined. This specific test case
     * uses a null key and updates its value to null.
     */
    @Test
    public void testPutOnExistingNullKeyReturnsPreviousValueWhenNoPredicates() {
        // Arrange
        // The map to be decorated, pre-populated with a null key and an initial value.
        Map<Object, Map<Integer, Integer>> underlyingMap = new HashMap<>();
        Map<Integer, Integer> initialValue = new HashMap<>();
        underlyingMap.put(null, initialValue);

        // Create a PredicatedMap with null predicates, meaning no validation will occur.
        PredicatedMap<Object, Map<Integer, Integer>> predicatedMap =
                new PredicatedMap<>(underlyingMap, null, null);

        Map<Integer, Integer> newValue = null;

        // Act
        // Update the value for the null key. The put method should return the old value.
        Map<Integer, Integer> previousValue = predicatedMap.put(null, newValue);

        // Assert
        // 1. The returned value should be the original value.
        assertSame("The put method should return the previous value.", initialValue, previousValue);

        // 2. The returned (previous) value was an empty map, as set up in Arrange.
        assertTrue("The previous value was an empty map.", previousValue.isEmpty());

        // 3. The map should now contain the new value for the null key.
        assertNull("The new value for the null key should be null.", predicatedMap.get(null));
        assertEquals("Map size should remain 1 after updating the key.", 1, predicatedMap.size());
    }
}