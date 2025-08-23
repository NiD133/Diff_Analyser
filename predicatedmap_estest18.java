package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests the constructor of {@link PredicatedMap} to ensure it correctly validates
 * pre-existing entries in the decorated map.
 */
public class PredicatedMapConstructorTest {

    /**
     * Verifies that the PredicatedMap constructor throws a RuntimeException
     * when an existing key in the decorated map fails the key predicate's validation.
     */
    @Test
    public void constructorShouldThrowExceptionWhenKeyPredicateFailsForExistingEntry() {
        // Arrange: Create a predicate that always throws a RuntimeException.
        // This simulates a validation failure.
        final Predicate<Object> failingPredicate = ExceptionPredicate.exceptionPredicate();

        // Arrange: Create a map with a pre-existing entry. The PredicatedMap constructor
        // is expected to validate this entry upon initialization.
        final Map<Predicate<Object>, Predicate<Object>> initialMap = new HashMap<>();
        initialMap.put(failingPredicate, failingPredicate);

        // Act & Assert: Attempt to create a PredicatedMap.
        // The constructor should evaluate the failingPredicate against the existing key,
        // which will trigger a RuntimeException. We use assertThrows to catch and
        // verify this expected exception.
        final RuntimeException thrown = assertThrows(
            RuntimeException.class,
            () -> new PredicatedMap<>(initialMap, failingPredicate, failingPredicate)
        );

        // Assert: Confirm that the caught exception is the one from our predicate,
        // ensuring that the validation logic was indeed executed.
        assertEquals("ExceptionPredicate invoked", thrown.getMessage());
    }
}