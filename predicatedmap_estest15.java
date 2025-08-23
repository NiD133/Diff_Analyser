package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the factory method {@link PredicatedMap#predicatedMap(Map, Predicate, Predicate)}.
 */
public class PredicatedMapTest {

    /**
     * Tests that the predicatedMap() factory method throws an exception if the
     * map being decorated already contains an element that violates the predicate.
     */
    @Test
    public void predicatedMapFactory_shouldThrowException_whenDecoratingMapWithInvalidInitialEntry() {
        // Arrange
        // An ExceptionPredicate is a special predicate that always throws a RuntimeException.
        // We use it here to simulate a validation failure.
        final Predicate<Object> failingPredicate = ExceptionPredicate.exceptionPredicate();

        // Create a map that already contains an entry. This entry will be validated
        // by the factory method.
        final Map<Object, Object> initialMap = new HashMap<>();
        initialMap.put("someKey", "someValue");

        // Act & Assert
        try {
            // The factory method should immediately validate the existing entries.
            // Since our predicate always fails, this call is expected to throw a RuntimeException.
            PredicatedMap.predicatedMap(initialMap, failingPredicate, failingPredicate);
            fail("A RuntimeException should have been thrown because the initial map fails validation.");
        } catch (final RuntimeException e) {
            // Verify that the exception is the one thrown by our predicate.
            assertEquals("ExceptionPredicate invoked", e.getMessage());
        }
    }
}