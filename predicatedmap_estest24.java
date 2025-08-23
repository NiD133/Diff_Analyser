package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link PredicatedMap}.
 */
public class PredicatedMapTest {

    /**
     * Tests that putAll() throws an IllegalArgumentException if a key from the source map
     * is rejected by the key predicate.
     */
    @Test
    public void putAllShouldThrowExceptionWhenKeyPredicateRejectsAnEntry() {
        // Arrange
        // 1. Create a source map containing an entry with a non-null key.
        final Map<Integer, String> sourceMap = new HashMap<>();
        sourceMap.put(123, "some value");

        // 2. Define a key predicate that only accepts null keys.
        final Predicate<Integer> keyPredicate = NullPredicate.nullPredicate();
        //    Define a value predicate that accepts any value.
        final Predicate<Object> valuePredicate = TruePredicate.truePredicate();

        // 3. Create the PredicatedMap with the restrictive key predicate.
        final Map<Integer, Object> decorated = new HashMap<>();
        final PredicatedMap<Integer, Object> predicatedMap =
                PredicatedMap.predicatedMap(decorated, keyPredicate, valuePredicate);

        // Act & Assert
        try {
            // Attempt to add all entries. This should fail because the source map's key (123)
            // violates the NullPredicate.
            predicatedMap.putAll(sourceMap);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (final IllegalArgumentException e) {
            // Verify the exception message is correct.
            assertEquals("Cannot add key - Predicate rejected it", e.getMessage());
        }

        // Finally, ensure the map remains empty after the failed operation.
        assertTrue("Map should be empty after failed putAll", predicatedMap.isEmpty());
    }
}