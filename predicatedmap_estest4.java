package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * This test class contains improved tests for the PredicatedMap class.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class PredicatedMap_ESTestTest4 { // Retaining original class name for context

    /**
     * Tests that isSetValueChecking() returns true when a value predicate is provided
     * to the constructor.
     */
    @Test
    public void isSetValueCheckingShouldReturnTrueWhenValuePredicateIsProvided() {
        // Arrange: Create a PredicatedMap with a non-null value predicate.
        // The specific types and predicates used are simplified to the bare minimum
        // required to express the test's intent.
        final Map<String, Integer> emptyMap = new HashMap<>();
        final Predicate<Object> anyPredicate = NotNullPredicate.notNullPredicate();

        final PredicatedMap<String, Integer> predicatedMap =
                new PredicatedMap<>(emptyMap, anyPredicate, anyPredicate);

        // Act: Call the method under test.
        final boolean result = predicatedMap.isSetValueChecking();

        // Assert: Verify that the result is true, as a value predicate was set.
        assertTrue("isSetValueChecking() should return true when a value predicate is configured.", result);
    }
}