package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PredicatedMap_ESTestTest17 {

    /**
     * Tests that checkSetValue() propagates exceptions thrown by the value predicate.
     * The checkSetValue() method is used to validate a value before it's updated
     * in a map entry.
     */
    @Test
    public void checkSetValueShouldThrowExceptionWhenValuePredicateFails() {
        // Arrange
        // Use a predicate that always throws a RuntimeException to test the failure case.
        final Predicate<Object> failingValuePredicate = ExceptionPredicate.exceptionPredicate();
        final Map<String, Object> emptyMap = new HashMap<>();

        // Create a PredicatedMap where the value predicate is the one that throws an exception.
        // The key predicate is set to always return true as it's not relevant to this test.
        final PredicatedMap<String, Object> predicatedMap =
            PredicatedMap.predicatedMap(emptyMap, TruePredicate.truePredicate(), failingValuePredicate);

        final Object valueToTest = "any value";

        // Act & Assert
        // The checkSetValue method should invoke the value predicate. Since our predicate
        // always throws a RuntimeException, we expect that exception to be propagated.
        final RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            predicatedMap.checkSetValue(valueToTest);
        });

        // Verify that the exception message is the one from ExceptionPredicate.
        assertEquals("ExceptionPredicate invoked", thrown.getMessage());
    }
}